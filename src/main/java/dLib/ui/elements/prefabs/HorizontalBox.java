package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;

public class HorizontalBox extends HorizontalListBox<UIElement>  {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalBox(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);
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
