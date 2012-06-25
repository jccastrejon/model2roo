package fr.imag.model2roo.ecore.ui.popupMenus;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import fr.imag.model2roo.ecore.main.Ecore2RooAnnotatedEcore;

/**
 * 
 * @author jccastrejon
 * 
 */
public class AnnotateEcoreAction extends ActionDelegate implements IObjectActionDelegate {

    @SuppressWarnings("unused")
    private Shell shell;
    private ISelection currentSelection;

    /**
     * Names of the roo annotations files.
     */
    private static List<String> rooAnnotationsNames;

    static {
        rooAnnotationsNames = new ArrayList<String>();
        rooAnnotationsNames.add("/rooCommand.ecore");
        rooAnnotationsNames.add("/rooStructure.ecore");
    }

    /**
     * Eclipse console.
     */
    private MessageConsole messageConsole;

    /**
     * Output Stream to the Eclipse console.
     */
    private MessageConsoleStream messageConsoleStream;

    public AnnotateEcoreAction() {
        this.messageConsole = this.findConsole("Model2Roo");
        this.messageConsoleStream = this.messageConsole.newMessageStream();
    }

    /**
     * 
     * @param currentSelection
     * @return
     */
    @SuppressWarnings("restriction")
    protected File getEcoreFile(ISelection currentSelection) {
        File returnValue;
        String ecoreFilePath;
        String workspacePath;

        workspacePath = this.getWorkspacePath();
        ecoreFilePath = ((org.eclipse.core.internal.resources.File) ((StructuredSelection) currentSelection)
                .getFirstElement()).getFullPath().toOSString();
        returnValue = new File(workspacePath + System.getProperty("file.separator") + ecoreFilePath);

        return returnValue;
    }

    /**
     * Get an Input Stream of the given resourceName, according to the class
     * path of the current object.
     * 
     * @param currentObject
     * @param resourceName
     * @return
     */
    protected InputStream getResourceStream(final Object currentObject, final String resourceName) {
        InputStream returnValue;
        ClassLoader currentClassLoader;

        currentClassLoader = this.getCurrentClassLoader(currentObject);
        returnValue = currentClassLoader.getResourceAsStream(resourceName);

        return returnValue;
    }

    /**
     * Get the class loader associated to the specified object.
     * 
     * @param currentObject
     * @return
     */
    protected ClassLoader getCurrentClassLoader(final Object currentObject) {
        ClassLoader returnValue;

        returnValue = Thread.currentThread().getContextClassLoader();
        if (returnValue == null) {
            returnValue = currentObject.getClass().getClassLoader();
        }

        return returnValue;
    }

    /**
     * Get the current workspace path.
     * 
     * @return
     */
    protected String getWorkspacePath() {
        return ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
    }

    /**
     * Get the Output Stream to the Eclipse console.
     * 
     * @return
     */
    protected MessageConsoleStream getConsoleStream() {
        return this.messageConsoleStream;
    }

    /**
     * Delete any messages previously written to the Eclipse console.
     */
    protected void clearConsole() {
        IConsoleView view;
        IWorkbenchPage page;

        try {
            this.messageConsole.clearConsole();
            page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            view = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
            view.display(messageConsole);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the Eclipse console identified with the given name.
     * 
     * @param name
     * @return
     */
    protected MessageConsole findConsole(final String name) {
        ConsolePlugin plugin;
        IConsoleManager conMan;
        IConsole[] existing;

        plugin = ConsolePlugin.getDefault();
        conMan = plugin.getConsoleManager();
        existing = conMan.getConsoles();
        for (int i = 0; i < existing.length; i++) {
            if (name.equals(existing[i].getName())) {
                return (MessageConsole) existing[i];
            }
        }

        // no console found, so create a new one
        MessageConsole myConsole = new MessageConsole(name, null);
        conMan.addConsoles(new IConsole[] { myConsole });
        return myConsole;
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
            ecoreFile = this.getEcoreFile(currentSelection);
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
            returnValue[i] = this.getResourceStream(this, AnnotateEcoreAction.rooAnnotationsNames.get(i));
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
