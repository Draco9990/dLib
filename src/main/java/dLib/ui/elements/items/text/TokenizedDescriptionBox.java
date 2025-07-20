package dLib.ui.elements.items.text;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.helpers.UIHelpers;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class TokenizedDescriptionBox extends VerticalBox {
    public TokenizedDescriptionBox(AbstractPosition xPos, AbstractPosition yPos) {
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }

    public TokenizedDescriptionBox(AbstractDimension width, AbstractDimension height) {
        this(Pos.px(0), Pos.px(0), width, height);
    }

    public TokenizedDescriptionBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        setTexture(UICommonResources.inputfield);
        setRenderColor(Color.WHITE);
        setHorizontalItemSpacing(15);
        setVerticalItemSpacing(10);
        setContentPadding(Padd.px(15));
        setVerticalContentAlignment(Alignment.VerticalAlignment.CENTER);
        setHorizontalContentAlignment(Alignment.HorizontalAlignment.CENTER);
        setGridMode(true);
    }

    //region Methods

    public void setFromText(String text) {
        setChildren(UIHelpers.tokenizeStr(text));
    }

    //endregion

}
