package dLib.tools.uicreator.ui.elements.interfaces;

import com.badlogic.gdx.Gdx;
import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.ui.GeneratedUIManager;
import dLib.ui.elements.UIElement;
import dLib.util.DLibLogger;
import dLib.util.SerializationHelpers;
import dLib.util.events.globalevents.Constructable;

import java.util.Arrays;

public interface IGeneratedUIElement {
    default void loadGeneratedData() {
        boolean found = false;
        Class<?> clazz = getClass();
        while (clazz != null && clazz != Object.class) {
            if (Arrays.asList(clazz.getInterfaces()).contains(IGeneratedUIElement.class)) {
                found = true;
                break;
            }
            clazz = clazz.getSuperclass();
        }

        if(!found){
            return;
        }

        String generatedObjectData = GeneratedUIManager.getGeneratedElementFile(clazz).readString();

        RootElement.RootElementData data = SerializationHelpers.fromString(generatedObjectData);
        data.inEditor = false;
        ((UIElement)this).addChild(data.makeUIElement((UIElement)this));
    }
}
