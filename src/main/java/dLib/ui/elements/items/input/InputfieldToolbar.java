package dLib.ui.elements.items.input;

import dLib.ui.animations.entry.UIAnimation_FadeIn;
import dLib.ui.animations.exit.UIAnimation_FadeOut;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;

public class InputfieldToolbar extends HorizontalBox {
    public InputfieldToolbar(AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos, Dim.auto(), Dim.px(60));

        setTexture(UICommonResources.tooltipBg);
        setContentPadding(Padd.px(5));

        addComponent(new UITransientElementComponent());

        setEntryAnimation(new UIAnimation_FadeIn(this));
        setExitAnimation(new UIAnimation_FadeOut(this));

        TextButton b1 = new TextButton("A", Dim.px(50), Dim.px(50));
        addChild(b1);

        b1 = new TextButton("A", Dim.px(50), Dim.px(50));
        addChild(b1);

        b1 = new TextButton("A", Dim.px(50), Dim.px(50));
        addChild(b1);

        b1 = new TextButton("A", Dim.px(50), Dim.px(50));
        addChild(b1);
    }
}
