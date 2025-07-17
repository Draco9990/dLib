package dLib.ui.elements.items.itembox;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.properties.objects.BooleanProperty;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIOverlayElementComponent;
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
import java.util.ArrayList;

public abstract class ItemBox extends Renderable implements ILayoutProvider {
    //region Variables

    // Elements
    protected ArrayList<UIElement> filteredChildren = new ArrayList<>();

    private Alignment.AlignmentType primaryAlignment;
    private Alignment.HorizontalAlignment horizontalContentAlignment = Alignment.HorizontalAlignment.LEFT;
    private Alignment.VerticalAlignment verticalContentAlignment = Alignment.VerticalAlignment.TOP;

    private AbstractPadding leftContentPadding;
    private AbstractPadding rightContentPadding;
    private AbstractPadding topContentPadding;
    private AbstractPadding bottomContentPadding;

    protected boolean gridMode = false;

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

        if(primaryAlignment == Alignment.AlignmentType.HORIZONTAL) {
            if (horizontalContentAlignment == Alignment.HorizontalAlignment.LEFT) updateListHorizontalLeftRight();
            else if (horizontalContentAlignment == Alignment.HorizontalAlignment.CENTER) updateListHorizontalCentered();
            else if (horizontalContentAlignment == Alignment.HorizontalAlignment.RIGHT) updateListHorizontalRightLeft();
        }
        else if(primaryAlignment == Alignment.AlignmentType.VERTICAL){
            if(verticalContentAlignment == Alignment.VerticalAlignment.BOTTOM) updateListVerticalBottomTop();
            else if(verticalContentAlignment == Alignment.VerticalAlignment.CENTER) updateListVerticalCentered();
            else if(verticalContentAlignment == Alignment.VerticalAlignment.TOP) updateListVerticalTopBottom();
        }
    }

    protected ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> generateVerticalLayers(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> layers = new ArrayList<>();
        float containerWidth = getWidth() - getContentPaddingLeft() - getContentPaddingRight();

        ArrayList<UIElement> currentLayer = new ArrayList<>();
        double highestLayerHeight = 0;
        double totalLayerWidth = 0;
        for(UIElement child : filteredChildren){
            if(!child.isActive() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }

            float childWidth = child.getPaddingLeft() + child.getWidth() + child.getPaddingRight();
            float childHeight = child.getPaddingTop() + child.getHeight() + child.getPaddingBottom();

            if(!currentLayer.isEmpty() && (totalLayerWidth + childWidth > containerWidth || !isGridMode())){
                layers.add(new Pair<>(currentLayer, new Pair<>(totalLayerWidth, highestLayerHeight)));

                currentLayer = new ArrayList<>();
                highestLayerHeight = 0;
                totalLayerWidth = 0;

                containerWidth = getWidth() - getContentPaddingLeft() - getContentPaddingRight();
            }

            currentLayer.add(child);
            totalLayerWidth += childWidth + itemSpacing;
            if(highestLayerHeight < childHeight) highestLayerHeight = childHeight;
        }

        if(!currentLayer.isEmpty()){
            layers.add(new Pair<>(currentLayer, new Pair<>(totalLayerWidth, highestLayerHeight)));
        }

        return layers;
    }

    protected void updateListVerticalTopBottom(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> layers = generateVerticalLayers();
        float containerWidth = getWidth() - getContentPaddingLeft() - getContentPaddingRight();

        float currentYPos = getHeight() - getContentPaddingTop();
        for (Pair<ArrayList<UIElement>, Pair<Double, Double>> layer : layers) {
            Double layerWidth = layer.getValue().getKey() - itemSpacing;
            Double layerHeight = layer.getValue().getValue();

            float xOffset = 0;
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER) xOffset = (float) ((containerWidth - layerWidth) * 0.5);
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT) xOffset = (float) (containerWidth - layerWidth);

            float currentXPos = getContentPaddingLeft() + xOffset;
            currentYPos -= layerHeight;

            for (UIElement child : layer.getKey()){
                currentXPos += child.getPaddingLeft();
                child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos);
                currentXPos += child.getWidth();
                currentXPos += child.getPaddingRight();
                currentXPos += itemSpacing;

                child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos + child.getPaddingBottom());
            }

            currentYPos -= itemSpacing;
        }
    }
    protected void updateListVerticalCentered(){

    }
    protected void updateListVerticalBottomTop(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> layers = generateVerticalLayers();
        float containerWidth = getWidth() - getContentPaddingLeft() - getContentPaddingRight();

        float currentYPos = 0;
        for (Pair<ArrayList<UIElement>, Pair<Double, Double>> layer : layers) {
            Double layerWidth = layer.getValue().getKey() - itemSpacing;
            Double layerHeight = layer.getValue().getValue();

            float xOffset = 0;
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER) xOffset = (float) ((containerWidth - layerWidth) * 0.5);
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT) xOffset = (float) (containerWidth - layerWidth);

            float currentXPos = getContentPaddingLeft() + xOffset;

            for (UIElement child : layer.getKey()){
                currentXPos += child.getPaddingLeft();
                child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos);
                currentXPos += child.getWidth();
                currentXPos += child.getPaddingRight();
                currentXPos += itemSpacing;

                child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos + child.getPaddingBottom());
            }

            currentYPos += layerHeight;
            currentYPos += itemSpacing;
        }
    }

    protected ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> generateHorizontalLayers(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> layers = new ArrayList<>();
        float containerHeight = getHeight() - getContentPaddingBottom() - getContentPaddingTop();

        ArrayList<UIElement> currentLayer = new ArrayList<>();
        double widestLayerWidth = 0;
        double totalLayerHeight = 0;
        for(UIElement child : filteredChildren){
            if(!child.isActive() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }

            float childWidth = child.getPaddingLeft() + child.getWidth() + child.getPaddingRight();
            float childHeight = child.getPaddingTop() + child.getHeight() + child.getPaddingBottom();

            if(!currentLayer.isEmpty() && (totalLayerHeight + childWidth > containerHeight || !isGridMode())){
                layers.add(new Pair<>(currentLayer, new Pair<>(totalLayerHeight, widestLayerWidth)));

                currentLayer = new ArrayList<>();
                widestLayerWidth = 0;
                totalLayerHeight = 0;

                containerHeight = getHeight() - getContentPaddingBottom() - getContentPaddingTop();
            }

            currentLayer.add(child);
            totalLayerHeight += childHeight + itemSpacing;
            if(widestLayerWidth < childWidth) widestLayerWidth = childWidth;
        }

        if(!currentLayer.isEmpty()){
            layers.add(new Pair<>(currentLayer, new Pair<>(totalLayerHeight, widestLayerWidth)));
        }

        return layers;
    }

    protected void updateListHorizontalLeftRight(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> layers = generateHorizontalLayers();
        float containerHeight = getHeight() - getContentPaddingBottom() - getContentPaddingTop();

        float currentXPos = 0;
        for (Pair<ArrayList<UIElement>, Pair<Double, Double>> layer : layers) {
            Double layerHeight = layer.getValue().getKey() - itemSpacing;
            Double layerWidth = layer.getValue().getValue();

            float yOffset = 0;
            if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) yOffset = (float) ((containerHeight - layerHeight) * 0.5);
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP) yOffset = (float) (containerHeight - layerHeight);

            float currentYPos = getContentPaddingBottom() + yOffset;

            for (UIElement child : layer.getKey()){
                child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos + child.getPaddingLeft());

                currentYPos -= child.getPaddingTop();
                child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos);
                currentYPos -= child.getWidth();
                currentYPos -= child.getPaddingBottom();
                currentYPos -= itemSpacing;
            }

            currentXPos += layerWidth;
            currentXPos += itemSpacing;
        }
    }
    protected void updateListHorizontalCentered(){

    }
    protected void updateListHorizontalRightLeft(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> layers = generateHorizontalLayers();
        float containerHeight = getHeight() - getContentPaddingBottom() - getContentPaddingTop();

        float currentXPos = getWidth() - getContentPaddingRight();
        for (Pair<ArrayList<UIElement>, Pair<Double, Double>> layer : layers) {
            Double layerHeight = layer.getValue().getKey() - itemSpacing;
            Double layerWidth = layer.getValue().getValue();

            float yOffset = 0;
            if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) yOffset = (float) ((containerHeight - layerHeight) * 0.5);
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP) yOffset = (float) (containerHeight - layerHeight);

            float currentYPos = getContentPaddingBottom() + yOffset;
            currentXPos -= layerWidth;

            for (UIElement child : layer.getKey()){
                child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos + child.getPaddingLeft());

                currentYPos -= child.getPaddingTop();
                child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos);
                currentYPos -= child.getWidth();
                currentYPos -= child.getPaddingBottom();
                currentYPos -= itemSpacing;
            }

            currentXPos -= itemSpacing;
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
        this.primaryAlignment = alignmentType;
    }
    public Alignment.AlignmentType getContentAlignmentType(){
        return primaryAlignment;
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
    public Pair<Boolean, Boolean> calculationPass(ElementCalculationManager.CalculationPass calculationPass) {
        Pair<Boolean, Boolean> passResult = super.calculationPass(calculationPass);

        boolean calculatedSomething = false;
        boolean isDone = true;

        if(getContentPaddingLeftRaw().needsRecalculation()){
            calculatedSomething |= getContentPaddingLeftRaw().calculateValue(this, calculationPass);
            isDone &= !getContentPaddingLeftRaw().needsRecalculation();
        }
        if(getContentPaddingBottomRaw().needsRecalculation()) {
            calculatedSomething |= getContentPaddingBottomRaw().calculateValue(this, calculationPass);
            isDone &= !getContentPaddingBottomRaw().needsRecalculation();
        }
        if(getContentPaddingRightRaw().needsRecalculation()){
            calculatedSomething |= getContentPaddingRightRaw().calculateValue(this, calculationPass);
            isDone &= !getContentPaddingRightRaw().needsRecalculation();
        }
        if(getContentPaddingTopRaw().needsRecalculation()){
            calculatedSomething |= getContentPaddingTopRaw().calculateValue(this, calculationPass);
            isDone &= !getContentPaddingTopRaw().needsRecalculation();
        }

        return new Pair<>(isDone && passResult.getKey(), calculatedSomething || passResult.getValue());
    }

    //endregion

    //region Grid mode

    public void setGridMode(boolean gridMode) {
        this.gridMode = gridMode;
    }
    public boolean isGridMode() {
        return gridMode;
    }

    //endregion Grid Mode

    //region Layout Provider Dimensions

    @Override
    public boolean providesWidth() {
        return primaryAlignment == Alignment.AlignmentType.HORIZONTAL;
    }
    @Override
    public Float calculateContentWidth() {
        ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> layers = generateHorizontalLayers();
        float totalWidth = 0;

        for (Pair<ArrayList<UIElement>, Pair<Double, Double>> layer : layers) {
            totalWidth += layer.getValue().getValue() + itemSpacing;
        }
        if(!layers.isEmpty()) totalWidth -= itemSpacing; // Remove last spacing

        return totalWidth;
    }

    @Override
    public boolean providesHeight() {
        return primaryAlignment == Alignment.AlignmentType.VERTICAL;
    }
    @Override
    public Float calculateContentHeight() {
        ArrayList<Pair<ArrayList<UIElement>, Pair<Double, Double>>> layers = generateVerticalLayers();
        float totalHeight = 0;

        for (Pair<ArrayList<UIElement>, Pair<Double, Double>> layer : layers) {
            totalHeight += layer.getValue().getValue() + itemSpacing;
        }
        if(!layers.isEmpty()) totalHeight -= itemSpacing; // Remove last spacing

        return totalHeight;
    }


    //endregion Layout Provider Dimensions

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
