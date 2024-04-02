package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.Spacer;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.IntegerVector2;
import dLib.util.settings.prefabs.IntegerVector2Property;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class IntegerVector2SettingUI extends AbstractSettingUI{
    //region Variables

    Inputfield xInput;
    Inputfield yInput;

    //endregion

    //region Constructors

    public IntegerVector2SettingUI(IntegerVector2Property setting, Integer xPos, Integer yPos, Integer width, Integer height) {
        super(setting, xPos, yPos, width, height);

        int renderWidth = (int) (width * valuePercX);
        int startingX = (width - renderWidth);

        int textWidth = (int) (0.2f * renderWidth);
        int inputfieldWidth = (int)(0.25f * renderWidth);

        HorizontalBox horizontalBox = new HorizontalBox(startingX, 0, renderWidth, valueHeight);
        if(setting.getXValueName() != null) {
            horizontalBox.addItem(new TextBox(setting.getXValueName(), 0, 0, textWidth, valueHeight, 0.15f, 0.15f).setTextRenderColor(Color.WHITE));
        }

        xInput = new Inputfield(setting.getXValue().toString(), 0, 0, inputfieldWidth, valueHeight).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
        xInput.getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                IntegerVector2 currentVal = setting.getValue();
                if(s.isEmpty()) {
                    currentVal.x = 0;
                }
                else{
                    currentVal.x = Integer.valueOf(s);
                }

                setting.setValue(currentVal);
            }
        });
        horizontalBox.addItem(xInput);

        horizontalBox.addItem(new Spacer((int) (0.1 * renderWidth), valueHeight));

        if(setting.getYValueName() != null){
            horizontalBox.addItem(new TextBox(setting.getYValueName(), 0, 0, textWidth, valueHeight, 0.15f, 0.15f).setTextRenderColor(Color.WHITE));
        }

        yInput = new Inputfield(setting.getYValue().toString(), 0, 0, inputfieldWidth, valueHeight).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
        yInput.getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
            @Override
            public void accept(String s) {
                IntegerVector2 currentVal = setting.getValue();
                if(s.isEmpty()) {
                    currentVal.y = 0;
                }
                else{
                    currentVal.y = Integer.parseInt(s);
                }

                setting.setValue(currentVal);
            }
        });
        horizontalBox.addItem(yInput);

        addChildCS(horizontalBox);

        setting.addOnValueChangedListener(new BiConsumer<IntegerVector2, IntegerVector2>() {
            @Override
            public void accept(IntegerVector2 integerVector2, IntegerVector2 integerVector22) {
                if(!xInput.getTextBox().getText().equals(String.valueOf(setting.getValue().x))){
                    xInput.getTextBox().setText(String.valueOf(setting.getValue().x));
                }

                if(!yInput.getTextBox().getText().equals(String.valueOf(setting.getValue().y))){
                    yInput.getTextBox().setText(String.valueOf(setting.getValue().y));
                }
            }
        });
    }

    //endregion

    //region Methods

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
