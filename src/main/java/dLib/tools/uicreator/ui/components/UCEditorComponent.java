package dLib.tools.uicreator.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.components.UIElementComponent;

public class UCEditorComponent extends UIElementComponent<UIElement> {

    public UIElement.UIElementData elementData;

    private boolean hoveredInHierarchy = false;

    public UCEditorComponent(UIElement.UIElementData elementData){
        this.elementData = elementData;
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        owner.setBoundWithinParent(true);
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
