package mx.itesm.model2roo.handlers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.m2m.atl.common.ATLLogFormatter;
import org.eclipse.m2m.atl.common.ATLLogger;
import org.eclipse.m2m.atl.common.ConsoleStreamHandler;
import org.eclipse.m2m.atl.drivers.emf4atl.AtlEMFModelHandler;
import org.eclipse.m2m.atl.engine.vm.AtlLauncher;
import org.eclipse.m2m.atl.engine.vm.AtlModelHandler;
import org.eclipse.m2m.atl.engine.vm.ModelLoader;
import org.eclipse.m2m.atl.engine.vm.nativelib.ASMModel;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Model2Roo utility methods
 * 
 * @author jccastrejon
 * 
 */
public class Util {

    /**
     * Thread in which Roo scripts can be executed.
     * 
     * @author jccastrejon
     * 
     */
    static class RooThread implements Runnable {
        private File rooFile;
        private MessageConsoleStream consoleStream;

        public RooThread(final File rooFile, final MessageConsoleStream consoleStream) {
            this.rooFile = rooFile;
            this.consoleStream = consoleStream;
        }

        @Override
        public void run() {
            Process process;
            int processCode;

            try {
                process = Runtime.getRuntime().exec("roo script --file " + rooFile.getName(), null,
                                rooFile.getParentFile());
                processCode = process.waitFor();
                if (processCode != 0) {
                    throw new RuntimeException();
                } else {
                    consoleStream.println("Roo script successfully executed!");
                }
            } catch (Exception e) {
                consoleStream.println("The Roo script could not be successfully executed");
            }
        }
    }

    /**
     * Get the current selected elements when a plugin operation is executed.
     * 
     * @param event
     * @return
     */
    public static List<?> getSelectedItems(final ExecutionEvent event) {
        List<?> returnValue;
        IStructuredSelection currentSelection;

        currentSelection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        returnValue = currentSelection.toList();

        return returnValue;
    }

    /**
     * Get the URL of the given resourceName, according to the class path of the
     * current object.
     * 
     * @param currentObject
     * @param resourceName
     * @return
     */
    public static URL getResourceURL(final Object currentObject, final String resourceName) {
        URL returnValue;
        ClassLoader currentClassLoader;

        currentClassLoader = Util.getCurrentClassLoader(currentObject);
        returnValue = currentClassLoader.getResource(resourceName);

        return returnValue;
    }

    /**
     * Get an Input Stream of the given resourceName, according to the class
     * path of the current object.
     * 
     * @param currentObject
     * @param resourceName
     * @return
     */
    public static InputStream getResourceStream(final Object currentObject, final String resourceName) {
        InputStream returnValue;
        ClassLoader currentClassLoader;

        currentClassLoader = Util.getCurrentClassLoader(currentObject);
        returnValue = currentClassLoader.getResourceAsStream(resourceName);

        return returnValue;
    }

