package fr.imag.model2roo.addon.graph;

import java.util.List;

import org.springframework.roo.model.JavaType;

/**
 * 
 * @author jccastrejon
 * 
 */
public interface GraphOperations {

    /**
     * 
     * @return
     */
    public boolean isGraphSetupAvailable();

    /**
     * 
     * @param graphProvider
     * @param dataStoreLocation
     */
    public void graphSetup(final GraphProvider graphProvider, final String dataStoreLocation);

    /**
     * 
     * @return
     */
    public boolean isNewEntityAvailable();

    /**
     * 
     * @param name
     * @param superClass
     * @param isAbstract
     */
    public void newEntity(final JavaType name, final JavaType superClass, final boolean isAbstract);

    /**
     * 
     * @return
     */
    public boolean isNewRepositoryAvailable();

    /**
     * 
     * @param name
     * @param domainType
     */
    public void newRepository(final JavaType name, final JavaType domainType);

    /**
     * 
     * @return
     */
    public boolean isNewRelationshipEntityAvailable();

    /**
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
     * 
     * @return
     */
    public boolean isNewRelationshipAvailable();

    /**
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