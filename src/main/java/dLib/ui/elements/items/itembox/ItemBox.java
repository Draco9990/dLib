package dLib.ui.elements.items.itembox;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.Renderable;
import dLib.ui.layout.ILayoutProvider;
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

public abstract class ItemBox extends Renderable implements ILayoutProvider {
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
        float currentYPos = 0 + getContentPaddingBottom();

        for(UIElement child : filteredChildren){
            if(!child.isActive() || child.hasComponent(UITransientElementComponent.class)){
                continue;
            }

            currentYPos += child.getPaddingBottom();
            child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos);
            child.getLocalPositionXRaw().overrideCalculatedValue(getContentPaddingLeft());

            currentYPos += child.getHeight();
            currentYPos += itemSpacing;
            currentYPos += child.getPaddingTop();
        }
    }
    protected void updateListVerticalCentered(){

    }
    protected void updateListVerticalTopBottom(){
        float currentYPos = getHeight() - getContentPaddingTop();

        for(UIElement child : filteredChildren){
            if(!child.isActive() || child.hasComponent(UITransientElementComponent.class)){
                continue;
            }

            currentYPos -= child.getPaddingTop();
            child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos - child.getHeight());
            child.getLocalPositionXRaw().overrideCalculatedValue(getContentPaddingLeft());

            currentYPos -= child.getHeight();
            currentYPos -= itemSpacing;
            currentYPos -= child.getPaddingBottom();
        }
    }

    protected void updateListHorizontalLeftRight(){
        float currentXPos = 0 + getContentPaddingLeft();

        for(UIElement child : filteredChildren){
            if(!child.isActive() || child.hasComponent(UITransientElementComponent.class)){
                continue;
            }

            currentXPos += child.getPaddingLeft();
            child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos);
            child.getLocalPositionYRaw().overrideCalculatedValue(getContentPaddingBottom());

            currentXPos += child.getWidth();
            currentXPos += itemSpacing;
            currentXPos += child.getPaddingRight();
        }
    }
    protected void updateListHorizontalCentered(){

    }
    protected void updateListHorizontalRightLeft(){
        float currentXPos = getWidth() - getContentPaddingRight();

        for(UIElement child : filteredChildren){
            if(!child.isActive() || child.hasComponent(UITransientElementComponent.class)){
                continue;
            }

            currentXPos -= child.getPaddingRight();
            currentXPos -= child.getWidth();
            child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos);
            child.getLocalPositionYRaw().overrideCalculatedValue(getContentPaddingBottom());

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
    public float getChildOffsetX() {
        if(getParent() == null){
            return super.getChildOffsetX();
        }

        //! This is a hack to get the local offset of the parent in case of recursive item boxes
        return getParent().getLocalChildOffsetXRaw() + super.getChildOffsetX();
    }

    @Override
    public float getChildOffsetY() {
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

    // Layout Provider Dimensions

    @Override
    public boolean providesWidth() {
        return alignmentType == Alignment.AlignmentType.HORIZONTAL;
    }
    @Override
    public Pair<Float, Float> calculateContentWidth() {
        int width = 0;
        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            width += child.getPaddingLeft();
            width += child.getWidth();
            width += itemSpacing;
            width += child.getPaddingRight();
        }
        width -= itemSpacing;

        return new Pair<>(getLocalPositionX(), getLocalPositionX() + width);
    }

    @Override
    public boolean providesHeight() {
        return alignmentType == Alignment.AlignmentType.VERTICAL;
    }
    @Override
    public Pair<Float, Float> calculateContentHeight() {
        int height = 0;
        for(UIElement child : filteredChildren){
            if(!child.isActive()){
                continue;
            }

            height += child.getPaddingBottom();
            height += child.getHeight();
            height += itemSpacing;
            height += child.getPaddingTop();
        }
        height -= itemSpacing;

        return new Pair<>(getLocalPositionX(), getLocalPositionX() + height);
    }


    // endregion Layout Provider Dimensions

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

    @Override
    public boolean isChildEnabled(UIElement child) {
        return filteredChildren.contains(child);
    }

    @Override
    public boolean isChildVisible(UIElement child){
        return filteredChildren.contains(child);
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
