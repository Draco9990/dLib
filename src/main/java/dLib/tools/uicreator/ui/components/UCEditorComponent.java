package dLib.tools.uicreator.ui.components;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIElementComponent;

public class UCEditorComponent extends UIElementComponent<UIElement> {

    private UIElement.UIElementData elementData;

    public UCEditorComponent(UIElement.UIElementData elementData){
        this.elementData = elementData;
    }

    @Override
    public void onRegisterComponent(UIElement owner) {

    }

    @Override
    public void onUnregisterComponent(UIElement owner) {

    }
}
