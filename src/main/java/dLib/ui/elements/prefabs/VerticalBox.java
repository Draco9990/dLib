package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.util.ESelectionMode;

public class VerticalBox extends VerticalListBox<UIElement> {
    //region Variables

    //endregion

    //region Constructors

    public VerticalBox(int xPos, int yPos, int width, int height) {
        this(xPos, yPos, width, height, false);
    }

    public VerticalBox(int xPos, int yPos, int width, int height, boolean noInitScrollbar) {
        super(xPos, yPos, width, height, noInitScrollbar);

        reinitializeElements();

        setSelectionMode(ESelectionMode.NONE);
        getBackground().setImage(null);
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
