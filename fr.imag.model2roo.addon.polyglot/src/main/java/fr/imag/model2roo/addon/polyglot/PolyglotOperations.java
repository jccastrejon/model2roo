package fr.imag.model2roo.addon.polyglot;


/**
 * Interface of operations this add-on offers. Typically used by a command type or an external add-on.
 *
 * @since 1.1
 */
public interface PolyglotOperations {

    /**
     * Indicate commands should be available
     * 
     * @return true if it should be available, otherwise false
     */
    boolean isSetupAvailable();
    
    /**
     * Setup all add-on artifacts (dependencies in this case)
     */
    void setup();
}