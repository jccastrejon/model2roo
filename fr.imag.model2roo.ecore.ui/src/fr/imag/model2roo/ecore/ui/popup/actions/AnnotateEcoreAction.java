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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import mx.itesm.model2roo.Ecore2RooAnnotatedEcore;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.MessageConsoleStream;

public class AnnotateEcoreAction extends BaseAction implements IObjectActionDelegate {

	@SuppressWarnings("unused")
	private Shell shell;
	private ISelection currentSelection;

	/**
	 * Names of the roo annotations files.
	 */
	private static List<String> rooAnnotationsNames;

	static {
		rooAnnotationsNames = new ArrayList<String>();
		rooAnnotationsNames.add("/profiles/rooCommand.ecore");
		rooAnnotationsNames.add("profiles/rooStructure.ecore");
	}

	/**
	 * Constructor for Action1.
	 */
	public AnnotateEcoreAction() {
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
		InputStream[] rooAnnotations;
		MessageConsoleStream consoleStream;

		consoleStream = this.getConsoleStream();
		rooAnnotations = this.getRooAnnotations();

		this.clearConsole();
		try {
			ecoreFile = Util.getEcoreFile(currentSelection);
			consoleStream.println("Annotating Ecore file " + ecoreFile.getName() + "...");
			Ecore2RooAnnotatedEcore.annotateEcore(ecoreFile, rooAnnotations);
			consoleStream.println("Ecore package successfully annotated!");
		} catch (Exception e) {
			consoleStream.println("The Ecore package could not be successfully annotated. (Error message: "
			        + e.getMessage() + ")");
			e.printStackTrace();
		}
	}

	/**
	 * Get the available Model2Roo annotations.
	 * 
	 * @return
	 */
	protected InputStream[] getRooAnnotations() {
		InputStream[] returnValue;

		returnValue = new InputStream[AnnotateEcoreAction.rooAnnotationsNames.size()];
		for (int i = 0; i < AnnotateEcoreAction.rooAnnotationsNames.size(); i++) {
			returnValue[i] = Util.getResourceStream(this, AnnotateEcoreAction.rooAnnotationsNames.get(i));
		}

		return returnValue;
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		currentSelection = selection;
	}

}
