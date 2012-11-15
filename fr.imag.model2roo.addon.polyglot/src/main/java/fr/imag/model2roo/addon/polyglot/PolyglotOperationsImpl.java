/*
 * Copyright 2012 jccastrejon
 *  
 * This file is part of Model2Roo.
 *
 * Model2Roo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Model2Roo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Model2Roo.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.imag.model2roo.addon.polyglot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.operations.jsr303.UploadedFileContentType;
import org.springframework.roo.file.monitor.event.FileDetails;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Implementation of the operations defined in the polyglot add-on.
 * 
 * @author jccastrejon
 * 
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

    @Reference
    private PathResolver pathResolver;

    @Reference
    private FileManager fileManager;

    /** {@inheritDoc} */
    public boolean isSetupAvailable() {
        // Check if a project has been created
        return projectOperations.isFocusedProjectAvailable();
    }

    /** {@inheritDoc} */
    public void setup() {
        projectOperations.removeDependencies("", this.getDependencies("/configuration/removeDependencies"));
    }

    /** {@inheritDoc} */
    public boolean isConfigureRestAvailable() {
        return this.isSetupAvailable();
    }

    /** {@inheritDoc} */
    public void configureRest() {
        Element webConfig;
        String webConfigPath;
        List<Element> addedBeans;
        Document webConfigDocument;

        // Add required dependencies
        projectOperations.addDependencies("", this.getDependencies("/configuration/addDependencies"));

        // Add json view resolver
        webConfigPath = pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF/spring/webmvc-config.xml");
        webConfigDocument = XmlUtils.readXml(fileManager.getInputStream(webConfigPath));
        webConfig = webConfigDocument.getDocumentElement();

        addedBeans = XmlUtils.findElements("/configuration/addBeans/bean", XmlUtils.getConfiguration(this.getClass()));
        for (Element element : addedBeans) {
            webConfig.appendChild(webConfigDocument.importNode(element, true));
        }

        fileManager.createOrUpdateTextFileIfRequired(webConfigPath, XmlUtils.nodeToString(webConfigDocument), false);
    }

    /** {@inheritDoc} */
    public boolean isConfigureRestMethodsAvailable() {
        return this.isSetupAvailable();
    }

    /** {@inheritDoc} */
    public void configureRestMethods(final JavaType entity) {
        String restMethods;
        String restImports;
        String outputContents;

        // Add rest methods and imports
        outputContents = this.getFileContents("restMethods-template.java");
        restImports = outputContents.substring(
                outputContents.indexOf("__INIT_IMPORTS__") + "__INIT_IMPORTS__".length(),
                outputContents.indexOf("__END_IMPORTS__"));
        restMethods = outputContents.substring(
                outputContents.indexOf("__INIT_METHODS__") + "__INIT_METHODS__".length(),
                outputContents.indexOf("__END_METHODS__"));

        restMethods = restMethods.replace("__ENTITY__", WordUtils.capitalize(entity.getSimpleTypeName()));
        restMethods = restMethods.replace("__ENTITY_LOWER__", entity.getSimpleTypeName().toLowerCase());

        this.updateFile(restImports + "\nimport org.springframework.web.bind.annotation.RequestMapping",
                "import org.springframework.web.bind.annotation.RequestMapping", entity.getSimpleTypeName()
                        + "Controller.java");
        this.updateFile("Controller {\n" + restMethods, "Controller {", entity.getSimpleTypeName() + "Controller.java");
    }

    /** {@inheritDoc} */
    public boolean isBlobSetupAvailable() {
        return this.isSetupAvailable();
    }

    /** {@inheritDoc} */
    public void blobSetup(final JavaType entity) {
        String binderMethod;
        String binderImports;
        String outputContents;

        // Add required dependencies
        projectOperations.addDependencies("", this.getDependencies("/configuration/blobDependencies"));

        // Add jsp tags to allow file upload
        this.copyFile("multi.tagx",
                pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "/WEB-INF/tags/form/multi.tagx"));
        this.copyFile("file.tagx",
                pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "/WEB-INF/tags/form/fields/file.tagx"));
        this.copyFile("url.tagx",
                pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "/WEB-INF/tags/form/fields/url.tagx"));
        this.copyFile("column.tagx",
                pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "/WEB-INF/tags/form/fields/column.tagx"));
        this.copyFile("table.tagx",
                pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "/WEB-INF/tags/form/fields/table.tagx"));

        // Use tags in create.jspx
        this.updateFile("form:multi", "form:create", "/" + entity.getSimpleTypeName().toLowerCase() + "*/create.jspx");

        // Add binder imports and method
        outputContents = this.getFileContents("binder-template.java");
        binderImports = outputContents.substring(
                outputContents.indexOf("__INIT_IMPORTS__") + "__INIT_IMPORTS__".length(),
                outputContents.indexOf("__END_IMPORTS__"));
        binderMethod = outputContents.substring(outputContents.indexOf("__INIT_BINDER__") + "__INIT_BINDER__".length(),
                outputContents.indexOf("__END_BINDER__"));

        this.updateFile(binderImports + "\nimport org.springframework.web.bind.annotation.RequestMapping",
                "import org.springframework.web.bind.annotation.RequestMapping", entity.getSimpleTypeName()
                        + "Controller.java");
        this.updateFile("Controller {\n" + binderMethod, "Controller {", entity.getSimpleTypeName() + "Controller.java");
    }

    /** {@inheritDoc} */
    public boolean isBlobPropertyAvailable() {
        return this.isSetupAvailable();
    }

    /** {@inheritDoc} */
    public void blobProperty(JavaType entity, String property, String contentType) {
        String showMethod;
        String showImports;
        String outputContents;

        // Update create.jspx
        this.updateFile("field:file field=\"" + property + "\"", "field:textarea field=\"" + property + "\"", "/"
                + entity.getSimpleTypeName().toLowerCase() + "*/create.jspx");

        // Update show.jspx
        this.updateFile("field:url field=\"" + property + "\"", "field:display field=\"" + property + "\"", "/"
                + entity.getSimpleTypeName().toLowerCase() + "*/show.jspx");

        // Update list.jspx
        this.updateFile("property=\"" + property + "\" url=\"true\"", "property=\"" + property + "\"", "/"
                + entity.getSimpleTypeName().toLowerCase() + "*/list.jspx");
        
        //TODO: Update update.jspx

        // Add show methods
        outputContents = this.getFileContents("getFile-template.java");
        showImports = outputContents.substring(
                outputContents.indexOf("__INIT_IMPORTS__") + "__INIT_IMPORTS__".length(),
                outputContents.indexOf("__END_IMPORTS__"));
        this.updateFile(showImports + "\nimport org.springframework.web.bind.annotation.RequestMapping",
                "import org.springframework.web.bind.annotation.RequestMapping", entity.getSimpleTypeName()
                        + "Controller.java");

        showMethod = outputContents.substring(outputContents.indexOf("__INIT_METHOD__") + "__INIT_METHOD__".length(),
                outputContents.indexOf("__END_METHOD__"));
        showMethod = showMethod.replace("__PROPERTY__", WordUtils.capitalize(property));
        showMethod = showMethod.replace("__ENTITY__", WordUtils.capitalize(entity.getSimpleTypeName()));
        showMethod = showMethod.replace("__ENTITY_LOWER__", entity.getSimpleTypeName().toLowerCase());
        showMethod = showMethod.replace("__CONTENT_TYPE__", UploadedFileContentType.valueOf(contentType)
                .getContentType());
        this.updateFile(showMethod + "\n@InitBinder", "@InitBinder", entity.getSimpleTypeName() + "Controller.java");
    }

    /**
     * Recover dependencies from the
     * src/main/resources/fr/imag/model2roo/addon/polyglot/configuration.xml
     * file, with the specified path
     * 
     * @param path
     * @return
     */
    private List<Dependency> getDependencies(final String path) {
        Element configuration;
        List<Dependency> returnValue;
        List<Element> providerDependences;

        returnValue = new ArrayList<Dependency>();
        configuration = XmlUtils.getConfiguration(this.getClass());

        // Recover the dependencies associated to the specified provider
        providerDependences = XmlUtils.findElements(path, configuration);
        for (Element dependency : providerDependences) {
            returnValue.add(new Dependency(dependency));
        }

        return returnValue;
    }

    /**
     * Copy the file contents to the destination path.
     * 
     * @param fileName
     * @param destinationPath
     */
    private void copyFile(final String fileName, final String destinationPath) {
        String outputContents;
        OutputStream outputStream;

        if (this.fileManager.exists(destinationPath)) {
            this.fileManager.delete(destinationPath);
        }

        outputStream = null;
        try {
            outputContents = this.getFileContents(fileName);
            outputStream = this.fileManager.createFile(destinationPath).getOutputStream();
            IOUtils.write(outputContents, outputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

    }

    /**
     * Get file contents.
     * 
     * @param fileName
     * @return
     */
    private String getFileContents(final String fileName) {
        String returnValue;
        InputStream inputStream;

        returnValue = null;
        inputStream = null;
        try {
            inputStream = FileUtils.getInputStream(this.getClass(), fileName);
            returnValue = IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return returnValue;
    }

    /**
     * Project root path.
     * 
     * @return
     */
    private String getRootPath() {
        return this.projectOperations.getFocusedModule().getRoot() + "**" + File.separator;
    }

    /**
     * Update a file contents.
     * 
     * @param newContent
     * @param startToken
     * @param endToken
     * @param fileName
     */
    private void updateFile(final String newContent, final String startToken, final String fileName) {
        String rootPath;
        String replaceContent;
        InputStream inputStream;
        String controllerContents;
        OutputStream outputStream;
        Set<FileDetails> matchingFiles;

        rootPath = this.getRootPath();
        matchingFiles = this.fileManager.findMatchingAntPath(rootPath + fileName);
        for (FileDetails fileDetails : matchingFiles) {
            inputStream = null;
            outputStream = null;
            try {
                // Get current content
                inputStream = new FileInputStream(fileDetails.getFile());
                controllerContents = IOUtils.toString(inputStream);

                // Decide whether the start token should be kept
                replaceContent = newContent;

                // Update content
                controllerContents = controllerContents.replace(startToken, replaceContent);
                outputStream = new FileOutputStream(fileDetails.getFile());
                IOUtils.write(controllerContents, outputStream);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            } finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            }
        }
    }
}