package dLib.ui.elements.items.input;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.animations.entry.UIAnimation_FadeIn;
import dLib.ui.animations.exit.UIAnimation_FadeOut;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.color.ColorPickerPopup;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;

public class InputfieldToolbar extends HorizontalBox {
    public InputfieldToolbar(AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos, Dim.auto(), Dim.px(40));

        setItemSpacing(3);

        setTexture(UICommonResources.bg04);
        setContentPadding(Padd.px(5));

        setEntryAnimation(new UIAnimation_FadeIn(this));
        setExitAnimation(new UIAnimation_FadeOut(this));

        setVisibilityAndEnabledInstantly(false, false);

        Button b1 = new Button(Dim.px(30), Dim.px(30));
        b1.postLeftClickEvent.subscribe(this, () -> {
            ColorPickerPopup colorPicker = new ColorPickerPopup(Color.WHITE, false, true, false, true);
            colorPicker.onSelectedColorChangedEvent.subscribe(this, (color, isStatic) -> getParentOfType(Inputfield.class).setColorForSelection(color));
            colorPicker.open();
        });
        {
            Image bImg = new Image(Tex.stat(UICommonResources.inputfield_color), Dim.fill(), Dim.fill());
            bImg.setPadding(Padd.px(5));
            b1.addChild(bImg);
        }
        addChild(b1);
    }
}
