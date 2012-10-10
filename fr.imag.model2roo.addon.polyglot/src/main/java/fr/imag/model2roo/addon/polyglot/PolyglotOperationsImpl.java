package fr.imag.model2roo.addon.polyglot;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Implementation of operations this add-on offers.
 * 
 * @since 1.1
 */
@Component
@Service
public class PolyglotOperationsImpl implements PolyglotOperations {

    /**
     * Use ProjectOperations to install new dependencies, plugins, properties,
     * etc into the project configuration
     */
    @Reference
    private ProjectOperations projectOperations;

    /**
     * Use TypeLocationService to find types which are annotated with a given
     * annotation in the project
     */
    @Reference
    private TypeLocationService typeLocationService;

    /**
     * Use TypeManagementService to change types
     */
    @Reference
    private TypeManagementService typeManagementService;

    /** {@inheritDoc} */
    public boolean isSetupAvailable() {
        // Check if a project has been created
        return projectOperations.isFocusedProjectAvailable();
    }

    /** {@inheritDoc} */
    public void setup() {
        this.removeDependencies();
    }

    /**
     * 
     */
    private void removeDependencies() {
        Element configuration;
        List<Dependency> dependencies;
        List<Element> providerDependences;

        dependencies = new ArrayList<Dependency>();
        configuration = XmlUtils.getConfiguration(this.getClass());

        // Recover the dependencies associated to the specified provider
        providerDependences = XmlUtils.findElements("/configuration/removeDependencies", configuration);
        for (Element dependency : providerDependences) {
            dependencies.add(new Dependency(dependency));
        }

        // Add all new dependencies to pom.xml
        projectOperations.removeDependencies("", dependencies);
    }
}