package dLib.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import dLib.util.ProjectInfoFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GeneratedUIManager {
    private static ArrayList<String> generatedUIPaths = new ArrayList<>();

    public static boolean registerMod(Class<?> modClass){
        InputStream infoFilePath = modClass.getResourceAsStream("/dLib.json");
        ProjectInfoFile infoFile = new Gson().fromJson(new InputStreamReader(infoFilePath, StandardCharsets.UTF_8), ProjectInfoFile.class);
        if(infoFile == null){
            return false;
        }

        if(generatedUIPaths.contains(infoFile.generatedResourcesPath + "/generated/ui/")){
            return false;
        }

        generatedUIPaths.add(infoFile.generatedResourcesPath + "/generated/ui/");
        return true;
    }

    public static FileHandle getGeneratedElementFile(Class<?> generatedClass){
        for(String path : generatedUIPaths){
            FileHandle file = Gdx.files.internal(path + generatedClass.getName() + ".genui");
            if(file.exists()){
                return file;
            }
        }

        return null;
    }
}
