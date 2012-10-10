package fr.imag.model2roo.addon.polyglot;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CommandMarker;

/**
 * 
 * @author jccastrejon
 * 
 */
@Component
@Service
public class PolyglotCommands implements CommandMarker {

    /**
     * Get a reference to the PolyglotOperations from the underlying OSGi
     * container
     */
    @Reference
    private PolyglotOperations operations;

    /**
     * This method is optional. It allows automatic command hiding in situations
     * when the command should not be visible. For example the 'entity' command
     * will not be made available before the user has defined his persistence
     * settings in the Roo shell or directly in the project.
     * 
     * You can define multiple methods annotated with
     * {@link CliAvailabilityIndicator} if your commands have differing
     * visibility requirements.
     * 
     * @return true (default) if the command should be visible at this stage,
     *         false otherwise
     */
    @CliAvailabilityIndicator("polyglot setup")
    public boolean isCommandAvailable() {
        return operations.isSetupAvailable();
    }

    /**
     * This method registers a command with the Roo shell. It has no command
     * attribute.
     * 
     */
    @CliCommand(value = "polyglot setup", help = "Setup Polyglot addon")
    public void setup() {
        operations.setup();
    }
}