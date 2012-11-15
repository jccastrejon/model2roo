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
package fr.imag.model2roo.addon.polyglot;

import org.springframework.roo.model.JavaType;

/**
 * Operations defined by the polyglot add-on.
 * 
 * @author jccastrejon
 * 
 */
public interface PolyglotOperations {

    /**
     * Determines if the setup command is available.
     * 
     * @return
     */
    boolean isSetupAvailable();

    /**
     * Setup configuration for polyglot persistence applications.
     */
    void setup();

    /**
     * Determines if the rest setup is available.
     * 
     * @return
     */
    boolean isConfigureRestAvailable();

    /**
     * Configuration or rest support.
     */
    void configureRest();

    /**
     * Determines if the configuration of rest methods is available.
     * 
     * @return
     */
    boolean isConfigureRestMethodsAvailable();

    /**
     * Configuration of rest methods for the specified entity.
     * 
     * @param entity
     */
    void configureRestMethods(final JavaType entity);

    /**
     * Determine if the command to add support for blob types.
     * 
     * @return
     */
    boolean isBlobSetupAvailable();

    /**
     * Add support for blob types.
     */
    void blobSetup(final JavaType entity);

    /**
     * Determine if the configuration for a blob property is available.
     * 
     * @return
     */
    boolean isBlobPropertyAvailable();

    /**
     * Add configuration for a blob property.
     * 
     * @param entity
     * @param property
     * @param contentType
     */
    void blobProperty(final JavaType entity, final String property, final String contentType);
}