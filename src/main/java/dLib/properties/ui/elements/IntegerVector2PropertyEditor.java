package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.Spacer;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.IntegerVector2;
import dLib.properties.objects.IntegerVector2Property;

public class IntegerVector2PropertyEditor extends AbstractPropertyEditor<IntegerVector2Property> {
    //region Variables

    Inputfield xInput;
    Inputfield yInput;

    //endregion

    //region Constructors

    public IntegerVector2PropertyEditor(IntegerVector2Property setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods


    @Override
    protected UIElement buildContent(IntegerVector2Property property, Integer width, Integer height) {
        int textWidth = (int) (0.2f * width);
        int inputfieldWidth = (int)(0.25f * width);

        HorizontalBox horizontalBox = new HorizontalBox(0, 0, width, height);
        if(property.getXValueName() != null) {
            horizontalBox.addItem(new TextBox(property.getXValueName(), 0, 0, textWidth, height, 0.15f, 0.15f).setTextRenderColor(Color.WHITE));
        }

        xInput = new Inputfield(property.getXValue().toString(), 0, 0, inputfieldWidth, height).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
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

        horizontalBox.addItem(new Spacer((int) (0.1 * width), height));

        if(property.getYValueName() != null){
            horizontalBox.addItem(new TextBox(property.getYValueName(), 0, 0, textWidth, height, 0.15f, 0.15f).setTextRenderColor(Color.WHITE));
        }

        yInput = new Inputfield(property.getYValue().toString(), 0, 0, inputfieldWidth, height).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
        yInput.getTextBox().addOnTextChangedConsumer(s -> {
            IntegerVector2 currentVal = property.getValue();
            if(s.isEmpty()) {
                currentVal.y = 0;
            }
            else{
                currentVal.y = Integer.parseInt(s);
            }

            property.setValue(currentVal);
        });
        horizontalBox.addItem(yInput);

        property.addOnValueChangedListener((integerVector2, integerVector22) -> {
            if(!xInput.getTextBox().getText().equals(String.valueOf(property.getValue().x))){
                xInput.getTextBox().setText(String.valueOf(property.getValue().x));
            }

            if(!yInput.getTextBox().getText().equals(String.valueOf(property.getValue().y))){
                yInput.getTextBox().setText(String.valueOf(property.getValue().y));
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
