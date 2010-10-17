package mx.itesm.model2roo.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mx.itesm.model2roo.UmlProfile2Ecore;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
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
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        URL atlQuery;
        List<Resource> ecoreResources;
        List<String> incorrectPackages;
        List<org.eclipse.uml2.uml.Package> umlPackages;

        // Transform each of the selected UML packages
        incorrectPackages = new ArrayList<String>();
        atlQuery = Util.getResourceURL(this, "/atl/Ecore2Roo.asm");
        umlPackages = (List<org.eclipse.uml2.uml.Package>) Util.getSelectedItems(event);
        for (org.eclipse.uml2.uml.Package umlPackage : umlPackages) {
            // Uml2Ecore
            ecoreResources = this.transformUmlPackageToEcoreResources(umlPackage);

            // Ecore2Roo
            for (Resource resource : ecoreResources) {
                try {
                    this.transformEcoreResourceToRooScript(umlPackage, resource, atlQuery);
                } catch (Exception e) {
                    incorrectPackages.add(umlPackage.getName());
                    e.printStackTrace();
                }
            }
        }

        Util.outputMessage(event, "The Spring Roo scripts were successfully generated",
                        "An error occurred while generating scripts for packages: ", incorrectPackages);
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
     * @param umlPackage
     * @param resource
     * @param atlQuery
     * @throws IOException
     * @throws JDOMException
     */
    private void transformEcoreResourceToRooScript(final org.eclipse.uml2.uml.Package umlPackage,
                    final Resource resource, final URL atlQuery) throws IOException, JDOMException {
        File umlFile;
        File ecoreFile;

        // Generate Ecore file
        resource.save(null);
        ecoreFile = Util.getEcoreFile(resource);
        umlFile = Util.getUmlFile(umlPackage);

        // UmlProfile2EcoreAnnotation
        UmlProfile2Ecore.transformUmlProfiles(ecoreFile, umlFile);

        // Uml2Roo
        Util.ecore2Roo(this, ecoreFile);
    }
}