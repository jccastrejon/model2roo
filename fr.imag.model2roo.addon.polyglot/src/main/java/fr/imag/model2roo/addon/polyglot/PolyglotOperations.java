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
}