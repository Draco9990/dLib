package dLib.tools.uicreator.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIElementComponent;

public class UCEditorComponent extends UIElementComponent<UIElement> {

    private UIElement.UIElementData elementData;

    public UCEditorComponent(UIElement.UIElementData elementData){
        this.elementData = elementData;
    }

    @Override
    public void onRegisterComponent(UIElement owner) {

    }

    @Override
    public void onUnregisterComponent(UIElement owner) {

    }

    @Override
    public void onRender(UIElement owner, SpriteBatch sb) {
        super.onRender(owner, sb);

        if(owner.isHovered()){
            sb.setColor(Color.BLUE);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, owner.getWorldPositionX() * Settings.xScale, owner.getWorldPositionY() * Settings.yScale, owner.getWidth() * Settings.xScale, owner.getHeight() * Settings.yScale);
            sb.flush();
        }
    }
}
