package dLib.tools.uicreator.ui.elements;

import dLib.tools.uicreator.UCEditor;
import dLib.ui.elements.prefabs.PropertyEditor;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.util.function.BiConsumer;

public class UCEPropertyEditor extends PropertyEditor {
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
        public UCEPEPropertyGroup(String name) {
            super(name);

            BiConsumer refreshElement = (__, ___) -> {
                UCEPropertyEditor editor = getParentOfType(UCEPropertyEditor.class);
                UCEditor mainEditor = getParentOfType(UCEditor.class);

                mainEditor.itemTree.refreshItem(editor.currentObject);
            };

            propertyList.addOnPropertyAddedConsumer(property -> property.addOnValueChangedListener(refreshElement));
            propertyList.addOnPropertyRemovedConsumer(property -> property.removeOnValueChangedListener(refreshElement));
        }
    }
}
