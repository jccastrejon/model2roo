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

import java.io.File;
import java.io.IOException;

import mx.itesm.model2roo.UmlProfile2Ecore;

import org.jdom.JDOMException;
import org.junit.After;
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
        File sourceUmlFile;
        File targetEcoreFile;

        targetEcoreFile = new File("./tst/model/toys.ecore");
        sourceUmlFile = new File("./tst/model/Toys.uml");
        UmlProfile2Ecore.transformUmlProfiles(targetEcoreFile, sourceUmlFile);
        this.testAnnotatedClassifiers(targetEcoreFile);
    }

    @After
    public void afterTest() throws JDOMException, IOException {
        this.removeAnnotations(new File("./tst/model/toys.ecore"));
    }
}
