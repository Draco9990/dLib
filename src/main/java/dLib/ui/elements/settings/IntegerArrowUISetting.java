package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.IntegerSetting;

import java.util.function.Consumer;

public class IntegerArrowUISetting extends AbstractUISetting {
    /** Constructors */
    public IntegerArrowUISetting(IntegerSetting setting, Integer xPos, Integer yPos, Integer width, int height, boolean showArrows){
        super(setting, xPos, yPos, width, height);

        if(showArrows){
            int arrowDim = Math.min((int)(2.5f * width), height);

            int hOffset = 0;
            if(arrowDim != height){
                hOffset = (int)((height-arrowDim) / 2);
            }

            left = new Button((int)(xPos + width * 0.75f), yPos + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.increment();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_left);
            right = new Button((int)(xPos + width * 0.975f), yPos + hOffset, arrowDim, arrowDim){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();
                    setting.decrement();
                }
            }.setImage(UIThemeManager.getDefaultTheme().arrow_right);

            middle = new Inputfield(setting.getCurrentValue().toString(), ((int)(xPos + width * 0.775f)), yPos, ((int)(width * 0.2f)), height).filterAddNumerical();
            ((Inputfield)middle).getTextBox().setOnTextChangedConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    setting.setCurrentValue(Integer.valueOf(s));
                }
            });
        }
        else{
            middle = new Inputfield(setting.getCurrentValue().toString(), ((int)(xPos + width * 0.75f)), yPos, ((int)(width * 0.25f)), height).filterAddNumerical();
            ((Inputfield)middle).getTextBox().setOnTextChangedConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    setting.setCurrentValue(Integer.valueOf(s));
                }
            });
        }
    }
}
