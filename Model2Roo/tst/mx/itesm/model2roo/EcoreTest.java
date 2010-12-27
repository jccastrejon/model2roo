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
package mx.itesm.model2roo;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
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
    protected void testAnnotatedClassifiers(final File ecoreFile) throws JDOMException, IOException {
        int classifiers;
        XPath classifiersPath;
        Document ecoreDocument;
        int annotatedClassifiers;

        // Number of classifiers
        ecoreDocument = this.getDocument(ecoreFile);
        classifiersPath = XPath.newInstance("//eClassifiers");
        classifiers = classifiersPath.selectNodes(ecoreDocument).size();

        // Number of annotated classifiers
        classifiersPath = XPath.newInstance("//eClassifiers[eAnnotations]");
        annotatedClassifiers = classifiersPath.selectNodes(ecoreDocument).size();

        // If the annotation process was correct, the number of annotated
        // classifiers should be the same as the total number of classifiers
        assertEquals(classifiers, annotatedClassifiers);
    }

    /**
     * 
     * @param ecoreFile
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected void removeAnnotations(final File ecoreFile) throws JDOMException, IOException {
        XMLOutputter out;
        XPath annotationsPath;
        Document ecoreDocument;
        List<Element> annotations;

        ecoreDocument = this.getDocument(ecoreFile);
        annotationsPath = XPath.newInstance("//eAnnotations");
        annotations = annotationsPath.selectNodes(ecoreDocument);

        // Remove all annotations
        for (Element annotation : annotations) {
            annotation.detach();
        }

        // Save Ecore file without annotations
        out = new XMLOutputter();
        out.output(ecoreDocument, new FileWriter(ecoreFile));
    }

    /**
     * 
     * @param xmlFile
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private Document getDocument(final File xmlFile) throws JDOMException, IOException {
        SAXBuilder saxBuilder;
        Document returnValue;

        saxBuilder = new SAXBuilder();
        returnValue = saxBuilder.build(xmlFile);

        return returnValue;
    }
}
