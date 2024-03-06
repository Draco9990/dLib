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
import dLib.util.IntVector2;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.StringSetting;
import dLib.util.settings.prefabs.IntVector2Setting;

import java.util.ArrayList;
import java.util.Objects;

public abstract class ScreenEditorItem extends Resizeable {
    /** Variables */
    private UIElementData elementData = makeElementData();

    private boolean proxyDragged;

    private boolean highlight;

    /** Settings */
    private StringSetting sID = (StringSetting) new StringSetting(getId()){
        @Override
        public Setting<String> setCurrentValue(String currentValue) {
            super.setCurrentValue(currentValue);
            setID(currentValue);
            getElementData().ID = currentValue;

            return this;
        }

        @Override
        public boolean isValidValue(String value) {
            if(value.isEmpty()){
                return false;
            }

            for(ScreenEditorItem item : ScreenEditorBaseScreen.instance.getPreviewScreen().getPreviewItems()){
                if(item.getId().equals(value) && !Objects.equals(this, item.sID)){
                    return false;
                }
            }

            return super.isValidValue(value);
        }
    }.setConfirmationMode(StringSetting.InputConfirmationMode.SELECTION_MANAGED).setTitle("ID:");

    private IntVector2Setting sPosition = (IntVector2Setting) new IntVector2Setting(new IntVector2(getPositionX(), getPositionY())){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            setPosition((int) getCurrentValue().x, (int) getCurrentValue().y);
        }
    }.setAxisNames("X:", "Y:").setTitle("Position:");

    private IntVector2Setting sDimensions = (IntVector2Setting) new IntVector2Setting(new IntVector2(getWidth(), getHeight())){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            setDimensions(getCurrentValue().x, getCurrentValue().y);
        }
    }.setAxisNames("W:", "H:").setTitle("Dimensions");

    /** Constructors */
    public ScreenEditorItem(Texture image) {
        super(image);
    }

    public ScreenEditorItem(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

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
    public void render(SpriteBatch sb) {
        if(ScreenEditorBaseScreen.instance.getActiveItemsManager().isItemActive(this) && !Settings.isDebug){
            Settings.isDebug = true;
            super.render(sb);
            Settings.isDebug = false;
        }
        else{
            super.render(sb);
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
            ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(this);
            ScreenEditorBaseScreen.instance.getActiveItemsManager().markAllForDrag();
        }
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        super.onLeftClickHeld(totalDuration);

        if(!proxyDragged){
            ScreenEditorBaseScreen.instance.getActiveItemsManager().markAllForDragUpdate(totalDuration);
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

        IntVector2 currentVal = sPosition.getCurrentValue();
        if(sPosition.getCurrentValue().x != x){
            currentVal.x = x;
            sPosition.trySetValue(currentVal);
        }
        if(sPosition.getCurrentValue().y != y){
            currentVal.y = y;
            sPosition.trySetValue(currentVal);
        }

        return this;
    }

    @Override
    public ScreenEditorItem setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);

        getElementData().width = width;
        getElementData().height = height;

        IntVector2 currentDimensions = sDimensions.getCurrentValue();
        if(width != currentDimensions.x){
            currentDimensions.x = width;
            sDimensions.trySetValue(currentDimensions);
        }
        if(height != currentDimensions.y){
            currentDimensions.y = height;
            sDimensions.trySetValue(currentDimensions);
        }

        return this;
    }

    /** Data */
    public abstract UIElementData makeElementData();
    public void initializeElementData(UIElementData data){
        data.ID = getId();
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
        sID.trySetValue(getId());
        sPosition.trySetValue(new IntVector2(getPositionX(), getPositionY()));
        sDimensions.trySetValue(new IntVector2(getWidth(), getHeight()));
    }

    /** ID */
    @Override
    public ScreenEditorItem setID(String newId) {
        String oldName = getId();

        super.setID(newId);

        getElementData().ID = newId;

        if(!Objects.equals(newId, sID.getCurrentValue())){
            sID.trySetValue(newId);
        }

        if(!Objects.equals(oldName, newId)){
            PluginManager.sendMessage("screenElementRename", ScreenEditorBaseScreen.instance.getEditingScreen(), oldName, newId);
        }

        return this;
    }

    /** Properties */
    public ArrayList<Setting<?>> getPropertiesForItem(){
        ArrayList<Setting<?>> propertiesList = new ArrayList<>();
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

    /** Get Live Instance Type */
    public abstract Class<? extends UIElement> getLiveInstanceType();
}
