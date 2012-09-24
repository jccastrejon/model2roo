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

import java.util.List;

import org.springframework.roo.model.JavaType;

/**
 * Operations defined in the graph add-on.
 * 
 * @author jccastrejon
 * 
 */
public interface GraphOperations {

    /**
     * Determines if the setup operation can be issued.
     * 
     * @return
     */
    public boolean isGraphSetupAvailable();

    /**
     * Setup operation.
     * 
     * @param graphProvider
     * @param dataStoreLocation
     */
    public void graphSetup(final GraphProvider graphProvider, final String dataStoreLocation);

    /**
     * Determines if the entity operation can be issued.
     * 
     * @return
     */
    public boolean isNewEntityAvailable();

    /**
     * Entity operation.
     * 
     * @param name
     * @param superClass
     * @param isAbstract
     */
    public void newEntity(final JavaType name, final JavaType superClass, final boolean isAbstract);

    /**
     * Determines if the repository operation can be issued.
     * 
     * @return
     */
    public boolean isNewRepositoryAvailable();

    /**
     * Repository operation.
     * 
     * @param name
     * @param domainType
     */
    public void newRepository(final JavaType name, final JavaType domainType);

    /**
     * Determines if the relationship-entity operation can be issued.
     * 
     * @return
     */
    public boolean isNewRelationshipEntityAvailable();

    /**
     * Relationship-Entity operation.
     * 
     * @param name
     * @param type
     * @param startNode
     * @param endNode
     * @param properties
     */
    public void newRelationshipEntity(final JavaType name, final String type, final JavaType startNode,
            final JavaType endNode, List<String> properties);

    /**
     * Determines if the relationship operation can be issued.
     * 
     * @return
     */
    public boolean isNewRelationshipAvailable();

    /**
     * Relationship operation.
     * 
     * @param node
     * @param relationNode
     * @param isVia
     * @param type
     * @param direction
     * @param fieldName
     * @param relationshipType
     */
    public void newRelationship(final JavaType node, final JavaType relationNode, final boolean isVia,
            final String type, final Direction direction, final String fieldName,
            final RelationshipType relationshipType);
}