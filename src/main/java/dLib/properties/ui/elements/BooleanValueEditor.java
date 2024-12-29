package dLib.properties.ui.elements;

import dLib.properties.objects.BooleanProperty;
import dLib.ui.elements.items.Toggle;
import dLib.ui.elements.items.Checkbox;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.Spacer;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class BooleanValueEditor extends AbstractValueEditor<Boolean, BooleanProperty> {
    //region Variables

    Toggle button;

    //endregion

    //region Constructors

    public BooleanValueEditor(Boolean value) {
        this(new BooleanProperty(value));
    }

    public BooleanValueEditor(BooleanProperty property){
        super(property);

        HorizontalBox box = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
        {
            box.addItem(new Spacer(Dim.fill(), Dim.px(1)));

            button = new Checkbox(Dim.mirror(), Dim.px(50)){
                @Override
                public void toggle() {
                    super.toggle();
                    boundProperty.setValue(isToggled());
                }
            }.setToggled(property.getValue());
            box.addItem(button);
        }

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(button.isToggled() != newVal){
                button.setToggled(newVal);
            }
        });

        addChildNCS(box);
    }

    //endregion
}
