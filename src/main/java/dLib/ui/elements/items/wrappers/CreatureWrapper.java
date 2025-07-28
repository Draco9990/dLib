package dLib.ui.elements.items.wrappers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public class CreatureWrapper extends UIElement {
    private AbstractCreature creature;
    private boolean hasUpdateMethod;

    private UIElement refElement;

    public CreatureWrapper(AbstractCreature creature) {
        super();

        commonInitialize(creature);
    }
    public CreatureWrapper(AbstractCreature creature, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        commonInitialize(creature);
    }
    public CreatureWrapper(AbstractCreature creature, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        commonInitialize(creature);
    }

    private void commonInitialize(AbstractCreature creature){
        this.creature = creature;
        hasUpdateMethod = Reflection.getMethodByNameAndParams("update", creature.getClass(), new Class[0]) != null;

        refElement = new UIElement(Dim.px(creature.hb.width), Dim.px(creature.hb.height));
        addChild(refElement);
    }

    public void setHorizontalContentAlignment(Alignment.HorizontalAlignment horizontalAlignment){
        refElement.setHorizontalAlignment(horizontalAlignment);
    }
    public void setVerticalContentAlignment(Alignment.VerticalAlignment verticalAlignment){
        refElement.setVerticalAlignment(verticalAlignment);
    }
    public void setContentAlignment(Alignment.HorizontalAlignment horizontalAlignment, Alignment.VerticalAlignment verticalAlignment){
        refElement.setAlignment(horizontalAlignment, verticalAlignment);
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();

        creature.drawX = refElement.getWorldPositionCenteredX() * Settings.xScale;
        creature.drawY = refElement.getWorldPositionY() * Settings.yScale;

        if(hasUpdateMethod) {
            Reflection.invokeMethod("update", creature);
        }
    }

    @Override
    protected void renderChildren(SpriteBatch sb) {
        super.renderChildren(sb);

        boolean pushedScissors = false;
        PositionBounds maskBounds = getWorldBounds();
        if(maskBounds != null){
            sb.flush();

            Rectangle scissors = new Rectangle();
            Rectangle mask = new Rectangle(maskBounds.left * Settings.xScale, maskBounds.bottom * Settings.yScale, (maskBounds.right - maskBounds.left) * Settings.xScale, (maskBounds.top - maskBounds.bottom) * Settings.yScale);

            ScissorStack.calculateScissors(getCamera(), sb.getTransformMatrix(), mask, scissors);
            pushedScissors = ScissorStack.pushScissors(scissors);
        }

        creature.render(sb);

        if(pushedScissors){
            ScissorStack.popScissors();
            sb.flush();
        }
    }

    public UIElement getRefElement(){
        return refElement;
    }
}
