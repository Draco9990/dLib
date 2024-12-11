package dLib.tools.uicreator.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.UCEditorItemTree;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.components.UIElementComponent;
import dLib.util.IntegerVector2;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.StaticPosition;

import java.util.UUID;

public class UCEditorItemComponent extends UIElementComponent<UIElement> {

    private boolean hoveredInHierarchy = false;

    private UUID onPositionChangedEventID;

    public UCEditorItemComponent(){
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        owner.setBoundWithinParent(true);

        onPositionChangedEventID = owner.addOnPositionChangedConsumer((element) -> {
            UCEditor editor = element.getParentOfType(UCEditor.class);

            UCEditorItemTree.UCEditorItemTreeEntry entry = editor.itemTree.findEntry(element);
            if(entry != null){
                AbstractPosition localPositionX = element.getLocalPositionXRaw();
                AbstractPosition localPositionY = element.getLocalPositionYRaw();

                if(localPositionX instanceof StaticPosition){
                    localPositionX = new StaticPosition((int) (((StaticPosition) localPositionX).getValueRaw() * 1.25f));
                }
                if(localPositionY instanceof StaticPosition){
                    localPositionY = new StaticPosition((int) (((StaticPosition) localPositionY).getValueRaw() * 1.25f));
                }

                if(entry.elementData.localPosition.getXPosition() != localPositionX || entry.elementData.localPosition.getYPosition() != localPositionY){
                    editor.properties.propertyEditor.itemBeingModifiedExternally = true;
                    entry.elementData.localPosition.setValue(localPositionX, localPositionY);
                    editor.properties.propertyEditor.itemBeingModifiedExternally = false;
                }
            }
        });
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {

    }

    @Override
    public void onRender(UIElement owner, SpriteBatch sb) {
        super.onRender(owner, sb);

        if(hoveredInHierarchy){
            sb.setColor(Color.PURPLE);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, owner.getWorldPositionX() * Settings.xScale, owner.getWorldPositionY() * Settings.yScale, owner.getWidth() * Settings.xScale, owner.getHeight() * Settings.yScale);
            sb.flush();
        }
        else if(owner.hasComponent(ElementGroupModifierComponent.class) && owner.getComponent(ElementGroupModifierComponent.class).isSelected()){
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
