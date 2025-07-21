package dLib.ui.elements.items.popup;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.DarkenLayer;

public class GenericPopupHolder extends UIElement {
    private DarkenLayer darkenLayer;

    public GenericPopupHolder(){
        super();

        setModal(true);
        setContextual(true);

        darkenLayer = new DarkenLayer();
        addChild(darkenLayer);
    }
}
