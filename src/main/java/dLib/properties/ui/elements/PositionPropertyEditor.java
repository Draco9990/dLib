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

public class PositionPropertyEditor extends AbstractPropertyEditor<TPositionProperty<? extends TPositionProperty>> {

    private UIElement xInputVal;
    private UIElement yInputVal;

    private ComboBox<AbstractPosition> xValueChanged;
    private ComboBox<AbstractPosition> yValueChanged;

    public PositionPropertyEditor(TPositionProperty<? extends TPositionProperty> property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(property, xPos, yPos, width, multiline);
    }

    @Override
    protected UIElement buildContent(TPositionProperty<? extends TPositionProperty> property, AbstractDimension width, AbstractDimension height) {
        ArrayList<AbstractPosition> positionOptions = new ArrayList<>();
        positionOptions.add(new StaticPosition(0));
        positionOptions.add(new PercentagePosition(0));

        HorizontalBox horizontalBox = new HorizontalBox(width, height);
        {
            TextBox xLabel = new TextBox("X:", Dim.perc(0.1), Dim.fill());
            horizontalBox.addItem(xLabel);

            buildXPositionValueBox(property, property.getXPosition());
            horizontalBox.addItem(xInputVal);

            xValueChanged = new ComboBox<AbstractPosition>(property.getXPosition(), positionOptions, Dim.px(28), Dim.px(15)){
                @Override
                public String itemToString(AbstractPosition item) {
                    return item.getFullDisplayName();
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

            buildYPositionValueBox(property, property.getYPosition());
            horizontalBox.addItem(yInputVal);

            yValueChanged = new ComboBox<AbstractPosition>(property.getYPosition(), positionOptions, Dim.px(28), Dim.px(15)){
                @Override
                public String itemToString(AbstractPosition item) {
                    return item.getFullDisplayName();
                }
            };
            yValueChanged.addOnSelectedItemChangedEvent((classComboBox, option) -> {
                if(option == null || property.getYPosition().getClass() == option.getClass()) return;
                property.setYPosition(option.cpy());
            });
            yValueChanged.getTextBox().setFontScaleOverride(0.5f);
            horizontalBox.addItem(yValueChanged);
        }

        property.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            delayedActions.add(() -> {
                if(oldValue.getKey().getClass() != newValue.getKey().getClass()){
                    xValueChanged.setSelectedItem(property.getXPosition());
                    buildXPositionValueBox(property, property.getXPosition());
                }

                if(oldValue.getValue().getClass() != newValue.getValue().getClass()){
                    yValueChanged.setSelectedItem(property.getYPosition());
                    buildYPositionValueBox(property, property.getYPosition());
                }
            });
        });

        return horizontalBox;
    }

    private void buildXPositionValueBox(TPositionProperty<? extends TPositionProperty> property, AbstractPosition positionValue){
        UIElement builtValueBox = buildPositionValueBox(positionValue);

        if(xInputVal != null){
            xInputVal.getParent().replaceChild(xInputVal, builtValueBox);
        }

        xInputVal = builtValueBox;

        if(positionValue instanceof StaticPosition){
            Inputfield inputfield = (Inputfield) builtValueBox;
            inputfield.addOnValueChangedListener(s -> property.setXPosition(new StaticPosition(Integer.parseInt(s))));

            property.onValueChangedEvent.subscribe(xInputVal, (abstractPositionAbstractPositionPair, abstractPositionAbstractPositionPair2) -> {
                if(abstractPositionAbstractPositionPair.getKey().getClass() == abstractPositionAbstractPositionPair2.getKey().getClass()){
                    ((Inputfield)xInputVal).getTextBox().setText(String.valueOf(((StaticPosition)abstractPositionAbstractPositionPair2.getKey()).getValueRaw()));
                }
            });
        }
        else if(positionValue instanceof PercentagePosition){
            Inputfield inputfield = (Inputfield) builtValueBox;
            inputfield.addOnValueChangedListener(s -> property.setXPosition(new PercentagePosition(Float.parseFloat(s))));

            property.onValueChangedEvent.subscribe(xInputVal, (abstractPositionAbstractPositionPair, abstractPositionAbstractPositionPair2) -> {
                if(abstractPositionAbstractPositionPair.getKey().getClass() == abstractPositionAbstractPositionPair2.getKey().getClass()){
                    ((Inputfield)xInputVal).getTextBox().setText(String.valueOf(((PercentagePosition)abstractPositionAbstractPositionPair2.getKey()).getValueRaw()));
                }
            });
        }
    }

    private void buildYPositionValueBox(TPositionProperty<? extends TPositionProperty> property, AbstractPosition positionValue){
        UIElement builtValueBox = buildPositionValueBox(positionValue);

        if(yInputVal != null){
            yInputVal.getParent().replaceChild(yInputVal, builtValueBox);
        }

        yInputVal = builtValueBox;

        if(positionValue instanceof StaticPosition){
            Inputfield inputfield = (Inputfield) builtValueBox;
            inputfield.addOnValueChangedListener(s -> property.setYPosition(new StaticPosition(Integer.parseInt(s))));

            property.onValueChangedEvent.subscribe(yInputVal, (abstractPositionAbstractPositionPair, abstractPositionAbstractPositionPair2) -> {
                if(abstractPositionAbstractPositionPair.getValue().getClass() == abstractPositionAbstractPositionPair2.getValue().getClass()){
                    ((Inputfield)yInputVal).getTextBox().setText(String.valueOf(((StaticPosition)abstractPositionAbstractPositionPair2.getValue()).getValueRaw()));
                }
            });
        }
        else if(positionValue instanceof PercentagePosition){
            Inputfield inputfield = (Inputfield) builtValueBox;
            inputfield.addOnValueChangedListener(s -> property.setYPosition(new PercentagePosition(Float.parseFloat(s))));

            property.onValueChangedEvent.subscribe(yInputVal, (abstractPositionAbstractPositionPair, abstractPositionAbstractPositionPair2) -> {
                if(abstractPositionAbstractPositionPair.getValue().getClass() == abstractPositionAbstractPositionPair2.getValue().getClass()){
                    ((Inputfield)yInputVal).getTextBox().setText(String.valueOf(((PercentagePosition)abstractPositionAbstractPositionPair2.getValue()).getValueRaw()));
                }
            });
        }
    }

    private UIElement buildPositionValueBox(AbstractPosition positionValue){
        UIElement builtValueBox = null;

        if(positionValue instanceof StaticPosition){
            Inputfield inputfield = new Inputfield(String.valueOf(((StaticPosition)positionValue).getValueRaw()), Dim.perc(0.25f), Dim.fill());
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);

            builtValueBox = inputfield;
        }
        else if(positionValue instanceof PercentagePosition){
            Inputfield inputfield = new Inputfield(String.valueOf(((PercentagePosition)positionValue).getValueRaw()), Dim.perc(0.25f), Dim.fill());
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL);

            builtValueBox = inputfield;
        }

        return builtValueBox;
    }
}
