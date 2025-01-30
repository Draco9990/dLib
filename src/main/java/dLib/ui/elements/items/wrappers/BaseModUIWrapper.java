package dLib.ui.elements.items.wrappers;

import basemod.IUIElement;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class BaseModUIWrapper extends UIElement {
    private IUIElement element;

    public BaseModUIWrapper(IUIElement element) {
        super();

        this.element = element;
    }
    public BaseModUIWrapper(IUIElement element, AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos);

        this.element = element;
    }
    public BaseModUIWrapper(IUIElement element, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        this.element = element;
    }
    public BaseModUIWrapper(IUIElement element, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        this.element = element;
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();

        element.update();
    }

    @Override
    protected void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);

        element.render(sb);
    }
}
