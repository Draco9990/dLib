package dLib.ui.elements.items.popup;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.DarkenLayer;

public class GenericScreenHolder extends UIElement {
    private DarkenLayer darkenLayer;

    public GenericScreenHolder(){
        super();

        setModal(true);

        setOverridesBaseScreen(true);

        darkenLayer = new DarkenLayer();
        addChild(darkenLayer);
    }
}
