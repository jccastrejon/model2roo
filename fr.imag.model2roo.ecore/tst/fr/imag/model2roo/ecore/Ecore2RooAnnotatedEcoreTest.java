package fr.imag.model2roo.ecore;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;

import fr.imag.model2roo.ecore.main.Ecore2RooAnnotatedEcore;

public class Ecore2RooAnnotatedEcoreTest {

	@Test
	public void testAnnotateEcore() throws Exception {
		Ecore2RooAnnotatedEcore
		        .annotateEcore(
		                new File(
		                        "/Users/jccastrejon/java/workspace_model2roo/fr.imag.model2roo.ecore/tst/PetClinic_test.ecore"),
		                new FileInputStream(
		                        new File(
		                                "/Users/jccastrejon/java/workspace_model2roo/fr.imag.model2roo.ecore/annotations/rooCommand.ecore")),
		                new FileInputStream(
		                        new File(
		                                "/Users/jccastrejon/java/workspace_model2roo/fr.imag.model2roo.ecore/annotations/rooStructure.ecore")));
	}
}
