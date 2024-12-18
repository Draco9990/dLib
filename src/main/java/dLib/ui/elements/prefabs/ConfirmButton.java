package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.Alignment;
import dLib.ui.animations.entry.UIAnimation_SlideInLeft;
import dLib.ui.animations.exit.UIAnimation_SlideOutRight;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class ConfirmButton extends TextButton{
    public ConfirmButton() {
        super("Confirm", Pos.px(1650), Pos.px(1080-950), Dim.px(270), Dim.px(90));

        this.getButton().setImage(Tex.stat("dLibResources/images/ui/common/ConfirmButton.png"));

        this.getTextBox().setWidth(186);
        this.getTextBox().setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        this.getTextBox().setFont(FontHelper.buttonLabelFont);
        this.getTextBox().setLocalPositionX(65);
        this.getTextBox().setFontScale(1f);

        this.setEntryAnimation(new UIAnimation_SlideInLeft(this));
        this.setExitAnimation(new UIAnimation_SlideOutRight(this));
        this.show();

        getTextBox().setTextRenderColor(Color.valueOf("FFEDA7"));
    }
}
