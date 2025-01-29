package dLib.ui.elements.items.buttons;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.animations.entry.UIAnimation_SlideInRight;
import dLib.ui.animations.exit.UIAnimation_SlideOutLeft;
import dLib.ui.elements.items.text.TextButton;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class CancelButton extends TextButton {
    public CancelButton() {
        super("Cancel", Pos.px(0), Pos.px(131), Dim.px(278), Dim.px(102));

        setTexture(Tex.stat("dLibResources/images/ui/common/CancelButton.png"));

        label.setDimensions(186, 54);
        label.setLocalPosition(27, 27);
        label.setFontSize(20);

        this.setEntryAnimation(new UIAnimation_SlideInRight(this));
        this.setExitAnimation(new UIAnimation_SlideOutLeft(this));
        this.show();

        label.setTextRenderColor(Color.valueOf("FFEDA7"));
    }
}
