package dLib.ui.elements.items.itembox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;
import java.util.*;

public abstract class ItemBox extends Renderable {
    //region Variables

    // Elements
    protected ArrayList<UIElement> filteredChildren = new ArrayList<>();

    private Alignment.AlignmentType alignmentType;
    private Alignment.HorizontalAlignment horizontalContentAlignment = Alignment.HorizontalAlignment.LEFT;
    private Alignment.VerticalAlignment verticalContentAlignment = Alignment.VerticalAlignment.TOP;

    protected String filterText = "";

    // Properties
    protected int itemSpacing = 1;
    protected boolean invertedItemOrder = false;

    //endregion

    //region Constructors

    public ItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(Tex.stat(UICommonResources.white_pixel), xPos, yPos, width, height);

        Color bgColor = Color.BLACK.cpy();
        bgColor.a = 0.4f;

        setRenderColor(bgColor);

        setPassthrough(true);

        registerCommonEvents();
    }

    public ItemBox(ItemBoxData data){
        super(data);

        this.itemSpacing = data.itemSpacing.getValue();
        this.invertedItemOrder = data.invertedItemOrder.getValue();

        registerCommonEvents();

        refilterItems();
    }

    public void registerCommonEvents(){
        onChildrenChangedEvent.subscribe(this, ItemBox.this::refilterItems);
    }

    //endregion

    //region Methods

    //region Update & Render


    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(alignmentType == Alignment.AlignmentType.HORIZONTAL) {
            if (horizontalContentAlignment == Alignment.HorizontalAlignment.LEFT) updateListHorizontalLeftRight();
            else if (horizontalContentAlignment == Alignment.HorizontalAlignment.CENTER) updateListHorizontalCentered();
            else if (horizontalContentAlignment == Alignment.HorizontalAlignment.RIGHT) updateListHorizontalRightLeft();
        }
        else if(alignmentType == Alignment.AlignmentType.VERTICAL){
            if(verticalContentAlignment == Alignment.VerticalAlignment.BOTTOM) updateListVerticalBottomTop();
            else if(verticalContentAlignment == Alignment.VerticalAlignment.CENTER) updateListVerticalCentered();
            else if(verticalContentAlignment == Alignment.VerticalAlignment.TOP) updateListVerticalTopBottom();
        }
    }

    protected void updateListVerticalBottomTop(){
        int currentYPos = 0;

        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            currentYPos += child.getPaddingBottom();
            child.setLocalPositionY(currentYPos);

            currentYPos += child.getHeight();
            currentYPos += itemSpacing;
            currentYPos += child.getPaddingTop();
        }
    }
    protected void updateListVerticalCentered(){

    }
    protected void updateListVerticalTopBottom(){
        int currentYPos = getHeight();

        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            currentYPos -= child.getPaddingTop();
            child.setLocalPositionY(currentYPos - child.getHeight());

            currentYPos -= child.getHeight();
            currentYPos -= itemSpacing;
            currentYPos -= child.getPaddingBottom();
        }
    }

    protected void updateListHorizontalLeftRight(){
        int currentXPos = 0;

        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            currentXPos += child.getPaddingLeft();
            child.setLocalPositionX(currentXPos);

            currentXPos += child.getWidth();
            currentXPos += itemSpacing;
            currentXPos += child.getPaddingRight();
        }
    }
    protected void updateListHorizontalCentered(){

    }
    protected void updateListHorizontalRightLeft(){

    }

    @Override
    protected void updateChildren() {
        for(int i = filteredChildren.size() - 1; i >= 0; i--){
            filteredChildren.get(i).update();
        }
    }

    @Override
    protected void renderChildren(SpriteBatch sb) {
        for(UIElement child : filteredChildren){
            child.render(sb);
        }
    }

    //endregion

    //region Item Properties

    public void setItemSpacing(int spacing){
        this.itemSpacing = spacing;
    }
    public int getItemSpacing(){
        return itemSpacing;
    }

    public void setInvertedItemOrder(boolean invertedItemOrder){
        this.invertedItemOrder = invertedItemOrder;
    }

    //endregion

    //region Filter

    public ArrayList<UIElement> getActiveChildren(){
        return filteredChildren;
    }

    public void setFilterText(String filterText){
        if(filterText == null) filterText = "";

        this.filterText = filterText;
        refilterItems();
    }

    public abstract void refilterItems();

    //endregion Filter

    //region Content Alignment

    protected void setContentAlignmentType(Alignment.AlignmentType alignmentType){
        this.alignmentType = alignmentType;
    }
    public Alignment.AlignmentType getContentAlignmentType(){
        return alignmentType;
    }

    public void setHorizontalContentAlignment(Alignment.HorizontalAlignment horizontalAlignment){
        this.horizontalContentAlignment = horizontalAlignment;
    }
    public void setVerticalContentAlignment(Alignment.VerticalAlignment verticalAlignment){
        this.verticalContentAlignment = verticalAlignment;
    }

    public Alignment.HorizontalAlignment getHorizontalContentAlignment(){
        return horizontalContentAlignment;
    }
    public Alignment.VerticalAlignment getVerticalContentAlignment(){
        return verticalContentAlignment;
    }

    //endregion Alignment

    //region Local Child Offsets

    @Override
    public int getLocalChildOffsetX() {
        if(getParent() == null){
            return super.getLocalChildOffsetX();
        }

        //! This is a hack to get the local offset of the parent in case of recursive item boxes
        return getParent().getLocalChildOffsetXRaw() + super.getLocalChildOffsetX();
    }

    @Override
    public int getLocalChildOffsetY() {
        if(getParent() == null){
            return super.getLocalChildOffsetY();
        }

        return getParent().getLocalChildOffsetYRaw() + super.getLocalChildOffsetY();
    }

    //endregion

    //endregion

    public static class ItemBoxData extends RenderableData implements Serializable {
        private static final long serialVersionUID = 1L;

        //TODO add a class picker property that determines item types

        public IntegerProperty itemSpacing = new IntegerProperty(1).setMinimumValue(1)
                .setName("Item Spacing")
                .setDescription("The spacing between items in the item box.")
                .setCategory("Item Box");

        public BooleanProperty invertedItemOrder = new BooleanProperty(false)
                .setName("Inverted Item Order")
                .setDescription("Whether the order of items in the item box should be inverted.")
                .setCategory("Item Box");

        public ItemBoxData(){
            super();

            width.setValue(Dim.px(300));
            height.setValue(Dim.px(300));
        }

        public boolean canReorder = false;
    }
}
