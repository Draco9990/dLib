package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.IntegerProperty;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class IntegerSettingUI extends AbstractSettingUI {
    //region Variables

    Button leftArrow;
    Button rightArrow;

    Inputfield middleInputfield;

    //endregion

    //region Constructors

    public IntegerSettingUI(IntegerProperty setting, Integer xPos, Integer yPos, Integer width, int height, boolean showArrows){
        super(setting, xPos, yPos, width, height);

        int startingX = (int) (xPos + width * (1-valuePercX));

        if(showArrows){
            int arrowDim = Math.min((int)(arrowPercX * width), valueHeight);

            int hOffset = 0;
            if(arrowDim != height){
                hOffset = (int)((height-arrowDim) / 2);
            }

            leftArrow = new Button(startingX, valuePosY + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.increment();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_left);
            addChildNCS(leftArrow);

            rightArrow = new Button((int)(xPos + width * (1-arrowPercX)), valuePosY + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.decrement();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_right);
            addChildNCS(rightArrow);

            middleInputfield = new Inputfield(setting.getValue().toString(), ((int)(xPos + width * ((1 - valuePercX) + arrowPercX))), valuePosY, ((int)(width * (valuePercX -2* arrowPercX))), valueHeight).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            middleInputfield.getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if(s.isEmpty()){
                        middleInputfield.getTextBox().setText("0");
                        return;
                    }
                    setting.setValue(Integer.valueOf(s));
                }
            });

        }
        else{
            middleInputfield = new Inputfield(setting.getValue().toString(), ((int)(xPos + width * (1 - valuePercX))), valuePosY, ((int)(width * valuePercX)), valueHeight).setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
            middleInputfield.getTextBox().addOnTextChangedConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if(s.isEmpty()) {
                        setting.setValue(0);
                        return;
                    }

                    setting.setValue(Integer.valueOf(s));
                }
            });
        }

        addChildCS(middleInputfield);

        setting.addOnValueChangedListener((integer, integer2) -> {
            if(!middleInputfield.getTextBox().getText().equals(setting.getValue().toString())){
                middleInputfield.getTextBox().setText(setting.getValue().toString());
            }
        });
    }

    //endregion

    //region Methods

    @Override
    public boolean onLeftInteraction() {
        if(leftArrow != null){
            leftArrow.trigger();
            return true;
        }
        return super.onLeftInteraction();
    }

    @Override
    public boolean onRightInteraction() {
        if(rightArrow != null){
            rightArrow.trigger();
            return true;
        }
        return super.onRightInteraction();
    }

    @Override
    public boolean onCancelInteraction() {
        middleInputfield.deselect();
        return true;
    }

    //endregion

    /** Constructors */
}
