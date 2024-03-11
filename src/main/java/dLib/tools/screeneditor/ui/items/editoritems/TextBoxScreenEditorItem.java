package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextBox;

public class TextBoxScreenEditorItem extends ScreenEditorItem<TextBox, TextBox.TextBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public TextBoxScreenEditorItem(){
        super(0, 0, 50, 50);
    }

    //endregion

    //region Methods

    @Override
    protected TextBox.TextBoxData makeDataType() {
        return new TextBox.TextBoxData();
    }

    //endregion
}
