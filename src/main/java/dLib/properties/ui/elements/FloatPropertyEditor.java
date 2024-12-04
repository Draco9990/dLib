package dLib.properties.ui.elements;

import dLib.properties.objects.FloatProperty;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.NumberInputBoxWithArrows;

public class FloatPropertyEditor extends AbstractPropertyEditor<FloatProperty> {
    //region Variables

    private NumberInputBoxWithArrows numberInputBoxWithArrows;
    Inputfield middleInputfield;

    //endregion

    //region Constructors

    public FloatPropertyEditor(FloatProperty setting, Integer xPos, Integer yPos, Integer width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(FloatProperty property, Integer width, Integer height) {
        numberInputBoxWithArrows = new NumberInputBoxWithArrows(0, 0, width, height, NumberInputBoxWithArrows.ENumberType.DECIMAL);
        numberInputBoxWithArrows.setLinkedProperty(property);
        return numberInputBoxWithArrows;
    }

    //endregion

    /** Constructors */
}
