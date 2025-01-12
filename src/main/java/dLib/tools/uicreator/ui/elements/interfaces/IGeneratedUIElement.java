package dLib.tools.uicreator.ui.elements.interfaces;

import com.badlogic.gdx.Gdx;
import dLib.tools.uicreator.ui.elements.GeneratedUIElementPatches;
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

    default void loadGeneratedData() {
        if(!GeneratedUIElementPatches.generatedElements.containsKey(getClass().getName())){
            DLibLogger.logError("No generated data found for class: " + getClass().getName());
            return;
        }

        String generatedObjectData = Gdx.files.internal(GeneratedUIElementPatches.generatedElements.get(getClass().getName())).readString();

        RootElement.RootElementData data = SerializationHelpers.fromString(generatedObjectData);
        data.inEditor = false;
        ((UIElement)this).addChild(data.makeUIElement());
    }
}
