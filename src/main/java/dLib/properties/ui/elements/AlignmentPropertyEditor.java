package dLib.properties.ui.elements;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Spacer;
import dLib.ui.elements.prefabs.TextButton;
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
        HorizontalBox contentBox = new HorizontalBox(width, height); //replace with ArrowButton
        {
            leftButton = new TextButton(property.getValue().horizontalAlignment.name(), Dim.perc(45), Dim.fill());
            leftButton.getButton().addOnLeftClickEvent(() -> {
                Alignment alignment = property.getValue();
                alignment.horizontalAlignment = (Alignment.HorizontalAlignment) EnumHelpers.nextEnum(alignment.horizontalAlignment);
                property.setValue(alignment);
            });
            contentBox.addItem(leftButton);

            contentBox.addItem(new Spacer(Dim.perc(10), Dim.fill()));

            rightButton = new TextButton(property.getValue().verticalAlignment.name(), Dim.perc(45), Dim.fill());
            rightButton.getButton().addOnLeftClickEvent(() -> {
                Alignment alignment = property.getValue();
                alignment.verticalAlignment = (Alignment.VerticalAlignment) EnumHelpers.nextEnum(alignment.verticalAlignment);
                property.setValue(alignment);
            });
            contentBox.addItem(rightButton);
        }

        property.addOnHorizontalAlignmentChangedListener((horizontalAlignment, horizontalAlignment2) -> {
            if(!leftButton.getTextBox().getText().equals(property.getValue().horizontalAlignment.name())){
                leftButton.getTextBox().setText(property.getValue().horizontalAlignment.name());
            }
        });
        property.addOnVerticalAlignmentChangedListener((verticalAlignment, verticalAlignment2) -> {
            if(!rightButton.getTextBox().getText().equals(property.getValue().verticalAlignment.name())){
                rightButton.getTextBox().setText(property.getValue().verticalAlignment.name());
            }
        });

        return contentBox;
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
