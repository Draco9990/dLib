package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.text.TextButton;

public class UCEITTextButton extends UCEITemplate {
    public UCEITTextButton() {
        super("Text Button");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new TextButton.TextButtonData();
    }
}
