package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.properties.objects.templates.TIntegerProperty;
import dLib.ui.elements.prefabs.NumberInputBoxWithArrows;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class IntegerPropertyEditor extends AbstractPropertyEditor<TIntegerProperty<?>> {
    //region Variables

    private NumberInputBoxWithArrows numberInputBoxWithArrows;
    Inputfield middleInputfield;

    //endregion

    //region Constructors

    public IntegerPropertyEditor(TIntegerProperty setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline){
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TIntegerProperty property, AbstractDimension width, AbstractDimension height) {
        numberInputBoxWithArrows = new NumberInputBoxWithArrows(width, height, NumberInputBoxWithArrows.ENumberType.WHOLE);
        numberInputBoxWithArrows.setLinkedProperty(property);
        return numberInputBoxWithArrows;
    }

    //endregion
}
