package fr.imag.model2roo.addon.graph;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.JavaType;

/**
 * Graph providers supported by this add-on.
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
     * Configuration path in the
     * 'resources/fr/imag/model2roo/addon/graph/configuration.xml' file.
     * 
     * @return
     */
    public String getConfigPrefix() {
        return "/configuration/graphstores/graphstore[@id='" + name() + "']/dependency";
    }

    /**
     * Get the annotations that should be associated to a graph node.
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getClassAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue.add(new AnnotationMetadataBuilder(new JavaType(
                "org.springframework.data.neo4j.annotation.NodeEntity")));

        return returnValue;
    }

    /**
     * Get the annotations that should be associated to a graph
     * relationship-entity.
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getRelationshipEntityAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue.add(new AnnotationMetadataBuilder(new JavaType(
                "org.springframework.data.neo4j.annotation.RelationshipEntity")));

        return returnValue;
    }

    /**
     * Get the annotations that should be associated to a graph relationship.
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getRelationshipAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue.add(new AnnotationMetadataBuilder(new JavaType(
                "org.springframework.data.neo4j.annotation.RelatedTo")));

        return returnValue;
    }

    /**
     * Get the annotations that should be associated to a relationship-entity
     * specification.
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getRelationshipViaAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue.add(new AnnotationMetadataBuilder(new JavaType(
                "org.springframework.data.neo4j.annotation.RelatedToVia")));

        return returnValue;
    }

    /**
     * Get the annotations that should be associated to a starting node in a
     * graph relationship.
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getRelationshipStartNodeAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue.add(new AnnotationMetadataBuilder(new JavaType(
                "org.springframework.data.neo4j.annotation.StartNode")));

        return returnValue;
    }

    /**
     * Get the annotations that should be associated to an ending node in a
     * graph relationship.
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getRelationshipEndNodeAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue
                .add(new AnnotationMetadataBuilder(new JavaType("org.springframework.data.neo4j.annotation.EndNode")));

        return returnValue;
    }

    /**
     * Get the annotations that should be associated to an id of a graph entity.
     * 
     * @return
     */
    public List<AnnotationMetadataBuilder> getIdAnnotations() {
        List<AnnotationMetadataBuilder> returnValue;

        returnValue = new ArrayList<AnnotationMetadataBuilder>();
        returnValue
                .add(new AnnotationMetadataBuilder(new JavaType("org.springframework.data.neo4j.annotation.GraphId")));

        return returnValue;
    }

    /**
     * Get the base classes associated to a graph repository.
     * 
     * @return
     */
    public List<String> getRepositoryBaseClasses() {
        List<String> returnValue;

        returnValue = new ArrayList<String>();
        returnValue.add("org.springframework.data.neo4j.repository.GraphRepository");

        return returnValue;
    }
}