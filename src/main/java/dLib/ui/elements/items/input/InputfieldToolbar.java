package dLib.ui.elements.items.input;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.animations.entry.UIAnimation_FadeIn;
import dLib.ui.animations.exit.UIAnimation_FadeOut;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.color.ColorPickerPopup;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.IntegerVector2;
import dLib.util.bindings.texture.Tex;
import dLib.util.helpers.UIHelpers;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.UUID;

public class InputfieldToolbar extends HorizontalBox {
    public InputfieldToolbar(AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos, Dim.auto(), Dim.px(40));

        setItemSpacing(3);

        setTexture(UICommonResources.tooltipBg);
        setContentPadding(Padd.px(5));

        addComponent(new UITransientElementComponent());

        setEntryAnimation(new UIAnimation_FadeIn(this));
        setExitAnimation(new UIAnimation_FadeOut(this));

        hideAndDisableInstantly();

        Button b1 = new Button(Dim.px(30), Dim.px(30));
        b1.onLeftClickEvent.subscribe(this, () -> {
            ColorPickerPopup colorPicker = new ColorPickerPopup(Color.WHITE);
            colorPicker.open();
        });
        {
            Image bImg = new Image(Tex.stat(UICommonResources.inputfield_color), Dim.fill(), Dim.fill());
            bImg.setPadding(Padd.px(5));
            b1.addChild(bImg);
        }
        addChild(b1);

        b1 = new Button(Dim.px(30), Dim.px(30));
        addChild(b1);
    }
}
