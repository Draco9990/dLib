package dLib.propertyeditors.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Toggle;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.prefabs.BooleanProperty;

public class TogglePropertyEditor extends AbstractPropertyEditor<BooleanProperty> {
    //region Variables

    Toggle button;

    //endregion

    //region Constructors

    public TogglePropertyEditor(BooleanProperty setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods


    @Override
    protected UIElement buildContent(BooleanProperty property, Integer width, Integer height) {
        int buttonDim = Math.min(width, height);

        button = new Toggle(UIThemeManager.getDefaultTheme().button_small, UIThemeManager.getDefaultTheme().button_small_confirm, 0, 0, buttonDim, buttonDim){
            @Override
            public void toggle() {
                super.toggle();
                property.toggle();
            }
        }.setToggled(property.getValue());

        property.addOnValueChangedListener((aBoolean, aBoolean2) -> {
            if(button.isToggled() != property.getValue()){
                button.setToggled(property.getValue());
            }
        });

        return button;
    }

    @Override
    public boolean canDisplayMultiline() {
        return false;
    }

    //endregion
}
