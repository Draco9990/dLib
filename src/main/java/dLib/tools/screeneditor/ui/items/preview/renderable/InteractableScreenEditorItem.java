package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.implementations.InteractableData;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.data.prefabs.ButtonData;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.screens.ScreenManager;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.MethodSetting;

import java.util.ArrayList;

public abstract class InteractableScreenEditorItem extends RenderableScreenEditorItem {
    /** Settings */
    private MethodSetting sOnLeftClick = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().onLeftClick = getCurrentValue();
            if(ScreenEditorBaseScreen.instance != null) ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.setTitle("On Left Click:");
    private MethodSetting sOnLeftClickHeld = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().onLeftClickHeld = getCurrentValue();
            if(ScreenEditorBaseScreen.instance != null) ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.declareParams(Float.class).setTitle("On Left Click Held:");
    private MethodSetting sOnLeftClickRelease = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().onLeftClickRelease = getCurrentValue();
            if(ScreenEditorBaseScreen.instance != null) ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.setTitle("On Left Click Release:");

    private MethodSetting sOnRightClick = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().onRightClick = getCurrentValue();
            if(ScreenEditorBaseScreen.instance != null) ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.setTitle("On Right Click:");
    private MethodSetting sOnRightClickHeld = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().onRightClickHeld = getCurrentValue();
            if(ScreenEditorBaseScreen.instance != null) ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.declareParams(Float.class).setTitle("On Right Click Held:");
    private MethodSetting sOnRightClickRelease = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().onRightClickRelease = getCurrentValue();
            if(ScreenEditorBaseScreen.instance != null) ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.setTitle("On Right Click Release:");

    /** Constructors */
    public InteractableScreenEditorItem(TextureBinding image) {
        super(image);
    }

    public InteractableScreenEditorItem(TextureBinding image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public InteractableScreenEditorItem(TextureBinding image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public InteractableScreenEditorItem(InteractableData data) {
        super(data);

        sOnLeftClick.trySetValue(data.onLeftClick);
        sOnLeftClickHeld.trySetValue(data.onLeftClickHeld);
        sOnLeftClickRelease.trySetValue(data.onLeftClickRelease);

        sOnRightClick.trySetValue(data.onRightClick);
        sOnRightClickHeld.trySetValue(data.onRightClickHeld);
        sOnRightClickRelease.trySetValue(data.onRightClickRelease);
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
        interactableData.onLeftClick = sOnLeftClick.getCurrentValue();
        interactableData.onLeftClickHeld = sOnLeftClickHeld.getCurrentValue();
        interactableData.onLeftClickRelease = sOnRightClickRelease.getCurrentValue();

        interactableData.onRightClick = sOnRightClick.getCurrentValue();
        interactableData.onRightClickHeld = sOnRightClickHeld.getCurrentValue();
        interactableData.onRightClickRelease = sOnRightClickRelease.getCurrentValue();
    }

    @Override
    public InteractableData getElementData() {
        return (InteractableData) super.getElementData();
    }

    /** Properties */
    @Override
    public ArrayList<Setting<?>> getPropertiesForItem() {
        ArrayList<Setting<?>> parentSettings = super.getPropertiesForItem();
        parentSettings.add(sOnLeftClick);
        parentSettings.add(sOnLeftClickHeld);
        parentSettings.add(sOnLeftClickRelease);
        parentSettings.add(sOnRightClick);
        parentSettings.add(sOnRightClickHeld);
        parentSettings.add(sOnRightClickRelease);
        return parentSettings;
    }
}
