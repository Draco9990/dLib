package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.plugin.intellij.util.settings.IntelliJMethodBindingProperty;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.implementations.InteractableData;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.settings.Property;

import java.util.ArrayList;

public abstract class InteractableScreenEditorItem extends RenderableScreenEditorItem {
    /** Settings */
    private IntelliJMethodBindingProperty sOnLeftClick = (IntelliJMethodBindingProperty) new IntelliJMethodBindingProperty(screenEditor){
        @Override
        public Property<MethodBinding> setValue_internal(MethodBinding value) {
            super.setValue_internal(value);
            getElementData().onLeftClick = getValue();
            if(screenEditor != null) screenEditor.getPropertiesScreen().markForRefresh();
            return this;
        }
    }.setName("On Left Click:");
    private IntelliJMethodBindingProperty sOnLeftClickHeld = (IntelliJMethodBindingProperty) new IntelliJMethodBindingProperty(screenEditor){
        @Override
        public Property<MethodBinding> setValue_internal(MethodBinding value) {
            super.setValue_internal(value);
            getElementData().onLeftClickHeld = getValue();
            if(screenEditor != null) screenEditor.getPropertiesScreen().markForRefresh();
            return this;
        }
    }.addParameter("timeElapsed", Float.class).setName("On Left Click Held:");
    private IntelliJMethodBindingProperty sOnLeftClickRelease = (IntelliJMethodBindingProperty) new IntelliJMethodBindingProperty(screenEditor){
        @Override
        public Property<MethodBinding> setValue_internal(MethodBinding value) {
            super.setValue_internal(value);
            getElementData().onLeftClickRelease = getValue();
            if(screenEditor != null) screenEditor.getPropertiesScreen().markForRefresh();
            return this;
        }
    }.setName("On Left Click Release:");

    private IntelliJMethodBindingProperty sOnRightClick = (IntelliJMethodBindingProperty) new IntelliJMethodBindingProperty(screenEditor){
        @Override
        public Property<MethodBinding> setValue_internal(MethodBinding value) {
            super.setValue_internal(value);
            getElementData().onRightClick = getValue();
            if(screenEditor != null) screenEditor.getPropertiesScreen().markForRefresh();
            return this;
        }
    }.setName("On Right Click:");
    private IntelliJMethodBindingProperty sOnRightClickHeld = (IntelliJMethodBindingProperty) new IntelliJMethodBindingProperty(screenEditor){
        @Override
        public Property<MethodBinding> setValue_internal(MethodBinding value) {
            super.setValue_internal(value);
            getElementData().onRightClickHeld = getValue();
            if(screenEditor != null) screenEditor.getPropertiesScreen().markForRefresh();
            return this;
        }
    }.addParameter("timeElapsed", Float.class).setName("On Right Click Held:");
    private IntelliJMethodBindingProperty sOnRightClickRelease = (IntelliJMethodBindingProperty) new IntelliJMethodBindingProperty(screenEditor){
        @Override
        public Property<MethodBinding> setValue_internal(MethodBinding value) {
            super.setValue_internal(value);
            getElementData().onRightClickRelease = getValue();
            if(screenEditor != null) screenEditor.getPropertiesScreen().markForRefresh();
            return this;
        }
    }.setName("On Right Click Release:");

    /** Constructors */
    public InteractableScreenEditorItem(TextureBinding image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public InteractableScreenEditorItem(InteractableData data) {
        super(data);

        sOnLeftClick.setValue(data.onLeftClick);
        sOnLeftClickHeld.setValue(data.onLeftClickHeld);
        sOnLeftClickRelease.setValue(data.onLeftClickRelease);

        sOnRightClick.setValue(data.onRightClick);
        sOnRightClickHeld.setValue(data.onRightClickHeld);
        sOnRightClickRelease.setValue(data.onRightClickRelease);
    }

    /** Data */
    @Override
    public InteractableData makeElementData() {
        return new InteractableData();
    }

    @Override
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        InteractableData interactableData = (InteractableData) data;
        interactableData.onLeftClick = sOnLeftClick.getValue();
        interactableData.onLeftClickHeld = sOnLeftClickHeld.getValue();
        interactableData.onLeftClickRelease = sOnRightClickRelease.getValue();

        interactableData.onRightClick = sOnRightClick.getValue();
        interactableData.onRightClickHeld = sOnRightClickHeld.getValue();
        interactableData.onRightClickRelease = sOnRightClickRelease.getValue();
    }

    @Override
    public InteractableData getElementData() {
        return (InteractableData) super.getElementData();
    }

    /** ID */
    @Override
    public ScreenEditorItem setID(String newId) {
        super.setID(newId);

        sOnLeftClick.setPreferredMethodName(newId + "_onLeftClick");
        sOnLeftClickHeld.setPreferredMethodName(newId + "_onLeftClickHeld");
        sOnLeftClickRelease.setPreferredMethodName(newId + "_onLeftClickRelease");

        sOnRightClick.setPreferredMethodName(newId + "_onRightClick");
        sOnRightClickHeld.setPreferredMethodName(newId + "_onRightClickHeld");
        sOnRightClickRelease.setPreferredMethodName(newId + "_onRightClickRelease");

        return this;
    }

    /** Properties */
    @Override
    public ArrayList<Property<?>> getPropertiesForItem() {
        ArrayList<Property<?>> parentProperties = super.getPropertiesForItem();
        parentProperties.add(sOnLeftClick);
        parentProperties.add(sOnLeftClickHeld);
        parentProperties.add(sOnLeftClickRelease);
        parentProperties.add(sOnRightClick);
        parentProperties.add(sOnRightClickHeld);
        parentProperties.add(sOnRightClickRelease);
        return parentProperties;
    }
}
