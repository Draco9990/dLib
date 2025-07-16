package dLib.ui.elements.items.itembox;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;

public class GridBox extends UIItemBox {
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
        float currentYPos = getHeight();
        float currentXPos = 0;

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
    public boolean providesWidth() {
        return false;
    }

    @Override
    public boolean providesHeight(){
        return true;
    }
    @Override
    public boolean canCalculateContentHeight(){
        return !getWidthRaw().needsRecalculation();
    }
    @Override
    public Float calculateContentHeight(){
        int height = 0;
        int currentXPos = 0;

        for(UIElement child : filteredChildren){
            if(filteredChildren.get(0) == child){
                height += child.getPaddingTop();
                height += child.getHeight();
                height += itemSpacing;
                height += child.getPaddingBottom();
            }

            if(!child.isActive()){
                continue;
            }

            if(currentXPos + child.getWidth() + itemSpacing > getWidth()){
                currentXPos = 0;

                height += child.getPaddingTop();
                height += child.getHeight();
                height += itemSpacing;
                height += child.getPaddingBottom();
            }

            currentXPos += child.getWidth() + itemSpacing + child.getPaddingRight();
        }

        return (float) height;
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
