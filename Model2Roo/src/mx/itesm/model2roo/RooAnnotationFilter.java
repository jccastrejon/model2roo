package mx.itesm.model2roo;

import org.jdom.Element;
import org.jdom.filter.Filter;

/**
 * 
 * @author jccastrejon
 * 
 */
public class RooAnnotationFilter implements Filter {

    /**
     * Serial Id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private static RooAnnotationFilter filter;

    /**
     * 
     */
    private RooAnnotationFilter() {
    }
    
    private static String annotationName;

    /**
     * 
     * @return
     */
    public static RooAnnotationFilter getFilter(final String annotationFilename) {
        if (RooAnnotationFilter.filter == null) {
            RooAnnotationFilter.filter = new RooAnnotationFilter();
        }

        annotationName = annotationFilename;
        return RooAnnotationFilter.filter;
    }

    @Override
    public boolean matches(Object content) {
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
