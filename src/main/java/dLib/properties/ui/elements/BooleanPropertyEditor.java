package dLib.properties.ui.elements;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.elements.prefabs.Checkbox;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.properties.objects.templates.TBooleanProperty;
import dLib.ui.elements.prefabs.Spacer;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class BooleanPropertyEditor extends AbstractPropertyEditor<TBooleanProperty<?>> {
    //region Variables

    Toggle button;

    //endregion

    //region Constructors

    public BooleanPropertyEditor(TBooleanProperty setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline){
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TBooleanProperty<?> property, AbstractDimension width, AbstractDimension height) {
        HorizontalBox box = new HorizontalBox(Pos.px(0), Pos.px(0), width, height);

        box.addItem(new Spacer(Dim.fill(), Dim.fill()));

        button = new Checkbox(Dim.height(), Dim.fill()){
            @Override
            public void toggle() {
                super.toggle();
                property.toggle();
            }
        }.setToggled(property.getValue());

        property.onValueChangedEvent.subscribe(this, (aBoolean, aBoolean2) -> {
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
