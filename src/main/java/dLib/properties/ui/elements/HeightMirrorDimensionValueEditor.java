package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.HeightMirrorDimension;

public class HeightMirrorDimensionValueEditor extends DimensionValueEditor<HeightMirrorDimension> {
    private TextBox textBox;

    public HeightMirrorDimensionValueEditor(HeightMirrorDimension value){
        this(new DimensionProperty(value));
    }

    public HeightMirrorDimensionValueEditor(DimensionProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.auto());
        {
            textBox = new TextBox(property.getValueForDisplay(), Dim.fill(), Dim.px(50));
            textBox.setImage(Tex.stat(UICommonResources.inputfield));
            contentBox.addItem(textBox);

            contentBox.addItem(makeSwapComboBox());
        }
        addChildNCS(contentBox);
    }
}
