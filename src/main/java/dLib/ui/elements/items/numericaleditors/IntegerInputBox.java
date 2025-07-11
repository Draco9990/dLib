package dLib.ui.elements.items.numericaleditors;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

public class IntegerInputBox extends UIElement {
    public Button leftArrow;
    public Button rightArrow;

    public Inputfield inputbox;


    public IntegerInputBox(AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos);
    }

    public IntegerInputBox(AbstractDimension width, AbstractDimension height) {
        super(width, height);
    }

    public IntegerInputBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);
    }

    @Override
    public void postConstruct() {
        super.postConstruct();

        HorizontalBox hBox = new HorizontalBox(Dim.fill(), Dim.fill());
        {
            leftArrow = new Button(Dim.mirror(), Dim.fill());
            leftArrow.setTexture(Tex.stat(UICommonResources.arrow_left));
            leftArrow.setControllerSelectable(false);
            hBox.addChild(leftArrow);

            inputbox = new Inputfield("", Dim.fill(), Dim.fill());
            inputbox.setPreset(Inputfield.EInputfieldPreset.NUMERICAL_WHOLE);
            inputbox.setControllerSelectable(false);
            hBox.addChild(inputbox);

            rightArrow = new Button(Dim.mirror(), Dim.fill());
            rightArrow.setTexture(Tex.stat(UICommonResources.arrow_right));
            rightArrow.setControllerSelectable(false);
            hBox.addChild(rightArrow);
        }
        addChild(hBox);

        setControllerSelectable(true);
    }

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
            if(inputbox.isToggled()) {
                inputbox.toggle(true);
            }

            leftArrow.proxyUnhover();
            rightArrow.proxyUnhover();
            inputbox.proxyUnhover();
        }

        super.deselect();
    }

    @Override
    public boolean onLeftInteraction(boolean byProxy) {
        if(inputbox.isToggled()) return inputbox.onLeftInteraction(true);

        return leftArrow.onConfirmInteraction(true);
    }

    @Override
    public boolean onRightInteraction(boolean byProxy) {
        if(inputbox.isToggled()) return inputbox.onRightInteraction(true);

        return rightArrow.onConfirmInteraction(true);
    }

    @Override
    public boolean onConfirmInteraction(boolean byProxy) {
        boolean inputBoxInteraction = inputbox.onConfirmInteraction(true);

        if(inputbox.isToggled()){
            leftArrow.proxyUnhover();
            rightArrow.proxyUnhover();
        }
        else {
            leftArrow.proxyHover();
            rightArrow.proxyHover();
        }

        return inputBoxInteraction;
    }
}
