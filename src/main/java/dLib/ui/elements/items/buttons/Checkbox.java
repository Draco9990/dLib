package dLib.ui.elements.items.buttons;

import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.string.Str;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;
import java.util.function.Supplier;

public class Checkbox extends Toggle {
    //region Variables

    //endregion

    //region Constructors

    public Checkbox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Checkbox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public Checkbox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(Tex.stat(UICommonResources.checkbox_unchecked), Tex.stat(UICommonResources.checkbox_checked), xPos, yPos, width, height);

        setSayTheSpireElementType("Checkbox");
    }

    public Checkbox(CheckboxData data) {
        super(data);
    }

    //endregion

    //region Methods

    //endregion

    public static class CheckboxData extends ToggleData implements Serializable {
        private static final long serialVersionUID = 1L;

        //region Variables

        //endregion

        //region Constructors

        public CheckboxData() {
            super();
        }

        //endregion

        //region Methods

        @Override
        public Checkbox makeUIElement_internal() {
            return new Checkbox(this);
        }

        //endregion
    }
}
