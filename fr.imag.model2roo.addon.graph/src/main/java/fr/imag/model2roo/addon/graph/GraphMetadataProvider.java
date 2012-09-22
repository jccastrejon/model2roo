package fr.imag.model2roo.addon.graph;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.customdata.taggers.CustomDataKeyDecorator;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;

/**
 * Provides {@link GraphMetadata}. This type is called by Roo to retrieve the
 * metadata for this add-on. Use this type to reference external types and
 * services needed by the metadata type. Register metadata triggers and
 * dependencies here. Also define the unique add-on ITD identifier.
 * 
 * @since 1.1
 */
@Component
@Service
public final class GraphMetadataProvider extends AbstractItdMetadataProvider {

    /**
     * 
     */
    @Reference
    private CustomDataKeyDecorator customDataKeyDecorator;

    /**
     * The activate method for this OSGi component, this will be called by the
     * OSGi container upon bundle activation (result of the 'addon install'
     * command)
     * 
     * @param context
     *            the component context can be used to get access to the OSGi
     *            container (ie find out if certain bundles are active)
     */
    protected void activate(ComponentContext context) {
        metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(),
                getProvidesType());
        addMetadataTrigger(new JavaType(RooGraph.class.getName()));
    }

    /**
     * The deactivate method for this OSGi component, this will be called by the
     * OSGi container upon bundle deactivation (result of the 'addon uninstall'
     * command)
     * 
     * @param context
     *            the component context can be used to get access to the OSGi
     *            container (ie find out if certain bundles are active)
     */
    protected void deactivate(ComponentContext context) {
        metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(),
                getProvidesType());
        removeMetadataTrigger(new JavaType(RooGraph.class.getName()));
    }

    /**
     * Return an instance of the Metadata offered by this add-on
     */
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(String metadataIdentificationString, JavaType aspectName,
            PhysicalTypeMetadata governorPhysicalTypeMetadata, String itdFilename) {
        // Pass dependencies required by the metadata in through its constructor
        return new GraphMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata);
    }

    /**
     * Define the unique ITD file name extension, here the resulting file name
     * will be **_ROO_Graph.aj
     */
    public String getItdUniquenessFilenameSuffix() {
        return "Graph";
    }

    protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
        JavaType javaType = GraphMetadata.getJavaType(metadataIdentificationString);
        LogicalPath path = GraphMetadata.getPath(metadataIdentificationString);
        return PhysicalTypeIdentifier.createIdentifier(javaType, path);
    }

    protected String createLocalIdentifier(JavaType javaType, LogicalPath path) {
        return GraphMetadata.createIdentifier(javaType, path);
    }

    public String getProvidesType() {
        return GraphMetadata.getMetadataIdentiferType();
    }
}