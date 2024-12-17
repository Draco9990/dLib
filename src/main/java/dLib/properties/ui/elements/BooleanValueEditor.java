package dLib.properties.ui.elements;

import dLib.properties.objects.BooleanProperty;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.elements.prefabs.Checkbox;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Spacer;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class BooleanValueEditor extends AbstractValueEditor<Boolean, BooleanProperty> {
    //region Variables

    Toggle button;

    //endregion

    //region Constructors

    public BooleanValueEditor(Boolean value, AbstractDimension width, AbstractDimension height) {
        this(new BooleanProperty(value), width, height);
    }

    public BooleanValueEditor(BooleanProperty property, AbstractDimension width, AbstractDimension height){
        super(property, width, height);

        HorizontalBox box = new HorizontalBox(Pos.px(0), Pos.px(0), width, height);
        {
            box.addItem(new Spacer(Dim.fill(), Dim.fill()));

            button = new Checkbox(Dim.height(), Dim.fill()){
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
