package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.util.ESelectionMode;

public class VerticalBox extends VerticalListBox<UIElement> {
    //region Variables

    //endregion

    //region Constructors

    public VerticalBox(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);
        setSelectionMode(ESelectionMode.NONE);
        //getBackground().setImage(null);
    }

    public VerticalBox(VerticalListBoxData data) {
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
