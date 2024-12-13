package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TDimensionProperty;
import dLib.properties.objects.templates.TPositionProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.util.ui.dimensions.*;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.PercentagePosition;
import dLib.util.ui.position.StaticPosition;

import java.util.ArrayList;

public class DimensionPropertyEditor extends AbstractPropertyEditor<TDimensionProperty<? extends TDimensionProperty>> {

    private UIElement xInputVal;
    private UIElement yInputVal;

    private ComboBox<AbstractDimension> xValueChanged;
    private ComboBox<AbstractDimension> yValueChanged;

    public DimensionPropertyEditor(TDimensionProperty<? extends TDimensionProperty> property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(property, xPos, yPos, width, multiline);
    }

    @Override
    protected UIElement buildContent(TDimensionProperty<? extends TDimensionProperty> property, AbstractDimension width, AbstractDimension height) {
        ArrayList<AbstractDimension> positionOptions = new ArrayList<>();
        positionOptions.add(new StaticDimension(0));
        positionOptions.add(new PercentageDimension(0));
        positionOptions.add(new FillDimension());
        positionOptions.add(new AutoDimension());

        ArrayList<AbstractDimension> widthPositionOptions = new ArrayList<>(positionOptions);
        widthPositionOptions.add(new HeightMirrorDimension());

        ArrayList<AbstractDimension> heightPositionOptions = new ArrayList<>(positionOptions);
        heightPositionOptions.add(new WidthMirrorDimension());

        HorizontalBox horizontalBox = new HorizontalBox(width, height);
        {
            TextBox xLabel = new TextBox("W:", Dim.perc(0.1), Dim.fill());
            horizontalBox.addItem(xLabel);

            buildXPositionValueBox(property, property.getWidth());
            horizontalBox.addItem(xInputVal);

            xValueChanged = new ComboBox<AbstractDimension>(property.getWidth(), widthPositionOptions, Dim.perc(0.1f), Dim.px(15)){
                @Override
                public String itemToString(AbstractDimension item) {
                    return item.getSimpleDisplayName();
                }
            };
            xValueChanged.addOnSelectedItemChangedEvent((classComboBox, option) -> {
                if(option == null || property.getWidth().getClass() == option.getClass()) return;
                property.setWidth(option.cpy());
            });
            xValueChanged.getTextBox().setFontScaleOverride(0.5f);
            horizontalBox.addItem(xValueChanged);

            horizontalBox.addItem(new Spacer(Dim.perc(0.1), Dim.fill()));

            TextBox yLabel = new TextBox("Y:", Dim.perc(0.1), Dim.fill());
            horizontalBox.addItem(yLabel);

            buildYPositionValueBox(property, property.getHeight());
            horizontalBox.addItem(yInputVal);

            yValueChanged = new ComboBox<AbstractDimension>(property.getHeight(), heightPositionOptions, Dim.perc(0.1f), Dim.px(15)){
                @Override
                public String itemToString(AbstractDimension item) {
                    return item.getSimpleDisplayName();
                }
            };
            yValueChanged.addOnSelectedItemChangedEvent((classComboBox, option) -> {
                if(option == null || property.getHeight().getClass() == option.getClass()) return;
                property.setHeight(option.cpy());
            });
            yValueChanged.getTextBox().setFontScaleOverride(0.5f);
            horizontalBox.addItem(yValueChanged);
        }

        property.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            delayedActions.add(() -> {
                if(oldValue.getKey().getClass() != newValue.getKey().getClass()){
                    xValueChanged.setSelectedItem(property.getWidth());
                    buildXPositionValueBox(property, property.getWidth());
                }

                if(oldValue.getValue().getClass() != newValue.getValue().getClass()){
                    yValueChanged.setSelectedItem(property.getHeight());
                    buildYPositionValueBox(property, property.getHeight());
                }
            });
        });

        return horizontalBox;
    }

    private void buildXPositionValueBox(TDimensionProperty<? extends TDimensionProperty> property, AbstractDimension positionValue){
        UIElement builtValueBox = buildPositionValueBox(positionValue);

        if(xInputVal != null){
            xInputVal.getParent().replaceChild(xInputVal, builtValueBox);
        }

        xInputVal = builtValueBox;

        if(positionValue instanceof StaticDimension){
            Inputfield inputfield = (Inputfield) builtValueBox;
            inputfield.addOnValueChangedListener(s -> property.setWidth(new StaticDimension(Integer.parseInt(s))));

            property.onValueChangedEvent.subscribe(xInputVal, (abstractPositionAbstractPositionPair, abstractPositionAbstractPositionPair2) -> {
                if(abstractPositionAbstractPositionPair.getKey().getClass() == abstractPositionAbstractPositionPair2.getKey().getClass()){
                    ((Inputfield)xInputVal).getTextBox().setText(String.valueOf(((StaticDimension)abstractPositionAbstractPositionPair2.getKey()).getValueRaw()));
                }
            });
        }
        else if(positionValue instanceof PercentageDimension){
            Inputfield inputfield = (Inputfield) builtValueBox;
            inputfield.addOnValueChangedListener(s -> property.setWidth(new PercentageDimension(Float.parseFloat(s))));

            property.onValueChangedEvent.subscribe(xInputVal, (abstractPositionAbstractPositionPair, abstractPositionAbstractPositionPair2) -> {
                if(abstractPositionAbstractPositionPair.getKey().getClass() == abstractPositionAbstractPositionPair2.getKey().getClass()){
                    ((Inputfield)xInputVal).getTextBox().setText(String.valueOf(((PercentageDimension)abstractPositionAbstractPositionPair2.getKey()).getValueRaw()));
                }
            });
        }
    }

    private void buildYPositionValueBox(TDimensionProperty<? extends TDimensionProperty> property, AbstractDimension positionValue){
        UIElement builtValueBox = buildPositionValueBox(positionValue);

        if(yInputVal != null){
            yInputVal.getParent().replaceChild(yInputVal, builtValueBox);
        }

        yInputVal = builtValueBox;

        if(positionValue instanceof StaticDimension){
            Inputfield inputfield = (Inputfield) builtValueBox;
            inputfield.addOnValueChangedListener(s -> property.setHeight(new StaticDimension(Integer.parseInt(s))));

            property.onValueChangedEvent.subscribe(yInputVal, (abstractPositionAbstractPositionPair, abstractPositionAbstractPositionPair2) -> {
                if(abstractPositionAbstractPositionPair.getValue().getClass() == abstractPositionAbstractPositionPair2.getValue().getClass()){
                    ((Inputfield)yInputVal).getTextBox().setText(String.valueOf(((StaticDimension)abstractPositionAbstractPositionPair2.getValue()).getValueRaw()));
                }
            });
        }
        else if(positionValue instanceof PercentageDimension){
            Inputfield inputfield = (Inputfield) builtValueBox;
            inputfield.addOnValueChangedListener(s -> property.setHeight(new PercentageDimension(Float.parseFloat(s))));

            property.onValueChangedEvent.subscribe(yInputVal, (abstractPositionAbstractPositionPair, abstractPositionAbstractPositionPair2) -> {
                if(abstractPositionAbstractPositionPair.getValue().getClass() == abstractPositionAbstractPositionPair2.getValue().getClass()){
                    ((Inputfield)yInputVal).getTextBox().setText(String.valueOf(((PercentageDimension)abstractPositionAbstractPositionPair2.getValue()).getValueRaw()));
                }
            });
        }
    }

    private UIElement buildPositionValueBox(AbstractDimension positionValue){
        UIElement builtValueBox = null;

        if(positionValue instanceof StaticDimension){
            Inputfield inputfield = new Inputfield(String.valueOf(((StaticDimension)positionValue).getValueRaw()), Dim.perc(0.25f), Dim.fill());
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);

            builtValueBox = inputfield;
        }
        else if(positionValue instanceof PercentageDimension){
            Inputfield inputfield = new Inputfield(String.valueOf(((PercentageDimension)positionValue).getValueRaw()), Dim.perc(0.25f), Dim.fill());
            inputfield.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL);

            builtValueBox = inputfield;
        }

        return builtValueBox;
    }
}
