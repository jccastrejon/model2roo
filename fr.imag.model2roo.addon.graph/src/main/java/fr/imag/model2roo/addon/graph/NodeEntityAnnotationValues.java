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
