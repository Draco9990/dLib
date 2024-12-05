package dLib.ui.elements.components;

import dLib.ui.elements.UIElement;

public abstract class UIElementComponent<RequiredElementType extends UIElement> {
    public abstract void onRegisterComponent(RequiredElementType owner);
    public abstract void onUnregisterComponent(RequiredElementType owner);
}
