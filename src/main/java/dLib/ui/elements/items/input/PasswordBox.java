package dLib.ui.elements.items.input;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class PasswordBox extends UIElement {
    public Inputfield inputfield;
    public Button showPasswordButton;

    public PasswordBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        HorizontalBox box = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        {
            inputfield = new Inputfield("", Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            inputfield.textBox.setFont(Font.stat(FontHelper.cardTitleFont));
            inputfield.textBox.setTextRenderColor(Color.WHITE);
            inputfield.textBox.setObscureText(true);
            box.addItem(inputfield);

            showPasswordButton = new Button(Pos.px(0), Pos.px(0), Dim.mirror(), Dim.fill());
            showPasswordButton.setImage(Tex.stat("dLibResources/images/ui/common/peekButton.png"));
            showPasswordButton.onLeftClickEvent.subscribeManaged(() -> {
                inputfield.textBox.setObscureText(false);
            });
            showPasswordButton.onLeftClickReleaseEvent.subscribeManaged(() -> {
                inputfield.textBox.setObscureText(true);
            });
            box.addItem(showPasswordButton);

            addChild(box);
        }
    }
}
