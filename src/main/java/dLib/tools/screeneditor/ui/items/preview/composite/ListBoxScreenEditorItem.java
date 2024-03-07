package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.CompositeScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ImageScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ListBoxData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.BooleanSetting;
import dLib.util.settings.prefabs.IntegerSetting;
import dLib.util.settings.prefabs.StringSetting;

import java.util.ArrayList;

/// No need to inherit from CompositeScreenEditor item, we aren't treating ListBoxes as regular composites
public class ListBoxScreenEditorItem extends ScreenEditorItem {
    /** Settings */
    private StringSetting sTitle = new StringSetting(""){
        @Override
        protected Setting<String> setCurrentValue(String currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().titleBoxText = getCurrentValue();
            reinitializePreviewItems();
            refreshPropertiesScreen();
            return this;
        }
    }.setTitle("Title:");
    private IntegerSetting sTitleBoxHeight = new IntegerSetting(50, 1, null){
        @Override
        public Setting<Integer> setCurrentValue(Integer currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().titleBoxHeight = getCurrentValue();
            reinitializePreviewItems();
            return this;
        }
    }.setTitle("Title Box Height:");

    private IntegerSetting sItemSpacing = new IntegerSetting(0, 0, null){
        @Override
        public Setting<Integer> setCurrentValue(Integer currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().itemSpacing = getCurrentValue();
            reinitializePreviewItems();
            return this;
        }
    }.setTitle("Row Spacing:");
    private BooleanSetting sInvertedItemOrder = new BooleanSetting(false){
        @Override
        protected Setting<Boolean> setCurrentValue(Boolean currentValue) {
            super.setCurrentValue(currentValue);
            getElementData().invertedItemOrder = getCurrentValue();
            reinitializePreviewItems();
            return this;
        }
    }.setTitle("Inverted Item Order:");

    /** Variables */
    private ListBox<Object> previewListBox;

    /** Constructors */
    public ListBoxScreenEditorItem(int xPos, int yPos, int width, int height) {
        super(null, xPos, yPos, width, height);

        reinitializePreviewItems();
    }

    public ListBoxScreenEditorItem(ListBoxData<?> data){
        super(data);

        reinitializePreviewItems();
    }

    private void reinitializePreviewItems(){
        previewListBox = new ListBox<Object>(getElementData());
        previewListBox.addItem("Row 1");
        previewListBox.addItem("Row 2");
        previewListBox.addItem("Row 3");
    }

    /** Update & Render */
    @Override
    public void update() {
        previewListBox.update();
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        previewListBox.render(sb);
    }

    /** Data */
    @Override
    public ListBoxData<Object> makeElementData() {
        return new ListBoxData<Object>();
    }

    @Override
    public ListBoxData<Object> getElementData() {
        return (ListBoxData<Object>) super.getElementData();
    }

    @Override
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        ListBoxData<Object> listBoxData = (ListBoxData<Object>) data;
        listBoxData.itemSpacing = sItemSpacing.getCurrentValue();
        listBoxData.invertedItemOrder = sInvertedItemOrder.getCurrentValue();
    }

    /** Settings */
    @Override
    public ArrayList<Setting<?>> getPropertiesForItem() {
        ArrayList<Setting<?>> settings = super.getPropertiesForItem();
        settings.add(sTitle);
        if(!sTitle.getCurrentValue().isEmpty()) settings.add(sTitleBoxHeight);
        settings.add(sItemSpacing);
        settings.add(sInvertedItemOrder);
        return settings;
    }

    /** Position & Dimensions */
    @Override
    public ScreenEditorItem setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);
        reinitializePreviewItems();
        return this;
    }

    @Override
    public ScreenEditorItem setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);
        reinitializePreviewItems();
        return this;
    }

    /** Copy */
    public static ScreenEditorItem makeNewInstance(){
        return (ScreenEditorItem) new ListBoxScreenEditorItem(0, 0, 500, 500).setRenderColor(Color.LIGHT_GRAY);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return ListBox.class;
    }
}
