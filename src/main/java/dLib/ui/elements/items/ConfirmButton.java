package dLib.ui.elements.items;

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

        this.getButton().setImage(Tex.stat("dLibResources/images/ui/common/ConfirmButton.png"));

        this.getTextBox().setDimensions(186, 54);
        this.getTextBox().setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        this.getTextBox().setLocalPosition(50, 30);
        this.getTextBox().setFontScale(1f);

        this.setEntryAnimation(new UIAnimation_SlideInLeft(this));
        this.setExitAnimation(new UIAnimation_SlideOutRight(this));
        this.show();

        getTextBox().setTextRenderColor(Color.valueOf("FFEDA7"));
    }
}
