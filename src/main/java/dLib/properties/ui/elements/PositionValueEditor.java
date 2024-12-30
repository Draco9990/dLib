package dLib.properties.ui.elements;

import dLib.properties.objects.PositionProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.ComboBox;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.PercentagePosition;
import dLib.util.ui.position.PixelPosition;

import java.util.ArrayList;

public class PositionValueEditor<ValueType> extends AbstractValueEditor<ValueType, PositionProperty> {
    public PositionValueEditor(PositionProperty property) {
        super(property);
    }

    protected UIElement makeSwapComboBox(){
        ArrayList<AbstractPosition> positionOptions = new ArrayList<>();
        positionOptions.add(new PixelPosition(0));
        positionOptions.add(new PercentagePosition(0));

        ComboBox<AbstractPosition> comboBox = new ComboBox<AbstractPosition>(boundProperty.getValue(), positionOptions, Dim.px(28), Dim.px(15)){
            @Override
            public String itemToString(AbstractPosition item) {
                return item.getDisplayValue();
            }
        };
        comboBox.label.setFontScale(0.2f);
        comboBox.onSelectionChangedEvent.subscribe(comboBox, (option) -> {
            if(option == null || boundProperty.getValue().getClass() == option.getClass()) return;
            boundProperty.setValue(option.cpy());
        });

        return comboBox;
    }
}
