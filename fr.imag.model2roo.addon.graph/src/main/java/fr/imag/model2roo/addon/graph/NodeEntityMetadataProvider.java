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

import org.springframework.roo.classpath.itd.ItdTriggerBasedMetadataProvider;

/**
 * Provides the metadata for an ITD that implements a Spring Data Neo4j domain
 * entity. Based on org.springframework.roo.addon.layers.repository.mongo.
 * MongoEntityMetadataProvider.
 * 
 * @author jccastrejon
 * 
 */
public interface NodeEntityMetadataProvider extends ItdTriggerBasedMetadataProvider {
}