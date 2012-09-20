package fr.imag.model2roo.addon.graph;

import static org.springframework.roo.model.JavaType.OBJECT;
import static org.springframework.roo.model.RooJavaType.ROO_JAVA_BEAN;
import static org.springframework.roo.model.RooJavaType.ROO_TO_STRING;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author jccastrejon
 * 
 */
@Component
@Service
public class GraphOperationsImpl implements GraphOperations {

    /**
     * 
     */
    private static final AnnotationMetadataBuilder ROO_JAVA_BEAN_BUILDER = new AnnotationMetadataBuilder(ROO_JAVA_BEAN);

    /**
     * 
     */
    private static final AnnotationMetadataBuilder ROO_TO_STRING_BUILDER = new AnnotationMetadataBuilder(ROO_TO_STRING);

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
     * Use ProjectOperations to install new dependencies, plugins, properties,
     * etc into the project configuration
     */
    @Reference
    private ProjectOperations projectOperations;

    /**
     * 
     */
    @Reference
    TypeManagementService typeManagementService;

    /**
     * 
     */
    @Reference
    private TypeLocationService typeLocationService;

    /**
     * 
     */
    public boolean isGraphSetupAvailable() {
        return (!fileManager.exists(this.getContextPath()));
    }

    /**
     * 
     */
    public void graphSetup(final GraphProvider provider, final String dataStoreLocation) {
        this.addDependencies(provider);
        this.addConfiguration(provider, dataStoreLocation);
    }

    /**
     * 
     */
    public boolean isNewEntityAvailable() {
        return (fileManager.exists(this.getContextPath()));
    }

