package dLib.tools.screeneditor.ui.items.editoritems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Resizeable;

public abstract class ScreenEditorItem<ElementType extends UIElement, DataType extends UIElement.UIElementData> extends Resizeable {
    //region Variables

    private ScreenEditorBaseScreen screenEditor;

    private UIElement previewInstance;

    private ElementType previewElement;
    private DataType elementData;

    //Temps
    private boolean proxyDragged;

    private boolean highlight;

    //endregion

    //region Constructors

    public ScreenEditorItem(int xPos, int yPos, int width, int height){
        super(null, xPos, yPos, width, height);
        elementData = makeDataType();
        remakePreviewElement();
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        previewElement.update();
    }

    @Override
    public void renderSelf(SpriteBatch sb) {
        previewElement.render(sb);

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
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, getWorldPositionX() * Settings.xScale, getWorldPositionY() * Settings.yScale, this.width * Settings.xScale, this.height * Settings.yScale);
        }
    }

    //endregion

    public ScreenEditorItem<ElementType, DataType> setScreenEditor(ScreenEditorBaseScreen screenEditor){
        this.screenEditor = screenEditor;
        return this;
    }

    //region Proxy Drag

    public void setProxyDragged(boolean isProxyDragged){
        proxyDragged = isProxyDragged;
    }

    //endregion

    //region Selection

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

    //endregion

    //region Highlighting

    public ScreenEditorItem setHighlight(boolean highlight){
        this.highlight = highlight;
        return this;
    }

    //endregion

    //region Preview Element & Data

    protected void remakePreviewElement(){
        previewElement = (ElementType) elementData.makeUIElement();
        previewElement.setLocalPosition(getLocalPositionX(), getLocalPositionY());
        previewElement.setDimensions(getWidth(), getHeight());
    }
    public Class<? extends UIElement> getElementClass(){
        return previewElement.getClass();
    }

    protected abstract DataType makeDataType();
    public DataType getElementData(){
        return elementData;
    }

    //endregion

    //region Position & Dimensions

    @Override
    public void onPositionChanged(int diffX, int diffY) {
        super.onPositionChanged(diffX, diffY);
        if(previewElement.getLocalPositionX() != getLocalPositionX() || previewElement.getLocalPositionY() != getLocalPositionY()){
            previewElement.setLocalPosition(getLocalPositionX(), getLocalPositionY());
        }
    }

    @Override
    public UIElement setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);

        if(previewElement.getWidth() != getWidth() || previewElement.getHeight() != getHeight()){
            previewElement.setDimensions(getWidth(), getHeight());
        }

        return this;
    }

    //endregion

    //endregion
}
