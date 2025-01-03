package dLib.files;

import dLib.util.AESEncryption;
import dLib.util.DLibLogger;
import dLib.util.Help;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonDataFile implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */
    private transient String filePath;

    private transient boolean encrypted = false;
    private transient String encryptionKey = "";

    /** Constructors */
    public JsonDataFile(String filePath){
        this.filePath = filePath;
    }

    /** Saving */
    public void save() {
        try{
            String jsonOutput = Help.Json.toJson(this);
            if(encrypted){
                jsonOutput = AESEncryption.encrypt(jsonOutput, encryptionKey);
            }

            Path pathToFile = Paths.get(this.filePath);
            Files.createDirectories(pathToFile.getParent());

            FileWriter writer = new FileWriter(filePath, false);
            writer.write(jsonOutput);
            writer.close();
        } catch (IOException e) {
            DLibLogger.log("Could not save file " + filePath + " due to " + e);
            e.printStackTrace();
        }
    }

    /** Loading */
    public static Object load(String filePath, Class<?> c){
        return load(filePath, c, null);
    }
    public static Object load(String filePath, Class<?> c, String decryptionKey){
        Path path = Paths.get(filePath);
        if(Files.exists(path)){
            try{
                String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                if(decryptionKey != null) json = AESEncryption.decrypt(json, decryptionKey);
                Object o = Help.Json.fromJson(json, c);
                return o;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Object load(Class<?> c) {
        if(encrypted) return load(filePath, c,encryptionKey);
        return load(filePath, c);
    }

    /** Utility methods */
    public boolean exists(){
        return new File(filePath).exists();
    }

    public static void delete(String filePath){
        try{
            Files.deleteIfExists(Paths.get(filePath));
        }catch (Exception e){
            DLibLogger.logError("Could not erase file due to " + e);
            e.printStackTrace();
        }
    }
}
