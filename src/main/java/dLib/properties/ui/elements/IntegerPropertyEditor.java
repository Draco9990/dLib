package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.properties.objects.templates.TIntegerProperty;
import dLib.ui.elements.prefabs.NumberInputBoxWithArrows;

public class IntegerPropertyEditor extends AbstractPropertyEditor<TIntegerProperty<?>> {
    //region Variables

    private NumberInputBoxWithArrows numberInputBoxWithArrows;
    Inputfield middleInputfield;

    //endregion

    //region Constructors

    public IntegerPropertyEditor(TIntegerProperty setting, Integer xPos, Integer yPos, Integer width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TIntegerProperty property, Integer width, Integer height) {
        numberInputBoxWithArrows = new NumberInputBoxWithArrows(0, 0, width, height, NumberInputBoxWithArrows.ENumberType.WHOLE);
        numberInputBoxWithArrows.setLinkedProperty(property);
        return numberInputBoxWithArrows;
    }

    //endregion

    /** Constructors */
}
