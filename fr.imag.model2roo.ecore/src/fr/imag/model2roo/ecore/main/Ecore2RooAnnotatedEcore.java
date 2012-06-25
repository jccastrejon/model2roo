package fr.imag.model2roo.ecore.main;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * Add to an existing Ecore file the Model2Roo annotations.
 * 
 * @author jccastrejon
 * 
 */
@SuppressWarnings("unchecked")
public class Ecore2RooAnnotatedEcore {

	/**
	 * Annotate the specified Ecore file with the annotations found in the
	 * specified annotations files.
	 * 
	 * @param ecoreFile
	 * @param annotationsFiles
	 */
	public static void annotateEcore(final File ecoreFile, final InputStream... annotationsFiles) {
		XMLOutputter out;
		Element enumObject;
		String dataTypeName;
		boolean isContainment;
		SAXBuilder saxBuilder;
		Document outputDocument;
		List<Element> elementsToAnnotate;
		Map<String, List<Element>> annotationsDetails;

		try {
			saxBuilder = new SAXBuilder();
			outputDocument = saxBuilder.build(ecoreFile);
			annotationsDetails = new HashMap<String, List<Element>>();

			// Remove all previous annotations
			elementsToAnnotate = XPath.newInstance("//eAnnotations[starts-with(@source, 'roo')]").selectNodes(
			        outputDocument);
			for (Element element : elementsToAnnotate) {
				element.detach();
			}

			// Get all possible annotations
			for (InputStream annotationsFile : annotationsFiles) {
				Ecore2RooAnnotatedEcore.getAnnotationsDetails(saxBuilder.build(annotationsFile), annotationsDetails);
			}

			// Annotate the ecore elements
			for (String ecoreElement : annotationsDetails.keySet()) {
				elementsToAnnotate = XPath.newInstance("//*[@xsi:type='ecore:" + ecoreElement + "']").selectNodes(
				        outputDocument);

				if ((elementsToAnnotate == null) || (elementsToAnnotate.isEmpty())) {
					elementsToAnnotate = XPath.newInstance("//" + ecoreElement + "']").selectNodes(outputDocument);
				}

				if (elementsToAnnotate != null) {
					for (Element elementToAnnotate : elementsToAnnotate) {
						for (Element annotation : annotationsDetails.get(ecoreElement)) {
							elementToAnnotate.addContent(((Element) annotation.clone()).setAttribute("references", ""));
						}
					}
				}
			}

			// Remove unnecessary annotations for data types
			elementsToAnnotate = XPath.newInstance("//*[@xsi:type='ecore:EAttribute']").selectNodes(outputDocument);
			for (Element elementToAnnotate : elementsToAnnotate) {
				dataTypeName = elementToAnnotate.getAttributeValue("eType");

				if (dataTypeName != null) {
					// Special case for enumerations
					if (dataTypeName.startsWith("#")) {
						dataTypeName = dataTypeName.substring(dataTypeName.lastIndexOf("/") + "/".length());
						enumObject = (Element) XPath.newInstance(
						        "//eClassifiers[attribute::name='" + dataTypeName + "']").selectSingleNode(
						        outputDocument);
						if (enumObject != null) {
							dataTypeName = "Enum";
						}
					}

					Ecore2RooAnnotatedEcore.detachAnnotations(elementToAnnotate, dataTypeName);
				}
			}

			// Remove unnecessary annotations for references. If the reference
			// is marked as containment, leave annotations that refer to 'sets'.
			elementsToAnnotate = XPath.newInstance("//*[@xsi:type='ecore:EReference']").selectNodes(outputDocument);
			for (Element elementToAnnotate : elementsToAnnotate) {
				isContainment = Boolean.parseBoolean(elementToAnnotate.getAttributeValue("containment"));
				dataTypeName = "Reference";
				if (isContainment) {
					dataTypeName = "Set";
				}

				Ecore2RooAnnotatedEcore.detachAnnotations(elementToAnnotate, dataTypeName);
			}

			// Save results
			out = new XMLOutputter();
			out.output(outputDocument, new FileWriter(new File(ecoreFile.getParentFile(), ecoreFile.getName())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Detach existing annotations from the specified element.
	 * 
	 * @param elementToAnnotate
	 * @param dataTypeName
	 */
	private static void detachAnnotations(final Element elementToAnnotate, final String dataTypeName) {
		String annotationName;
		List<Element> elementsToDetach;

		// If there's a data type, remove all the annotations that
		// don't make reference to that particular data type
		elementsToDetach = new ArrayList<Element>();
		for (Element child : (List<Element>) elementToAnnotate.getChildren()) {
			annotationName = child.getAttributeValue("source");
			if (annotationName != null) {
				annotationName = annotationName.substring(annotationName.lastIndexOf("Field") + "Field".length());
				if ((annotationName != null) && (!dataTypeName.endsWith(annotationName))) {
					elementsToDetach.add(child);
				}
			}
		}

		for (Element child : elementsToDetach) {
			child.detach();
		}
	}

	/**
	 * Get the details associated to a set of annotations.
	 * 
	 * @param annotationsFile
	 * @param annotationsDetails
	 * @throws JDOMException
	 */
	private static void getAnnotationsDetails(final Document annotationsFile,
	        final Map<String, List<Element>> annotationsDetails) throws JDOMException {
		String classifierName;
		List<Element> classifiers;
		List<Element> currentDetails;

		classifiers = XPath.newInstance("//eClassifiers").selectNodes(annotationsFile);
		if (classifiers != null) {
			for (Element classifier : classifiers) {
				classifierName = classifier.getAttributeValue("name");
				currentDetails = annotationsDetails.get(classifierName);

				if (currentDetails == null) {
					currentDetails = new ArrayList<Element>();
					annotationsDetails.put(classifierName, currentDetails);
				}

				currentDetails.addAll(XPath.newInstance(
				        "//eAnnotations[contains(@references, '#//" + classifierName + "')]").selectNodes(
				        annotationsFile));
			}
		}
	}
}