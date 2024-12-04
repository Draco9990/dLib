package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.ui.elements.prefabs.TextBox;
import dLib.util.ui.position.Pos;

public class TextBoxScreenEditorItem extends ScreenEditorItem<TextBox, TextBox.TextBoxData> {
    //region Variables
    //endregion

    //region Constructors

    public TextBoxScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), 300, 75);
    }

    public TextBoxScreenEditorItem(TextBox.TextBoxData elementData) {
        super(elementData);
    }

    //endregion

    //region Methods

    @Override
    protected TextBox.TextBoxData makeDataType() {
        return new TextBox.TextBoxData();
    }

    //endregion
}
