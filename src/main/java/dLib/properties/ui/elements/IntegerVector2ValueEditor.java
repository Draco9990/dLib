package dLib.properties.ui.elements;

import dLib.properties.objects.IntegerVector2Property;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.Spacer;
import dLib.ui.elements.items.text.TextBox;
import dLib.util.IntegerVector2;
import dLib.util.ui.dimensions.Dim;

import java.util.Objects;

public class IntegerVector2ValueEditor extends AbstractValueEditor<IntegerVector2, IntegerVector2Property> {
    //region Variables

    Inputfield xInput;
    Inputfield yInput;

    //endregion

    //region Constructors

    public IntegerVector2ValueEditor(IntegerVector2 val) {
        this(new IntegerVector2Property(val));
    }

    public IntegerVector2ValueEditor(IntegerVector2Property property) {
        super(property);

        HorizontalBox mainContentBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            if(property.getXValueName() != null) {
                TextBox xLabel = new TextBox(property.getXValueName() + ":", Dim.perc(0.2), Dim.fill());
                mainContentBox.addItem(xLabel);
            }

            xInput = new Inputfield(String.valueOf(property.getXValue()), Dim.perc(0.25), Dim.fill()).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE);
            xInput.textBox.addOnTextChangedConsumer(s -> {
                IntegerVector2 currentVal = property.getValue();
                currentVal.x = Integer.valueOf(s);
                property.setValue(currentVal);
            });
            mainContentBox.addItem(xInput);

            mainContentBox.addItem(new Spacer(Dim.perc(0.1), Dim.fill()));

            if(property.getYValueName() != null){
                TextBox yLabel = new TextBox(property.getYValueName() + ":", Dim.perc(0.2), Dim.fill());
                mainContentBox.addItem(yLabel);
            }

            yInput = new Inputfield(String.valueOf(property.getYValue()), Dim.perc(0.25), Dim.fill()).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE);
            yInput.textBox.addOnTextChangedConsumer(s -> {
                IntegerVector2 currentVal = property.getValue();
                currentVal.y = Integer.valueOf(s);
                property.setValue(currentVal);
            });
            mainContentBox.addItem(yInput);
        }

        property.onValueChangedEvent.subscribe(this, (integerVector2, integerVector22) -> {
            if(!isEditorValidForPropertyChange()) return;

            TextBox xBox = xInput.textBox;
            TextBox yBox = yInput.textBox;

            if(!Objects.equals(xBox.getText(), property.getValueForDisplay())){
                xBox.setText(property.getXValue().toString());
            }
            if(!Objects.equals(yBox.getText(), property.getValueForDisplay())){
                yBox.setText(property.getYValue().toString());
            }
        });

        addChildNCS(mainContentBox);
    }


    //endregion
}
