package dLib.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;
import dLib.util.SerializationHelpers;

import java.io.Serializable;
import java.util.ArrayList;

public class GeneratedAbstractScreen extends UIElement {
    //region Variables
    //endregion

    //region Constructors

    public GeneratedAbstractScreen(){
        super(0, 0, 1920, 1080);
        initialize();
    }

    public void initialize(){
        AbstractScreen_DEPRECATED.AbstractScreenData screenData = loadDataFromClassname(getClass().getSimpleName());
        ArrayList<UIElement> makeLiveItems = screenData.makeLiveItems();
        for (UIElement element : makeLiveItems) {
            Reflection.setFieldValue(element.getId(), this, element);

            addChildNCS(element); // TODO elements should have a setting for Controller Selectable
        }
    }

    //endregion

    //region Methods

    private static AbstractScreen_DEPRECATED.AbstractScreenData loadDataFromClassname(String className){
        FileHandle fileHandle = Gdx.files.internal("dLibResources/screens/" + className + ".dscreen");
        return (AbstractScreen_DEPRECATED.AbstractScreenData) SerializationHelpers.fromString(fileHandle.readString());
    }

    //endregion

    public static class GeneratedScreenData extends AbstractScreen_DEPRECATED.AbstractScreenData implements Serializable {
        private static final long serialVersionUID = 1L;

        public String screenClass;

        @Override
        public GeneratedAbstractScreen makeUIElement() {
            return new GeneratedAbstractScreen();
        }
    }
}