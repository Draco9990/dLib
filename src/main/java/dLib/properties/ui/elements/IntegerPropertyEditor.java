package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.elements.prefabs.NumberInputBoxWithArrows;

public class IntegerPropertyEditor extends AbstractPropertyEditor<IntegerProperty> {
    //region Variables

    private NumberInputBoxWithArrows numberInputBoxWithArrows;
    Inputfield middleInputfield;

    //endregion

    //region Constructors

    public IntegerPropertyEditor(IntegerProperty setting, Integer xPos, Integer yPos, Integer width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(IntegerProperty property, Integer width, Integer height) {
        numberInputBoxWithArrows = new NumberInputBoxWithArrows(0, 0, width, height, NumberInputBoxWithArrows.ENumberType.WHOLE);
        numberInputBoxWithArrows.setLinkedProperty(property);
        return numberInputBoxWithArrows;
    }

    //endregion

    /** Constructors */
}
