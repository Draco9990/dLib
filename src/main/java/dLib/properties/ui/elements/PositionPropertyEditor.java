package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TPositionProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.PercentagePosition;
import dLib.util.ui.position.StaticPosition;

import java.util.ArrayList;

public class PositionPropertyEditor extends AbstractPropertyEditor<TPositionProperty<?>> {

    public PositionPropertyEditor(TPositionProperty<?> property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(property, xPos, yPos, width, multiline);
    }

    @Override
    protected UIElement buildContent(TPositionProperty<?> property, AbstractDimension width, AbstractDimension height) {
        ArrayList<AbstractPosition> positionOptions = new ArrayList<>();
        positionOptions.add(new StaticPosition(0));
        positionOptions.add(new PercentagePosition(0));

        HorizontalBox horizontalBox = new HorizontalBox(width, height);
        {
            TextBox xLabel = new TextBox("X:", Dim.perc(0.1), Dim.fill());
            horizontalBox.addItem(xLabel);

            horizontalBox.addItem(buildPositionValueBox(property, property.getXPosition(), true));

            ComboBox<AbstractPosition> xValueChanged = new ComboBox<AbstractPosition>(property.getXPosition(), positionOptions, Dim.perc(0.1f), Dim.px(15)){
                @Override
                public String itemToString(AbstractPosition item) {
                    return item.getSimpleDisplayName();
                }
            };
            xValueChanged.addOnSelectedItemChangedEvent((classComboBox, option) -> {
                if(option == null || property.getXPosition().getClass() == option.getClass()) return;
                property.setXPosition(option.cpy());
            });
            xValueChanged.getTextBox().setFontScaleOverride(0.5f);
            horizontalBox.addItem(xValueChanged);

            horizontalBox.addItem(new Spacer(Dim.perc(0.1), Dim.fill()));

            TextBox yLabel = new TextBox("Y:", Dim.perc(0.1), Dim.fill());
            horizontalBox.addItem(yLabel);

            horizontalBox.addItem(buildPositionValueBox(property, property.getYPosition(), false));

            ComboBox<AbstractPosition> yValueChanged = new ComboBox<AbstractPosition>(property.getYPosition(), positionOptions, Dim.perc(0.1f), Dim.px(15)){
                @Override
                public String itemToString(AbstractPosition item) {
                    return item.getSimpleDisplayName();
                }
            };
            yValueChanged.addOnSelectedItemChangedEvent((classComboBox, option) -> {
                if(option == null || property.getYPosition().getClass() == option.getClass()) return;
                property.setYPosition(option.cpy());
            });
            yValueChanged.getTextBox().setFontScaleOverride(0.5f);
            horizontalBox.addItem(yValueChanged);

            property.onValueChangedEvent.subscribe(this, (_property, oldValue, newValue) -> {
                xValueChanged.setSelectedItem(property.getXPosition());
                yValueChanged.setSelectedItem(property.getYPosition());
            });
        }

        return horizontalBox;
    }

    private UIElement buildPositionValueBox(TPositionProperty<?> property, AbstractPosition positionValue, boolean isXPos){
        if(positionValue instanceof StaticPosition){
            Inputfield inputfield = new Inputfield(String.valueOf(((StaticPosition)positionValue).getValueRaw()), Dim.perc(0.25f), Dim.fill());
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);

            if(isXPos){
                inputfield.addOnValueChangedListener((newVal) -> {
                    property.setXPosition(new StaticPosition(Integer.parseInt(newVal)));
                });
            }
            else{
                inputfield.addOnValueChangedListener((newVal) -> {
                    property.setYPosition(new StaticPosition(Integer.parseInt(newVal)));
                });
            }


            if(isXPos){
                property.onValueChangedEvent.subscribe(this, (_property, oldValue, newValue) -> inputfield.getTextBox().setText(String.valueOf(((StaticPosition)newValue.getKey()).getValueRaw())));
            }
            else{
                property.onValueChangedEvent.subscribe(this, (_property, oldValue, newValue) -> inputfield.getTextBox().setText(String.valueOf(((StaticPosition)newValue.getValue()).getValueRaw())));
            }

            return inputfield;
        }
        else{
            return new Spacer(Dim.fill(), Dim.fill());
        }
    }
}
