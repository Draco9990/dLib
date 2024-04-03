package dLib.tools.screeneditor.ui.items.editoritems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Resizeable;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.IntegerVector2;
import dLib.util.Reflection;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.settings.Property;
import dLib.util.settings.prefabs.MethodBindingProperty;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;

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
        elementData = makeDataType_wrapper();
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

    private DataType makeDataType_wrapper(){
        DataType elementData = makeDataType();

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
        elementData.dimensions.addOnValueChangedListener(new BiConsumer<IntegerVector2, IntegerVector2>() {
            @Override
            public void accept(IntegerVector2 integerVector2, IntegerVector2 integerVector22) {
                if(!integerVector22.equals(getDimensions())){
                    setDimensions(integerVector22.x, integerVector22.y);
                }
            }
        });

        for(Property<?> property : elementData.getEditableProperties()){
            if(property instanceof MethodBindingProperty){
                ((MethodBindingProperty)property).addOnValueChangedListener((methodBinding, methodBinding2) -> screenEditor.getPropertiesScreen().markForRefresh());
            }
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
        super.setID(newId);

        if(previewElement != null){
            previewElement.setID(getId());
        }
        if(elementData != null && !elementData.id.getValue().equals(newId)){
            elementData.id.setValue(getId());
        }

        return this;
    }


    //endregion

    //region Position & Dimensions

    @Override
    public void onPositionChanged(int diffX, int diffY) {
        super.onPositionChanged(diffX, diffY);

        if(elementData != null && !elementData.localPosition.getValue().equals(getLocalPosition())){
            elementData.localPosition.setValue(getLocalPosition());
        }

        remakePreviewElement();
    }

    @Override
    public UIElement setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);

        if(elementData != null && (!Objects.equals(elementData.dimensions.getXValue(), newWidth) || !Objects.equals(elementData.dimensions.getYValue(), newHeight))) {
            elementData.dimensions.setValue(new IntegerVector2(newWidth, newHeight));
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


    //endregion
}
