package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Toggle;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.BooleanSetting;

public class ToggleSettingUI extends AbstractSettingUI {
    /** Variables */
    /** Constructors */
    public ToggleSettingUI(BooleanSetting setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        int buttonDim = Math.min((int)(width * valuePercX), valueHeight);

        middle = new Toggle(UIThemeManager.getDefaultTheme().button_small, UIThemeManager.getDefaultTheme().button_small_confirm, xPos + width - buttonDim, valuePosY, buttonDim, buttonDim){
            @Override
            public void toggle() {
                super.toggle();
                setting.toggle();
            }
        }.setToggled(setting.getCurrentValue());

        setting.setOnValueChangedConsumer(new Runnable() {
            @Override
            public void run() {
                Toggle element = (Toggle) middle;
                if(element.isToggled() != setting.getCurrentValue()){
                    element.setToggled(setting.getCurrentValue());
                }
            }
        });
    }

    @Override
    public boolean canDisplayMultiline() {
        return false;
    }
}
