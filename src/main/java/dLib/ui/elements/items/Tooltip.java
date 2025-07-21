package dLib.ui.elements.items;

import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class Tooltip extends ImageTextBox {
    public Tooltip(String text) {
        super(text, Pos.px(0), Pos.px(0), Dim.auto(), Dim.auto());

        setTexture(Tex.stat(UICommonResources.bg04));

        setPassthrough(true);

        textBox.setFontSize(12);
        textBox.setFont(Font.stat(FontHelper.tipHeaderFont));
    }
}
