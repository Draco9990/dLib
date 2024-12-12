package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.Spacer;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.IntegerVector2;
import dLib.properties.objects.templates.TIntegerVector2Property;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

import java.util.Objects;

public class IntegerVector2PropertyEditor extends AbstractPropertyEditor<TIntegerVector2Property<TIntegerVector2Property>> {
    //region Variables

    Inputfield xInput;
    Inputfield yInput;

    //endregion

    //region Constructors

    public IntegerVector2PropertyEditor(TIntegerVector2Property<TIntegerVector2Property> setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods


    @Override
    protected UIElement buildContent(TIntegerVector2Property<TIntegerVector2Property> property, AbstractDimension width, AbstractDimension height) {
        HorizontalBox horizontalBox = new HorizontalBox(width, height);
        {
            if(property.getXValueName() != null) {
                TextBox xLabel = new TextBox(property.getXValueName() + ":", Dim.perc(0.2), Dim.fill());
                horizontalBox.addItem(xLabel);
            }

            xInput = new Inputfield(String.valueOf(property.getXValue()), Dim.perc(0.25), Dim.fill()).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            xInput.getTextBox().addOnTextChangedConsumer(s -> {
                IntegerVector2 currentVal = property.getValue();
                if(s.isEmpty()) {
                    currentVal.x = 0;
                }
                else{
                    currentVal.x = Integer.valueOf(s);
                }

                property.setValue(currentVal);
            });
            horizontalBox.addItem(xInput);

            horizontalBox.addItem(new Spacer(Dim.perc(0.1), Dim.fill()));

            if(property.getYValueName() != null){
                TextBox yLabel = new TextBox(property.getYValueName() + ":", Dim.perc(0.2), Dim.fill());
                horizontalBox.addItem(yLabel);
            }

            yInput = new Inputfield(String.valueOf(property.getYValue()), Dim.perc(0.25), Dim.fill()).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            yInput.getTextBox().addOnTextChangedConsumer(s -> {
                IntegerVector2 currentVal = property.getValue();
                if(s.isEmpty()) {
                    currentVal.y = 0;
                }
                else{
                    currentVal.y = Integer.valueOf(s);
                }

                property.setValue(currentVal);
            });
            horizontalBox.addItem(yInput);
        }

        property.onValueChangedEvent.subscribe(this, (integerVector2, integerVector22) -> {
            TextBox xBox = xInput.getTextBox();
            TextBox yBox = yInput.getTextBox();

            if(xBox.getText().isEmpty() || !Objects.equals(Float.valueOf(xBox.getText()), property.getValue().x)){
                boolean allowedDifference = false;
                if((property.getValue().x == 0 && xBox.getText().isEmpty())){
                    allowedDifference = true;
                }

                if(!allowedDifference) xBox.setText(String.valueOf(property.getValue().x));
            }

            if(yBox.getText().isEmpty() || !Objects.equals(Float.valueOf(yBox.getText()), property.getValue().y)){
                boolean allowedDifference = false;
                if((property.getValue().y == 0 && yBox.getText().isEmpty())){
                    allowedDifference = true;
                }

                if(!allowedDifference) yBox.setText(String.valueOf(property.getValue().y));
            }
        });

        return horizontalBox;
    }

    @Override
    public boolean onLeftInteraction() {
        xInput.select();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        yInput.select();
        return true;
    }

    @Override
    public boolean onCancelInteraction() {
        xInput.deselect();
        yInput.deselect();
        return true;
    }

    //endregion
}
