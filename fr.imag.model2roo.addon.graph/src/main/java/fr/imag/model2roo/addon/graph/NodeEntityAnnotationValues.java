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

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;

/**
 * The values of a {@link NodeEntity} annotation. Based on
 * org.springframework.roo
 * .addon.layers.repository.mongo.MongoEntityAnnotationValues.
 * 
 * @author jccastrejon
 * 
 */
public class NodeEntityAnnotationValues extends AbstractAnnotationValues {

    @AutoPopulate
    private JavaType identifierType = JavaType.LONG_OBJECT;

    /**
     * 
     * @param governorPhysicalTypeMetadata
     */
    public NodeEntityAnnotationValues(final PhysicalTypeMetadata governorPhysicalTypeMetadata) {
        super(governorPhysicalTypeMetadata, new JavaType("org.springframework.data.neo4j.annotation.NodeEntity"));
        AutoPopulationUtils.populate(this, annotationMetadata);
    }

    /**
     * Returns the Identifier type for this domain entity
     * 
     * @return a non-<code>null</code> type
     */
    public JavaType getIdentifierType() {
        return this.identifierType;
    }
}
