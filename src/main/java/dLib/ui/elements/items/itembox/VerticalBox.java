package dLib.ui.elements.items.itembox;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class VerticalBox extends UIItemBox {
    //region Variables

    //endregion

    //region Constructors

    public VerticalBox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public VerticalBox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public VerticalBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        setContentAlignmentType(Alignment.AlignmentType.VERTICAL);
    }

    public VerticalBox(VerticalBoxData data) {
        super(data);

        setContentAlignmentType(Alignment.AlignmentType.VERTICAL);
    }

    //endregion

    public static class VerticalBoxData extends UIItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public VerticalBoxData() {
            super();
        }

        @Override
        public UIElement makeUIElement_internal() {
            return new VerticalBox(this);
        }
    }
}
