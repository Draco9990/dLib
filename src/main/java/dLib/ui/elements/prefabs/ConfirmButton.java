package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.Alignment;
import dLib.util.TextureManager;

public class ConfirmButton extends TextButton{
    public ConfirmButton() {
        super("CONFIRM", 1650, 1080-950, 270, 90);

        this.getButton().setImage(TextureManager.getTexture("dLibResources/images/ui/common/ConfirmButton.png"));

        this.getTextBox().setDimensions(186, 51);
        this.getTextBox().setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        this.getTextBox().setFont(FontHelper.buttonLabelFont);
        this.getTextBox().setLocalPosition(65, 30);
        this.getTextBox().setFontScaleOverride(1f);

        getTextBox().setTextRenderColor(Color.valueOf("FFEDA7"));
    }
}
