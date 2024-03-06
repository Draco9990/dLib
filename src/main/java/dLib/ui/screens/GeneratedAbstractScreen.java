package dLib.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import dLib.ui.data.AbstractScreenData;
import dLib.ui.data.prefabs.BackgroundData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.Reflection;

import java.util.ArrayList;

public abstract class GeneratedAbstractScreen extends AbstractScreen{
    public GeneratedAbstractScreen(){
        initialize();
    }

    public void initialize(){
        AbstractScreenData screenData = loadDataFromClassname(getClass().getSimpleName());
        ArrayList<UIElement> makeLiveItems = screenData.makeLiveItems();
        for (int i = 0; i < makeLiveItems.size(); i++) {
            UIElement element = makeLiveItems.get(i);

            if(screenData.data.get(i) instanceof BackgroundData){
                background = (Renderable) element;
            }

            Reflection.setFieldValue(element.getId(), this, element);

            addElement(element);
        }
    }

    /** Class Methods */
    private static AbstractScreenData loadDataFromClassname(String className){
        FileHandle fileHandle = Gdx.files.internal("dLibResources/screens/" + className + ".dscreen");
        return AbstractScreenData.deserializeFromString(fileHandle.readString());
    }
}