package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.text.TextBox;

public class UCEITTextBox extends UCEITemplate {
    public UCEITTextBox() {
        super("Text Box");
    }

    @Override
    protected UIElement.UIElementData generateElementData() {
        return new TextBox.TextBoxData();
    }
}
