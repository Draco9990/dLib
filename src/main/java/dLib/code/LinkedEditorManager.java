package dLib.code;

import dLib.code.editors.intellij.IntelliJEditor;

import java.util.ArrayList;

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

        get().setActiveEditor(get().registeredEditors.get(0)); //TODO: User selection or maybe not needed at all, maybe any that reply and can connect just work?
        //And then settings can check if there's a currently active editor and the editor would introduce itself
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
