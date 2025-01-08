package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.buttons.Button;

public class UCEITButton extends UCEITemplate {
    public UCEITButton() {
        super("Button");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new Button.ButtonData();
    }
}
