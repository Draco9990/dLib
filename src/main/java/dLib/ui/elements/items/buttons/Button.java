package dLib.ui.elements.items.buttons;

import dLib.ui.elements.items.Interactable;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Button extends Interactable {
    //region Variables
    //endregion

    //region Constructors

    public Button(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Button(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public Button(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(Tex.stat(UICommonResources.button03_horizontal), xPos, yPos, width, height);
    }

    public Button(ButtonData data){
        super(data);
    }

    //endregion

    public static class ButtonData extends Interactable.InteractableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public ButtonData(){
            super();
        }

        @Override
        public Button makeUIElement_internal() {
            return new Button(this);
        }
    }
}
