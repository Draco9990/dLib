package dLib.ui.elements.prefabs;

import dLib.properties.objects.templates.TNumericProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.resources.UICommonResources;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class NumberInputBoxWithArrows extends UIElement {
    public Button leftArrow;
    public Button rightArrow;

    public Inputfield inputbox;

    private TNumericProperty<?, ?> linkedProperty;

    public NumberInputBoxWithArrows(AbstractDimension width, AbstractDimension height, ENumberType type) {
        this(Pos.px(0), Pos.px(0), width, height, type);
    }
    public NumberInputBoxWithArrows(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height, ENumberType type) {
        super(xPos, yPos, width, height);

        leftArrow = new Button(Pos.px(0), Pos.px(0), Dim.height(), Dim.fill());
        leftArrow.setImage(UICommonResources.arrow_left);
        addChildNCS(leftArrow);

        inputbox = new Inputfield("", Pos.perc(0.25), Pos.px(0), Dim.fill(), Dim.fill());
        if(type == ENumberType.WHOLE){
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE);
        }
        else if(type == ENumberType.DECIMAL){
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL);
        }
        addChildNCS(inputbox);

        rightArrow = new Button(Pos.perc(0.75), Pos.px(0), Dim.height(), Dim.fill());
        rightArrow.setImage(UICommonResources.arrow_right);
        addChildNCS(rightArrow);
    }

    //region Methods

    public NumberInputBoxWithArrows setLinkedProperty(TNumericProperty<?, ?> property) {
        inputbox.getTextBox().setText(property.getValue().toString());

        linkedProperty = property;

        leftArrow.onLeftClickEvent.subscribeManaged(property::decrement);
        leftArrow.onLeftClickHeldEvent.subscribeManaged(aFloat -> property.decrement());
        rightArrow.onLeftClickEvent.subscribeManaged(property::increment);
        rightArrow.onLeftClickHeldEvent.subscribeManaged(aFloat -> property.increment());

        inputbox.addOnValueChangedListener(property::setValueFromString);

        property.onValueChangedEvent.subscribe(this, (integer, integer2) -> {
            if(!inputbox.getTextBox().getText().equals(property.getValue().toString())){
                inputbox.getTextBox().setText(property.getValue().toString());
            }
        });

        return this;
    }

    @Override
    public boolean onLeftInteraction() {
        return leftArrow.onConfirmInteraction();
    }

    @Override
    public boolean onRightInteraction() {
        return rightArrow.onConfirmInteraction();
    }

    @Override
    public boolean onConfirmInteraction() {
        return inputbox.onConfirmInteraction();
    }

    //endregion

    public enum ENumberType{
        WHOLE,
        DECIMAL
    }
}
