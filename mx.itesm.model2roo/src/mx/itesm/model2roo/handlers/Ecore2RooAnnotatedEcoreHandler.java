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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import mx.itesm.model2roo.Ecore2RooAnnotatedEcore;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Handler to annotate an Ecore model with the Spring Roo details
 * 
 * @author jccastrejon
 * 
 */
public class Ecore2RooAnnotatedEcoreHandler extends Model2RooHandler {

    /**
     * Names of the roo annotations files.
     */
    private static List<String> rooAnnotationsNames;

    static {
        rooAnnotationsNames = new ArrayList<String>();
        rooAnnotationsNames.add("/profiles/rooCommand.ecore");
        rooAnnotationsNames.add("profiles/rooStructure.ecore");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        File ecoreFile;
        InputStream[] rooAnnotations;
        List<EPackage> ecorePackages;
        MessageConsoleStream consoleStream;

        consoleStream = this.getConsoleStream();
        rooAnnotations = this.getRooAnnotations();
        ecorePackages = (List<EPackage>) Util.getSelectedItems(event);

        this.clearConsole();
        for (EPackage ecorePackage : ecorePackages) {
            try {
                ecoreFile = Util.getEcoreFile(ecorePackage);
                consoleStream.println("Annotating Ecore package " + ecorePackage.getName() + "...");
                Ecore2RooAnnotatedEcore.annotateEcore(ecoreFile, rooAnnotations);
                consoleStream.println("Ecore package successfully annotated!");
            } catch (Exception e) {
                consoleStream.println("The Ecore package could not be successfully annotated");
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get the available Model2Roo annotations.
     * 
     * @return
     */
    protected InputStream[] getRooAnnotations() {
        InputStream[] returnValue;

        returnValue = new InputStream[Ecore2RooAnnotatedEcoreHandler.rooAnnotationsNames.size()];
        for (int i = 0; i < Ecore2RooAnnotatedEcoreHandler.rooAnnotationsNames.size(); i++) {
            returnValue[i] = Util.getResourceStream(this, Ecore2RooAnnotatedEcoreHandler.rooAnnotationsNames.get(i));
        }

        return returnValue;
    }

}
