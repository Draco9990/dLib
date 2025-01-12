package dLib.tools.uicreator.ui.elements;

import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import javassist.CtBehavior;

import java.util.HashMap;

public class GeneratedUIElementPatches implements PostInitializeSubscriber {
    //region Generated Element Tracking

    public static HashMap<String, String> generatedElements = new HashMap<>();

    @Override
    public void receivePostInitialize() {
        for(FileHandle handle : Gdx.files.internal(Gdx.files.getLocalStoragePath()).list()){
            if(handle.isDirectory() && handle.nameWithoutExtension().contains("Resources")){
                indexDirectoryRecursively(handle.path());
            }
        }
    }

    private static void indexDirectoryRecursively(String directory){
        FileHandle dir = Gdx.files.internal(directory);
        for(FileHandle file : dir.list()){
            if(file.isDirectory()){
                indexDirectoryRecursively(file.path());
            }
            else{
                if(file.extension().equals("genui")){
                    String className = file.nameWithoutExtension();
                    String classPath = file.path();
                    generatedElements.put(className, classPath);
                }
            }
        }
    }

    //endregion
}
