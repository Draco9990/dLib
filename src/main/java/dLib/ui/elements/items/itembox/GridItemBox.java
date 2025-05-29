package dLib.ui.elements.items.itembox;

import basemod.Pair;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;

//Gridboxes do not support elements that change their size after being added. All GridBox items must be identical in dimensions. This is a TODO.
public class GridItemBox<ItemType> extends DataItemBox<ItemType> implements IGridBoxCommons {
    //region Variables

    //endregion

    //region Constructors

    public GridItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        setContentAlignmentType(Alignment.AlignmentType.VERTICAL);

        defaultItemHeight = 75;
        defaultItemWidth = 75;

        itemSpacing = 5;
    }

    public GridItemBox(GridItemBoxData data){
        super(data);

        setContentAlignmentType(Alignment.AlignmentType.VERTICAL);
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    protected void updateListVerticalBottomTop() {
    }

    @Override
    protected void updateListVerticalCentered() {
    }

    @Override
    protected void updateListVerticalTopBottom() {
        float currentYPos = getHeight();
        float currentXPos = 0;

        for(UIElement child : children){
            if(!child.isActive()){
                continue;
            }

            if(currentXPos + child.getWidth() + itemSpacing > getWidth()){
                currentXPos = 0;

                currentYPos -= child.getHeight();
                currentYPos -= itemSpacing;
                currentYPos -= child.getPaddingBottom();
            }

            child.setLocalPosition(currentXPos, currentYPos - child.getHeight());

            currentXPos += child.getWidth() + itemSpacing + child.getPaddingRight();
        }
    }

    //endregion

    //region Item Management

    //endregion

    //region ILayoutProvider

    @Override
    public boolean providesHeight(){
        return IGridBoxCommons.super.providesHeight();
    }
    @Override
    public boolean canCalculateContentHeight(){
        return IGridBoxCommons.super.canCalculateContentHeight();
    }
    @Override
    public Pair<Float, Float> calculateContentHeight(){
        return IGridBoxCommons.super.calculateContentHeight();
    }

    //endregion

    //endregion Methods

    public static class GridItemBoxData extends DataItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement_internal() {
            return new GridItemBox<>(this);
        }
    }
}
