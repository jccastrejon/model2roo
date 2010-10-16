package mx.itesm.model2roo;

import java.io.File;
import java.io.IOException;

import mx.itesm.model2roo.Ecore2RooAnnotatedEcore;

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
        fileToAnnotate = new File("./tst/rosy/Rosy.ecore");
        annotationsFile = new File("./profiles/rooCommand.ecore");
        Ecore2RooAnnotatedEcore.annotateEcore(fileToAnnotate, annotationsFile);
        System.out.println("Se agregaron aotaciones de comandos");
        annotationsFile = new File("./profiles/rooStructure.ecore");
        Ecore2RooAnnotatedEcore.annotateEcore(fileToAnnotate, annotationsFile);
        System.out.println("Se agregaron anotaciones de estructura");
//        this.testAnnotatedClassifiers(fileToAnnotate);
    }
}