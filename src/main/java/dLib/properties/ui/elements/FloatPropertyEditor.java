package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TFloatProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.NumberInputBoxWithArrows;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class FloatPropertyEditor extends AbstractPropertyEditor<TFloatProperty<?>> {
    //region Variables

    private NumberInputBoxWithArrows numberInputBoxWithArrows;
    Inputfield middleInputfield;

    //endregion

    //region Constructors

    public FloatPropertyEditor(TFloatProperty setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TFloatProperty property, AbstractDimension width, AbstractDimension height) {
        numberInputBoxWithArrows = new NumberInputBoxWithArrows(width, height, NumberInputBoxWithArrows.ENumberType.DECIMAL);
        numberInputBoxWithArrows.setLinkedProperty(property);
        return numberInputBoxWithArrows;
    }

    //endregion
}
