package mx.itesm.mde;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * 
 * @author jccastrejon
 * 
 */
public abstract class EcoreTest {
    /**
     * 
     * @param ecoreFile
     * @throws JDOMException
     * @throws IOException
     */
    public void testAnnotatedClassifiers(final File ecoreFile) throws JDOMException, IOException {
        int classifiers;
        XPath classifiersPath;
        SAXBuilder saxBuilder;
        Document ecoreDocument;
        int annotatedClassifiers;

        saxBuilder = new SAXBuilder();
        ecoreDocument = saxBuilder.build(ecoreFile);

        // Number of classifiers
        classifiersPath = XPath.newInstance("//eClassifiers");
        classifiers = classifiersPath.selectNodes(ecoreDocument).size();

        // Number of annotated classifiers
        classifiersPath = XPath.newInstance("//eClassifiers/eAnnotations");
        annotatedClassifiers = classifiersPath.selectNodes(ecoreDocument).size();

        // If the annotation process was correct, the number of annotated
        // classifiers should be the same as the total number of classifiers
        assertEquals(classifiers, annotatedClassifiers);
    }
}
