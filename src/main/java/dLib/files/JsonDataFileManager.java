package dLib.files;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamRemoteStorage;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import dLib.util.AESEncryption;
import dLib.util.DLibLogger;
import dLib.util.Reflection;
import dLib.util.SerializationHelpers;
import dLib.util.helpers.SteamHelpers;

import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class JsonDataFileManager {
    public static HashMap<Class<? extends JsonDataFile>, JsonStorageFileRules> fileRules = new HashMap<>();

    public static void registerFileRules(Class<? extends JsonDataFile> fileClass, JsonStorageFileRules rules) {
        fileRules.put(fileClass, rules);
    }

    public static void saveFile(JsonDataFile file){
        if(!fileRules.containsKey(file.getClass())) {
            throw new IllegalArgumentException("No rules registered for " + file.getClass().getName());
        }

        JsonStorageFileRules rules = fileRules.get(file.getClass());

        String jsonOutput = new Gson().toJson(file);
        if(rules.encryptionKey != null){
            jsonOutput = AESEncryption.encrypt(jsonOutput, rules.encryptionKey);
        }

        String fileName = rules.fileName;
        if(rules.perSave){
            fileName += CardCrawlGame.saveSlot;
        }
        fileName += rules.extension;

        if(rules.saveLocal){
            String stsDir = Gdx.files.local("").file().getAbsolutePath() + rules.localRelativeDirPath + "/";
            try{
                Path pathToFile = Paths.get(stsDir + fileName);
                Files.createDirectories(pathToFile.getParent());

                FileWriter writer = new FileWriter(stsDir + fileName, false);
                writer.write(jsonOutput);
                writer.close();
            }
            catch (Exception e) {
                DLibLogger.log("Could not create directories for file " + rules.localRelativeDirPath + " due to " + e);
                e.printStackTrace();
            }
        }
        if(rules.saveSteamCloud && SteamHelpers.isSteamAvailable()){
            SteamRemoteStorage remoteStorage = SteamHelpers.remoteStorage;
            if(remoteStorage != null){
                try{
                    remoteStorage.fileWrite(fileName, SerializationHelpers.toByteBuffer(jsonOutput));
                }catch (Exception e){
                    DLibLogger.logError("Failed to save file to Steam Cloud due to " + e.getLocalizedMessage(), DLibLogger.ErrorType.NON_FATAL);
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T extends JsonDataFile> T load(Class<T> file){
        if(!fileRules.containsKey(file)) {
            throw new IllegalArgumentException("No rules registered for " + file.getName());
        }

        JsonStorageFileRules<T> rules = fileRules.get(file);

        String fileName = rules.fileName;
        if(rules.perSave){
            fileName += CardCrawlGame.saveSlot;
        }
        fileName += rules.extension;

        return loadForFile(file, rules, fileName);
    }
    public static <T extends JsonDataFile> T load(Class<T> file, int saveSlot){
        if(!fileRules.containsKey(file)) {
            throw new IllegalArgumentException("No rules registered for " + file.getName());
        }

        JsonStorageFileRules<T> rules = fileRules.get(file);

        String fileName = rules.fileName;
        if(rules.perSave){
            fileName += saveSlot;
        }
        fileName += rules.extension;

        return loadForFile(file, rules, fileName);
    }
    private static <T extends JsonDataFile> T loadForFile(Class<T> file, JsonStorageFileRules<T> rules, String fileName){
        String jsonData = null;
        if(rules.saveSteamCloud && SteamHelpers.isSteamAvailable()){
            SteamRemoteStorage remoteStorage = SteamHelpers.remoteStorage;
            if(remoteStorage != null){
                try{
                    ByteBuffer dataBuffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);
                    remoteStorage.fileRead(fileName, dataBuffer);
                    jsonData = SerializationHelpers.fromByteBuffer(dataBuffer);
                }catch (Exception e){
                    DLibLogger.logError("Failed to load file from Steam Cloud due to " + e.getLocalizedMessage(), DLibLogger.ErrorType.NON_FATAL);
                    e.printStackTrace();
                }
            }
        }
        if(jsonData == null && rules.saveLocal){
            String stsDir = Gdx.files.local("").file().getAbsolutePath() + rules.localRelativeDirPath + "/";
            try{
                Path pathToFile = Paths.get(stsDir + fileName);
                Files.createDirectories(pathToFile.getParent());

                jsonData = new String(Files.readAllBytes(pathToFile), StandardCharsets.UTF_8);
            }
            catch (Exception e) {
                DLibLogger.log("Could not create directories for file " + rules.localRelativeDirPath + " due to " + e);
                e.printStackTrace();
            }
        }

        if(jsonData == null){
            return rules.makeNew.get();
        }

        if(rules.encryptionKey != null){
            jsonData = AESEncryption.decrypt(jsonData, rules.encryptionKey);
        }

        return new Gson().fromJson(jsonData, file);
    }

    public static void delete(Class<? extends JsonDataFile> file){
        if(!fileRules.containsKey(file)) {
            throw new IllegalArgumentException("No rules registered for " + file.getName());
        }

        JsonStorageFileRules rules = fileRules.get(file);

        String fileName = rules.fileName + rules.extension;

        deleteForFile(file, rules, fileName);
    }
    public static void delete(Class<? extends JsonDataFile> file, int saveSlot){
        if(!fileRules.containsKey(file)) {
            throw new IllegalArgumentException("No rules registered for " + file.getName());
        }

        JsonStorageFileRules rules = fileRules.get(file);

        String fileName = rules.fileName;
        if(rules.perSave){
            fileName += saveSlot;
        }
        fileName += rules.extension;

        deleteForFile(file, rules, fileName);
    }
    private static void deleteForFile(Class<? extends JsonDataFile> file, JsonStorageFileRules rules, String fileName){
        if(rules.saveSteamCloud && SteamHelpers.isSteamAvailable()){
            SteamRemoteStorage remoteStorage = SteamHelpers.remoteStorage;
            if(remoteStorage != null){
                try{
                    remoteStorage.fileDelete(fileName);
                }catch (Exception e){
                    DLibLogger.logError("Failed to delete file from Steam Cloud due to " + e.getLocalizedMessage(), DLibLogger.ErrorType.NON_FATAL);
                    e.printStackTrace();
                }
            }
        }
        if(rules.saveLocal){
            String stsDir = Gdx.files.local("").file().getAbsolutePath() + rules.localRelativeDirPath + "/";
            try{
                if(Files.exists(Paths.get(stsDir + fileName))){
                    Files.delete(Paths.get(stsDir + fileName));
                }
            }
            catch (Exception e) {
                DLibLogger.log("Could not create delete file " + rules.localRelativeDirPath + " due to " + e);
                e.printStackTrace();
            }
        }
    }
}
