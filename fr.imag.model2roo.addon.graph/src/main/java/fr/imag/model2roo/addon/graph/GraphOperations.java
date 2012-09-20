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
     * @param provider
     * @param dataStoreLocation
     */
    public void graphSetup(final GraphProvider provider, final String dataStoreLocation);

    /**
     * 
     * @return
     */
    public boolean isNewEntityAvailable();

    /**
     * 
     * @param name
     * @param graphProvider
     */
    public void newEntity(final JavaType name, final GraphProvider graphProvider);

    /**
     * 
     * @return
     */
    public boolean isNewRepositoryAvailable();

    /**
     * 
     * @param domainType
     * @param graphProvider
     */
    public void newRepository(final JavaType domainType, final GraphProvider graphProvider);

    /**
     * 
     * @return
     */
    public boolean isNewRelationshipEntityAvailable();

    /**
     * 
     * @param graphProvider
     * @param name
     * @param type
     * @param startNode
     * @param endNode
     * @param properties
     */
    public void newRelationshipEntity(final GraphProvider graphProvider, final JavaType name, final String type,
            final JavaType startNode, final JavaType endNode, List<String> properties);

    /**
     * 
     * @return
     */
    public boolean isNewRelationshipAvailable();

    /**
     * 
     * @param graphProvider
     * @param node
     * @param relationNode
     * @param isVia
     * @param type
     * @param direction
     * @param fieldName
     * @param relationshipType
     */
    public void newRelationship(final GraphProvider graphProvider, final JavaType node, final JavaType relationNode,
            final boolean isVia, final String type, final Direction direction, final String fieldName,
            final RelationshipType relationshipType);
}