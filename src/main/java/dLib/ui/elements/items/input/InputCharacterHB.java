package dLib.ui.elements.items.input;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.Interactable;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class InputCharacterHB extends Interactable {
    public int glyphRowIndex;
    public int glyphIndex;

    public ECharHbSide side;

    public InputCharacterHB(AbstractPosition x, AbstractPosition y, AbstractDimension width, AbstractDimension height, int glyphRowIndex, int glyphIndex, ECharHbSide side) {
        super(Tex.stat(UICommonResources.white_pixel), x, y, width, height);

        addComponent(new UITransientElementComponent());

        setRenderColor(new Color(0.6f, 0.6f, 1, 0.75f));

        setPassthrough(true);

        removeOnHoverSoundKey();
        removeOnTriggerSoundKey();

        this.glyphRowIndex = glyphRowIndex;
        this.glyphIndex = glyphIndex;

        hideInstantly();

        this.side = side;
    }

    public enum ECharHbSide{
        Left,
        Right
    }
}
