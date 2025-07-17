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

public class VerticalDataBox<ItemType> extends DataItemBox<ItemType> {
    //region Variables

    //endregion

    //region Constructors

    public VerticalDataBox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public VerticalDataBox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public VerticalDataBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        setContentAlignmentType(Alignment.AlignmentType.VERTICAL);
    }

    public VerticalDataBox(VerticalDataBoxData data){
        super(data);

        setContentAlignmentType(Alignment.AlignmentType.VERTICAL);
    }

    //endregion

    //region Methods

    //region Update & Render

    public VerticalDataBox<ItemType> setDefaultItemHeight(int defaultItemHeight){
        this.defaultItemHeight = defaultItemHeight;
        return this;
    }

    //endregion

    //region Item Management

    @Override
    public UIElement makeUIForItem(ItemType item) {
        ImageTextBox box = (ImageTextBox) super.makeUIForItem(item);
        box.setTexture(Tex.stat(UICommonResources.button03_square));
        box.textBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        return box;
    }

    //endregion

    public static class VerticalDataBoxData extends DataItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public VerticalDataBoxData(){
        }

        @Override
        public UIElement makeUIElement_internal() {
            return new VerticalDataBox<>(this);
        }
    }
}
