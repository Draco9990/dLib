package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.data.UIElementData;
import dLib.ui.elements.implementations.Resizeable;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.IntegerSetting;
import dLib.util.settings.prefabs.StringSetting;

import java.util.ArrayList;
import java.util.Objects;

public abstract class UIPreviewItem extends Resizeable {
    /** Variables */
    private UIElementData elementData;

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
    }.setTitle("ID:");

    private IntegerSetting sPosX = (IntegerSetting) new IntegerSetting(getPositionX()){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            setPositionX(currentValue);
        }
    }.setTitle("X Position:");
    private IntegerSetting sPosY = (IntegerSetting) new IntegerSetting(getPositionY()){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            setPositionY(currentValue);
        }
    }.setTitle("Y Position:");

    private IntegerSetting sWidth = (IntegerSetting) new IntegerSetting(getWidth(), null, null){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            setWidth(currentValue);
        }
    }.setTitle("Width:");
    private IntegerSetting sHeight = (IntegerSetting) new IntegerSetting(getHeight(), null, null){
        @Override
        public void onValueChanged() {
            super.onValueChanged();
            setHeight(currentValue);
        }
    }.setTitle("Height:");

    /** Constructors */
    public UIPreviewItem(Texture image) {
        super(image);
    }

    public UIPreviewItem(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public UIPreviewItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public void postInitialize(){
        elementData = makeElementData();
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
    public UIPreviewItem setHighlight(boolean highlight){
        this.highlight = highlight;
        return this;
    }

    /** Position and dimensions */
    @Override
    public UIPreviewItem setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);

        getElementData().x = x;
        getElementData().y = y;

        if(sPosX.getCurrentValue() != x){
            sPosX.setCurrentValue(x);
        }
        if(sPosY.getCurrentValue() != y){
            sPosY.setCurrentValue(y);
        }

        return this;
    }

    @Override
    public UIPreviewItem setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);

        getElementData().width = width;
        getElementData().height = height;

        if(width != sWidth.getCurrentValue()){
            sWidth.setCurrentValue(width);
        }
        if(height != sHeight.getCurrentValue()){
            sHeight.setCurrentValue(height);
        }

        return this;
    }

    /** Copy */
    public abstract UIPreviewItem makeCopy();

    /** Data */
    public abstract UIElementData makeElementData();
    public UIElementData getElementData(){
        return elementData;
    }

    /** ID */
    @Override
    public void setID(String newId) {
        super.setID(newId);

        getElementData().ID = newId;

        if(!Objects.equals(newId, sID.getCurrentValue())){
            sID.setCurrentValue(newId);
        }
    }

    /** Properties */
    public ArrayList<Setting<?>> getPropertiesForItem(){
        ArrayList<Setting<?>> propertiesList = new ArrayList<>();
        propertiesList.add(sID);

        propertiesList.add(sPosX);
        propertiesList.add(sPosY);

        propertiesList.add(sWidth);
        propertiesList.add(sHeight);

        return propertiesList;
    }

    /** Misc */
    @Override
    public String toString() {
        return getId();
    }
}
