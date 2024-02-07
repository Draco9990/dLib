package dLib.ui.screens;

import dLib.ui.data.screens.GeneratedScreenData;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;

public abstract class GeneratedAbstractScreen extends AbstractScreen{
    public GeneratedAbstractScreen(GeneratedScreenData data){
        for(String key : data.elementData.keySet()){
            UIElement generatedElement = data.elementData.get(key).makeLiveInstance(null);
            if(generatedElement != null){
                Reflection.setFieldValue(key, this, generatedElement);
            }
        }
    }
}