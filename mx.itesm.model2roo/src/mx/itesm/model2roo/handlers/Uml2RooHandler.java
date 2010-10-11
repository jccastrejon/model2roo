package mx.itesm.model2roo.handlers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;

import mx.itesm.model2roo.UmlProfile2Ecore;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.m2m.atl.common.ATLLogFormatter;
import org.eclipse.m2m.atl.common.ATLLogger;
import org.eclipse.m2m.atl.common.ConsoleStreamHandler;
import org.eclipse.m2m.atl.drivers.emf4atl.AtlEMFModelHandler;
import org.eclipse.m2m.atl.engine.vm.AtlLauncher;
import org.eclipse.m2m.atl.engine.vm.AtlModelHandler;
import org.eclipse.m2m.atl.engine.vm.ModelLoader;
import org.eclipse.m2m.atl.engine.vm.nativelib.ASMModel;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.uml2.uml.util.UMLUtil;
import org.jdom.JDOMException;

/**
 * Handler to transform from UML to Ecore.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Uml2RooHandler extends AbstractHandler {

    /**
     * Transformation process.
     */
    @SuppressWarnings("unchecked")
    public Object execute(ExecutionEvent event) throws ExecutionException {
        String outputMessage;
        IWorkbenchWindow window;
        List<Resource> ecoreResources;
        List<String> incorrectPackages;
        IStructuredSelection currentSelection;
        List<org.eclipse.uml2.uml.Package> umlPackages;

        // Transform each of the selected UML packages
        incorrectPackages = new ArrayList<String>();
        currentSelection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        umlPackages = (List<org.eclipse.uml2.uml.Package>) currentSelection.toList();
        for (org.eclipse.uml2.uml.Package umlPackage : umlPackages) {
            // Uml2Ecore
            ecoreResources = this.transformUmlPackageToEcoreResources(umlPackage);

            // Ecore2Roo
            for (Resource resource : ecoreResources) {
                try {
                    this.transformEcoreResourceToRooScript(resource);
                } catch (Exception e) {
                    incorrectPackages.add(umlPackage.getName());
                    e.printStackTrace();
                }
            }
        }

        // Transformation status
        if (incorrectPackages.isEmpty()) {
            outputMessage = "The Spring Roo scripts were successfully generated";
        } else {
            outputMessage = "An error occurred while generating scripts for packages: " + incorrectPackages.toString();
        }

        // Output results
        window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        MessageDialog.openInformation(window.getShell(), "Uml2Roo", outputMessage);

        return null;
    }

    /**
     * 
     * @param umlPackage
     * @return
     */
    private List<Resource> transformUmlPackageToEcoreResources(final org.eclipse.uml2.uml.Package umlPackage) {
        URI resourceUri;
        Resource ecoreResource;
        ResourceSet ecoreResourceSet;
        List<Resource> ecoreResources;
        Collection<EPackage> ecorePackages;

        ecoreResource = umlPackage.eResource();
        ecoreResourceSet = ecoreResource.getResourceSet();
        resourceUri = ecoreResourceSet.getURIConverter().normalize(ecoreResource.getURI()).trimFileExtension()
                        .trimSegments(1);

        ecorePackages = UMLUtil.convertToEcore(umlPackage, null);
        ecoreResources = new ArrayList<Resource>(ecorePackages.size());
        for (EPackage ecorePackage : ecorePackages) {
            ecoreResources.add(ecoreResource = ecoreResourceSet.createResource(resourceUri.appendSegment(
                            ecorePackage.getName()).appendFileExtension("ecore")));
            ecoreResource.getContents().add(ecorePackage);
        }

        return ecoreResources;
    }

    /**
     * 
     * @param resource
     * @throws IOException
     * @throws JDOMException
     */
    private void transformEcoreResourceToRooScript(final Resource resource) throws IOException, JDOMException {
        File umlFile;
        File rooFile;
        URL atlQuery;
        File ecoreFile;
        Handler rooHandler;
        String workspacePath;
        ModelLoader modelLoader;
        ASMModel ecoreMetaModel;
        ClassLoader classLoader;
        AtlEMFModelHandler modelHandler;
        Map<String, Object> transformationModels;

        resource.save(null);
        workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
        ecoreFile = new File(workspacePath + "/" + resource.getURI().toPlatformString(false));
        umlFile = new File(ecoreFile.getAbsolutePath().replace(".ecore", ".uml"));
        rooFile = new File(ecoreFile.getAbsolutePath().replace(".ecore", ".roo"));

        // TODO: Don't depend on file names:.
        if (umlFile.exists()) {
            // The roo script will be regenerated
            rooFile.delete();
            rooFile.createNewFile();

            // UmlProfile2EcoreAnnotation
            UmlProfile2Ecore.transformUmlProfiles(ecoreFile, umlFile);

            // Load the ATL query
            classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = this.getClass().getClassLoader();
            }
            atlQuery = classLoader.getResource("/atl/Ecore2Roo.asm");

            // Ours will be the only ATL logger
            for (Handler logHandler : ATLLogger.getLogger().getHandlers()) {
                ATLLogger.getLogger().removeHandler(logHandler);
            }
            rooHandler = new ConsoleStreamHandler(new BufferedOutputStream(new FileOutputStream(rooFile)));
            rooHandler.setFormatter(ATLLogFormatter.INSTANCE);
            rooHandler.setLevel(Level.ALL);
            ATLLogger.getLogger().addHandler(rooHandler);

            // Execute the ATL query
            modelHandler = (AtlEMFModelHandler) AtlModelHandler.getDefault(AtlModelHandler.AMH_EMF);
            modelLoader = modelHandler.createModelLoader();
            ecoreMetaModel = modelLoader.loadModel("Ecore", null, "uri:http://www.eclipse.org/emf/2002/Ecore");
            transformationModels = new HashMap<String, Object>();
            transformationModels.put("Ecore", ecoreMetaModel);
            transformationModels.put("IN", modelLoader.loadModel("IN", ecoreMetaModel, new BufferedInputStream(
                            new FileInputStream(ecoreFile))));
            AtlLauncher.getDefault().launch(atlQuery, Collections.EMPTY_MAP, transformationModels,
                            Collections.EMPTY_MAP, Collections.EMPTY_LIST, Collections.EMPTY_MAP);
        }
    }
}