package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class CreatureWrapper extends UIElement {
    private AbstractCreature creature;
    private boolean hasUpdateMethod;

    public CreatureWrapper(AbstractCreature creature) {
        super();

        hasUpdateMethod = Reflection.getMethodByNameAndParams("update", creature.getClass(), new Class[0]) != null;
    }
    public CreatureWrapper(AbstractCreature creature, AbstractDimension width, AbstractDimension height) {
        super(width, height);
    }
    public CreatureWrapper(AbstractCreature creature, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);
    }

    @Override
    protected void updateChildren() {
        super.updateChildren();

        if(hasUpdateMethod) {
            Reflection.invokeMethod("update", creature);
        }
    }

    @Override
    protected void renderChildren(SpriteBatch sb) {
        super.renderChildren(sb);

        creature.render(sb);
    }
}
