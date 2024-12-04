package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.elements.prefabs.Checkbox;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.themes.UIThemeManager;
import dLib.properties.objects.BooleanProperty;

public class BooleanPropertyEditor extends AbstractPropertyEditor<BooleanProperty> {
    //region Variables

    Toggle button;

    //endregion

    //region Constructors

    public BooleanPropertyEditor(BooleanProperty setting, Integer xPos, Integer yPos, int width, int height){
        super(setting, xPos, yPos, width, height);
    }

    //endregion

    //region Methods


    @Override
    protected UIElement buildContent(BooleanProperty property, Integer width, Integer height) {
        int buttonDim = Math.min(width, height);

        HorizontalBox box = new HorizontalBox(0, 0, width, height, true);
        box.addItem(new UIElement(0, 0, width - buttonDim, height));

        button = new Checkbox(0, 0, buttonDim, buttonDim){
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

        box.addItem(button);

        return box;
    }

    @Override
    public boolean canDisplayMultiline() {
        return false;
    }

    //endregion
}
