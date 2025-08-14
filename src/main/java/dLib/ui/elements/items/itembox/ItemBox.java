package dLib.ui.elements.items.itembox;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.properties.objects.BooleanProperty;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptor;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIOverlayElementComponent;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.text.TokenizedDescriptionBox;
import dLib.ui.layout.ILayoutProvider;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.FillDimension;
import dLib.util.ui.padding.AbstractPadding;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

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
    protected int verticalItemSpacing = 1;
    protected int horizontalItemSpacing = 1;

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

        this.horizontalItemSpacing = data.horizontalItemSpacing.getValue();
        this.verticalItemSpacing = data.verticalItemSpacing.getValue();

        this.invertedItemOrder = data.invertedItemOrder.getValue();

        setContentPadding(Padd.px(0));

        refilterItems();

        registerCommonEvents();
    }

    protected void registerCommonEvents(){
        onChildrenChangedEvent.subscribe(this, ItemBox.this::refilterItems);

        postActiveStateChangedGlobalEvent.subscribe(this, (element, newVisibility) -> {
            if(isDirectChild(element)){
                refilterItems();
            }
        });
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(primaryAlignment == Alignment.AlignmentType.HORIZONTAL) {
            if (horizontalContentAlignment == Alignment.HorizontalAlignment.LEFT) updateListHorizontal(0f);
            else if (horizontalContentAlignment == Alignment.HorizontalAlignment.CENTER) updateListHorizontal(0.5f);
            else if (horizontalContentAlignment == Alignment.HorizontalAlignment.RIGHT) updateListHorizontal(1f);
        }
        else if(primaryAlignment == Alignment.AlignmentType.VERTICAL){
            if(verticalContentAlignment == Alignment.VerticalAlignment.BOTTOM) updateListVertical(1f);
            else if(verticalContentAlignment == Alignment.VerticalAlignment.CENTER) updateListVertical(0.5f);
            else if(verticalContentAlignment == Alignment.VerticalAlignment.TOP) updateListVertical(0);
        }
    }

    protected ArrayList<Pair<ArrayList<UIElement>, Pair<Float, Float>>> generateVerticalLayers(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Float, Float>>> layers = new ArrayList<>();
        float containerWidth = getWidth() - getContentPaddingLeft() - getContentPaddingRight();

        ArrayList<UIElement> currentLayer = new ArrayList<>();
        float highestLayerHeight = 0;
        float totalLayerWidth = 0;
        for(UIElement child : filteredChildren){
            if(!child.isActive() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }

            float childWidth = child.getPaddingLeft() + child.getWidth() + child.getPaddingRight();
            float childHeight = child.getPaddingTop() + child.getHeight() + child.getPaddingBottom();

            if(!currentLayer.isEmpty() && (totalLayerWidth + childWidth > containerWidth || !isGridMode()) || child instanceof TokenizedDescriptionBox.NLBreak){
                layers.add(new Pair<>(currentLayer, new Pair<>(totalLayerWidth, highestLayerHeight)));

                currentLayer = new ArrayList<>();
                highestLayerHeight = 0;
                totalLayerWidth = 0;

                containerWidth = getWidth() - getContentPaddingLeft() - getContentPaddingRight();
            }

            currentLayer.add(child);
            totalLayerWidth += childWidth + getHorizontalItemSpacing();
            if(highestLayerHeight < childHeight) highestLayerHeight = childHeight;
        }

        if(!currentLayer.isEmpty()){
            layers.add(new Pair<>(currentLayer, new Pair<>(totalLayerWidth, highestLayerHeight)));
        }

        return layers;
    }

    protected void updateListVertical(float lerp){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Float, Float>>> layers = generateVerticalLayers();
        float currentYPos = getHeight() - getContentPaddingTop();

        float containerHeight = getHeight() - getContentPaddingBottom() - getContentPaddingTop();

        float totalContentHeight = 0;
        for (Pair<ArrayList<UIElement>, Pair<Float, Float>> layer : layers) {
            totalContentHeight += layer.getValue().getValue() + getVerticalItemSpacing();
        }
        if(!layers.isEmpty()) totalContentHeight -= getVerticalItemSpacing(); // Remove last spacing

        float difference = containerHeight - totalContentHeight;
        difference *= lerp; // Center the content

        currentYPos -= difference;

        float containerWidth = getWidth() - getContentPaddingLeft() - getContentPaddingRight();
        for (Pair<ArrayList<UIElement>, Pair<Float, Float>> layer : layers) {
            Float layerWidth = layer.getValue().getKey() - getHorizontalItemSpacing();
            Float layerHeight = layer.getValue().getValue();

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
                currentXPos += getHorizontalItemSpacing();

                child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos + child.getPaddingBottom());
            }

            currentYPos -= getVerticalItemSpacing();
        }
    }

    protected ArrayList<Pair<ArrayList<UIElement>, Pair<Float, Float>>> generateHorizontalLayers(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Float, Float>>> layers = new ArrayList<>();
        float containerHeight = getHeight() - getContentPaddingBottom() - getContentPaddingTop();

        ArrayList<UIElement> currentLayer = new ArrayList<>();
        float widestLayerWidth = 0;
        float totalLayerHeight = 0;
        for(UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }

            float childWidth = child.getPaddingLeft() + child.getWidth() + child.getPaddingRight();
            float childHeight = child.getPaddingTop() + child.getHeight() + child.getPaddingBottom();

            if(!currentLayer.isEmpty() && (totalLayerHeight + childHeight > containerHeight || !isGridMode()) || child instanceof TokenizedDescriptionBox.NLBreak){
                layers.add(new Pair<>(currentLayer, new Pair<>(totalLayerHeight, widestLayerWidth)));

                currentLayer = new ArrayList<>();
                widestLayerWidth = 0;
                totalLayerHeight = 0;

                containerHeight = getHeight() - getContentPaddingBottom() - getContentPaddingTop();
            }

            currentLayer.add(child);
            totalLayerHeight += childHeight + getVerticalItemSpacing();
            if(widestLayerWidth < childWidth) widestLayerWidth = childWidth;
        }

        if(!currentLayer.isEmpty()){
            layers.add(new Pair<>(currentLayer, new Pair<>(totalLayerHeight, widestLayerWidth)));
        }

        return layers;
    }

    protected void updateListHorizontal(float lerp){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Float, Float>>> layers = generateHorizontalLayers();
        float containerWidth = getWidth() - getContentPaddingLeft() - getContentPaddingRight();
        float currentXPos = 0 + getContentPaddingLeft();

        float totalContainerWidth = 0;
        for (Pair<ArrayList<UIElement>, Pair<Float, Float>> layer : layers) {
            totalContainerWidth += layer.getValue().getValue() + getHorizontalItemSpacing();
        }
        if(!layers.isEmpty()) totalContainerWidth -= getVerticalItemSpacing(); // Remove last spacing

        float difference = containerWidth - totalContainerWidth;
        difference *= lerp; // Center the content

        currentXPos += difference;

        float workingHeight = getHeight();
        for (Pair<ArrayList<UIElement>, Pair<Float, Float>> layer : layers) {
            Float layerHeight = layer.getValue().getKey() - getVerticalItemSpacing();
            Float layerWidth = layer.getValue().getValue();

            float yOffset = 0;
            if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) yOffset = (float) ((workingHeight - layerHeight) * 0.5);
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM) yOffset = (float) (workingHeight - layerHeight);

            float currentYPos = workingHeight - yOffset - getContentPaddingTop();

            for (UIElement child : layer.getKey()){
                child.getLocalPositionXRaw().overrideCalculatedValue(currentXPos + child.getPaddingLeft());

                currentYPos -= child.getPaddingTop();
                currentYPos -= child.getHeight();
                child.getLocalPositionYRaw().overrideCalculatedValue(currentYPos);
                currentYPos -= child.getPaddingBottom();
                currentYPos -= getVerticalItemSpacing();
            }

            currentXPos += layerWidth;
            currentXPos += getHorizontalItemSpacing();
        }
    }

    @Override
    protected void updateChildren() {
        ArrayList<UIElement> childrenCopy = new ArrayList<>(filteredChildren);

        for (int i = childrenCopy.size() - 1; i >= 0; i--) {
            UIElement child = childrenCopy.get(i);
            if (child.isDisposedRaw() || !filteredChildren.contains(child)) {
                continue;
            }

            child.update();

            if (disposed) { //!If any of the children disposes us and themselves, we should stop updating the children
                return;
            }
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
        this.horizontalItemSpacing = spacing;
        this.verticalItemSpacing = spacing;
    }
    public void setHorizontalItemSpacing(int spacing){
        this.horizontalItemSpacing = spacing;
    }
    public void setVerticalItemSpacing(int spacing){
        this.verticalItemSpacing = spacing;
    }
    public int getHorizontalItemSpacing(){
        return horizontalItemSpacing;
    }
    public int getVerticalItemSpacing(){
        return verticalItemSpacing;
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
        return true;
    }

    @Override
    public boolean canCalculateContentWidth(ElementDescriptor refCollector) {
        if (getContentAlignmentType() == Alignment.AlignmentType.VERTICAL){
            if (isGridMode()){
                return canCalculateTotalItemWidth(refCollector);
            }
            else{
                return canCalculateWidestChildWidth(refCollector);
            }
        }
        else{
            if (isGridMode()){
                if(getHeightRaw() instanceof AutoDimension){
                    return canCalculateWidestChildWidth(refCollector);
                }
                else{
                    return canCalculateCalcWidthLayered(refCollector);
                }
            }
            else{
                return canCalculateTotalItemWidth(refCollector);
            }
        }
    }

    @Override
    public Float calculateContentWidth() {
        if (getContentAlignmentType() == Alignment.AlignmentType.VERTICAL){
            if (isGridMode()){
                return totalItemWidth();
            }
            else{
                return getWidestChildWidth();
            }
        }
        else{
            if (isGridMode()){
                if(getHeightRaw() instanceof AutoDimension){
                    return getWidestChildWidth();
                }
                else{
                    return calcWidthLayered();
                }
            }
            else{
                return totalItemWidth();
            }
        }
    }

    private boolean canCalculateWidestChildWidth(ElementDescriptor descriptor){
        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }
            if(child.getWidthRaw().needsRecalculation() && child.getWidthRaw() instanceof FillDimension){
                continue;
            }

            if(child.getWidthRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getWidthRaw());

            if(child.getPaddingLeftRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getPaddingLeftRaw());

            if(child.getPaddingRightRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getPaddingRightRaw());
        }

        return true;
    }
    private Float getWidestChildWidth(){
        float highestWidth = 0;
        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }
            if(child.getWidthRaw().needsRecalculation() && child.getWidthRaw() instanceof FillDimension){
                continue;
            }

            float childWidth = child.getPaddingLeft() + child.getWidth() + child.getPaddingRight();
            if(childWidth > highestWidth){
                highestWidth = childWidth;
            }
        }
        return highestWidth;
    }

    private boolean canCalculateTotalItemWidth(ElementDescriptor descriptor){
        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }
            if(child.getWidthRaw().needsRecalculation() && child.getWidthRaw() instanceof FillDimension){
                continue;
            }

            if(child.getWidthRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getWidthRaw());

            if(child.getPaddingLeftRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getPaddingLeftRaw());

            if(child.getPaddingRightRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getPaddingRightRaw());
        }

        return true;
    }
    private Float totalItemWidth(){
        float totalWidth = 0;

        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }
            if(child.getWidthRaw().needsRecalculation() && child.getWidthRaw() instanceof FillDimension){
                continue;
            }

            totalWidth += child.getPaddingLeft() + child.getWidth() + child.getPaddingRight() + getHorizontalItemSpacing();
        }
        if(!filteredChildren.isEmpty()) totalWidth -= getHorizontalItemSpacing(); // Remove last spacing

        return totalWidth;
    }

    private boolean canCalculateCalcWidthLayered(ElementDescriptor refCollector){
        if(getHeightRaw().needsRecalculation()) return false;
        refCollector.registerDependency(getHeightRaw());

        if(getContentPaddingLeftRaw().needsRecalculation()) return false;
        refCollector.registerDependency(getContentPaddingLeftRaw());

        if(getContentPaddingRightRaw().needsRecalculation()) return false;
        refCollector.registerDependency(getContentPaddingRightRaw());

        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }

            if(child.getWidthRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getWidthRaw());

            if(child.getHeightRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getHeightRaw());

            if(child.getLocalPositionXRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getLocalPositionXRaw());

            if(child.getLocalPositionYRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getLocalPositionYRaw());

            if(child.getPaddingLeftRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getPaddingLeftRaw());

            if(child.getPaddingRightRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getPaddingRightRaw());

            if(child.getPaddingTopRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getPaddingTopRaw());

            if(child.getPaddingBottomRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getPaddingBottomRaw());
        }

        return true;
    }
    private Float calcWidthLayered(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Float, Float>>> layers = generateHorizontalLayers();
        float totalWidth = 0;

        for (Pair<ArrayList<UIElement>, Pair<Float, Float>> layer : layers) {
            totalWidth += layer.getValue().getValue() + getHorizontalItemSpacing();
        }
        if(!layers.isEmpty()) totalWidth -= getHorizontalItemSpacing(); // Remove last spacing

        return totalWidth;
    }

    @Override
    public boolean providesHeight() {
        return true;
    }

    @Override
    public boolean canCalculateContentHeight(ElementDescriptor refCollector) {
        if (getContentAlignmentType() == Alignment.AlignmentType.VERTICAL){
            if (isGridMode()){
                if(getWidthRaw() instanceof AutoDimension){
                    return canCalculateHighestChildHeight(refCollector);
                }
                else{
                    return canCalculateCalcHeightLayered(refCollector);
                }
            }
            else{
                return canCalculateTotalItemHeight(refCollector);
            }
        }
        else{
            if (isGridMode()){
                return canCalculateTotalItemHeight(refCollector);
            }
            else{
                return canCalculateHighestChildHeight(refCollector);
            }
        }
    }

    @Override
    public Float calculateContentHeight() {
        if (getContentAlignmentType() == Alignment.AlignmentType.VERTICAL){
            if (isGridMode()){
                if(getWidthRaw() instanceof AutoDimension){
                    return getHighestChildHeight();
                }
                else{
                    return calcHeightLayered();
                }
            }
            else{
                return totalItemHeight();
            }
        }
        else{
            if (isGridMode()){
                return totalItemHeight();
            }
            else{
                return getHighestChildHeight();
            }
        }
    }

    private boolean canCalculateHighestChildHeight(ElementDescriptor descriptor){
        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }
            if(child.getHeightRaw().needsRecalculation() && child.getHeightRaw() instanceof FillDimension){
                continue;
            }

            if(child.getHeightRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getHeightRaw());

            if(child.getPaddingTopRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getPaddingTopRaw());

            if(child.getPaddingBottomRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getPaddingBottomRaw());
        }

        return true;
    }
    private Float getHighestChildHeight(){
        float highestHeight = 0;
        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }
            if(child.getHeightRaw().needsRecalculation() && child.getHeightRaw() instanceof FillDimension){
                continue;
            }

            float childHeight = child.getPaddingTop() + child.getHeight() + child.getPaddingBottom();
            if(childHeight > highestHeight){
                highestHeight = childHeight;
            }
        }
        return highestHeight;
    }

    private boolean canCalculateTotalItemHeight(ElementDescriptor descriptor){
        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }
            if(child.getHeightRaw().needsRecalculation() && child.getHeightRaw() instanceof FillDimension){
                continue;
            }

            if(child.getHeightRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getHeightRaw());

            if(child.getPaddingTopRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getPaddingTopRaw());

            if(child.getPaddingBottomRaw().needsRecalculation()) return false;
            descriptor.registerDependency(child.getPaddingBottomRaw());
        }

        return true;
    }
    private Float totalItemHeight(){
        float totalHeight = 0;

        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }
            if(child.getHeightRaw().needsRecalculation() && child.getHeightRaw() instanceof FillDimension){
                continue;
            }

            totalHeight += child.getPaddingTop() + child.getHeight() + child.getPaddingBottom() + getVerticalItemSpacing();
        }
        if(!filteredChildren.isEmpty()) totalHeight -= getVerticalItemSpacing(); // Remove last spacing

        return totalHeight;
    }

    private boolean canCalculateCalcHeightLayered(ElementDescriptor refCollector){
        if(getWidthRaw().needsRecalculation()) return false;
        refCollector.registerDependency(getWidthRaw());

        if(getContentPaddingBottomRaw().needsRecalculation()) return false;
        refCollector.registerDependency(getContentPaddingBottomRaw());

        if(getContentPaddingTopRaw().needsRecalculation()) return false;
        refCollector.registerDependency(getContentPaddingTopRaw());

        for (UIElement child : filteredChildren){
            if(!child.isActiveRaw() || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }

            if(child.getWidthRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getWidthRaw());

            if(child.getHeightRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getHeightRaw());

            if(child.getLocalPositionXRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getLocalPositionXRaw());

            if(child.getLocalPositionYRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getLocalPositionYRaw());

            if(child.getPaddingLeftRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getPaddingLeftRaw());

            if(child.getPaddingRightRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getPaddingRightRaw());

            if(child.getPaddingTopRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getPaddingTopRaw());

            if(child.getPaddingBottomRaw().needsRecalculation()) return false;
            refCollector.registerDependency(child.getPaddingBottomRaw());
        }

        return true;
    }
    private Float calcHeightLayered(){
        ArrayList<Pair<ArrayList<UIElement>, Pair<Float, Float>>> layers = generateVerticalLayers();
        float totalHeight = 0;

        for (Pair<ArrayList<UIElement>, Pair<Float, Float>> layer : layers) {
            totalHeight += layer.getValue().getValue() + getVerticalItemSpacing();
        }
        if(!layers.isEmpty()) totalHeight -= getVerticalItemSpacing(); // Remove last spacing

        return totalHeight;
    }


    //endregion Layout Provider Dimensions

    @Override
    public PositionBounds getFullChildLocalBounds() {
        PositionBounds bounds = null;
        for(UIElement child : filteredChildren){
            if(!(child.isActiveRaw()) || child.hasComponent(UIOverlayElementComponent.class)){
                continue;
            }

            PositionBounds childBounds = child.getFullLocalBounds();
            if(bounds == null){
                bounds = childBounds;
                continue;
            }

            if(childBounds.left < bounds.left) bounds.left = childBounds.left;
            if(childBounds.right > bounds.right) bounds.right = childBounds.right;
            if(childBounds.bottom < bounds.bottom) bounds.bottom = childBounds.bottom;
            if(childBounds.top > bounds.top) bounds.top = childBounds.top;
        }

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

    public static class NLBreak extends UIElement{
        public NLBreak() {
            super(Pos.px(0), Pos.px(0), Dim.px(1), Dim.px(1));
        }
    }

    public static class ItemBoxData extends RenderableData implements Serializable {
        private static final long serialVersionUID = 1L;

        //TODO add a class picker property that determines item types

        public IntegerProperty horizontalItemSpacing = new IntegerProperty(1).setMinimumValue(1)
                .setName("Horizontal Item Spacing")
                .setDescription("The horizontal spacing between items in the item box.")
                .setCategory("Item Box");
        public IntegerProperty verticalItemSpacing = new IntegerProperty(1).setMinimumValue(1)
                .setName("Vertical Item Spacing")
                .setDescription("The vertical spacing between items in the item box.")
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
