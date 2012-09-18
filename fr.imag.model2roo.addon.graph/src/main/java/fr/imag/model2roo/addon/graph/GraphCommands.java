package fr.imag.model2roo.addon.graph;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.operations.Cardinality;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * 
 * @author jccastrejon
 * 
 */
@Component
@Service
public class GraphCommands implements CommandMarker {

    /**
     * 
     */
    @Reference
    private GraphOperations operations;

    @CliAvailabilityIndicator("graph setup")
    public boolean isGraphSetupAvailable() {
        return true;
    }

    @CliCommand(value = "graph setup", help = "Install or updates a Graph database provider in your project")
    public void installGraphPersistence(
            @CliOption(key = { "provider" }, mandatory = true, help = "The graph provider to support") final GraphProvider graphProvider,
            @CliOption(key = { "databaseLocation" }, mandatory = true, help = "The database location to use") final String databaseLocation) {
        this.operations.graphSetup(graphProvider, databaseLocation);
    }

    @CliAvailabilityIndicator("graph entity")
    public boolean isGraphEntityAvailable() {
        return true;
    }

    @CliCommand(value = "graph entity", help = "Creates a new graph entity in SRC_MAIN_JAVA")
    public void newGraphEntity(
            @CliOption(key = "class", optionContext = "update,project", mandatory = true, help = "Name of the entity to create") final JavaType name,
            @CliOption(key = "extends", mandatory = false, unspecifiedDefaultValue = "java.lang.Object", help = "The superclass (defaults to java.lang.Object)") final JavaType superclass,
            @CliOption(key = "abstract", mandatory = false, specifiedDefaultValue = "true", unspecifiedDefaultValue = "false", help = "Whether the generated class should be marked as abstract") final boolean createAbstract,
            @CliOption(key = "testAutomatically", mandatory = false, specifiedDefaultValue = "true", unspecifiedDefaultValue = "false", help = "Create automatic integration tests for this entity") final boolean testAutomatically) {
    }

    @CliAvailabilityIndicator("graph relationship")
    public boolean isGraphRelationshipAvailable() {
        return true;
    }

    @CliCommand(value = "graph relationship", help = "Creates a new relationship between two graph entities")
    public void newGraphRelationship(
            @CliOption(key = "via", mandatory = false, help = "Name of explicit relationship class") final JavaType via,
            @CliOption(key = "fieldName", mandatory = true, help = "Name of the field name") final String fieldName,
            @CliOption(key = "type", mandatory = false, help = "Name of relationship") final String relationshipName,
            @CliOption(key = "from", optionContext = "update,project", mandatory = true, help = "Name of the start graph entity") final JavaType from,
            @CliOption(key = "to", optionContext = "update,project", mandatory = true, help = "Name of the end graph entity") final JavaType to,
            @CliOption(key = "direction", mandatory = false, unspecifiedDefaultValue = "OUTGOING", specifiedDefaultValue = "OUTGOIONG", help = "INCOMING or OUTGOING") final Direction direction,
            @CliOption(key = "cardinality", mandatory = false, unspecifiedDefaultValue = "ONE_TO_ONE", specifiedDefaultValue = "ONE_TO_MANY", help = "The relationship cardinarily") final Cardinality cardinality) {
    }
}