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
                if(entry.elementData.localPosition.getXValue() != element.getLocalPositionX() || entry.elementData.localPosition.getYValue() != element.getLocalPositionY()){
                    editor.properties.propertyEditor.itemBeingModifiedExternally = true;
                    entry.elementData.localPosition.setValue(new IntegerVector2(element.getLocalPositionX(), element.getLocalPositionY()));
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
