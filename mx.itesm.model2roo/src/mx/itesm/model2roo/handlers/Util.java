package mx.itesm.model2roo.handlers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
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

/**
 * 
 * @author jccastrejon
 * 
 */
public class Util {

    /**
     * 
     * @param event
     * @return
     */
    public static List<?> getSelectedItems(final ExecutionEvent event) {
        List<?> returnValue;
        IStructuredSelection currentSelection;

        currentSelection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        returnValue = currentSelection.toList();

        return returnValue;
    }

    /**
     * 
     * @param currentObject
     * @param resourceName
     * @return
     */
    public static URL getResourceURL(final Object currentObject, final String resourceName) {
        URL returnValue;
        ClassLoader currentClassLoader;

        currentClassLoader = Util.getCurrentClassLoader(currentObject);
        returnValue = currentClassLoader.getResource(resourceName);

        return returnValue;
    }

    /**
     * 
     * @param currentObject
     * @param resourceName
     * @return
     */
    public static InputStream getResourceStream(final Object currentObject, final String resourceName) {
        InputStream returnValue;
        ClassLoader currentClassLoader;

        currentClassLoader = Util.getCurrentClassLoader(currentObject);
        returnValue = currentClassLoader.getResourceAsStream(resourceName);

        return returnValue;
    }

    /**
     * 
     * @param event
     * @param successMessage
     * @param failMessage
     * @param errors
     * @throws ExecutionException
     */
    public static void outputMessage(final ExecutionEvent event, final String successMessage, final String failMessage,
                    final List<?> errors) throws ExecutionException {
        String outputMessage;
        IWorkbenchWindow window;

        outputMessage = successMessage;
        if ((errors != null) && (!errors.isEmpty())) {
            outputMessage = failMessage + errors.toString();
        }

        // Output results
        window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        MessageDialog.openInformation(window.getShell(), "Model2Roo", outputMessage);
    }

    /**
     * 
     * @param currentObject
     * @param ecoreFile
     * @throws IOException
     */
    public static void ecore2Roo(final Object currentObject, final File ecoreFile) throws IOException {
        URL atlQuery;
        File rooFile;
        Handler rooHandler;
        ModelLoader modelLoader;
        ASMModel ecoreMetaModel;
        Map<String, URL> libraries;
        AtlEMFModelHandler modelHandler;
        Map<String, Object> transformationModels;

        // The roo script will be regenerated
        rooFile = new File(ecoreFile.getAbsolutePath().replace(".ecore", ".roo"));
        rooFile.delete();
        rooFile.createNewFile();

        // Ours will be the only ATL logger
        for (Handler logHandler : ATLLogger.getLogger().getHandlers()) {
            ATLLogger.getLogger().removeHandler(logHandler);
        }
        rooHandler = new ConsoleStreamHandler(new BufferedOutputStream(new FileOutputStream(rooFile)));
        rooHandler.setFormatter(ATLLogFormatter.INSTANCE);
        rooHandler.setLevel(Level.ALL);
        ATLLogger.getLogger().addHandler(rooHandler);

        // Load query libraries
        libraries = new HashMap<String, URL>();
        libraries.put("Enum", Util.getResourceURL(currentObject, "/atl/Enum.asm"));
        libraries.put("Annotation", Util.getResourceURL(currentObject, "/atl/Annotation.asm"));
        libraries.put("Entity", Util.getResourceURL(currentObject, "/atl/Entity.asm"));

        // Execute the ATL query
        atlQuery = Util.getResourceURL(currentObject, "/atl/Ecore2Roo.asm");
        modelHandler = (AtlEMFModelHandler) AtlModelHandler.getDefault(AtlModelHandler.AMH_EMF);
        modelLoader = modelHandler.createModelLoader();
        ecoreMetaModel = modelLoader.loadModel("Ecore", null, "uri:http://www.eclipse.org/emf/2002/Ecore");
        transformationModels = new HashMap<String, Object>();
        transformationModels.put("Ecore", ecoreMetaModel);
        transformationModels.put("IN", modelLoader.loadModel("IN", ecoreMetaModel, new BufferedInputStream(
                        new FileInputStream(ecoreFile))));
        AtlLauncher.getDefault().launch(atlQuery, libraries, transformationModels, Collections.EMPTY_MAP,
                        Collections.EMPTY_LIST, Collections.EMPTY_MAP);
    }

    /**
     * 
     * @param ecorePackage
     * @return
     */
    public static File getEcoreFile(final EPackage ecorePackage) {
        File returnValue;
        String ecoreFilePath;
        String workspacePath;

        workspacePath = Util.getWorkspacePath();
        ecoreFilePath = ecorePackage.eResource().getURI().toPlatformString(false);
        returnValue = new File(workspacePath + "/" + ecoreFilePath);

        return returnValue;
    }

    /**
     * 
     * @param resource
     * @return
     */
    public static File getEcoreFile(final Resource resource) {
        File returnValue;
        String ecoreFilePath;
        String workspacePath;

        workspacePath = Util.getWorkspacePath();
        ecoreFilePath = resource.getURI().toPlatformString(false);
        returnValue = new File(workspacePath + "/" + ecoreFilePath);

        return returnValue;
    }

    /**
     * 
     * @param umlPackage
     * @return
     */
    public static File getUmlFile(final org.eclipse.uml2.uml.Package umlPackage) {
        File returnValue;
        String umlFilePath;
        String workspacePath;

        workspacePath = Util.getWorkspacePath();
        umlFilePath = umlPackage.eResource().getURI().toPlatformString(false);
        returnValue = new File(workspacePath + "/" + umlFilePath);

        return returnValue;
    }

    /**
     * 
     * @return
     */
    private static String getWorkspacePath() {
        return ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
    }

    /**
     * 
     * @param currentObject
     * @return
     */
    private static ClassLoader getCurrentClassLoader(final Object currentObject) {
        ClassLoader returnValue;

        returnValue = Thread.currentThread().getContextClassLoader();
        if (returnValue == null) {
            returnValue = currentObject.getClass().getClassLoader();
        }

        return returnValue;
    }
}
