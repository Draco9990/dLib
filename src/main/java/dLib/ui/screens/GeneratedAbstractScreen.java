package dLib.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.Reflection;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class GeneratedAbstractScreen extends AbstractScreen{
    //region Variables
    //endregion

    //region Constructors

    public GeneratedAbstractScreen(){
        initialize();
    }

    public void initialize(){
        AbstractScreenData screenData = loadDataFromClassname(getClass().getSimpleName());
        ArrayList<UIElement> makeLiveItems = screenData.makeLiveItems();
        for (UIElement element : makeLiveItems) {
            Reflection.setFieldValue(element.getId(), this, element);

            addChildNCS(element); // TODO elements should have a setting for Controller Selectable
        }
    }

    //endregion

    //region Methods

    private static AbstractScreenData loadDataFromClassname(String className){
        FileHandle fileHandle = Gdx.files.internal("dLibResources/screens/" + className + ".dscreen");
        return (AbstractScreenData) GeneratedScreenData.deserializeFromString(fileHandle.readString());
    }

    //endregion

    public static class GeneratedScreenData extends AbstractScreenData implements Serializable {
        private static final long serialVersionUID = 1L;

        public String screenClass;

        @Override
        public GeneratedAbstractScreen makeUIElement() {
            return new GeneratedAbstractScreen() {
                @Override
                public String getModId() {
                    return modID;
                }
            };
        }
    }
}