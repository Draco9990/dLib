package dLib.ui.elements.items.buttons;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.animations.entry.UIAnimation_SlideInLeft;
import dLib.ui.animations.exit.UIAnimation_SlideOutRight;
import dLib.ui.elements.items.text.TextButton;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class ConfirmButton extends TextButton {
    public ConfirmButton() {
        super("Confirm", Pos.px(1650), Pos.px(130), Dim.px(270), Dim.px(90));

        setTexture(Tex.stat("dLibResources/images/ui/common/ConfirmButton.png"));

        label.setDimensions(186, 54);
        label.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        label.setLocalPosition(50, 30);
        label.setFontSize(20);

        this.setEntryAnimation(new UIAnimation_SlideInLeft(this));
        this.setExitAnimation(new UIAnimation_SlideOutRight(this));
        this.show();

        label.setTextRenderColor(Color.valueOf("FFEDA7"));
    }
}
