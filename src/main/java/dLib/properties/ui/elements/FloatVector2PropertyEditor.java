package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import dLib.properties.objects.FloatVector2Property;
import dLib.properties.objects.IntegerVector2Property;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.Spacer;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.IntegerVector2;

public class FloatVector2PropertyEditor extends AbstractPropertyEditor<FloatVector2Property> {
    //region Variables

    Inputfield xInput;
    Inputfield yInput;

    //endregion

    //region Constructors

    public FloatVector2PropertyEditor(FloatVector2Property setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods


    @Override
    protected UIElement buildContent(FloatVector2Property property, Integer width, Integer height) {
        int textWidth = (int) (0.2f * width);
        int inputfieldWidth = (int)(0.25f * width);

        HorizontalBox horizontalBox = new HorizontalBox(0, 0, width, height);
        if(property.getXValueName() != null) {
            horizontalBox.addItem(new TextBox(property.getXValueName(), 0, 0, textWidth, height, 0.15f, 0.15f).setTextRenderColor(Color.WHITE));
        }

        xInput = new Inputfield(String.valueOf(property.getXValue()), 0, 0, inputfieldWidth, height).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
        xInput.getTextBox().addOnTextChangedConsumer(s -> {
            Vector2 currentVal = property.getValue();
            if(s.isEmpty()) {
                currentVal.x = 0;
            }
            else{
                currentVal.x = Float.valueOf(s);
            }

            property.setValue(currentVal);
        });
        horizontalBox.addItem(xInput);

        horizontalBox.addItem(new Spacer((int) (0.1 * width), height));

        if(property.getYValueName() != null){
            horizontalBox.addItem(new TextBox(property.getYValueName(), 0, 0, textWidth, height, 0.15f, 0.15f).setTextRenderColor(Color.WHITE));
        }

        yInput = new Inputfield(String.valueOf(property.getYValue()), 0, 0, inputfieldWidth, height).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
        yInput.getTextBox().addOnTextChangedConsumer(s -> {
            Vector2 currentVal = property.getValue();
            if(s.isEmpty()) {
                currentVal.y = 0;
            }
            else{
                currentVal.y = Float.valueOf(s);
            }

            property.setValue(currentVal);
        });
        horizontalBox.addItem(yInput);

        property.addOnValueChangedListener((integerVector2, integerVector22) -> {
            TextBox xBox = xInput.getTextBox();
            TextBox yBox = yInput.getTextBox();

            if(!xBox.getText().equals(String.valueOf(property.getValue().x))){
                boolean allowedDifference = false;
                if((property.getValue().x == 0 && xBox.getText().isEmpty())){
                    allowedDifference = true;
                }
                if((int)property.getXValue() == property.getValue().x){
                    allowedDifference = true;
                }

                if(!allowedDifference) xBox.setText(String.valueOf(property.getValue().x));
            }

            if(!yBox.getText().equals(String.valueOf(property.getValue().y))){
                boolean allowedDifference = false;
                if((property.getValue().y == 0 && yBox.getText().isEmpty())){
                    allowedDifference = true;
                }
                if((int)property.getYValue() == property.getValue().y){
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
