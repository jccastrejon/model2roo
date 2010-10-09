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
public class UmlProfile2EcoreTest extends EcoreTest {

    /**
     * 
     * @throws JDOMException
     * @throws IOException
     */
    @Test
    public void testTransformUmlProfiles() throws JDOMException, IOException {
        File profileFile;
        File targetEcoreFile;

        targetEcoreFile = new File("./tst/ecore/pizzaShop.ecore");
        profileFile = new File("./profiles/rooCommand.profile.uml");
        UmlProfile2Ecore.transformUmlProfiles(targetEcoreFile, profileFile);
        this.testAnnotatedClassifiers(targetEcoreFile);
    }
}
