package dLib.tools.uicreator.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;

public class UCERootElement extends UIElement {

    public UCERootElement(){
        super();

        onLeftClickEvent.subscribeManaged(() -> ElementGroupModifierComponent.deselectGroupComponents("editorItem"));
    }
}
