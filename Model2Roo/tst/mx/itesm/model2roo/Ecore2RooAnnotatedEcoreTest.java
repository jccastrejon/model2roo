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

import org.jdom.JDOMException;
import org.junit.After;
import org.junit.Test;

/**
 * Test the correct annotation of Ecore elements.
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

        // Annotate with roo profiles
        fileToAnnotate = new File("./tst/model/PetClinic.ecore");
        Ecore2RooAnnotatedEcore.annotateEcore(fileToAnnotate, new File("./profiles/rooCommand.ecore"), new File(
                        "./profiles/rooStructure.ecore"));

        this.testAnnotatedClassifiers(fileToAnnotate);
    }

    @After
    public void afterTest() throws JDOMException, IOException {
        this.removeAnnotations(new File("./tst/model/PetClinic.ecore"));
    }
}