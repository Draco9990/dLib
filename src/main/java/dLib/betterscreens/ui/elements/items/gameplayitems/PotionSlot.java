package dLib.betterscreens.ui.elements.items.gameplayitems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import dLib.ui.elements.items.buttons.Button;
import dLib.util.Reflection;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public class PotionSlot extends Button {
    private AbstractPotion potion;

    public PotionSlot(AbstractPosition xPos, AbstractPosition yPos, float widthHeight) {
        super(xPos, yPos, Dim.px(widthHeight), Dim.mirror());

        setTexture(ImageMaster.POTION_PLACEHOLDER);
        setPreserveAspectRatio(true);
    }

    public void setPotion(AbstractPotion potion){
        this.potion = potion;
    }
    public AbstractPotion getPotion() {
        return potion;
    }

    @Override
    protected void renderCall(SpriteBatch sb, NinePatch ninePatchToRender, float renderPosX, float renderPosY, float renderWidth, float renderHeight) {
        if(potion == null){
            super.renderCall(sb, ninePatchToRender, renderPosX, renderPosY, renderWidth, renderHeight);
        }
        else{
            sb.setColor(potion.liquidColor);

            Texture liquidImg = Reflection.getFieldValue("liquidImg", potion);
            sb.draw(liquidImg, renderPosX, renderPosY, renderWidth, renderHeight);
            if (potion.hybridColor != null) {
                sb.setColor(potion.hybridColor);

                Texture hybridImg = Reflection.getFieldValue("hybridImg", potion);
                sb.draw(hybridImg, renderPosX, renderPosY, renderWidth, renderHeight);
            }

            if (potion.spotsColor != null) {
                sb.setColor(potion.spotsColor);

                Texture spotsImg = Reflection.getFieldValue("spotsImg", potion);
                sb.draw(spotsImg, renderPosX, renderPosY, renderWidth, renderHeight);
            }

            sb.setColor(Color.WHITE);

            Texture containerImg = Reflection.getFieldValue("containerImg", potion);
            sb.draw(containerImg, renderPosX, renderPosY, renderWidth, renderHeight);
        }
    }
}
