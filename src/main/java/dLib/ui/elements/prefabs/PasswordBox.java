package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.elements.UIElement;
import dLib.util.TextureManager;

public class PasswordBox extends UIElement {
    private static int PASSWORD_BOX_SPACING = 5;

    public Inputfield inputfield;
    public Button showPasswordButton;

    public PasswordBox(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        if(width < PASSWORD_BOX_SPACING){
            throw new IllegalArgumentException("Width must be greater than " + PASSWORD_BOX_SPACING);
        }

        int buttonWidth = height;
        if(width - buttonWidth - PASSWORD_BOX_SPACING <= 0) {
            buttonWidth = (width - PASSWORD_BOX_SPACING) / 2;
        }

        inputfield = new Inputfield("", 0, 0, width - buttonWidth - PASSWORD_BOX_SPACING, height);
        inputfield.getTextBox().setFont(FontHelper.cardTitleFont);
        inputfield.getTextBox().setMaxFontScale(0.8f);
        inputfield.getTextBox().setTextRenderColor(Color.WHITE);
        inputfield.getTextBox().setObscureText(true);
        addChildCS(inputfield);

        showPasswordButton = new Button(width - buttonWidth, 0, buttonWidth, height);
        showPasswordButton.setImage(TextureManager.getTexture("dLibResources/images/ui/common/peekButton.png"));
        showPasswordButton.addOnLeftClickConsumer(() -> {
            inputfield.getTextBox().setObscureText(false);
        });
        showPasswordButton.addOnLeftClickReleaseConsumer(() -> {
            inputfield.getTextBox().setObscureText(true);
        });
        addChildCS(showPasswordButton);
    }
}
