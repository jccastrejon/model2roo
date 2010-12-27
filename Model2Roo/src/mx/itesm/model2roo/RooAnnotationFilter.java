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
