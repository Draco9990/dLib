package dLib.ui.screens;

import dLib.ui.data.AbstractScreenData;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;

public abstract class GeneratedAbstractScreen extends AbstractScreen{
    public GeneratedAbstractScreen(AbstractScreenData data){/*
        for(String key : data.data.keySet()){
            UIElement generatedElement = data.data.get(key).makeLiveInstance(null);
            if(generatedElement != null){
                Reflection.setFieldValue(key, this, generatedElement);
            }
        }*/
    }
}