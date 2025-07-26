package dLib.ui.elements.items.popup;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class SimpleTextPopup extends GenericScreenHolder {
    public SimpleTextPopup(String text) {
        super();

        Image bg = new Image(Tex.stat(UICommonResources.bg01), Pos.px(573), Pos.px(306), Dim.px(806), Dim.px(453));
        {
            TextBox textBox = new TextBox(text, Pos.px(89), Pos.px(62), Dim.px(629), Dim.px(319));
            textBox.setWrap(true);
            bg.addChild(textBox);
        }
        bg.setEntryAnimation(new UIAnimation_SlideInUp(bg));
        bg.setExitAnimation(new UIAnimation_SlideOutDown(bg));
        addChild(bg);

        Button quitButton = new Button(Pos.px(934), Pos.px(284), Dim.px(505), Dim.px(388));
        {
            TextBox textBox = new TextBox("Close", Pos.px(162), Pos.px(29), Dim.px(253), Dim.px(57));
            quitButton.addChild(textBox);
        }
        quitButton.setTexture(Tex.stat(ImageMaster.OPTION_EXIT));
        quitButton.postLeftClickEvent.subscribe(quitButton, getTopParent()::dispose);
        quitButton.setEntryAnimation(new UIAnimation_SlideInUp(quitButton));
        quitButton.setExitAnimation(new UIAnimation_SlideOutDown(quitButton));
        addChild(quitButton);
    }
}
