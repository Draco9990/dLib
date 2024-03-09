package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ListBoxData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.ListBox;
import dLib.util.settings.Property;
import dLib.util.settings.prefabs.BooleanProperty;
import dLib.util.settings.prefabs.IntegerProperty;
import dLib.util.settings.prefabs.StringProperty;

import java.util.ArrayList;

/// No need to inherit from CompositeScreenEditor item, we aren't treating ListBoxes as regular composites
public class ListBoxScreenEditorItem extends ScreenEditorItem {
    /** Settings */
    private StringProperty sTitle = new StringProperty(""){
        @Override
        protected Property<String> setValue_internal(String value) {
            super.setValue_internal(value);
            getElementData().titleBoxText = getValue();
            reinitializePreviewItems();
            refreshPropertiesScreen();
            return this;
        }
    }.setName("Title:");
    private IntegerProperty sTitleBoxHeight = new IntegerProperty(50, 1, null){
        @Override
        public Property<Integer> setValue_internal(Integer value) {
            super.setValue_internal(value);
            getElementData().titleBoxHeight = getValue();
            reinitializePreviewItems();
            return this;
        }
    }.setName("Title Box Height:");

    private IntegerProperty sItemSpacing = new IntegerProperty(0, 0, null){
        @Override
        public Property<Integer> setValue_internal(Integer value) {
            super.setValue_internal(value);
            getElementData().itemSpacing = getValue();
            reinitializePreviewItems();
            return this;
        }
    }.setName("Row Spacing:");
    private BooleanProperty sInvertedItemOrder = new BooleanProperty(false){
        @Override
        protected Property<Boolean> setValue_internal(Boolean value) {
            super.setValue_internal(value);
            getElementData().invertedItemOrder = getValue();
            reinitializePreviewItems();
            return this;
        }
    }.setName("Inverted Item Order:");

    private IntegerProperty sScrollbarWidth = new IntegerProperty(50, 0, null){
        @Override
        public Property<Integer> setValue_internal(Integer value) {
            super.setValue_internal(value);
            getElementData().scrollbarWidth = getValue();
            reinitializePreviewItems();
            return this;
        }
    }.setName("Scrollbar Width:");

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
        for(int i = 0; i < 20; i++){
            previewListBox.addItem("PREVIEW ROW " + (i + 1));
        }
    }

    /** Update & Render */
    @Override
    public void updateSelf() {
        super.updateSelf();
        previewListBox.update();
    }

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);
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
        listBoxData.titleBoxText = sTitle.getValue();
        listBoxData.titleBoxHeight = sTitleBoxHeight.getValue();

        listBoxData.itemSpacing = sItemSpacing.getValue();
        listBoxData.invertedItemOrder = sInvertedItemOrder.getValue();

        listBoxData.scrollbarWidth = sScrollbarWidth.getValue();
    }

    /** Settings */
    @Override
    public ArrayList<Property<?>> getPropertiesForItem() {
        ArrayList<Property<?>> properties = super.getPropertiesForItem();
        properties.add(sTitle);
        if(!sTitle.getValue().isEmpty()) properties.add(sTitleBoxHeight);

        properties.add(sItemSpacing);
        properties.add(sInvertedItemOrder);

        properties.add(sScrollbarWidth);
        return properties;
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
    public static ScreenEditorItem makeNewInstance(ScreenEditorBaseScreen screenEditor){
        return (ScreenEditorItem) new ListBoxScreenEditorItem(0, 0, 500, 500).setRenderColor(Color.LIGHT_GRAY);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return ListBox.class;
    }
}
