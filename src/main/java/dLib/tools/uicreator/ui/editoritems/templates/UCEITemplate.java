package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.tools.uicreator.ui.editoritems.UCEditorItem;
import dLib.ui.elements.UIElement;

public abstract class UCEITemplate {
    private String displayName;

    public UCEITemplate(String displayName) {
        this.displayName = displayName;
    }

    public abstract UCEditorItem makeEditorItem();
    protected abstract UIElement.UIElementData makeElementData();

    @Override
    public String toString() {
        return displayName;
    }
}
