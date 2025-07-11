package dLib.modsscreen.ui.elements.items;

import basemod.ModPanel;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.DarkenLayer;
import dLib.ui.elements.items.buttons.CancelButton;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class ModPanelUIWrapper extends UIElement {
    private ModPanel element;

    private DarkenLayer darkenLayer;

    public ModPanelUIWrapper(ModPanel element) {
        super();

        commonInit(element);
    }
    public ModPanelUIWrapper(ModPanel element, AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos);

        commonInit(element);
    }
    public ModPanelUIWrapper(ModPanel element, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        commonInit(element);
    }
    public ModPanelUIWrapper(ModPanel element, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        commonInit(element);
    }

    private void commonInit(ModPanel panel){
        darkenLayer = new DarkenLayer();
        addChild(darkenLayer);

        CancelButton closeButton = new CancelButton();
        closeButton.label.setText("Close");
        closeButton.onLeftClickEvent.subscribeManaged(() -> getTopParent().close());
        addChild(closeButton);

        element = panel;
    }

    @Override
    protected void updateChildren() {
        element.update();

        super.updateChildren();
    }

    @Override
    protected void renderChildren(SpriteBatch sb) {
        super.renderChildren(sb);

        element.render(sb);
    }
}
