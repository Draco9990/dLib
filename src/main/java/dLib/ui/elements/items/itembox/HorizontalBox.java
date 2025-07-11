package dLib.ui.elements.items.itembox;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class HorizontalBox extends UIItemBox {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalBox(AbstractPosition xPos, AbstractPosition yPos) {
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public HorizontalBox(AbstractDimension width, AbstractDimension height) {
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public HorizontalBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        setContentAlignmentType(Alignment.AlignmentType.HORIZONTAL);
    }

    public HorizontalBox(HorizontalBoxData data) {
        super(data);

        setContentAlignmentType(Alignment.AlignmentType.HORIZONTAL);
    }

    //endregion

    public static class HorizontalBoxData extends UIItemBox.UIItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public HorizontalBoxData() {
            super();
        }

        @Override
        public UIElement makeUIElement_internal() {
            return new HorizontalBox(this);
        }
    }
}
