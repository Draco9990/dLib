package dLib.ui.elements.items.input;

import basemod.Pair;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.Interactable;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.IntegerVector2;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.helpers.TextHelpers;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public class InputCharacterHB extends Interactable {
    public int glyphRowIndex;
    public int glyphIndex;

    public ECharHbSide side;

    public InputCharacterHB(AbstractPosition x, AbstractPosition y) {
        super(Tex.stat(UICommonResources.white_pixel), x, y, Dim.px(5), Dim.px(5));

        addComponent(new UITransientElementComponent());

        setRenderColor(new Color(0.6f, 0.6f, 1, 0.45f));

        setPassthrough(true);

        removeOnHoverSoundKey();
        removeOnTriggerSoundKey();
        hideInstantly();
    }

    public void setForCharacter(float worldPosX, float worldPosY, AbstractDimension width, AbstractDimension height, int glyphRowIndex, int glyphIndex, ECharHbSide side){
        setWidth(width);
        setHeight(height);
        this.glyphRowIndex = glyphRowIndex;
        this.glyphIndex = glyphIndex;
        this.side = side;

        setWorldPosition(worldPosX, worldPosY);
    }

    @Override
    public void postConstruct() {
        super.postConstruct();

        onLeftClickEvent.subscribe(this, () -> {
            int totalCharsUpTo = 0;
            Inputfield parent = getParentOfType(Inputfield.class);
            Pair<Vector2, GlyphLayout> layout1 = parent.textBox.prepareForRender();
            for (int i = 0; i < glyphRowIndex; i++){
                totalCharsUpTo += layout1.getValue().runs.get(i).glyphs.size;
            }
            totalCharsUpTo += glyphIndex;

            parent.setCaretOffset(TextHelpers.getTotalGlyphCount(layout1.getValue()) - totalCharsUpTo);
        });
    }

    public enum ECharHbSide{
        Left,
        Right
    }
}
