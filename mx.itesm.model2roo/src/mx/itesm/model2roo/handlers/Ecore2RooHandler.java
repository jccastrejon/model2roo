package mx.itesm.model2roo.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EPackage;

/**
 * 
 * @author jccastrejon
 * 
 */
public class Ecore2RooHandler extends AbstractHandler {

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        File ecoreFile;
        List<EPackage> ecorePackages;
        List<String> incorrectPackages;

        incorrectPackages = new ArrayList<String>();
        ecorePackages = (List<EPackage>) Util.getSelectedItems(event);
        for (EPackage ecorePackage : ecorePackages) {
            try {
                ecoreFile = Util.getEcoreFile(ecorePackage);
                Util.ecore2Roo(this, ecoreFile);
            } catch (Exception e) {
                incorrectPackages.add(ecorePackage.getName());
                e.printStackTrace();
            }
        }

        // Output results
        Util.outputMessage(event, "The Spring Roo scripts were successfully generated",
                        "An error occurred while generating scripts for packages: ", incorrectPackages);

        return null;
    }
}
