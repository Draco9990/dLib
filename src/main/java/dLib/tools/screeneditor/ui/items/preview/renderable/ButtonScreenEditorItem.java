package dLib.tools.screeneditor.ui.items.preview.renderable;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.RenderableScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.ButtonData;
import dLib.ui.themes.UITheme;
import dLib.util.Reflection;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.MethodSetting;

import java.util.ArrayList;

public class ButtonScreenEditorItem extends RenderableScreenEditorItem {
    /** Settings */
    private MethodSetting sOnLeftClick = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.setTitle("On Left Click:");
    private MethodSetting sOnLeftClickHeld = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.declareParams(Float.class).setTitle("On Left Click Held:");
    private MethodSetting sOnLeftClickRelease = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.setTitle("On Left Click Release:");

    private MethodSetting sOnRightClick = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.setTitle("On Right Click:");
    private MethodSetting sOnRightClickHeld = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.declareParams(Float.class).setTitle("On Right Click Held:");
    private MethodSetting sOnRightClickRelease = new MethodSetting(){
        @Override
        public Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
            super.setCurrentValue(currentValue);
            ScreenEditorBaseScreen.instance.getPropertiesScreen().refreshProperties();
            return this;
        }
    }.setTitle("On Right Click Release:");

    /** Constructors */
    public ButtonScreenEditorItem(){
        super(new TextureThemeBinding("button_small"), 0, 0, 75, 75);
    }

    public ButtonScreenEditorItem(TextureBinding textureBinding, int xPos, int yPos, int width, int height) {
        super(textureBinding, xPos, yPos, width, height);
    }

    /** Data */
    @Override
    public ButtonData makeElementData() {
        return new ButtonData();
    }

    @Override
    public ButtonData getElementData() {
        return (ButtonData) super.getElementData();
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

    /** Copy */
    @Override
    public ScreenEditorItem makeCopy() {
        return new ButtonScreenEditorItem(sTexture.getCurrentValue(), x, y, width, height);
    }

}
