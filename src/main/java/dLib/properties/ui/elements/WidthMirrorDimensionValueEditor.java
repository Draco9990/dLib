package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.WidthMirrorDimension;

public class WidthMirrorDimensionValueEditor extends DimensionValueEditor<WidthMirrorDimension> {
    private ImageTextBox textBox;

    public WidthMirrorDimensionValueEditor(WidthMirrorDimension value){
        this(new DimensionProperty(value));
    }

    public WidthMirrorDimensionValueEditor(DimensionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            textBox = new ImageTextBox(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
            contentBox.addItem(textBox);

            contentBox.addItem(makeSwapComboBox());
        }
        addChildNCS(contentBox);
    }
}
