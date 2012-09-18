package fr.imag.model2roo.addon.graph;

/**
 * Support graph providers.
 * 
 * @author jccastrejon
 * 
 */
public enum GraphProvider {
    Neo4j;

    @Override
    public String toString() {
        return name();
    }

    public String getConfigPrefix() {
        return "/configuration/graphstores/graphstore[@id='" + name() + "']";
    }

    public String getLocationProperty() {
        return name().toLowerCase() + ".location";
    }

    public String getPropertyFileName() {
        return name().toLowerCase() + ".properties";
    }
}