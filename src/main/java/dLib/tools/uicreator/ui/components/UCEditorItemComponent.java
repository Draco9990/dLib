package dLib.tools.uicreator.ui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.UCEditorItemTree;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.components.UIElementComponent;
import dLib.ui.screens.UIManager;
import dLib.util.ui.bounds.Bound;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.PixelDimension;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.PixelPosition;

public class UCEditorItemComponent extends UIElementComponent<UIElement> {

    private boolean hoveredInHierarchy = false;

    public UCEditorItemComponent(){
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        UCEditor editorr = UIManager.getOpenElementOfType(UCEditor.class);
        owner.setContainerBounds(Bound.element(editorr.canvas));

        owner.onPositionChangedEvent.subscribeManaged((element) -> {
            UCEditor editor = element.getParentOfType(UCEditor.class);

            UIElement.UIElementData elementData = editor.itemTree.findElementDataRecursively(editor.itemTree.rootElementData, element);
            if(elementData != null){
                AbstractPosition localPositionX = element.getLocalPositionXRaw();
                AbstractPosition localPositionY = element.getLocalPositionYRaw();

                if(localPositionX instanceof PixelPosition){
                    localPositionX = new PixelPosition((int) (((PixelPosition) localPositionX).getValueRaw() * 1.25f));
                }
                if(localPositionY instanceof PixelPosition){
                    localPositionY = new PixelPosition((int) (((PixelPosition) localPositionY).getValueRaw() * 1.25f));
                }

                if(elementData.localPositionX.getValue() != localPositionX){
                    editor.properties.propertyEditor.itemBeingModifiedExternally = true;
                    elementData.localPositionX.setValue(localPositionX);
                    editor.properties.propertyEditor.itemBeingModifiedExternally = false;
                }
                if(elementData.localPositionY.getValue() != localPositionY){
                    editor.properties.propertyEditor.itemBeingModifiedExternally = true;
                    elementData.localPositionY.setValue(localPositionY);
                    editor.properties.propertyEditor.itemBeingModifiedExternally = false;
                }
            }
        });

        owner.onDimensionsChangedEvent.subscribeManaged((element) -> {
            UCEditor editor = element.getParentOfType(UCEditor.class);

            UIElement.UIElementData elementData = editor.itemTree.findElementDataRecursively(editor.itemTree.rootElementData, element);
            if(elementData != null){
                AbstractDimension width = element.getWidthRaw();
                AbstractDimension height = element.getHeightRaw();

                if(width instanceof PixelDimension){
                    width = new PixelDimension((int) (((PixelDimension) width).getValueRaw() * 1.25f));
                }
                if(height instanceof PixelDimension){
                    height = new PixelDimension((int) (((PixelDimension) height).getValueRaw() * 1.25f));
                }

                if(elementData.width.getValue() != width){
                    editor.properties.propertyEditor.itemBeingModifiedExternally = true;
                    elementData.width.setValue(width);
                    editor.properties.propertyEditor.itemBeingModifiedExternally = false;
                }
                if(elementData.height.getValue() != height){
                    editor.properties.propertyEditor.itemBeingModifiedExternally = true;
                    elementData.height.setValue(height);
                    editor.properties.propertyEditor.itemBeingModifiedExternally = false;
                }
            }
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {

    }

    @Override
    public void onUpdate(UIElement owner) {
        super.onUpdate(owner);

        if(owner.getComponent(ElementGroupModifierComponent.class).isSelected()){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)){
                if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
                    UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
                    editor.itemTree.duplicateItem(owner);
                }
            }

            if(Gdx.input.isKeyPressed(Input.Keys.FORWARD_DEL)){
                UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
                editor.itemTree.deleteItem(owner);
            }
        }
    }

    @Override
    public void onRender(UIElement owner, SpriteBatch sb) {
        super.onRender(owner, sb);

        if(hoveredInHierarchy){
            sb.setColor(Color.PURPLE);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, owner.getWorldPositionX() * Settings.xScale, owner.getWorldPositionY() * Settings.yScale, owner.getWidth() * Settings.xScale, owner.getHeight() * Settings.yScale);
            sb.flush();
        }
        else if(owner.getComponent(ElementGroupModifierComponent.class).isSelected()){
            sb.setColor(Color.GREEN);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, owner.getWorldPositionX() * Settings.xScale, owner.getWorldPositionY() * Settings.yScale, owner.getWidth() * Settings.xScale, owner.getHeight() * Settings.yScale);
            sb.flush();
        }
        else if(owner.isHovered()){
            sb.setColor(Color.BLUE);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, owner.getWorldPositionX() * Settings.xScale, owner.getWorldPositionY() * Settings.yScale, owner.getWidth() * Settings.xScale, owner.getHeight() * Settings.yScale);
            sb.flush();
        }
    }

    public void setHoveredInHierarchy(boolean hoveredInHierarchy) {
        this.hoveredInHierarchy = hoveredInHierarchy;
    }
}
