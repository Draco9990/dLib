package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.elements.UIElement;
import dLib.util.TextureManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class PasswordBox extends UIElement {
    public Inputfield inputfield;
    public Button showPasswordButton;

    public PasswordBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        HorizontalBox box = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill(), true);
        {
            inputfield = new Inputfield("", Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            inputfield.getTextBox().setFont(FontHelper.cardTitleFont);
            inputfield.getTextBox().setMaxFontScale(0.8f);
            inputfield.getTextBox().setTextRenderColor(Color.WHITE);
            inputfield.getTextBox().setObscureText(true);
            box.addItem(inputfield);

            showPasswordButton = new Button(Pos.px(0), Pos.px(0), Dim.height(), Dim.fill());
            showPasswordButton.setImage(TextureManager.getTexture("dLibResources/images/ui/common/peekButton.png"));
            showPasswordButton.addOnLeftClickEvent(() -> {
                inputfield.getTextBox().setObscureText(false);
            });
            showPasswordButton.addOnLeftClickReleaseEvent(() -> {
                inputfield.getTextBox().setObscureText(true);
            });
            box.addItem(showPasswordButton);

            addChildNCS(box);
        }
    }
}
