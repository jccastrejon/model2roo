package fr.imag.model2roo.addon.graph;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.JavaType;

/**
 * Support graph providers.
 * 
 * @author jccastrejon
 * 
 */
public enum GraphProvider {
    Neo4j;

    @Override
    public String toString() {
        return name();
    }

    /**
     * 
     * @return
     */
    public String getConfigPrefix() {
        return "/configuration/graphstores/graphstore[@id='" + name() + "']";
    }

    /**
     * 
     * @return
     */
    public String getLocationProperty() {
        return name().toLowerCase() + ".location";
    }

    /**
     * 
     * @return
     */
    public String getPropertyFileName() {
        return name().toLowerCase() + ".properties";
    }

    /**
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getClassAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue.add(new AnnotationMetadataBuilder(new JavaType("org.springframework.data.neo4j.annotation.NodeEntity")));

        return returnValue;
    }
    
    /**
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getIdAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue.add(new AnnotationMetadataBuilder(new JavaType("org.springframework.data.neo4j.annotation.GraphId")));

        return returnValue;
    }
}