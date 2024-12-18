package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.ImageTextBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
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
            contentBox.addItem(textBox);

            contentBox.addItem(makeSwapComboBox());
        }
        addChildNCS(contentBox);
    }
}
