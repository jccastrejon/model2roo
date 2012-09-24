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
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.shell.Completion;
import org.springframework.roo.shell.Converter;
import org.springframework.roo.shell.MethodTarget;

/**
 * Custom id type converter for {@link NodeIdType} to limit options in
 * {@link GraphCommands}. Based on
 * org.springframework.roo.addon.layers.repository.mongo.MongoIdTypeConverter.
 * 
 * @author jccastrejon
 * 
 */
@Component
@Service
public class NodeIdTypeConverter implements Converter<NodeIdType> {

    public NodeIdType convertFromText(final String value, final Class<?> targetType, final String optionContext) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return new NodeIdType(value);
    }

    public boolean getAllPossibleValues(final List<Completion> completions, final Class<?> targetType,
            final String existingData, final String optionContext, final MethodTarget target) {
        final SortedSet<String> types = new TreeSet<String>();
        types.add(Long.class.getName());

        for (final String type : types) {
            if (type.startsWith(existingData) || existingData.startsWith(type)) {
                completions.add(new Completion(type));
            }
        }
        return false;
    }

    public boolean supports(final Class<?> type, final String optionContext) {
        return NodeIdType.class.isAssignableFrom(type);
    }
}