    /**
     * 
     */
    public void newEntity(final JavaType name, final JavaType superClass, final boolean isAbstract,
            final GraphProvider graphProvider) {
        int modifier;
        String entityId;
        FieldMetadataBuilder fieldBuilder;
        ClassOrInterfaceTypeDetails entityDetails;
        ClassOrInterfaceTypeDetails superClassDetails;
        ClassOrInterfaceTypeDetailsBuilder entityBuilder;

        Validate.notNull(name, "Entity name required");
        Validate.notNull(graphProvider, "Graph provider required");

        // Class modifier
        modifier = Modifier.PUBLIC;
        if (isAbstract) {
            modifier = Modifier.ABSTRACT;
        }

        // Create entity class
        entityId = PhysicalTypeIdentifier.createIdentifier(name, pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
        entityBuilder = new ClassOrInterfaceTypeDetailsBuilder(entityId, modifier, name, PhysicalTypeCategory.CLASS);

        // Base class
        if (!superClass.equals(OBJECT)) {
            superClassDetails = typeLocationService.getTypeDetails(superClass);
            if (superClassDetails != null) {
                entityBuilder.setSuperclass(new ClassOrInterfaceTypeDetailsBuilder(superClassDetails));
            }
        }
        entityBuilder.setExtendsTypes(Arrays.asList(superClass));

        // Associate appropriate annotations
        entityBuilder.setAnnotations(graphProvider.getClassAnnotations());
        entityBuilder.addAnnotation(ROO_JAVA_BEAN_BUILDER);
        entityBuilder.addAnnotation(ROO_TO_STRING_BUILDER);
        typeManagementService.createOrUpdateTypeOnDisk(entityBuilder.build());

        // Add id field
        entityDetails = typeLocationService.getTypeDetails(name);
        fieldBuilder = new FieldMetadataBuilder(entityDetails.getDeclaredByMetadataId(), Modifier.PROTECTED,
                graphProvider.getIdAnnotations(), new JavaSymbolName("nodeId"), JavaType.LONG_OBJECT);
        typeManagementService.addField(fieldBuilder.build());
    }

    /**
     * 
     */
    public boolean isNewRepositoryAvailable() {
        return (fileManager.exists(this.getContextPath()));
    }

    /**
     * 
     */
    public void newRepository(final JavaType domainType, final GraphProvider graphProvider) {
        String entityId;
        JavaType repository;
        List<JavaType> repositoryParameters;
        ClassOrInterfaceTypeDetailsBuilder entityBuilder;

        // Create repository class
        repository = new JavaType(domainType.getPackage().getFullyQualifiedPackageName() + "."
                + domainType.getSimpleTypeName() + "Repository");
        entityId = PhysicalTypeIdentifier.createIdentifier(repository, pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
        entityBuilder = new ClassOrInterfaceTypeDetailsBuilder(entityId, Modifier.PUBLIC, repository,
                PhysicalTypeCategory.CLASS);

        // Add neo4j repository base class
        repositoryParameters = new ArrayList<JavaType>();
        repositoryParameters.add(domainType);
        for (String baseClass : graphProvider.getRepositoryBaseClasses()) {
            entityBuilder.addExtendsTypes(new JavaType(baseClass, 0, DataType.TYPE, null, repositoryParameters));
        }

        // Save repository
        typeManagementService.createOrUpdateTypeOnDisk(entityBuilder.build());
    }

    /**
     * 
     * @return
     */
    public boolean isNewRelationshipEntityAvailable() {
        return (fileManager.exists(this.getContextPath()));
    }

    /**
     * 
     * @param name
     */
    public void newRelationshipEntity(final GraphProvider graphProvider, final JavaType name, final String type,
            final JavaType startNode, final JavaType endNode, List<String> properties) {
        String entityId;
        FieldMetadataBuilder fieldBuilder;
        ClassOrInterfaceTypeDetails entityDetails;
        ClassOrInterfaceTypeDetailsBuilder entityBuilder;
        List<AnnotationMetadataBuilder> relationshipAnnotations;

        // Create relationship class
        entityId = PhysicalTypeIdentifier.createIdentifier(name, pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
        entityBuilder = new ClassOrInterfaceTypeDetailsBuilder(entityId, Modifier.PUBLIC, name,
                PhysicalTypeCategory.CLASS);

        // Associate appropriate annotations
        relationshipAnnotations = graphProvider.getRelationshipEntityAnnotations();
        for (AnnotationMetadataBuilder annotation : relationshipAnnotations) {
            annotation.addStringAttribute("type", type);
        }
        entityBuilder.setAnnotations(relationshipAnnotations);
        entityBuilder.addAnnotation(ROO_JAVA_BEAN_BUILDER);
        entityBuilder.addAnnotation(ROO_TO_STRING_BUILDER);
        typeManagementService.createOrUpdateTypeOnDisk(entityBuilder.build());

        // Id
        entityDetails = typeLocationService.getTypeDetails(name);
        fieldBuilder = new FieldMetadataBuilder(entityDetails.getDeclaredByMetadataId(), Modifier.PROTECTED,
                graphProvider.getIdAnnotations(), new JavaSymbolName("relationshipId"), JavaType.LONG_OBJECT);
        entityBuilder.addField(fieldBuilder.build());

        // Start node
        fieldBuilder = new FieldMetadataBuilder(entityDetails.getDeclaredByMetadataId(), Modifier.PROTECTED,
                graphProvider.getRelationshipStartNodeAnnotations(), new JavaSymbolName("startNode"), startNode);
        entityBuilder.addField(fieldBuilder.build());

        // End node
        fieldBuilder = new FieldMetadataBuilder(entityDetails.getDeclaredByMetadataId(), Modifier.PROTECTED,
                graphProvider.getRelationshipEndNodeAnnotations(), new JavaSymbolName("endNode"), endNode);
        entityBuilder.addField(fieldBuilder.build());

        // Properties
        if (properties != null) {
            // One field of type String for each property
            for (String property : properties) {
                fieldBuilder = new FieldMetadataBuilder(entityDetails.getDeclaredByMetadataId(), Modifier.PROTECTED,
                        new ArrayList<AnnotationMetadataBuilder>(), new JavaSymbolName(property), JavaType.STRING);
                entityBuilder.addField(fieldBuilder.build());
            }
        }

        typeManagementService.createOrUpdateTypeOnDisk(entityBuilder.build());
    }

    /**
     * 
     */
    public boolean isNewRelationshipAvailable() {
        return (fileManager.exists(this.getContextPath()));
    }

    /**
     * 
     */
    public void newRelationship(final GraphProvider graphProvider, final JavaType fromNode,
            final JavaType relationNode, final boolean isVia, final String type, final Direction direction,
            final String fieldName, final RelationshipType relationshipType) {
        List<JavaType> parameters;
        FieldMetadataBuilder fieldBuilder;
        ClassOrInterfaceTypeDetails entityDetails;
        List<AnnotationMetadataBuilder> fieldAnnotations;

        fieldBuilder = null;
        entityDetails = typeLocationService.getTypeDetails(fromNode);
        switch (relationshipType) {
        case SINGLE:
            fieldBuilder = new FieldMetadataBuilder(entityDetails.getDeclaredByMetadataId(), Modifier.PROTECTED,
                    new ArrayList<AnnotationMetadataBuilder>(), new JavaSymbolName(fieldName), relationNode);
            break;
        case MODIFIABLE:
            parameters = new ArrayList<JavaType>();
            parameters.add(relationNode);
            fieldBuilder = new FieldMetadataBuilder(entityDetails.getDeclaredByMetadataId(), Modifier.PROTECTED,
                    new ArrayList<AnnotationMetadataBuilder>(), new JavaSymbolName(fieldName), new JavaType(
                            "java.util.Set", 0, DataType.TYPE, null, parameters));
            break;
        case READ_ONLY:
            parameters = new ArrayList<JavaType>();
            parameters.add(relationNode);
            fieldBuilder = new FieldMetadataBuilder(entityDetails.getDeclaredByMetadataId(), Modifier.PROTECTED,
                    new ArrayList<AnnotationMetadataBuilder>(), new JavaSymbolName(fieldName), new JavaType(
                            "java.lang.Iterable", 0, DataType.TYPE, null, parameters));
            break;
        }

        if (fieldBuilder != null) {
            // Associate appropriate annotations
            fieldAnnotations = graphProvider.getRelationshipAnnotations();
            if (isVia) {
                fieldAnnotations = graphProvider.getRelationshipViaAnnotations();
            }
            for (AnnotationMetadataBuilder annotation : fieldAnnotations) {
                if (type != null) {
                    annotation.addStringAttribute("type", type);
                }
                if (direction != null) {
                    annotation.addEnumAttribute("direction", Direction.class.getCanonicalName(), direction.toString());
                }
                fieldBuilder.addAnnotation(annotation);
            }

            typeManagementService.addField(fieldBuilder.build());
        }
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
        String outputContents;
        InputStream templateStream;
        OutputStream configurationStream;

        // Create a new graph configuration file every time
        contextPath = this.getContextPath();
        if (this.fileManager.exists(contextPath)) {
            this.fileManager.delete(contextPath);
        }

        // The new file content is based on the appropriate provider template
        templateStream = null;
        configurationStream = null;
        try {
            // Read template contents and update required variables
            templateStream = FileUtils.getInputStream(this.getClass(), "applicationContext-graph-"
                    + provider.name().toLowerCase() + ".xml");
            outputContents = IOUtils.toString(templateStream);
            outputContents = outputContents.replace("${store.location}", dataStoreLocation);
            configurationStream = this.fileManager.createFile(contextPath).getOutputStream();
            IOUtils.write(outputContents, configurationStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(templateStream);
            IOUtils.closeQuietly(configurationStream);
        }
    }

    /**
     * 
     * @return
     */
    private String getContextPath() {
        return pathResolver.getFocusedIdentifier(Path.SPRING_CONFIG_ROOT, "applicationContext-graph.xml");
    }
}