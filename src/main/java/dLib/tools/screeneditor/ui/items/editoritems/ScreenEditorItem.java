package dLib.tools.screeneditor.ui.items.editoritems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.plugin.intellij.PluginMessageSender;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Resizeable;
import dLib.util.DLibLogger;
import dLib.util.IntegerVector2;
import dLib.properties.objects.Property;
import dLib.util.SerializationHelpers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class ScreenEditorItem<ElementType extends UIElement, DataType extends UIElement.UIElementData> extends Resizeable {
    //region Variables

    private ScreenEditorBaseScreen screenEditor;

    private ElementType previewElement;
    private DataType elementData;

    //Temps
    private boolean proxyDragged;

    private boolean highlight;

    //endregion

    //region Constructors

    public ScreenEditorItem(int xPos, int yPos, int width, int height){
        super(null, xPos, yPos, width, height);
        elementData = wrapDataType(makeDataType());
        remakePreviewElement();
    }

    public ScreenEditorItem(DataType elementData){
        super(null, elementData.localPosition.getXValue(), elementData.localPosition.getYValue(), elementData.dimensions.getXValue(), elementData.dimensions.getYValue());
        this.elementData = wrapDataType(elementData);
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
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, getWorldPositionX() * Settings.xScale, getWorldPositionY() * Settings.yScale, getWidth() * Settings.xScale, getHeight() * Settings.yScale);
            sb.flush();  //* We have to flush after drawing because ScissorStack only applies to the last drawn elements for some reason
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
        previewElement.setParent(getParent());
    }
    public Class<? extends UIElement> getElementClass(){
        return previewElement.getClass();
    }

    private DataType wrapDataType(DataType elementData){
        //Bind all screen editor specific stuff
        elementData.id.setValue(getId());
        elementData.id.addOnValueChangedListener(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                if(!getId().equals(s2)){
                    setID(s2);
                }
            }
        });

        elementData.localPosition.setValue(getLocalPosition());
        elementData.localPosition.addOnValueChangedListener(new BiConsumer<IntegerVector2, IntegerVector2>() {
            @Override
            public void accept(IntegerVector2 integerVector2, IntegerVector2 integerVector22) {
                if(!getLocalPosition().equals(integerVector22)){
                    setLocalPosition(integerVector22.x, integerVector22.y);
                }
            }
        });

        elementData.dimensions.setValue(new IntegerVector2(getWidth(), getHeight()));
        elementData.dimensions.addOnValueChangedListener((integerVector2, integerVector22) -> {
            if(!integerVector22.equals(getDimensions())){
                setDimensions(integerVector22.x, integerVector22.y);
            }
        });

        for(Property<?> property : elementData.getEditableProperties()){
            property.addOnValueChangedListener(this::markForRefresh);
        }

        return elementData;
    }
    protected abstract DataType makeDataType();
    public DataType getElementData(){
        return elementData;
    }

    //endregion

    @Override
    public UIElement setParent(UIElement parent) {
        if(previewElement != null){
            previewElement.setParent(parent);
        }
        return super.setParent(parent);
    }

    //region ID

    @Override
    public UIElement setID(String newId) {
        String oldId = getId();

        super.setID(newId);

        if(previewElement != null){
            previewElement.setID(getId());
        }
        if(elementData != null && !elementData.id.getValue().equals(newId)){
            elementData.id.setValue(getId());
        }

        if(oldId != null) PluginMessageSender.Send_RenameVariableInClass(screenEditor.getEditingScreen(), oldId, newId);

        return this;
    }


    //endregion

    //region Position & Dimensions

    @Override
    public UIElement setLocalPosition(int newPositionX, int newPositionY) {
        if(screenEditor != null && screenEditor.getEditorProperties().isGridOn()){
            newPositionX = Math.round(newPositionX / 10.0f) * 10;
            newPositionY = Math.round(newPositionY / 10.0f) * 10;
        }

        return super.setLocalPosition(newPositionX, newPositionY);
    }

    @Override
    public void onPositionChanged(int diffX, int diffY) {
        super.onPositionChanged(diffX, diffY);

        if(elementData != null && !elementData.localPosition.getValue().equals(getLocalPosition())){
            elementData.localPosition.setValue(getLocalPosition());
        }

        remakePreviewElement();
    }

    @Override
    public UIElement setDimensions(int newWidth, int newHeight) {
        if(screenEditor != null && screenEditor.getEditorProperties().isGridOn()){
            int upperX = getLocalPositionX() + getWidth();
            int upperY = getLocalPositionY() + getHeight();

            int gridUpperX = Math.round(upperX / 10.0f) * 10;
            int gridUpperY = Math.round(upperY / 10.0f) * 10;

            int diffX = gridUpperX - upperX;
            int diffY = gridUpperY - upperY;

            if(diffX != 0 && newWidth != -1) newWidth += diffX;
            if(diffY != 0 && newHeight != -1) newHeight += diffY;
        }

        super.setDimensions(newWidth, newHeight);

        if(elementData != null) {
            if(newWidth != -1 && !Objects.equals(elementData.dimensions.getXValue(), newWidth)) elementData.dimensions.setXValue(newWidth);
            if(newHeight != -1 && !Objects.equals(elementData.dimensions.getYValue(), newHeight)) elementData.dimensions.setYValue(newHeight);
        }

        remakePreviewElement();

        return this;
    }

    //endregion

    //region Properties

    public ArrayList<Property<?>> getItemProperties(){
        return elementData.getEditableProperties();
    }

    //endregion

    @Override
    public String toString() {
        return getId();
    }

    @Override
    protected void onRefreshElement() {
        super.onRefreshElement();

        remakePreviewElement();
    }

    public ScreenEditorItem<ElementType, DataType> copy(){
        try{
            DataType cpy = SerializationHelpers.deepCopySerializable(getElementData());
            Constructor<?> constructor = this.getClass().getConstructor(getElementData().getClass());

            return (ScreenEditorItem<ElementType, DataType>) constructor.newInstance(cpy);
        }catch (Exception e){
            DLibLogger.logError("Failed to create a copy of the screen editor item due to: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    //endregion
}
