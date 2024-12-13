package dLib.properties.ui.elements;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.util.EnumHelpers;
import dLib.properties.objects.templates.TAlignmentProperty;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public class AlignmentPropertyEditor extends AbstractPropertyEditor<TAlignmentProperty<?>> {
    //region Variables

    TextButton leftButton;
    TextButton rightButton;

    //endregion

    //region Constructors

    public AlignmentPropertyEditor(TAlignmentProperty setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TAlignmentProperty property, AbstractDimension width, AbstractDimension height) {
        PredefinedGrid grid = new PredefinedGrid(3, 3, width, Dim.width());
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                grid.setGridSlotElement(i, j, new Button(Dim.fill(), Dim.fill()));
            }
        }
        return grid;
    }

    @Override
    public boolean onLeftInteraction() {
        leftButton.getButton().trigger();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        rightButton.getButton().trigger();
        return true;
    }

    //endregion
}
