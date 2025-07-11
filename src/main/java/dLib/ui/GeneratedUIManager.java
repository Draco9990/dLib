package dLib.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import dLib.util.DLibLogger;
import dLib.util.Reflection;
import org.clapper.util.classutil.ClassFinder;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class GeneratedUIManager {
    private static HashMap<String, String> generatedUIFiles = new HashMap<>();

    public static FileHandle getGeneratedElementFile(Class<?> generatedClass){
        String path = generatedUIFiles.get(generatedClass.getName());

        if (path == null) {
            return null;
        }

        return Gdx.files.internal(path);
    }

    public static void initialize() {
        ClassFinder finder = Reflection.generateClassFinder();

        Iterator var4 = ((LinkedHashMap<String, File>)Reflection.getFieldValue("placesToSearch", finder)).values().iterator();

        String className;
        while(var4.hasNext()) {
            File file = (File)var4.next();
            className = file.getPath();

            if (className.toLowerCase().endsWith(".jar")) {
                processJar(className);
            }
            else if (className.toLowerCase().endsWith(".zip")) {
                processZip(className);
            }
            else {
                processDirectory(file);
            }
        }
    }

    private static void processJar(String jarName) {
        JarFile jar = null;

        try {
            jar = new JarFile(jarName);
            File jarFile = new File(jarName);
            processOpenZip(jar, jarFile);
        } catch (IOException e1) {
            DLibLogger.logError("Can't open jar file " + jarName + " due to " + e1.getMessage());
        }

        try {
            if (jar != null) {
                jar.close();
            }
        } catch (IOException e2) {
            DLibLogger.logError("Can't close " + jarName + " due to " + e2.getMessage());
        }
    }

    private static void processZip(String zipName) {
        ZipFile zip = null;

        try {
            zip = new ZipFile(zipName);
            File zipFile = new File(zipName);
            processOpenZip(zip, zipFile);
        } catch (IOException e1) {
            DLibLogger.logError("Can't open zip file " + zipName + " due to " + e1.getMessage());
        }

        try {
            if (zip != null) {
                zip.close();
            }
        } catch (IOException e2) {
            DLibLogger.logError("Can't close " + zipName + " due to " + e2.getMessage());
        }
    }

    private static void processOpenZip(ZipFile zip, File zipFile) {
        String zipName = zipFile.getPath();
        Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".genui")) {
                try {
                    //Put in map file name and file path
                    String fileName = entry.getName().substring(entry.getName().lastIndexOf("/") + 1);
                    fileName = fileName.replace(".genui", "");
                    generatedUIFiles.put(fileName, entry.getName());
                } catch (Exception e) {
                    DLibLogger.logError("Can't add " + entry.getName() + " from " + zipName + " due to " + e.getMessage());
                }
            }
        }
    }

    private static List<File> processDirectory(File dir) {
        List<File> genuiFiles = new ArrayList<>();
        findGenuiFiles(dir, genuiFiles);
        return genuiFiles;
    }

    private static void findGenuiFiles(File dir, List<File> genuiFiles) {
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findGenuiFiles(file, genuiFiles);
                } else if (file.getName().toLowerCase().endsWith(".genui")) {
                    genuiFiles.add(file);
                }
            }
        }
    }
}
