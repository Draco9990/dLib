package dLib.ui.elements.items.itembox;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.FillDimension;
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

    private AbstractPadding leftContentPadding;
    private AbstractPadding rightContentPadding;
    private AbstractPadding topContentPadding;
    private AbstractPadding bottomContentPadding;

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

        setContentPadding(Padd.px(0));

        registerCommonEvents();
    }

    public ItemBox(ItemBoxData data){
        super(data);

        this.itemSpacing = data.itemSpacing.getValue();
        this.invertedItemOrder = data.invertedItemOrder.getValue();

        setContentPadding(Padd.px(0));

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
            child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos);

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
            child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos - child.getHeight());

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
            child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos);

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
            child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos);

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

    public boolean isChildVisible(UIElement child){
        return filteredChildren.contains(child);
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
        refilterItems();
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

    @Override
    protected void onChildVisibilityChanged(UIElement changedChild) {
        super.onChildVisibilityChanged(changedChild);

        refilterItems();
    }

    @Override
    protected void onChildEnabledStatusChanged(UIElement changedChild) {
        super.onEnabledStatusChanged();

        refilterItems();
    }

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
    public int getChildOffsetX() {
        if(getParent() == null){
            return super.getChildOffsetX();
        }

        //! This is a hack to get the local offset of the parent in case of recursive item boxes
        return getParent().getLocalChildOffsetXRaw() + super.getChildOffsetX();
    }

    @Override
    public int getChildOffsetY() {
        if(getParent() == null){
            return super.getChildOffsetY();
        }

        return getParent().getLocalChildOffsetYRaw() + super.getChildOffsetY();
    }

    //endregion

    //region Content Padding

    //TODO standardize and move to UIElement
    public void setLeftContentPadding(AbstractPadding padding){
        this.leftContentPadding = padding;
        leftContentPadding.setReference(AbstractPadding.ReferenceDimension.HORIZONTAL);
    }
    public void setRightContentPadding(AbstractPadding padding){
        this.rightContentPadding = padding;
        rightContentPadding.setReference(AbstractPadding.ReferenceDimension.HORIZONTAL);
    }
    public void setTopContentPadding(AbstractPadding padding){
        this.topContentPadding = padding;
        topContentPadding.setReference(AbstractPadding.ReferenceDimension.VERTICAL);
    }
    public void setBottomContentPadding(AbstractPadding padding){
        this.bottomContentPadding = padding;
        bottomContentPadding.setReference(AbstractPadding.ReferenceDimension.VERTICAL);
    }

    public void setHorizontalContentPadding(AbstractPadding padding){
        setLeftContentPadding(padding.cpy());
        setRightContentPadding(padding.cpy());
    }
    public void setVerticalContentPadding(AbstractPadding padding){
        setTopContentPadding(padding.cpy());
        setBottomContentPadding(padding.cpy());
    }

    public void setContentPadding(AbstractPadding padding){
        setLeftContentPadding(padding.cpy());
        setRightContentPadding(padding.cpy());
        setTopContentPadding(padding.cpy());
        setBottomContentPadding(padding.cpy());
    }

    public int getContentPaddingLeft(){
        return leftContentPadding.getCalculatedValue();
    }
    public int getContentPaddingRight(){
        return rightContentPadding.getCalculatedValue();
    }
    public int getContentPaddingTop(){
        return topContentPadding.getCalculatedValue();
    }
    public int getContentPaddingBottom(){
        return bottomContentPadding.getCalculatedValue();
    }

    public AbstractPadding getContentPaddingLeftRaw(){
        return leftContentPadding;
    }
    public AbstractPadding getContentPaddingRightRaw(){
        return rightContentPadding;
    }
    public AbstractPadding getContentPaddingTopRaw(){
        return topContentPadding;
    }
    public AbstractPadding getContentPaddingBottomRaw(){
        return bottomContentPadding;
    }

    //endregion

    //region Calculation Instructions

    @Override
    public ArrayList<Pair<Integer, ElementCalculationManager.ElementCalculationInstruction>> collectCalculationInstructions() {
        ArrayList<Pair<Integer, ElementCalculationManager.ElementCalculationInstruction>> calculationInstructions = super.collectCalculationInstructions();

        if(getContentPaddingLeftRaw().needsRecalculation()){
            calculationInstructions.add(getContentPaddingLeftRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getContentPaddingLeftRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getContentPaddingBottomRaw().needsRecalculation()){
            calculationInstructions.add(getContentPaddingBottomRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getContentPaddingBottomRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getContentPaddingRightRaw().needsRecalculation()){
            calculationInstructions.add(getContentPaddingRightRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getContentPaddingRightRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getContentPaddingTopRaw().needsRecalculation()){
            calculationInstructions.add(getContentPaddingTopRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getContentPaddingTopRaw().getCalculationInstruction(this); //* For Debug
            }
        }

        return calculationInstructions;
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
    public void onChildDimensionsChanged(UIElement child) {
        super.onChildDimensionsChanged(child);

        for(UIElement element : getChildren()){
            if(element.getWidthRaw() instanceof FillDimension){
                element.getWidthRaw().requestRecalculation();
            }
            if(element.getHeightRaw() instanceof FillDimension){
                element.getHeightRaw().requestRecalculation();
            }
        }
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
