/*
 * Copyright 2012 jccastrejon
 *  
 * This file is part of Model2Roo.
 *
 * Model2Roo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Model2Roo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Model2Roo.  If not, see <http://www.gnu.org/licenses/>.
 */
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