package dLib.ui.elements.items.itembox;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class HorizontalDataBox<ItemType> extends DataItemBox<ItemType> {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalDataBox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public HorizontalDataBox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public HorizontalDataBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        setContentAlignmentType(Alignment.AlignmentType.HORIZONTAL);
    }

    public HorizontalDataBox(HorizontalDataBoxData data){
        super(data);

        setContentAlignmentType(Alignment.AlignmentType.HORIZONTAL);
    }

    //endregion

    //region Methods

    //region Update & Render

    //endregion

    //region Item Management

    //region Item UI

    @Override
    public UIElement makeUIForItem(ItemType item) {
        ImageTextBox box = (ImageTextBox) super.makeUIForItem(item);
        box.setTexture(Tex.stat(UICommonResources.button03_square));
        return box;
    }

    //endregion

    //endregion

    public static class HorizontalDataBoxData extends DataItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public HorizontalDataBoxData(){
            super();
        }

        @Override
        public UIElement makeUIElement_internal() {
            return new HorizontalDataBox<>(this);
        }
    }
}
