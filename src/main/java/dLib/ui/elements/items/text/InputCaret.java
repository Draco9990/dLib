package dLib.ui.elements.items.text;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.animations.misc.UIAnimation_Blinking;
import dLib.ui.elements.items.Image;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

public class InputCaret extends Image {
    public InputCaret(AbstractDimension height) {
        super(Tex.stat(UICommonResources.white_pixel), Pos.px(0), Pos.px(0), Dim.px(5), height);

        setIdleAnimation(new UIAnimation_Blinking(this));
        setRenderColor(Color.BLACK);

        Image fill = new Image(Tex.stat(UICommonResources.white_pixel), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        fill.setPadding(Padd.px(1));
        addChild(fill);
    }
}
