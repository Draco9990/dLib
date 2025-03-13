package dLib.ui.elements.items.itembox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.AbstractPadding;
import dLib.util.ui.padding.Padd;
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

    private AbstractPadding leftContentPadding = Padd.px(0);
    private AbstractPadding rightContentPadding = Padd.px(0);
    private AbstractPadding topContentPadding = Padd.px(0);
    private AbstractPadding bottomContentPadding = Padd.px(0);

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
        int currentYPos = 0 + getContentPaddingBottom();

        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            currentYPos += child.getPaddingBottom();
            child.setCalculatedLocalPositionY(currentYPos);

            currentYPos += child.getHeight();
            currentYPos += itemSpacing;
            currentYPos += child.getPaddingTop();
        }
    }
    protected void updateListVerticalCentered(){

    }
    protected void updateListVerticalTopBottom(){
        int currentYPos = getHeight() - getContentPaddingTop();

        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            currentYPos -= child.getPaddingTop();
            child.setCalculatedLocalPositionY(currentYPos - child.getHeight());

            currentYPos -= child.getHeight();
            currentYPos -= itemSpacing;
            currentYPos -= child.getPaddingBottom();
        }
    }

    protected void updateListHorizontalLeftRight(){
        int currentXPos = 0 + getContentPaddingLeft();

        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            currentXPos += child.getPaddingLeft();
            child.setCalculatedLocalPositionX(currentXPos);

            currentXPos += child.getWidth();
            currentXPos += itemSpacing;
            currentXPos += child.getPaddingRight();
        }
    }
    protected void updateListHorizontalCentered(){

    }
    protected void updateListHorizontalRightLeft(){
        int currentXPos = getWidth() - getContentPaddingRight();

        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            currentXPos -= child.getPaddingRight();
            currentXPos -= child.getWidth();
            child.setCalculatedLocalPositionX(currentXPos);

            currentXPos += child.getPaddingLeft();
            currentXPos += itemSpacing;
        }
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

    //region Content Padding

    public void setLeftContentPadding(AbstractPadding padding){
        this.leftContentPadding = padding;
    }
    public void setRightContentPadding(AbstractPadding padding){
        this.rightContentPadding = padding;
    }
    public void setTopContentPadding(AbstractPadding padding){
        this.topContentPadding = padding;
    }
    public void setBottomContentPadding(AbstractPadding padding){
        this.bottomContentPadding = padding;
    }

    public void setHorizontalContentPadding(AbstractPadding padding){
        setLeftContentPadding(padding);
        setRightContentPadding(padding);
    }
    public void setVerticalContentPadding(AbstractPadding padding){
        setTopContentPadding(padding);
        setBottomContentPadding(padding);
    }

    public void setContentPadding(AbstractPadding padding){
        setLeftContentPadding(padding);
        setRightContentPadding(padding);
        setTopContentPadding(padding);
        setBottomContentPadding(padding);
    }

    public int getContentPaddingLeft(){
        return leftContentPadding.getHorizontal(this);
    }
    public int getContentPaddingRight(){
        return rightContentPadding.getHorizontal(this);
    }
    public int getContentPaddingTop(){
        return topContentPadding.getVertical(this);
    }
    public int getContentPaddingBottom(){
        return bottomContentPadding.getVertical(this);
    }

    //endregion

    @Override
    public PositionBounds getFullChildLocalBounds() {
        PositionBounds bounds = super.getFullChildLocalBounds();
        if(bounds == null) return null;

        bounds.left -= getContentPaddingLeft();
        bounds.right += getContentPaddingRight();
        bounds.top += getContentPaddingTop();
        bounds.bottom -= getContentPaddingBottom();
        return bounds;
    }

    @Override
    public PositionBounds getFullChildLocalBoundsForAutoDim() {
        PositionBounds bounds = super.getFullChildLocalBoundsForAutoDim();
        if(bounds == null) return null;

        bounds.left -= getContentPaddingLeft();
        bounds.right += getContentPaddingRight();
        bounds.top += getContentPaddingTop();
        bounds.bottom -= getContentPaddingBottom();
        return bounds;
    }


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
