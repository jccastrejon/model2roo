/*
 * Copyright 2010 jccastrejon, rosatzimba
 *  
 * This file is part of Model2Roo.
 *
 * Model2Roo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Model2Roo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Model2Roo.  If not, see <http://www.gnu.org/licenses/>.
*/
package mx.itesm.model2roo.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mx.itesm.model2roo.UmlProfile2Ecore;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.uml2.uml.util.UMLUtil;
import org.jdom.JDOMException;

/**
 * Handler to transform an UML model to a Spring Roo application.
 * 
 * @author jccastrejon
 * 
 */
public class Uml2RooHandler extends Model2RooHandler {

    /**
     * Transformation process.
     */
    @SuppressWarnings("unchecked")
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        List<Resource> ecoreResources;
        MessageConsoleStream consoleStream;
        List<org.eclipse.uml2.uml.Package> umlPackages;

        // Transform each of the selected UML packages
        consoleStream = this.getConsoleStream();
        umlPackages = (List<org.eclipse.uml2.uml.Package>) Util.getSelectedItems(event);

        this.clearConsole();
        for (org.eclipse.uml2.uml.Package umlPackage : umlPackages) {
            // Uml2Ecore
            ecoreResources = this.transformUmlPackageToEcoreResources(umlPackage);

            // Ecore2Roo
            for (Resource resource : ecoreResources) {
                try {
                    consoleStream.println("Generating script for package: " + umlPackage.getName() + "...");
                    this.transformEcoreResourceToRooScript(umlPackage, resource, consoleStream);
                } catch (Exception e) {
                    consoleStream.println("The Uml package could not be successfully transformed to a Spring Roo script");
                    e.printStackTrace();
                }
            }
        }
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
     * @param consoleStream
     * @throws IOException
     * @throws JDOMException
     * @throws InterruptedException
     */
    private void transformEcoreResourceToRooScript(final org.eclipse.uml2.uml.Package umlPackage,
                    final Resource resource, MessageConsoleStream consoleStream) throws IOException, JDOMException,
                    InterruptedException {
        File umlFile;
        File ecoreFile;

        // Generate Ecore file
        resource.save(null);
        ecoreFile = Util.getEcoreFile(resource);
        umlFile = Util.getUmlFile(umlPackage);

        // UmlProfile2EcoreAnnotation
        UmlProfile2Ecore.transformUmlProfiles(ecoreFile, umlFile);

        // Uml2Roo
        Util.ecore2Roo(this, ecoreFile, consoleStream);
    }
}