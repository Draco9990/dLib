package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.Dim;

public class AutoDimensionValueEditor extends DimensionValueEditor<AutoDimension> {
    private ImageTextBox textBox;

    public AutoDimensionValueEditor(AutoDimension value){
        this(new DimensionProperty(value));
    }

    public AutoDimensionValueEditor(DimensionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            textBox = new ImageTextBox(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
            contentBox.addChild(textBox);

            contentBox.addChild(makeSwapComboBox());
        }
        addChild(contentBox);
    }
}
