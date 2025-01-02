package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.FillDimension;

public class FillDimensionValueEditor extends DimensionValueEditor<FillDimension> {
    private ImageTextBox textBox;

    public FillDimensionValueEditor(FillDimension value){
        this(new DimensionProperty(value));
    }

    public FillDimensionValueEditor(DimensionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            textBox = new ImageTextBox(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
            contentBox.addItem(textBox);

            contentBox.addItem(makeSwapComboBox());
        }
        addChild(contentBox);
    }
}
