package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.EnumBoxWithArrows;
import dLib.ui.elements.prefabs.TextButton;
import dLib.properties.objects.templates.TEnumProperty;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

public class EnumPropertyEditor extends AbstractPropertyEditor<TEnumProperty<? extends Enum<?>, ?>> {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    TextButton middleButton;

    //endregion

    //region Constructors

    public EnumPropertyEditor(TEnumProperty<? extends Enum<?>, ?> setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline){
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TEnumProperty<? extends Enum<?>, ?> property, AbstractDimension width, AbstractDimension height) {
        EnumBoxWithArrows box = new EnumBoxWithArrows(width, height);
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
