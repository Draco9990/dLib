package dLib.tools.uicreator.ui.elements;

import dLib.properties.objects.templates.TProperty;
import dLib.tools.uicreator.UCEditor;
import dLib.ui.elements.prefabs.PropertyEditor;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UCEPropertyEditor extends PropertyEditor {
    public boolean itemBeingModifiedExternally = false;

    private UIElementData currentObject;

    public UCEPropertyEditor(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);
    }

    @Override
    public boolean shouldBuildMultiline() {
        return true;
    }

    public void setProperties(UIElementData elementData){
        this.currentObject = elementData;
        setProperties(elementData.getEditableProperties());
    }

    @Override
    protected PropertyGroup makePropertyGroup(String category) {
        return new UCEPEPropertyGroup(category);
    }

    public static class UCEPEPropertyGroup extends PropertyGroup{
        private UUID onValueChangedEventID;

        public UCEPEPropertyGroup(String name) {
            super(name);

            TriConsumer refreshElement = (____, __, ___) -> {
                if(getParentOfType(UCEPropertyEditor.class).itemBeingModifiedExternally){
                    return;
                }

                UCEPropertyEditor editor = getParentOfType(UCEPropertyEditor.class);
                UCEditor mainEditor = getParentOfType(UCEditor.class);

                mainEditor.itemTree.refreshItem(editor.currentObject);
            };

            propertyList.addOnPropertyAddedConsumer(tProperty -> onValueChangedEventID = tProperty.onValueChangedEvent.subscribeManaged(refreshElement));
            propertyList.addOnPropertyRemovedConsumer(tProperty -> tProperty.onValueChangedEvent.unsubscribeManaged(onValueChangedEventID));
        }
    }
}
