package dLib.ui.elements.settings;

import dLib.ui.elements.prefabs.Toggle;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.BooleanProperty;

import java.util.function.BiConsumer;

public class ToggleSettingUI extends AbstractSettingUI {
    //region Variables

    Toggle button;

    //endregion

    //region Constructors

    public ToggleSettingUI(BooleanProperty setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);

        int buttonDim = Math.min((int)(width * valuePercX), valueHeight);

        button = new Toggle(UIThemeManager.getDefaultTheme().button_small, UIThemeManager.getDefaultTheme().button_small_confirm, xPos + width - buttonDim, valuePosY, buttonDim, buttonDim){
            @Override
            public void toggle() {
                super.toggle();
                setting.toggle();
            }
        }.setToggled(setting.getValue());
        addChildCS(button);

        setting.addOnValueChangedListener(new BiConsumer<Boolean, Boolean>() {
            @Override
            public void accept(Boolean aBoolean, Boolean aBoolean2) {
                if(button.isToggled() != setting.getValue()){
                    button.setToggled(setting.getValue());
                }
            }
        });
    }

    //endregion

    //region Methods

    @Override
    public boolean canDisplayMultiline() {
        return false;
    }

    @Override
    protected float getTextWidthPerc() {
        return 0.75f;
    }

    //endregion
}
