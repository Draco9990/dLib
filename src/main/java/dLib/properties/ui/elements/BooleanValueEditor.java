package dLib.properties.ui.elements;

import dLib.ui.elements.implementations.Toggle;
import dLib.ui.elements.prefabs.Checkbox;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Spacer;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class BooleanValueEditor extends AbstractValueEditor<Boolean> {
    //region Variables

    Toggle button;

    //endregion

    //region Constructors

    public BooleanValueEditor(Boolean value, AbstractDimension width, AbstractDimension height) {
        super(width, height);

        HorizontalBox box = new HorizontalBox(Pos.px(0), Pos.px(0), width, height);
        {
            box.addItem(new Spacer(Dim.fill(), Dim.fill()));

            button = new Checkbox(Dim.height(), Dim.fill()){
                @Override
                public void toggle() {
                    super.toggle();
                    setValueEvent.invoke(objectConsumer -> objectConsumer.accept(isToggled()));
                }
            }.setToggled(value);
            box.addItem(button);
        }

        onValueChangedEvent.subscribe(this, (newVal) -> {
            if(button.isToggled() != newVal){
                button.setToggled(newVal);
            }
        });

        addChildNCS(box);
    }


    //endregion
}
