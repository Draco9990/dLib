package dLib.ui.elements.items;

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

        this.getButton().setImage(Tex.stat("dLibResources/images/ui/common/CancelButton.png"));

        this.getTextBox().setDimensions(186, 54);
        this.getTextBox().setLocalPosition(27, 27);
        this.getTextBox().setFontScale(1f);

        this.setEntryAnimation(new UIAnimation_SlideInRight(this));
        this.setExitAnimation(new UIAnimation_SlideOutLeft(this));
        this.show();

        getTextBox().setTextRenderColor(Color.valueOf("FFEDA7"));
    }
}
