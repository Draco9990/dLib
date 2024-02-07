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

import java.util.ArrayList;

public abstract class UIPreviewItem extends Resizeable {
    /** Variables */
    private UIElementData elementData;

    private boolean proxyDragged;

    private boolean highlight;

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
    public UIPreviewItem setPosition(int newPosX, int newPosY) {
        super.setPosition(newPosX, newPosY);

        getElementData().x = newPosX;
        getElementData().y = newPosY;

        return this;
    }

    @Override
    public UIPreviewItem setDimensions(int newWidth, int newHeight) {
        super.setDimensions(newWidth, newHeight);

        getElementData().width = newWidth;
        getElementData().height = newHeight;

        return this;
    }

    /** Copy */
    public abstract UIPreviewItem makeCopy();

    /** Data */
    public abstract UIElementData makeElementData();
    public UIElementData getElementData(){
        return elementData;
    }

    /** Properties */
    public ArrayList<Setting<?>> getPropertiesForItem(){
        ArrayList<Setting<?>> propertiesList = new ArrayList<>();
        propertiesList.add(getElementData().name);

        return propertiesList;
    }

    /** Misc */
    @Override
    public String toString() {
        return getId();
    }
}
