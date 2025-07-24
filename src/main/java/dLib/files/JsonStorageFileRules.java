package dLib.files;

import java.util.function.Supplier;

public class JsonStorageFileRules<T extends JsonDataFile> {
    public String encryptionKey = null;

    public boolean saveSteamCloud;

    public boolean saveLocal;
    public String localRelativeDirPath = "";

    public String extension = ".json";

    public String fileName;

    public boolean perSave;

    public Supplier<T> makeNew;
}