    /**
     * Transform from an Ecore model to a Spring Roo application.
     * 
     * @param currentObject
     * @param ecoreFile
     * @param consoleStream
     * @throws IOException
     * @throws InterruptedException
     */
    public static void ecore2Roo(final Object currentObject, final File ecoreFile,
                    final MessageConsoleStream consoleStream) throws IOException, InterruptedException {
        URL atlQuery;
        File rooFile;
        File pomFile;
        Handler rooHandler;
        ModelLoader modelLoader;
        ASMModel ecoreMetaModel;
        Map<String, URL> libraries;
        AtlEMFModelHandler modelHandler;
        Map<String, Object> transformationModels;

        // The roo script will be regenerated
        rooFile = new File(ecoreFile.getAbsolutePath().replace(".ecore", ".roo"));
        rooFile.delete();
        rooFile.createNewFile();

        // Delete the results of previous executions
        Util.deleteDirectory(new File(rooFile.getParentFile(), "/src"));
        pomFile = new File(rooFile.getParentFile(), "pom.xml");
        if (pomFile.exists()) {
            pomFile.delete();
        }
        if (rooFile.exists()) {
            rooFile.delete();
        }

        // Ours will be the only ATL logger
        for (Handler logHandler : ATLLogger.getLogger().getHandlers()) {
            ATLLogger.getLogger().removeHandler(logHandler);
        }
        rooHandler = new ConsoleStreamHandler(new BufferedOutputStream(new FileOutputStream(rooFile)));
        rooHandler.setFormatter(ATLLogFormatter.INSTANCE);
        rooHandler.setLevel(Level.ALL);
        ATLLogger.getLogger().addHandler(rooHandler);

        // Load query libraries
        libraries = new HashMap<String, URL>();
        libraries.put("Enum", Util.getResourceURL(currentObject, "/atl/Enum.asm"));
        libraries.put("Annotation", Util.getResourceURL(currentObject, "/atl/Annotation.asm"));
        libraries.put("Entity", Util.getResourceURL(currentObject, "/atl/Entity.asm"));

        // Execute the ATL query
        atlQuery = Util.getResourceURL(currentObject, "/atl/Ecore2Roo.asm");
        modelHandler = (AtlEMFModelHandler) AtlModelHandler.getDefault(AtlModelHandler.AMH_EMF);
        modelLoader = modelHandler.createModelLoader();
        ecoreMetaModel = modelLoader.loadModel("Ecore", null, "uri:http://www.eclipse.org/emf/2002/Ecore");
        transformationModels = new HashMap<String, Object>();
        transformationModels.put("Ecore", ecoreMetaModel);
        transformationModels.put("IN", modelLoader.loadModel("IN", ecoreMetaModel, new BufferedInputStream(
                        new FileInputStream(ecoreFile))));
        consoleStream.println("Generating Roo script: " + rooFile.getName() + "...");
        AtlLauncher.getDefault().launch(atlQuery, libraries, transformationModels, Collections.EMPTY_MAP,
                        Collections.EMPTY_LIST, Collections.EMPTY_MAP);
        consoleStream.println("Roo script successfully generated!");

        // Execute Roo script
        consoleStream.println("Executing Roo script...");
        new Thread(new RooThread(rooFile, consoleStream)).start();
    }

    /**
     * Get the Ecore file associated to the given Ecore package.
     * 
     * @param ecorePackage
     * @return
     */
    public static File getEcoreFile(final EPackage ecorePackage) {
        File returnValue;
        String ecoreFilePath;
        String workspacePath;

        workspacePath = Util.getWorkspacePath();
        ecoreFilePath = ecorePackage.eResource().getURI().toPlatformString(false);
        returnValue = new File(workspacePath + "/" + ecoreFilePath);

        return returnValue;
    }

    /**
     * Get the Ecore file associated to the given Eclipse resource.
     * 
     * @param resource
     * @return
     */
    public static File getEcoreFile(final Resource resource) {
        File returnValue;
        String ecoreFilePath;
        String workspacePath;

        workspacePath = Util.getWorkspacePath();
        ecoreFilePath = resource.getURI().toPlatformString(false);
        returnValue = new File(workspacePath + "/" + ecoreFilePath);

        return returnValue;
    }

    /**
     * Get the UML file associated to the given UML package.
     * 
     * @param umlPackage
     * @return
     */
    public static File getUmlFile(final org.eclipse.uml2.uml.Package umlPackage) {
        File returnValue;
        String umlFilePath;
        String workspacePath;

        workspacePath = Util.getWorkspacePath();
        umlFilePath = umlPackage.eResource().getURI().toPlatformString(false);
        returnValue = new File(workspacePath + "/" + umlFilePath);

        return returnValue;
    }

    /**
     * Get the current workspace path.
     * 
     * @return
     */
    private static String getWorkspacePath() {
        return ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
    }

    /**
     * Get the class loader associated to the specified object.
     * 
     * @param currentObject
     * @return
     */
    private static ClassLoader getCurrentClassLoader(final Object currentObject) {
        ClassLoader returnValue;

        returnValue = Thread.currentThread().getContextClassLoader();
        if (returnValue == null) {
            returnValue = currentObject.getClass().getClassLoader();
        }

        return returnValue;
    }

    /**
     * Delete a non-empty directory
     * 
     * @param path
     * @return
     */
    private static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
}
