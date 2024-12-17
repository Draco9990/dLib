package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.FillDimension;

public class AutoDimensionValueEditor extends DimensionValueEditor<AutoDimension> {
    private TextBox textBox;

    public AutoDimensionValueEditor(AutoDimension value, AbstractDimension width, AbstractDimension height){
        this(new DimensionProperty(value), width, height);
    }

    public AutoDimensionValueEditor(DimensionProperty property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.fill());
        {
            textBox = new TextBox(property.getValueForDisplay(), Dim.fill(), Dim.fill());
            textBox.setImage(UICommonResources.inputfield);
            contentBox.addItem(textBox);

            contentBox.addItem(makeSwapComboBox());
        }
    }
}
