package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.animations.entry.UIAnimation_FadeIn;
import dLib.ui.animations.exit.UIAnimation_FadeOut;
import dLib.ui.resources.UICommonResources;

import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class DarkenLayer extends Image {
    public DarkenLayer(){
        super(Tex.stat(UICommonResources.white_pixel), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());

        setRenderColor(new Color(0, 0, 0, 0.85f));

        setEntryAnimation(new UIAnimation_FadeIn(this));
        setExitAnimation(new UIAnimation_FadeOut(this));

        setPassthrough(false);
    }

    @Override
    public boolean isPassthrough() {
        return super.isPassthrough();
    }
}
