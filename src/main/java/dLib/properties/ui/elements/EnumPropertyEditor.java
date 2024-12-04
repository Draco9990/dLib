package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.EnumBoxWithArrows;
import dLib.ui.elements.prefabs.TextButton;
import dLib.properties.objects.templates.TEnumProperty;

public class EnumPropertyEditor extends AbstractPropertyEditor<TEnumProperty<? extends Enum<?>, ?>> {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    TextButton middleButton;

    //endregion

    //region Constructors

    public EnumPropertyEditor(TEnumProperty<? extends Enum<?>, ?> setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TEnumProperty<? extends Enum<?>, ?> property, Integer width, Integer height) {
        EnumBoxWithArrows box = new EnumBoxWithArrows(0, 0, width, height);
        box.setLinkedProperty(property);
        return box;
    }

    @Override
    public boolean onLeftInteraction() {
        leftArrow.trigger();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        rightArrow.trigger();
        return true;
    }

    //endregion
}
