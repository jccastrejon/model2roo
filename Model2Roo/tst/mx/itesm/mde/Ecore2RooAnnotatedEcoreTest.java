package mx.itesm.mde;

import java.io.File;
import java.io.IOException;

import org.jdom.JDOMException;
import org.junit.Test;

/**
 * 
 * @author jccastrejon
 * 
 */
public class Ecore2RooAnnotatedEcoreTest extends EcoreTest {

    /**
     * 
     * @throws JDOMException
     * @throws IOException
     */
    @Test
    public void testAnnotateEcore() throws JDOMException, IOException {
        File fileToAnnotate;
        File annotationsFile;

        // Annotate ecore file
        fileToAnnotate = new File("./tst/ecore/PizzaShop.ecore");
        annotationsFile = new File("./profiles/rooCommand.ecore");
        Ecore2RooAnnotatedEcore.annotateEcore(fileToAnnotate, annotationsFile);
        this.testAnnotatedClassifiers(fileToAnnotate);
    }
}