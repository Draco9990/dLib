package dLib.properties.ui.elements;

import com.badlogic.gdx.math.Vector2;
import dLib.properties.objects.FloatVector2Property;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.Spacer;
import dLib.ui.elements.items.text.TextBox;
import dLib.util.ui.dimensions.Dim;

public class FloatVector2ValueEditor extends AbstractValueEditor<Vector2, FloatVector2Property> {
    //region Variables

    Inputfield xInput;
    Inputfield yInput;

    //endregion

    //region Constructors

    public FloatVector2ValueEditor(Vector2 val) {
        this(new FloatVector2Property(val));
    }

    public FloatVector2ValueEditor(FloatVector2Property property) {
        super(property);

        HorizontalBox mainContentBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            if(property.getXValueName() != null) {
                TextBox xLabel = new TextBox(property.getXValueName(), Dim.perc(0.2), Dim.fill());
                mainContentBox.addItem(xLabel);
            }

            xInput = new Inputfield(String.valueOf(property.getXValue()), Dim.perc(0.25), Dim.fill()).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE);
            xInput.textBox.addOnTextChangedConsumer(s -> {
                Vector2 currentVal = boundProperty.getValue();
                currentVal.x = Float.parseFloat(s);
                boundProperty.setValue(currentVal);
            });
            mainContentBox.addItem(xInput);

            mainContentBox.addItem(new Spacer(Dim.perc(0.1), Dim.fill()));

            if(property.getYValueName() != null){
                TextBox yLabel = new TextBox(property.getYValueName(), Dim.perc(0.2), Dim.fill());
                mainContentBox.addItem(yLabel);
            }

            yInput = new Inputfield(String.valueOf(property.getYValue()), Dim.perc(0.25), Dim.fill()).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE);
            yInput.textBox.addOnTextChangedConsumer(s -> {
                Vector2 currentVal = boundProperty.getValue();
                currentVal.y = Float.parseFloat(s);
                boundProperty.setValue(currentVal);
            });
            mainContentBox.addItem(yInput);
        }

        property.onValueChangedEvent.subscribe(this, (integerVector2, integerVector22) -> {
            if(!isEditorValidForPropertyChange()) return;

            TextBox xBox = xInput.textBox;
            TextBox yBox = yInput.textBox;

            if(!xBox.getText().equals(boundProperty.getValueForDisplay())){
                xBox.setText(String.valueOf(boundProperty.getXValue()));
            }
            if(!yBox.getText().equals(boundProperty.getValueForDisplay())){
                yBox.setText(String.valueOf(boundProperty.getYValue()));
            }
        });

        addChildNCS(mainContentBox);
    }

    //endregion
}
