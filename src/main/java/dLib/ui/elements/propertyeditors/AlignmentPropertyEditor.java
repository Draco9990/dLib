package dLib.ui.elements.propertyeditors;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Spacer;
import dLib.ui.elements.prefabs.TextButton;
import dLib.util.EnumHelpers;
import dLib.util.settings.prefabs.AlignmentProperty;

public class AlignmentPropertyEditor extends AbstractPropertyEditor<AlignmentProperty> {
    //region Variables

    TextButton leftButton;
    TextButton rightButton;

    //endregion

    //region Constructors

    public AlignmentPropertyEditor(AlignmentProperty setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(AlignmentProperty property, Integer width, Integer height) {
        HorizontalBox contentBox = new HorizontalBox(0, 0, width, height); //replace with ArrowButton

        int buttonWidth = (int)(0.45f * width);
        int spacerWidth = (int)(0.1f * width);

        leftButton = new TextButton(property.getValue().horizontalAlignment.name(), 0, 0, buttonWidth, height);
        leftButton.getButton().addOnLeftClickConsumer(() -> {
            Alignment alignment = property.getValue();
            alignment.horizontalAlignment = (Alignment.HorizontalAlignment) EnumHelpers.nextEnum(alignment.horizontalAlignment);
            property.setValue(alignment);
        });
        contentBox.addItem(leftButton);

        contentBox.addItem(new Spacer(spacerWidth, height));

        rightButton = new TextButton(property.getValue().verticalAlignment.name(), 0, 0, buttonWidth, height);
        rightButton.getButton().addOnLeftClickConsumer(() -> {
            Alignment alignment = property.getValue();
            alignment.verticalAlignment = (Alignment.VerticalAlignment) EnumHelpers.nextEnum(alignment.verticalAlignment);
            property.setValue(alignment);
        });
        contentBox.addItem(rightButton);

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
