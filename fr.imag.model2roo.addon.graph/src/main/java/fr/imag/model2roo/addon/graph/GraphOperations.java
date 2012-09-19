package fr.imag.model2roo.addon.graph;

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
}