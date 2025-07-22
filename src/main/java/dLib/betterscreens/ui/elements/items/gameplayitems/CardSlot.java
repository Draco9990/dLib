package dLib.betterscreens.ui.elements.items.gameplayitems;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public class CardSlot extends Button {
    private AbstractCard cardSlot;

    private float scale = 1f;

    public CardSlot(AbstractPosition xPos, AbstractPosition yPos, float scale) {
        super(xPos, yPos, Dim.px(302 * scale), Dim.px(419 * scale));

        setTexture(UICommonResources.cardSlot);

        this.scale = scale;
    }

    public void setCard(AbstractCard card){
        cardSlot = card;
    }
    public AbstractCard getCard() {
        return cardSlot;
    }

    @Override
    protected void renderCall(SpriteBatch sb, NinePatch ninePatchToRender, float renderPosX, float renderPosY, float renderWidth, float renderHeight) {
        if(cardSlot == null){
            super.renderCall(sb, ninePatchToRender, renderPosX, renderPosY, renderWidth, renderHeight);
        }
        else{
            float prevX = cardSlot.current_x;
            float prevY = cardSlot.current_y;

            cardSlot.current_x = renderPosX + 151 * Settings.xScale * scale;
            cardSlot.current_y = renderPosY + 209.5f * Settings.yScale * scale;

            cardSlot.drawScale = scale;

            cardSlot.render(sb);

            cardSlot.current_x = prevX;
            cardSlot.current_y = prevY;
        }
    }
}
