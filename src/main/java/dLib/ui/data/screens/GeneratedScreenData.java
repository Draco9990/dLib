package dLib.ui.data.screens;

import dLib.ui.data.UIElementData;
import dLib.ui.screens.GeneratedAbstractScreen;

import java.util.HashMap;

public class GeneratedScreenData {
    public HashMap<String, UIElementData> elementData = new HashMap<>();

    public void addElement(String key, UIElementData value){
        elementData.put(key, value);
    }

    public void removeElement(String key){
        elementData.remove(key);
    }
}
