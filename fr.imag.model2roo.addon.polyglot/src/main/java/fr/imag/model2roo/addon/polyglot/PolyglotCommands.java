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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * Commands provided by the polyglot add-on.
 * 
 * @author jccastrejon
 * 
 */
@Component
@Service
public class PolyglotCommands implements CommandMarker {

    /**
     * Get a reference to the PolyglotOperations from the underlying OSGi
     * container
     */
    @Reference
    private PolyglotOperations operations;

    /**
     * Determines if the setup command is available.
     * 
     * @return
     */
    @CliAvailabilityIndicator("polyglot setup")
    public boolean isCommandAvailable() {
        return operations.isSetupAvailable();
    }

    /**
     * Setup configuration for polyglot persistence applications.
     */
    @CliCommand(value = "polyglot setup", help = "Setup Polyglot addon")
    public void setup() {
        operations.setup();
    }

    /**
     * Determines if the rest setup is available.
     * 
     * @return
     */
    @CliAvailabilityIndicator("polyglot rest setup")
    public boolean isConfigureRestAvailable() {
        return operations.isConfigureRestAvailable();
    }

    /**
     * Configuration or rest support.
     */
    @CliCommand(value = "polyglot rest setup", help = "Setup Rest addon")
    public void configureRest() {
        operations.configureRest();
    }

    /**
     * Determines if the configuration of rest methods is available.
     * 
     * @return
     */
    @CliAvailabilityIndicator("polyglot rest methods")
    public boolean isConfigureRestMethodsAvailable() {
        return operations.isConfigureRestMethodsAvailable();
    }

    /**
     * Configuration of rest methods for the specified entity.
     * 
     * @param entity
     */
    @CliCommand(value = "polyglot rest methods", help = "Setup rest methods for an entity")
    public void configureRestMethods(
            @CliOption(key = { "entity" }, mandatory = true, help = "The entity for which rest methods will be created") final JavaType entity) {
        operations.configureRestMethods(entity);
    }

    /**
     * Determine if the command to add support for blob types.
     * 
     * @return
     */
    @CliAvailabilityIndicator("polyglot blob setup")
    public boolean isBlobSetupAvailable() {
        return operations.isBlobSetupAvailable();
    }

    /**
     * Add support for blob types.
     * 
     * @param entity
     */
    @CliCommand(value = "polyglot blob setup", help = "Blob setup")
    public void blobSetup(
            @CliOption(key = { "entity" }, mandatory = true, help = "The entity with associated blob properties") final JavaType entity) {
        operations.blobSetup(entity);
    }

    /**
     * Determine if the configuration for a blob property is available.
     * 
     * @return
     */
    @CliAvailabilityIndicator("polyglot blob property")
    public boolean isBlobPropertyAvailable() {
        return operations.isBlobPropertyAvailable();
    }

    /**
     * Add configuration for a blob property.
     * 
     * @param entity
     * @param property
     * @param contentType
     */
    @CliCommand(value = "polyglot blob property", help = "Blob property setup")
    public void blobProperty(
            @CliOption(key = { "entity" }, mandatory = true, help = "The entity with associated blob properties") final JavaType entity,
            @CliOption(key = { "property" }, mandatory = true, help = "Property name") final String property,
            @CliOption(key = { "contentType" }, mandatory = true, help = "Property content-type") final String contentType) {
        operations.blobProperty(entity, property, contentType);
    }
}