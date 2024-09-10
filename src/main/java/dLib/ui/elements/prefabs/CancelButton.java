package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.util.TextureManager;

public class CancelButton extends TextButton{
    public CancelButton() {
        super("CANCEL", 0, 1080-949, 278, 102);

        this.getButton().setImage(TextureManager.getTexture("dLibResources/images/ui/common/CancelButton.png"));

        this.getTextBox().setDimensions(186, 54);
        this.getTextBox().setFont(FontHelper.buttonLabelFont);
        this.getTextBox().setLocalPosition(27, 27);

        getTextBox().setTextRenderColor(Color.valueOf("FFEDA7"));
    }
}
