package dLib.ui.elements.items.itembox;

import dLib.ui.elements.UIElement;
import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class HorizontalBox extends HorizontalListBox<UIElement> {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalBox(AbstractPosition xPos, AbstractPosition yPos) {
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public HorizontalBox(AbstractDimension width, AbstractDimension height) {
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public HorizontalBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        disableItemWrapping();

        setSelectionMode(ESelectionMode.NONE);
        setImage(new TextureNoneBinding());
    }

    public HorizontalBox(HorizontalListBoxData data) {
        super(data);
    }

    //endregion

    //region Methods

    @Override
    public UIElement makeUIForItem(UIElement item) {
        return item;
    }

    //endregion
}
