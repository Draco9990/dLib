package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.Dim;

public class AutoDimensionValueEditor extends DimensionValueEditor<AutoDimension> {
    private TextBox textBox;

    public AutoDimensionValueEditor(AutoDimension value){
        this(new DimensionProperty(value));
    }

    public AutoDimensionValueEditor(DimensionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            textBox = new TextBox(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
            textBox.setImage(UICommonResources.inputfield);
            contentBox.addItem(textBox);

            contentBox.addItem(makeSwapComboBox());
        }
        addChildNCS(contentBox);
    }
}
