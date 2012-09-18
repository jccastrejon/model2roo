package fr.imag.model2roo.addon.graph;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Implementation of operations this add-on offers.
 * 
 * @since 1.1
 */
@Component
@Service
public class GraphOperationsImpl implements GraphOperations {

    /**
     * Use ProjectOperations to install new dependencies, plugins, properties,
     * etc into the project configuration
     */
    @Reference
    private ProjectOperations projectOperations;

    /**
     * 
     */
    @Reference
    private FileManager fileManager;

    /**
     * 
     */
    @Reference
    private PathResolver pathResolver;

    /**
     * 
     */
    public void graphSetup(final GraphProvider provider, final String dataStoreLocation) {
        this.addDependencies(provider);
        this.addConfiguration(provider, dataStoreLocation);
    }

    /**
     * 
     * @param provider
     */
    private void addDependencies(final GraphProvider provider) {
        Element configuration;
        List<Dependency> dependencies;
        List<Element> providerDependences;

        dependencies = new ArrayList<Dependency>();
        configuration = XmlUtils.getConfiguration(this.getClass());

        // Recover the dependencies associated to the specified provider
        providerDependences = XmlUtils.findElements(provider.getConfigPrefix(), configuration);
        for (Element dependency : providerDependences) {
            dependencies.add(new Dependency(dependency));
        }

        // Add all new dependencies to pom.xml
        projectOperations.addDependencies("", dependencies);
    }

    /**
     * 
     * @param provider
     * @param dataStoreLocation
     */
    private void addConfiguration(final GraphProvider provider, final String dataStoreLocation) {
        String contextPath;
        InputStream templateStream;
        StringBuilder outputContents;
        List<String> templateContents;
        OutputStream configurationStream;

        // Create a new graph configuration file every time
        contextPath = pathResolver.getFocusedIdentifier(Path.SPRING_CONFIG_ROOT, "applicationContext-graph.xml");
        if (this.fileManager.exists(contextPath)) {
            this.fileManager.delete(contextPath);
        }

        // The new file content is based on the appropriate provider template
        templateStream = null;
        configurationStream = null;
        try {
            templateStream = FileUtils.getInputStream(this.getClass(), "applicationContext-graph-"
                    + provider.name().toLowerCase() + ".xml");
            templateContents = IOUtils.readLines(templateStream);

            outputContents = new StringBuilder();
            for (String line : templateContents) {
                outputContents.append(line.replace("${store.location}", dataStoreLocation) + "\n");
            }

            configurationStream = this.fileManager.createFile(contextPath).getOutputStream();
            IOUtils.write(outputContents, configurationStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(templateStream);
            IOUtils.closeQuietly(configurationStream);
        }

    }
}