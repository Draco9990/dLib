package dLib.properties.ui.elements;

import dLib.modcompat.ModManager;
import dLib.modcompat.saythespire.SayTheSpireIntegration;
import dLib.properties.objects.FloatProperty;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.string.Str;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class FloatValueEditor extends AbstractValueEditor<Float, FloatProperty> {
    //region Variables

    public Button leftArrow;
    public Button rightArrow;

    public Inputfield inputbox;

    //endregion

    //region Constructors

    public FloatValueEditor(Float value){
        this(new FloatProperty(value));
    }

    public FloatValueEditor(FloatProperty property) {
        super(property);

        HorizontalBox hBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            leftArrow = new Button(Dim.mirror(), Dim.fill());
            leftArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.decrement());
            leftArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.decrement());
            leftArrow.setTexture(Tex.stat(UICommonResources.arrow_left));
            leftArrow.setControllerSelectable(false);
            leftArrow.setSayTheSpireElementName((String)null);
            leftArrow.setSayTheSpireElementType((String)null);
            hBox.addChild(leftArrow);

            inputbox = new Inputfield("", Dim.fill(), Dim.fill());
            inputbox.textBox.setText(boundProperty.getValueForDisplay());
            inputbox.onValueChangedEvent.subscribeManaged(s -> boundProperty.setValueFromString(s));
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE);
            inputbox.setControllerSelectable(false);
            inputbox.setSayTheSpireElementName((String)null);
            inputbox.setSayTheSpireElementType((String)null);
            hBox.addChild(inputbox);

            rightArrow = new Button(Dim.mirror(), Dim.fill());
            rightArrow.onLeftClickEvent.subscribe(this, () -> boundProperty.increment());
            rightArrow.onLeftClickHeldEvent.subscribe(this, (heldTime) -> boundProperty.increment());
            rightArrow.setTexture(Tex.stat(UICommonResources.arrow_right));
            rightArrow.setControllerSelectable(false);
            rightArrow.setSayTheSpireElementName((String)null);
            rightArrow.setSayTheSpireElementType((String)null);
            hBox.addChild(rightArrow);
        }
        addChild(hBox);

        setControllerSelectable(true);
        setSayTheSpireElementName(Str.lambda(property::getName));
        setSayTheSpireElementValue(Str.lambda(property::getValueForDisplay));

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(!isEditorValidForPropertyChange()) return;

            if(!inputbox.textBox.getText().equals(String.valueOf(newVal))){
                inputbox.textBox.setText(String.valueOf(newVal));
            }

            if(ModManager.SayTheSpire.isActive()){
                SayTheSpireIntegration.Output(boundProperty.getName() + " value changed to " + boundProperty.getValueForDisplay());
            }
        });
    }

    //endregion

    //region Methods

    @Override
    public void select(boolean byController) {
        super.select(byController);

        if(byController){
            leftArrow.proxyHover();
            rightArrow.proxyHover();
            inputbox.proxyHover();
        }
    }

    @Override
    public void deselect() {
        if(isControllerSelected()){
            leftArrow.proxyUnhover();
            rightArrow.proxyUnhover();
            inputbox.proxyUnhover();
        }

        super.deselect();
    }

    @Override
    public boolean onLeftInteraction(boolean byProxy) {
        return leftArrow.onConfirmInteraction(true);
    }

    @Override
    public boolean onRightInteraction(boolean byProxy) {
        return rightArrow.onConfirmInteraction(true);
    }

    @Override
    public boolean onConfirmInteraction(boolean byProxy) {
        return inputbox.onConfirmInteraction(true);
    }

    //endregion
}
