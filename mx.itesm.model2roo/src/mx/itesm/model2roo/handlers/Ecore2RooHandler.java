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
