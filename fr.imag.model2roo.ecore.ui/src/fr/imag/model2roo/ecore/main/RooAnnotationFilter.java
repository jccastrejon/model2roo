package fr.imag.model2roo.ecore.main;

import org.jdom.Element;
import org.jdom.filter.Filter;

/**
 * Identifies elements of an XML documents that match an specified annotation
 * name.
 * 
 * @author jccastrejon
 * 
 */
public class RooAnnotationFilter implements Filter {

	/**
     * 
     */
	private static String annotationName;

	/**
	 * Singleton filter.
	 */
	private static RooAnnotationFilter filter;

	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	private RooAnnotationFilter() {
	}

	/**
	 * 
	 * @return
	 */
	public static RooAnnotationFilter getFilter(final String annotationName) {
		if (RooAnnotationFilter.filter == null) {
			RooAnnotationFilter.filter = new RooAnnotationFilter();
		}

		RooAnnotationFilter.annotationName = annotationName;
		return RooAnnotationFilter.filter;
	}

	public boolean matches(final Object content) {
		Element element;
		boolean returnValue;

		if (Element.class.isAssignableFrom(content.getClass())) {
			element = (Element) content;
			returnValue = ((element.getName().equals("eAnnotations")) && (element.getAttributeValue("source")
			        .startsWith(annotationName)));
		} else {
			returnValue = false;
		}

		return returnValue;
	}
}
