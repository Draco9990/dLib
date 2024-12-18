package dLib.properties.ui.elements;

import dLib.properties.objects.DimensionProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.ComboBox;
import dLib.util.ui.dimensions.*;

import java.util.ArrayList;

public class DimensionValueEditor<ValueType extends AbstractDimension> extends AbstractValueEditor<ValueType, DimensionProperty> {
    public DimensionValueEditor(DimensionProperty property) {
        super(property);
    }

    protected UIElement makeSwapComboBox(){
        ArrayList<AbstractDimension> positionOptions = new ArrayList<>();
        positionOptions.add(new StaticDimension(0));
        positionOptions.add(new PercentageDimension(0));
        positionOptions.add(new FillDimension());
        positionOptions.add(new AutoDimension());
        positionOptions.add(new HeightMirrorDimension());
        positionOptions.add(new WidthMirrorDimension());

        ComboBox<AbstractDimension> comboBox = new ComboBox<AbstractDimension>(boundProperty.getValue(), positionOptions, Dim.px(28), Dim.px(15)){
            @Override
            public String itemToString(AbstractDimension item) {
                return item.getSimpleDisplayName();
            }
        };
        comboBox.getTextBox().setFontScale(0.2f);
        comboBox.addOnSelectedItemChangedEvent((classComboBox, option) -> {
            if(option == null || boundProperty.getValue().getClass() == option.getClass()) return;

            boundProperty.setValue(option);
        });

        return comboBox;
    }
}
