package fr.imag.model2roo.addon.graph;

import org.springframework.roo.model.JavaType;

/**
 * Custom id type to limit options in {@link NideCommands}. Based on
 * org.springframework.roo.addon.layers.repository.mongo.MongoIdType.
 * 
 * @author Stefan Schmidt
 * @since 1.2.0
 */
public class NodeIdType {

    private final JavaType javaType;

    /**
     * Constructor
     * 
     * @param type
     *            the fully-qualified type name (required)
     */
    public NodeIdType(final String type) {
        javaType = new JavaType(type);
    }

    public JavaType getJavaType() {
        return javaType;
    }
}
