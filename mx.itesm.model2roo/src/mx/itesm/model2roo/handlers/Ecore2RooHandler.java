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
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Handler to transform an Ecore model to a Spring Roo application.
 * 
 * @author jccastrejon
 * 
 */
public class Ecore2RooHandler extends Model2RooHandler {

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        File ecoreFile;
        List<EPackage> ecorePackages;
        MessageConsoleStream consoleStream;

        consoleStream = this.getConsoleStream();
        ecorePackages = (List<EPackage>) Util.getSelectedItems(event);

        this.clearConsole();
        for (EPackage ecorePackage : ecorePackages) {
            try {
                ecoreFile = Util.getEcoreFile(ecorePackage);
                consoleStream.println("Generating script for package: " + ecorePackage.getName() + "...");
                Util.ecore2Roo(this, ecoreFile, consoleStream);
            } catch (Exception e) {
                consoleStream.println("The Ecore package could not be successfully transformed to a Spring Roo script");
                e.printStackTrace();
            }
        }

        return null;
    }
}
