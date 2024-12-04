package dLib.tools.screeneditorold.ui.items.editoritems;

import dLib.ui.elements.prefabs.TextButton;
import dLib.util.ui.position.Pos;

public class TextButtonScreenEditorItem extends ScreenEditorItem<TextButton, TextButton.TextButtonData> {
    //region Variables
    //endregion

    //region Constructors

    public TextButtonScreenEditorItem(){
        super(Pos.px(0), Pos.px(0), 75, 75);
    }

    public TextButtonScreenEditorItem(TextButton.TextButtonData elementData) {
        super(elementData);
    }

    //endregion

    //region Methods

    @Override
    protected TextButton.TextButtonData makeDataType() {
        return new TextButton.TextButtonData();
    }

    //endregion
}
