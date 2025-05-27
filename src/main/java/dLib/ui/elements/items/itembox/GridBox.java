package dLib.ui.elements.items.itembox;

import basemod.Pair;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.layout.ILayoutProvider;
import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;

public class GridBox extends UIItemBox implements IGridBoxCommons {
    public GridBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        setContentAlignmentType(Alignment.AlignmentType.VERTICAL);
        
    }

    public GridBox(GridBoxData data){
        super(data);

        setContentAlignmentType(Alignment.AlignmentType.VERTICAL);
    }

    @Override
    protected void updateListVerticalBottomTop() {
    }

    @Override
    protected void updateListVerticalCentered() {
    }

    @Override
    protected void updateListVerticalTopBottom() {
        int currentYPos = getHeight();
        int currentXPos = 0;

        for(UIElement child : filteredChildren){
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
    public Pair<Integer, Integer> calculateContentHeight(){
        return IGridBoxCommons.super.calculateContentHeight();
    }

    //endregion

    public static class GridBoxData extends UIItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public GridBoxData() {
            super();
        }

        @Override
        public UIElement makeUIElement_internal() {
            return new GridBox(this);
        }
    }
}
