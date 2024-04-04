package dLib.tools.screeneditor.ui.items.editoritems;

import dLib.ui.elements.prefabs.Image;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.bindings.texture.TextureThemeBinding;

public class InputfieldScreenEditorItem extends ScreenEditorItem<Inputfield, Inputfield.InputfieldData> {
    //region Variables
    //endregion

    //region Constructors

    public InputfieldScreenEditorItem(){
        super(0, 0, 500, 75);
    }

    public InputfieldScreenEditorItem(Inputfield.InputfieldData elementData) {
        super(elementData);
    }

    //endregion

    //region Methods

    @Override
    protected Inputfield.InputfieldData makeDataType() {
        return new Inputfield.InputfieldData();
    }

    //endregion
}
