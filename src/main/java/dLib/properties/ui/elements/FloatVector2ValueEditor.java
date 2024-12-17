package dLib.properties.ui.elements;

import com.badlogic.gdx.math.Vector2;
import dLib.properties.objects.FloatVector2Property;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.Spacer;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;

public class FloatVector2ValueEditor extends AbstractValueEditor<Vector2, FloatVector2Property> {
    //region Variables

    Inputfield xInput;
    Inputfield yInput;

    //endregion

    //region Constructors

    public FloatVector2ValueEditor(Vector2 val, AbstractDimension width, AbstractDimension height) {
        this(new FloatVector2Property(val), width, height);
    }

    public FloatVector2ValueEditor(FloatVector2Property property, AbstractDimension width, AbstractDimension height) {
        super(property);

        HorizontalBox mainContentBox = new HorizontalBox(width, height);
        {
            if(property.getXValueName() != null) {
                TextBox xLabel = new TextBox(property.getXValueName(), Dim.perc(0.2), Dim.fill());
                mainContentBox.addItem(xLabel);
            }

            xInput = new Inputfield(String.valueOf(property.getXValue()), Dim.perc(0.25), Dim.fill()).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            xInput.getTextBox().addOnTextChangedConsumer(s -> {
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

            yInput = new Inputfield(String.valueOf(property.getYValue()), Dim.perc(0.25), Dim.fill()).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            yInput.getTextBox().addOnTextChangedConsumer(s -> {
                Vector2 currentVal = property.getValue();
                currentVal.y = Float.parseFloat(s);
                property.setValue(currentVal);
            });
            mainContentBox.addItem(yInput);
        }

        property.onValueChangedEvent.subscribe(this, (integerVector2, integerVector22) -> {
            if(!isEditorValidForPropertyChange()) return;

            TextBox xBox = xInput.getTextBox();
            TextBox yBox = yInput.getTextBox();

            if(!xBox.getText().equals(property.getValueForDisplay())){
                xBox.setText(property.getValueForDisplay());
            }
            if(!yBox.getText().equals(property.getValueForDisplay())){
                yBox.setText(property.getValueForDisplay());
            }
        });

        addChildNCS(mainContentBox);
    }

    //endregion
}
