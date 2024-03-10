package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.plugin.intellij.PluginManager;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.data.UIElementData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Resizeable;
import dLib.util.IntegerVector2;
import dLib.util.settings.Property;
import dLib.util.settings.prefabs.IntegerVector2Property;
import dLib.util.settings.prefabs.StringProperty;

import java.util.ArrayList;
import java.util.Objects;

public abstract class ScreenEditorItem<DataType extends UIElementData> extends Resizeable {
    //region Variables

    private ScreenEditorBaseScreen screenEditor;

    private UIElement previewInstance;

    private DataType elementData;

    //Properties
    private boolean proxyDragged;

    private boolean highlight;

    //endregion

    //region Settings
    //endregion Settings

    //region Constructors

    public ScreenEditorItem(int xPos, int yPos, int width, int height){
        super(null, xPos, yPos, width, height);
        elementData = DataType.makeNew();
    }

    //endregion

    //region Methods
    //endregion

    /** Variables */

    /** Settings */
    private StringProperty sID = new StringProperty(getId()){
        @Override
        public Property<String> setValue_internal(String value) {
            super.setValue_internal(value);
            setID(value);
            getElementData().id = value;

            return this;
        }

        @Override
        public boolean isValidValue(String value) {
            if(value.isEmpty()){
                return false;
            }

            for(ScreenEditorItem item : screenEditor.getPreviewScreen().getPreviewItems()){
                if(item.getId().equals(value) && !Objects.equals(this, item.sID)){
                    return false;
                }
            }

            return super.isValidValue(value);
        }
    }.setConfirmationMode(StringProperty.InputConfirmationMode.SELECTION_MANAGED).setName("ID:");

    private IntegerVector2Property sPosition = (IntegerVector2Property) new IntegerVector2Property(new IntegerVector2(getPositionX(), getPositionY())){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            setPosition((int) getValue().x, (int) getValue().y);
        }
    }.setValueNames("X:", "Y:").setName("Position:");

    private IntegerVector2Property sDimensions = (IntegerVector2Property) new IntegerVector2Property(new IntegerVector2(getWidth(), getHeight())){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            setDimensions(getValue().x, getValue().y);
        }
    }.setValueNames("W:", "H:").setName("Dimensions");

    /** Constructors */
    public ScreenEditorItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public ScreenEditorItem(UIElementData data){
        super(null, data.x, data.y, data.width, data.height);
        this.elementData = data;
    }

    public void postInitialize(){
        initializeSettingsData();
        initializeElementData(elementData);
    }

    /** Update and render */
    @Override
    public void renderSelf(SpriteBatch sb) {
        if(screenEditor.getActiveItemsManager().isItemActive(this) && !Settings.isDebug){
            Settings.isDebug = true;
            super.renderSelf(sb);
            Settings.isDebug = false;
        }
        else{
            super.renderSelf(sb);
        }

        if(highlight){
            sb.setColor(Color.BLUE);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, this.x * Settings.xScale, this.y * Settings.yScale, this.width * Settings.xScale, this.height * Settings.yScale);
        }
    }

    /** Proxy Drag */
    public void setProxyDragged(boolean isProxyDragged){
        proxyDragged = isProxyDragged;
    }

    /** Selection */
    @Override
    protected void onLeftClick() {
        super.onLeftClick();
        if(!proxyDragged){
            screenEditor.getActiveItemsManager().addActiveItem(this);
            screenEditor.getActiveItemsManager().markAllForDrag();
        }
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        super.onLeftClickHeld(totalDuration);

        if(!proxyDragged){
            screenEditor.getActiveItemsManager().markAllForDragUpdate(totalDuration);
        }
    }

    /** Highlighting */
    public ScreenEditorItem setHighlight(boolean highlight){
        this.highlight = highlight;
        return this;
    }

    /** Position and dimensions */
    @Override
    public ScreenEditorItem setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);

        getElementData().x = x;
        getElementData().y = y;

        IntegerVector2 currentVal = sPosition.getValue();
        if(sPosition.getValue().x != x){
            currentVal.x = x;
            sPosition.setValue(currentVal);
        }
        if(sPosition.getValue().y != y){
            currentVal.y = y;
            sPosition.setValue(currentVal);
        }

        return this;
    }

    @Override
    public ScreenEditorItem setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);

        getElementData().width = width;
        getElementData().height = height;

        IntegerVector2 currentDimensions = sDimensions.getValue();
        if(width != currentDimensions.x){
            currentDimensions.x = width;
            sDimensions.setValue(currentDimensions);
        }
        if(height != currentDimensions.y){
            currentDimensions.y = height;
            sDimensions.setValue(currentDimensions);
        }

        return this;
    }

    /** Data */
    public abstract UIElementData makeElementData();
    public void initializeElementData(UIElementData data){
        data.id = getId();
        data.x = x;
        data.y = y;
        data.width = width;
        data.height = height;
    }
    public UIElementData getElementData(){
        return elementData;
    }

    /** Settings */
    public void initializeSettingsData(){
        sID.setValue(getId());
        sPosition.setValue(new IntegerVector2(getPositionX(), getPositionY()));
        sDimensions.setValue(new IntegerVector2(getWidth(), getHeight()));
    }

    /** ID */
    @Override
    public ScreenEditorItem setID(String newId) {
        String oldName = getId();

        super.setID(newId);

        getElementData().id = newId;

        if(!Objects.equals(newId, sID.getValue())){
            sID.setValue(newId);
        }

        if(!Objects.equals(oldName, newId)){
            PluginManager.sendMessage("screenElementRename", screenEditor.getEditingScreen(), oldName, newId);
        }

        return this;
    }

    /** Properties */
    public ArrayList<Property<?>> getPropertiesForItem(){
        ArrayList<Property<?>> propertiesList = new ArrayList<>();
        propertiesList.add(sID);

        propertiesList.add(sPosition);
        propertiesList.add(sDimensions);

        return propertiesList;
    }

    /** Misc */
    @Override
    public String toString() {
        return getId();
    }

    protected void refreshPropertiesScreen(){
        screenEditor.getPropertiesScreen().markForRefresh();
    }

    /** Get Live Instance Type */
    public abstract Class<? extends UIElement> getLiveInstanceType();
}
