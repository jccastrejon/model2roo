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

import static org.springframework.roo.classpath.customdata.CustomDataKeys.IDENTIFIER_ACCESSOR_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.IDENTIFIER_FIELD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.IDENTIFIER_MUTATOR_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.PERSISTENT_TYPE;

import java.util.Arrays;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.customdata.taggers.AnnotatedTypeMatcher;
import org.springframework.roo.classpath.customdata.taggers.CustomDataKeyDecorator;
import org.springframework.roo.classpath.customdata.taggers.FieldMatcher;
import org.springframework.roo.classpath.customdata.taggers.MethodMatcher;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.scanner.MemberDetails;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.model.SpringJavaType;
import org.springframework.roo.project.LogicalPath;

/**
 * Implementation of {@link NodeEntityMetadataProvider}. Based on
 * org.springframework
 * .roo.addon.layers.repository.mongo.MongoEntityMetadataProviderImpl
 * 
 * @author jccastrejon
 * 
 */
@Component(immediate = true)
@Service
public class NodeEntityMetadataProviderImpl extends AbstractItdMetadataProvider implements NodeEntityMetadataProvider {

    private static final JavaType NEO4J_NODE = new JavaType("org.springframework.data.neo4j.annotation.NodeEntity");

    private static final FieldMatcher ID_FIELD_MATCHER = new FieldMatcher(IDENTIFIER_FIELD,
            AnnotationMetadataBuilder.getInstance(SpringJavaType.DATA_ID.getFullyQualifiedTypeName()));
    private static final MethodMatcher ID_ACCESSOR_MATCHER = new MethodMatcher(Arrays.asList(ID_FIELD_MATCHER),
            IDENTIFIER_ACCESSOR_METHOD, true);
    private static final MethodMatcher ID_MUTATOR_MATCHER = new MethodMatcher(Arrays.asList(ID_FIELD_MATCHER),
            IDENTIFIER_MUTATOR_METHOD, false);
    private static final AnnotatedTypeMatcher PERSISTENT_TYPE_MATCHER = new AnnotatedTypeMatcher(PERSISTENT_TYPE,
            NodeEntityMetadataProviderImpl.NEO4J_NODE);

    @Reference
    private CustomDataKeyDecorator customDataKeyDecorator;

    @SuppressWarnings("unchecked")
    protected void activate(final ComponentContext context) {
        super.setDependsOnGovernorBeingAClass(false);
        metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(),
                getProvidesType());
        addMetadataTrigger(NodeEntityMetadataProviderImpl.NEO4J_NODE);
        customDataKeyDecorator.registerMatchers(getClass(), PERSISTENT_TYPE_MATCHER, ID_FIELD_MATCHER,
                ID_ACCESSOR_MATCHER, ID_MUTATOR_MATCHER);
    }

    @Override
    protected String createLocalIdentifier(final JavaType javaType, final LogicalPath path) {
        return NodeEntityMetadata.createIdentifier(javaType, path);
    }

    protected void deactivate(final ComponentContext context) {
        metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(),
                getProvidesType());
        removeMetadataTrigger(NodeEntityMetadataProviderImpl.NEO4J_NODE);
        customDataKeyDecorator.unregisterMatchers(getClass());
    }

    @Override
    protected String getGovernorPhysicalTypeIdentifier(final String metadataIdentificationString) {
        final JavaType javaType = NodeEntityMetadata.getJavaType(metadataIdentificationString);
        final LogicalPath path = NodeEntityMetadata.getPath(metadataIdentificationString);
        return PhysicalTypeIdentifier.createIdentifier(javaType, path);
    }

    public String getItdUniquenessFilenameSuffix() {
        return "Graph_Entity";
    }

    @Override
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(final String metadataIdentificationString,
            final JavaType aspectName, final PhysicalTypeMetadata governorPhysicalType, final String itdFilename) {
        final NodeEntityAnnotationValues annotationValues = new NodeEntityAnnotationValues(governorPhysicalType);
        final JavaType identifierType = annotationValues.getIdentifierType();
        if (!annotationValues.isAnnotationFound() || identifierType == null) {
            return null;
        }

        // Get the governor's member details
        final MemberDetails memberDetails = getMemberDetails(governorPhysicalType);
        if (memberDetails == null) {
            return null;
        }

        final NodeEntityMetadata parent = getParentMetadata(governorPhysicalType.getMemberHoldingTypeDetails());

        // If the parent is null, but the type has a super class it is likely
        // that the we don't have information to proceed
        if (parent == null && governorPhysicalType.getMemberHoldingTypeDetails().getSuperclass() != null) {
            // If the superclass is not annotated with the RooMongoEntity
            // trigger
            // annotation then we can be pretty sure that we don't have enough
            // information to proceed
            if (MemberFindingUtils.getAnnotationOfType(governorPhysicalType.getMemberHoldingTypeDetails()
                    .getAnnotations(), NodeEntityMetadataProviderImpl.NEO4J_NODE) != null) {
                return null;
            }
        }

        return new NodeEntityMetadata(metadataIdentificationString, aspectName, governorPhysicalType, parent,
                identifierType, memberDetails);
    }

    public String getProvidesType() {
        return NodeEntityMetadata.getMetadataIdentiferType();
    }
}
