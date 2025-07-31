package dLib.files;

import com.google.gson.Gson;
import dLib.util.AESEncryption;
import dLib.util.DLibLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class JsonDataFile {
    //region Methods

    public void save() {
        JsonDataFileManager.save(this);
    }

    public static <T extends JsonDataFile> T load(Class<T> c){
        return JsonDataFileManager.load(c);
    }

    public static <T extends JsonDataFile> T load(Class<T> c, int saveSlot){
        return JsonDataFileManager.load(c, saveSlot);
    }

    public static void delete(Class<? extends JsonDataFile> c) {
        JsonDataFileManager.delete(c);
    }
    public static void delete(Class<? extends JsonDataFile> c, int saveSlot) {
        JsonDataFileManager.delete(c, saveSlot);
    }

    //endregion Methods
}
