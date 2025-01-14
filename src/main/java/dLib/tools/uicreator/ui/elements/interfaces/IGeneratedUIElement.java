package dLib.tools.uicreator.ui.elements.interfaces;

import com.badlogic.gdx.Gdx;
import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.ui.elements.UIElement;
import dLib.util.DLibLogger;
import dLib.util.SerializationHelpers;
import dLib.util.events.globalevents.Constructable;

public interface IGeneratedUIElement extends Constructable {
    @Override
    default void postConstruct() {
        Constructable.super.postConstruct();
        loadGeneratedData();
    }

    String getGeneratedDataPath();

    default void loadGeneratedData() {
        String generatedObjectData = Gdx.files.internal(getGeneratedDataPath()).readString();

        RootElement.RootElementData data = SerializationHelpers.fromString(generatedObjectData);
        data.inEditor = false;
        ((UIElement)this).addChild(data.makeUIElement());
    }
}
