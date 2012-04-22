/*
 * Copyright 2012 jccastrejon
 *  
 * This file is part of Model2Roo.
 *
 * Model2Roo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Model2Roo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Model2Roo.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.imag.model2roo.ecore.ui.popup.actions;

import java.io.File;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.MessageConsoleStream;

public class Ecore2RooAction extends BaseAction implements IObjectActionDelegate {

	@SuppressWarnings("unused")
	private Shell shell;
	private ISelection currentSelection;

	/**
	 * Constructor for Action1.
	 */
	public Ecore2RooAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		File ecoreFile;
		MessageConsoleStream consoleStream;

		consoleStream = this.getConsoleStream();
		this.clearConsole();

		try {
			ecoreFile = Util.getEcoreFile(currentSelection);
			consoleStream.println("Generating script for file: " + ecoreFile.getName() + "...");
			Util.ecore2Roo(this, ecoreFile, consoleStream);
		} catch (Exception e) {
			consoleStream
			        .println("The Ecore package could not be successfully transformed to a Spring Roo script. (Error message: "
			                + e.getMessage() + ")");
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		currentSelection = selection;
	}

}
