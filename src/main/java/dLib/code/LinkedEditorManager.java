package dLib.code;

import dLib.plugin.intellij.IntelliJEditor;
import dLib.plugin.intellij.PluginManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LinkedEditorManager {

    //region Singleton

    private static LinkedEditorManager instance;

    public static LinkedEditorManager get(){
        if(instance == null){
            instance = new LinkedEditorManager();
        }
        return instance;
    }

    public static void initialize(){
        get().registerEditor(new IntelliJEditor());
    }

    //endregion

    //region Variables

    private ArrayList<LinkedEditor> registeredEditors = new ArrayList<>();

    private LinkedEditor activeEditor;

    //endregion

    public LinkedEditorManager(){

    }

    public void registerEditor(LinkedEditor editor){
        registeredEditors.add(editor);
    }

    public void setActiveEditor(LinkedEditor editor){
        activeEditor = editor;
    }

    public LinkedEditor getActiveEditor(){
        return activeEditor;
    }
}
