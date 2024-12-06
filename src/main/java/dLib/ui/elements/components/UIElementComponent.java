package dLib.ui.elements.components;

import dLib.ui.elements.UIElement;

public abstract class UIElementComponent<RequiredElementType extends UIElement> {
    public void onRegisterComponent(RequiredElementType owner){}
    public void onUnregisterComponent(RequiredElementType owner){}

    public void onUpdate(RequiredElementType owner){}
    public void onRender(RequiredElementType owner){}
}
