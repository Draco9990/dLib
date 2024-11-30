package dLib.ui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import dLib.properties.objects.BooleanProperty;
import dLib.ui.animations.UIAnimation;
import dLib.ui.screens.UIManager;
import dLib.util.DLibLogger;
import dLib.util.IntegerVector2;
import dLib.util.IntegerVector4;
import dLib.util.Reflection;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.properties.objects.Property;
import dLib.properties.objects.IntegerVector2Property;
import dLib.properties.objects.StringProperty;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UIElement {
    //region Variables

    protected String ID;

    protected UIElement parent;
    protected List<UIElementChild> children = new ArrayList<>();

    private IntegerVector2 localPosition = new IntegerVector2(0, 0);
    private ArrayList<BiConsumer<Integer, Integer>> positionChangedConsumers = new ArrayList<>();
    private boolean dockedToParent = true;

    private IntegerVector2 dimensions = new IntegerVector2(1, 1);
    private boolean scaleWithParent = true;

    private transient float widthScale = 1.0f;
    private transient float heightScale = 1.0f;

    private IntegerVector2 lowerLocalBounds = new IntegerVector2(null, null);
    private IntegerVector2 upperLocalBounds = new IntegerVector2(null, null);
    private IntegerVector2 lowerWorldBounds = new IntegerVector2(null, null);
    private IntegerVector2 upperWorldBounds = new IntegerVector2(null, null);
    private boolean boundWithinParent = false;
    private boolean borderToBorderBound = false;

    private UIElement elementMask = null;

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    private Color darkenedColor = Color.BLACK;
    private float darkenedColorMultiplier = 0.4f;
    protected boolean isDarkened = false;

    private boolean selected;
    private ArrayList<Consumer<Boolean>> onSelectionStateChangedConsumers = new ArrayList<>();

    private boolean pendingRefresh = false;

    private float totalLifespan = -1f;
    private float remainingLifespan = -1f;

    //TODO: Expose to data and screen editor
    private UIAnimation entryAnimation;
    private UIAnimation reentryAnimation; //TODO
    private UIAnimation exitAnimation;
    private UIAnimation animation;

    private UIAnimation playingAnimation;

    private UIStrings stringTable = null;

    //endregion

    //region Constructors

    public UIElement(int xPos, int yPos, int width, int height){
        this.ID = getClass().getSimpleName() + "_" + UUID.randomUUID().toString().replace("-", "");
        localPosition = new IntegerVector2(xPos, yPos);
        dimensions = new IntegerVector2(width, height);

        String uiStrings = getUIStringsKey();
        if(uiStrings != null){
            stringTable = CardCrawlGame.languagePack.getUIString(uiStrings);
        }
    }

    public UIElement(UIElementData data){
        setID(data.id.getValue());

        setLocalPosition(data.localPosition.getXValue(), data.localPosition.getYValue());
        setDockedToParent(data.dockedToParent);

        dimensions = new IntegerVector2(data.dimensions.getXValue(), data.dimensions.getYValue());
        scaleWithParent = data.scaleWithParent;

        setLowerLocalBounds(data.lowerLocalBound.x, data.lowerLocalBound.y);
        setUpperLocalBounds(data.upperLocalBound.x, data.upperLocalBound.y);
        setLowerWorldBounds(data.lowerWorldBound.x, data.lowerWorldBound.y);
        setUpperWorldBounds(data.upperWorldBound.x, data.upperWorldBound.y);
        setBoundWithinParent(data.boundWithinParent);
        setBorderToBorderBound(data.borderToBorderBound);

        setVisibility(data.isVisible.getValue());
        setEnabled(data.isEnabled.getValue());

        this.darkenedColor = Color.valueOf(data.darkenedColor);
        this.darkenedColorMultiplier = data.darkenedColorMultiplier;

        onSelectionStateChangedConsumers.add(aBoolean -> data.onSelectionStateChangedBinding.executeBinding(aBoolean));
    }

    //endregion

    //region Methods

    //region Update & Render
    public final void update(){
        if(!shouldUpdate()) return;

        updateChildren();
        updateSelf();

        ensureElementWithinBounds();
    }
    protected void updateSelf(){
        if(playingAnimation != null){
            playingAnimation.update();
            if(!playingAnimation.isPlaying()){
                playingAnimation.finishInstantly();
                playingAnimation = null;
            }
        }

        if(pendingRefresh){
            pendingRefresh = false;
            onRefreshElement();
        }

        updateLifespan();
    }
    protected void updateChildren(){
        for(int i = children.size() - 1; i >= 0; i--){
            children.get(i).element.update();
        }
    }

    public final void render(SpriteBatch sb){
        if(!shouldRender()) return;

        IntegerVector4 maskBounds = getMaskWorldBounds();
        if(maskBounds != null){
            sb.flush();

            Rectangle scissors = new Rectangle();
            Rectangle mask = new Rectangle(maskBounds.x * Settings.xScale, maskBounds.y * Settings.yScale, maskBounds.w * Settings.xScale, maskBounds.h * Settings.yScale);

            OrthographicCamera camera = Reflection.getFieldValue("camera", Gdx.app.getApplicationListener());
            ScissorStack.calculateScissors(camera, sb.getTransformMatrix(), mask, scissors);
            ScissorStack.pushScissors(scissors);
        }

        renderSelf(sb);

        if(maskBounds != null){
            ScissorStack.popScissors();
            sb.flush();
        }

        renderChildren(sb);
    }

    protected void renderSelf(SpriteBatch sb){

    }

    protected void renderChildren(SpriteBatch sb){
        for(UIElementChild child : children){
            child.element.render(sb);
        }
    }

    protected boolean shouldUpdate(){
        return isActive() && (isEnabled() || isVisible());
    }
    protected boolean shouldRender(){
        return isActive() && isVisible();
    }
    //endregion

    //region Id
    public UIElement setID(String newId){
        this.ID = newId;
        return this;
    }
    public String getId(){
        return ID;
    }
    //endregion

    //region Parent & Children

    //region Parent
    public UIElement setParent(UIElement parent){
        this.parent = parent;
        return this;
    }
    public UIElement getParent(){
        return parent;
    }
    public boolean hasParent(){
        return parent != null;
    }

    public UIElement getTopParent(){
        if(parent == null) return this;
        return parent.getTopParent();
    }

    //endregion

    //region Children
    public UIElement addChildNCS(UIElement child){
        return addChild(child, false);
    }
    public UIElement addChildCS(UIElement child){
        return addChild(child, true);
    }
    public UIElement addChild(UIElement child, boolean isControllerSelectable){
        return addChild(new UIElementChild(child, isControllerSelectable));
    }
    public UIElement addChild(UIElementChild child){
        this.children.add(child);
        child.element.setParent(this);
        onChildrenChanged();
        return this;
    }

    public UIElement setChildren(ArrayList<UIElementChild> children){
        clearChildren();
        for(UIElementChild child : children){
            addChild(child);
        }
        return this;
    }

    public UIElement swapChildren(int index1, int index2){
        Collections.swap(this.children, index1, index2);
        return this;
    }

    public boolean hasChild(UIElement child){
        for(UIElementChild childEle : children){
            if(Objects.equals(childEle.element, child)) return true;
        }

        return false;
    }

    public UIElement removeChild(UIElement child){
        this.children.removeIf(next -> next.element.equals(child));
        if(Objects.equals(child.getParent(), this)) child.setParent(null);
        onChildrenChanged();
        return this;
    }
    public UIElement clearChildren(){
        for(UIElementChild child : children){
            if(Objects.equals(child.element.getParent(), this)){
                child.element.setParent(null);
            }
        }
        children.clear();
        onChildrenChanged();
        return this;
    }

    protected void onChildrenChanged(){ //TODO Expose
    }

    public UIElementChild getFirstChild(){
        if(children.isEmpty()) return null;
        return children.get(0);
    }
    public UIElementChild getLastChild(){
        if(children.isEmpty()) return null;
        return children.get(children.size() - 1);
    }
    public ArrayList<UIElement> getChildren(){
        ArrayList<UIElement> childElements = new ArrayList<>();
        for(UIElementChild child : children){
            childElements.add(child.element);
        }
        return childElements;
    }
    public ArrayList<UIElementChild> getChildrenRaw(){
        return new ArrayList<>(children);
    }

    public ArrayList<UIElementChild> getAllChildrenRaw(){
        ArrayList<UIElementChild> allChildren = new ArrayList<>();
        for(UIElementChild child : children){
            allChildren.add(child);
            allChildren.addAll(child.element.getAllChildrenRaw());
        }
        return allChildren;
    }

    public UIElement getSelectedChild(){
        for(UIElementChild child : children){
            if(child.element.isSelected()){
                return child.element;
            }

            UIElement selectedChild = child.element.getSelectedChild();
            if(selectedChild != null){
                return selectedChild;
            }
        }

        return null;
    }

    public UIElement findChildById(String elementId){
        for(UIElementChild child : children){
            if(child.element.getId().equals(elementId)){
                return child.element;
            }
        }

        return null;
    }

    //endregion

    //endregion

    //region Position

    //region Local Position
    public UIElement setLocalPositionX(int newPosition){
        return setLocalPosition(newPosition, getLocalPositionY());
    }
    public UIElement setLocalPositionY(int newPosition){
        return setLocalPosition(getLocalPositionX(), newPosition);
    }
    public UIElement setLocalPosition(int newPositionX, int newPositionY){
        int xDiff = newPositionX - localPosition.x;
        int yDiff = newPositionY - localPosition.y;

        this.localPosition.x = newPositionX;
        this.localPosition.y = newPositionY;

        if(xDiff != 0 || yDiff != 0){
            onPositionChanged(xDiff, yDiff);
        }

        ensureElementWithinBounds();

        return this;
    }

    public final int getLocalPositionX(){
        return getLocalPosition().x;
    }
    public final int getLocalPositionY(){
        return getLocalPosition().y;
    }
    public final IntegerVector2 getLocalPosition(){
        return localPosition.copy();
    }

    public final int getLocalPositionScaledX(){
        return getLocalPositionScaled().x;
    }
    public final int getLocalPositionScaledY(){
        return getLocalPositionScaled().y;
    }
    public final IntegerVector2 getLocalPositionScaled(){
        IntegerVector2 localPos = getLocalPosition();
        if(parent != null){
            localPos.x = (int) (localPos.x * parent.widthScale);
            localPos.y = (int) (localPos.y * parent.heightScale);
        }
        return localPos;
    }

    public UIElement setLocalPositionCenteredX(int newPos){
        return setLocalPositionCentered(newPos, getLocalPositionCenteredY());
    }
    public UIElement setLocalPositionCenteredY(int newPos){
        return setLocalPositionCentered(getLocalPositionCenteredX(), newPos);
    }
    public UIElement setLocalPositionCentered(int newPosX, int newPosY){
        int wHalf = (int)(getWidth() * 0.5f);
        int hHalf = (int)(getHeight() * 0.5f);
        return setLocalPosition(newPosX - wHalf, newPosY - hHalf);
    }

    public final int getLocalPositionCenteredX(){
        return getLocalPositionCentered().x;
    }
    public final int getLocalPositionCenteredY(){
        return getLocalPositionCentered().y;
    }
    public final IntegerVector2 getLocalPositionCentered(){
        IntegerVector2 localPosition = getLocalPosition();
        localPosition.x += (int)(getWidth() * 0.5f);
        localPosition.y += (int)(getHeight() * 0.5f);
        return localPosition;
    }

    //endregion

    //region World Position
    public UIElement setWorldPositionX(int newPos){
        return setWorldPosition(newPos, getWorldPositionY());
    }
    public UIElement setWorldPositionY(int newPos){
        return setWorldPosition(getWorldPositionX(), newPos);
    }
    public UIElement setWorldPosition(int newPosX, int newPosY){
        int xDiff = newPosX - getWorldPositionX();
        int yDiff = newPosY - getWorldPositionY();
        offset(xDiff, yDiff);
        return this;
    }

    public final int getWorldPositionX(){
        return getWorldPosition().x;
    }
    public final int getWorldPositionY(){
        return getWorldPosition().y;
    }
    public final IntegerVector2 getWorldPosition(){
        if(!hasParent()){
            return getLocalPosition();
        }
        else{
            IntegerVector2 parentWorld = parent.getWorldPosition();
            parentWorld.x += getLocalPositionScaledX();
            parentWorld.y += getLocalPositionScaledY();
            return parentWorld;
        }
    }

    public UIElement setWorldPositionCenteredX(int newPos){
        return setWorldPositionCentered(newPos, getWorldPositionCenteredY());
    }
    public UIElement setWorldPositionCenteredY(int newPos){
        return setWorldPositionCentered(getWorldPositionCenteredX(), newPos);
    }
    public UIElement setWorldPositionCentered(int newPosX, int newPosY){
        int wHalf = (int)(getWidth() * 0.5f);
        int hHalf = (int)(getHeight() * 0.5f);
        return setWorldPosition(newPosX - wHalf, newPosY - hHalf);
    }

    public final int getWorldPositionCenteredX(){
        return getWorldPositionCentered().x;
    }
    public final int getWorldPositionCenteredY(){
        return getWorldPositionCentered().y;
    }
    public final IntegerVector2 getWorldPositionCentered(){
        IntegerVector2 worldPosition = getWorldPosition();
        worldPosition.x += (int)(getWidth() * 0.5f);
        worldPosition.y += (int)(getHeight() * 0.5f);
        return worldPosition;
    }
    //endregion

    //region Offset
    public UIElement offsetX(int xOffset){
        offset(xOffset, 0);
        return this;
    }
    public UIElement offsetY(int yOffset){
        offset(0, yOffset);
        return this;
    }
    public UIElement offset(int xOffset, int yOffset){
        setLocalPosition(getLocalPositionX() + xOffset, getLocalPositionY() + yOffset);
        return this;
    }
    //endregion

    //region Docking
    public UIElement dockToParent(){
        return setDockedToParent(true);
    }
    public UIElement undockFromParent(){
        return setDockedToParent(false);
    }
    public UIElement setDockedToParent(boolean dockedToParent){
        this.dockedToParent = dockedToParent;
        return this;
    }

    public boolean isDockedToParent(){
        return dockedToParent;
    }
    //endregion

    //region Transforming

    public IntegerVector2 worldToLocal(IntegerVector2 worldPosition){
        IntegerVector2 localPosition = new IntegerVector2(null, null);
        if(worldPosition.x != null) {
            localPosition.x = getLocalPositionX();
            localPosition.x += worldPosition.x - getWorldPositionX();
        }
        if(worldPosition.y != null){
            localPosition.y = getLocalPositionY();
            localPosition.y += worldPosition.y - getWorldPositionY();
        }
        return localPosition;
    }
    public IntegerVector2 localToWorld(IntegerVector2 localPosition){
        IntegerVector2 worldPosition = new IntegerVector2(null, null);
        if(localPosition.x != null) {
            worldPosition.x = getWorldPositionX();
            worldPosition.x += localPosition.x - getLocalPositionX();
        }
        if(localPosition.y != null){
            worldPosition.y = getWorldPositionY();
            worldPosition.y += localPosition.y - getLocalPositionY();
        }
        return worldPosition;
    }


    //endregion

    public void onPositionChanged(int diffX, int diffY){
        for(BiConsumer<Integer, Integer> consumer : positionChangedConsumers) consumer.accept(diffX, diffY);

        for(UIElementChild child : children){
            child.element.onParentPositionChanged(diffX, diffY);
        }
    }
    public UIElement addOnPositionChangedConsumer(BiConsumer<Integer, Integer> consumer){
        positionChangedConsumers.add(consumer);
        return this;
    }

    protected void onParentPositionChanged(int diffX, int diffY){
        if(!isDockedToParent()){
            offset(-diffX, -diffY);
        }
    }

    //endregion

    //region Bounds

    //region Lower Bounds

    public UIElement setLowerLocalBoundX(Integer bound){
        return setLowerLocalBounds(bound, lowerLocalBounds.y);
    }
    public final UIElement unsetLowerLocalBoundX(){
        return setLowerLocalBoundX(null);
    }
    public final UIElement setLowerLocalBoundY(Integer bound){
        return setLowerLocalBounds(lowerLocalBounds.x, bound);
    }
    public final UIElement unsetLowerLocalBoundY(){
        return setLowerLocalBoundY(null);
    }
    public UIElement setLowerLocalBounds(Integer boundX, Integer boundY){
        lowerLocalBounds.x = boundX;
        lowerLocalBounds.y = boundY;

        ensureElementWithinBounds();

        return this;
    }

    public IntegerVector2 getLowerLocalBounds(){
        IntegerVector2 lowerBound = lowerLocalBounds.copy();

        IntegerVector2 lowerWorldBoundConverted = worldToLocal(lowerWorldBounds);
        if(lowerBound.x == null || (lowerWorldBoundConverted.x != null && lowerWorldBoundConverted.x < lowerBound.x)) lowerBound.x = lowerWorldBoundConverted.x;
        if(lowerBound.y == null || (lowerWorldBoundConverted.y != null && lowerWorldBoundConverted.y < lowerBound.y)) lowerBound.y = lowerWorldBoundConverted.y;

        if(isBoundWithinParent() && hasParent()){
            lowerBound.x = 0;
            lowerBound.y = 0;
        }

        return lowerBound;
    }

    public UIElement setLowerWorldBoundX(Integer bound){
        return setLowerWorldBounds(bound, lowerWorldBounds.y);
    }
    public final UIElement unsetLowerWorldBoundX(){
        return setLowerWorldBoundX(null);
    }
    public final UIElement setLowerWorldBoundY(Integer bound){
        return setLowerWorldBounds(lowerWorldBounds.x, bound);
    }
    public final UIElement unsetLowerWorldBoundY(){
        return setLowerWorldBoundY(null);
    }
    public UIElement setLowerWorldBounds(Integer boundX, Integer boundY){
        lowerWorldBounds.x = boundX;
        lowerWorldBounds.y = boundY;

        ensureElementWithinBounds();

        return this;
    }

    //endregion

    //region Upper Bounds

    public UIElement setUpperLocalBoundX(Integer bound){
        return setUpperLocalBounds(bound, upperLocalBounds.y);
    }
    public final UIElement unsetUpperLocalBoundX(){
        return setUpperLocalBoundX(null);
    }
    public final UIElement setUpperLocalBoundY(Integer bound){
        return setUpperLocalBounds(upperLocalBounds.x, bound);
    }
    public final UIElement unsetUpperLocalBoundY(){
        return setUpperLocalBoundY(null);
    }
    public UIElement setUpperLocalBounds(Integer boundX, Integer boundY){
        upperLocalBounds.x = boundX;
        upperLocalBounds.y = boundY;

        ensureElementWithinBounds();

        return this;
    }

    public IntegerVector2 getUpperLocalBounds(){
        IntegerVector2 upperBound = upperLocalBounds.copy();

        IntegerVector2 upperWorldBoundsConverted = worldToLocal(upperWorldBounds);
        if(upperBound.x == null || (upperWorldBoundsConverted.x != null && upperWorldBoundsConverted.x > upperBound.x)) upperBound.x = upperWorldBoundsConverted.x;
        if(upperBound.y == null || (upperWorldBoundsConverted.y != null && upperWorldBoundsConverted.y > upperBound.y)) upperBound.y = upperWorldBoundsConverted.y;

        if(isBoundWithinParent() && hasParent()){
            upperBound.x = parent.getWidth();
            upperBound.y = parent.getHeight();
        }

        return upperBound;
    }

    public UIElement setUpperWorldBoundX(Integer bound){
        return setUpperWorldBounds(bound, upperWorldBounds.y);
    }
    public final UIElement unsetUpperWorldBoundX(){
        return setUpperWorldBoundX(null);
    }
    public final UIElement setUpperWorldBoundY(Integer bound){
        return setUpperWorldBounds(upperWorldBounds.x, bound);
    }
    public final UIElement unsetUpperWorldBoundY(){
        return setUpperWorldBoundY(null);
    }
    public UIElement setUpperWorldBounds(Integer boundX, Integer boundY){
        upperWorldBounds.x = boundX;
        upperWorldBounds.y = boundY;

        ensureElementWithinBounds();

        return this;
    }

    //endregion

    public UIElement setBoundWithinParent(boolean boundWithinParent){
        this.boundWithinParent = boundWithinParent;
        if(boundWithinParent){
            ensureElementWithinBounds();
        }
        return this;
    }
    public boolean isBoundWithinParent(){
        return boundWithinParent;
    }

    public UIElement setBorderToBorderBound(boolean borderToBorderBound){
        this.borderToBorderBound = borderToBorderBound;
        return this;
    }
    public boolean isBorderToBorderBound(){
        return borderToBorderBound;
    }

    private void ensureElementWithinBounds(){
        IntegerVector2 lowerLocalBounds = getLowerLocalBounds();
        IntegerVector2 upperLocalBounds = getUpperLocalBounds();

        int desiredPositionX = getLocalPositionX();
        int desiredPositionY = getLocalPositionY();

        int boundBoxUpperPosX = desiredPositionX + (borderToBorderBound ? 0 : getWidth());
        int boundBoxUpperPosY = desiredPositionY + (borderToBorderBound ? 0 : getHeight());

        if(upperLocalBounds.x != null && boundBoxUpperPosX > upperLocalBounds.x){
            desiredPositionX = upperLocalBounds.x - (borderToBorderBound ? 0 : getWidth());
            boundBoxUpperPosX = desiredPositionX + (borderToBorderBound ? 0 : getWidth());
        }
        if(upperLocalBounds.y != null && boundBoxUpperPosY > upperLocalBounds.y){
            desiredPositionY = upperLocalBounds.y - (borderToBorderBound ? 0 : getHeight());
            boundBoxUpperPosY = desiredPositionY + (borderToBorderBound ? 0 : getHeight());
        }

        if(lowerLocalBounds.x != null && desiredPositionX < lowerLocalBounds.x){
            desiredPositionX = lowerLocalBounds.x;
            boundBoxUpperPosX = desiredPositionX + (borderToBorderBound ? 0 : getWidth());
        }
        if(lowerLocalBounds.y != null && desiredPositionY < lowerLocalBounds.y){
            desiredPositionY = lowerLocalBounds.y;
            boundBoxUpperPosY = desiredPositionY + (borderToBorderBound ? 0 : getHeight());
        }

        int desiredWidth = getWidth();
        int desiredHeight = getHeight();

        //If we're still OOB, resize.
        if(upperLocalBounds.x != null && lowerLocalBounds.x != null && boundBoxUpperPosX > upperLocalBounds.x){
            desiredWidth = upperLocalBounds.x - lowerLocalBounds.x;
        }
        if(upperLocalBounds.y != null && lowerLocalBounds.y != null && boundBoxUpperPosY > upperLocalBounds.y){
            desiredHeight = upperLocalBounds.y - lowerLocalBounds.y;
        }

        if(desiredWidth != getWidth() || desiredHeight != getHeight()){
            setDimensions(desiredWidth, desiredHeight);
        }
        else if(desiredPositionX != getLocalPositionX() || desiredPositionY != getLocalPositionY()){
            setLocalPosition(desiredPositionX, desiredPositionY);
        }
    }

    //endregion

    //region Interactions
    public boolean onLeftInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onLeftInteraction();
        return hasInteraction;
    }
    public boolean onRightInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onRightInteraction();
        return hasInteraction;
    }
    public boolean onUpInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onUpInteraction();
        return hasInteraction;
    }
    public boolean onDownInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onDownInteraction();
        return hasInteraction;
    }

    public boolean onConfirmInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onConfirmInteraction();
        return hasInteraction;
    }
    public boolean onCancelInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onCancelInteraction();
        return hasInteraction;
    }
    //endregion

    //region Selection
    public UIElement select(){
        return setSelected(true);
    }
    public UIElement deselect(){
        return setSelected(false);
    }
    public UIElement setSelected(boolean selected){
        this.selected = selected;
        onSelectionStateChanged();
        return this;
    }

    public boolean isSelected(){
        return selected;
    }

    public void onSelectionStateChanged(){
        for(Consumer<Boolean> consumer : onSelectionStateChangedConsumers) consumer.accept(selected);
    }
    public UIElement addOnSelectionStateChangedConsumer(Consumer<Boolean> consumer){
        onSelectionStateChangedConsumers.add(consumer);
        return this;
    }

    public final UIElement getInnerMostSelectedChild(){
        for(UIElementChild child : children) {
            if(child.element.isSelected()){
                UIElement selectedChild = child.element.getInnerMostSelectedChild();
                return selectedChild == null ? child.element : selectedChild;
            }
        }
        return null;
    }

    public final boolean hasPreviousChildToSelect(){
        if(children.isEmpty()) return false;

        UIElementChild firstChild = getFirstChild();
        if(firstChild.element.isSelected()){
            return firstChild.element.hasPreviousChildToSelect();
        }

        return children.size() > 1;
    }
    public final void selectPreviousChild(){
        boolean selectedPreviousChild = false;
        for(UIElementChild child : children){
            if(child.element.isSelected()){
                if(child.element.hasPreviousChildToSelect()){
                    selectedPreviousChild = true;
                    child.element.selectPreviousChild();
                }
            }
        }

        if(!selectedPreviousChild){
            for(int i = children.size() - 1; i >= 0; i--){
                if(!children.get(i).element.isSelected()){
                    continue;
                }

                children.get(i).element.deselect();

                if(i - 1 < 0){
                    children.get(children.size() - 1).element.select();
                }
                else{
                    children.get(0).element.select();
                }

                selectedPreviousChild = true;
            }
        }

        if(!selectedPreviousChild && !children.isEmpty()){
            children.get(children.size() - 1).element.select();
        }
    }

    public final boolean hasNextChildToSelect(){
        if(children.isEmpty()) return false;

        UIElementChild lastChild = getLastChild();
        if(lastChild.element.isSelected()){
            return lastChild.element.hasNextChildToSelect();
        }

        return children.size() > 1;
    }
    public final void selectNextChild(){
        boolean selectedNextChild = false;
        for(UIElementChild child : children){
            if(child.element.isSelected()){
                if(child.element.hasNextChildToSelect()){
                    selectedNextChild = true;
                    child.element.selectNextChild();
                    break;
                }
            }
        }

        if(!selectedNextChild){
            for(int i = 0; i < children.size(); i++){
                if(!children.get(i).element.isSelected()){
                    continue;
                }

                children.get(i).element.deselect();

                if(i + 1 >= children.size()){
                    children.get(0).element.select();
                }
                else{
                    children.get(i + 1).element.select();
                }

                selectedNextChild = true;
            }
        }

        if(!selectedNextChild && !children.isEmpty()){
            children.get(0).element.select();
        }
    }
    //endregion

    //region Visible & Enabled States

    //region Visibility

    public void hide(){
        if(!isVisible) return;

        if(exitAnimation == null){
            setVisibility(false);
        }
        else{
            playAnimation(exitAnimation);
        }
    }
    public void hideInstantly(){
        if(!isVisible) return;

        setVisibility(false);
    }

    public void show(){
        if(isVisible) return;

        setVisibility(true);
        playAnimation(entryAnimation);
    }
    public void showInstantly(){
        if(isVisible) return;

        setVisibility(true);
    }

    protected void setVisibility(boolean visible){
        isVisible = visible;
    }
    public boolean isVisible(){
        if(hasParent() && !parent.isVisible()) return false;
        return isVisible;
    }

    //endregion

    //region Enabled State

    public void disable(){
        setEnabled(false);
    }
    public void enable(){
        setEnabled(true);
    }
    protected void setEnabled(boolean enabled){
        isEnabled = enabled;
    }
    public boolean isEnabled(){
        if(hasParent() && !parent.isEnabled()) return false;
        return isEnabled;
    }

    //endregion

    public void hideAndDisable(){
        hide();
        disable();
    }
    public void showAndEnable(){
        show();
        enable();
    }

    public void hideAndDisableInstantly(){
        hideInstantly();
        disable();
    }

    public void showAndEnableInstantly(){
        showInstantly();
        enable();
    }

    public boolean isActive(){
        if(hasParent() && !parent.isActive()) return false;

        IntegerVector4 maskBounds = getMaskWorldBounds();
        //if(maskBounds != null && !overlaps(maskBounds)) return false;

        return isVisible() || isEnabled();
    }

    //endregion

    //region Darken & Lighten

    public UIElement setDarkenedColor(Color darkenedColor){
        this.darkenedColor = darkenedColor;
        return this;
    }
    public Color getDarkenedColor(){
        return darkenedColor;
    }

    public UIElement setDarkenedColorMultiplier(float darkenedColorMultiplier){
        this.darkenedColorMultiplier = darkenedColorMultiplier;
        if(this.darkenedColorMultiplier > 1.0f) this.darkenedColorMultiplier = 1.0f;
        return this;
    }
    public Float getDarkenedColorMultiplier(){
        return darkenedColorMultiplier;
    }

    public UIElement darkenInstantly(){
        return setDarkened(true);
    }
    public UIElement lightenInstantly(){
        return setDarkened(false);
    }
    private UIElement setDarkened(boolean darkened){
        this.isDarkened = darkened;
        return this;
    }

    public boolean isDarkened(){
        return isDarkened || (hasParent() && parent.isDarkened());
    }

    //endregion

    //region Refresh

    public void markForRefresh(){
        pendingRefresh = true;
    }
    protected void onRefreshElement(){}

    //endregion

    //region Width & Height

    public UIElement setWidth(int newWidth){
        return setDimensions(newWidth, -1);
    }
    public UIElement setHeight(int newHeight){
        return setDimensions(-1, newHeight);
    }
    public UIElement setDimensions(int newWidth, int newHeight){
        if(newWidth != -1 && newWidth < 1) newWidth = 1;
        if(newHeight != -1 && newHeight < 1) newHeight = 1;

        float oldScaleX = widthScale;
        float oldScaleY = heightScale;

        widthScale = newWidth != -1 ? (float) newWidth / dimensions.x : widthScale;
        heightScale = newHeight != -1 ? (float) newHeight / dimensions.y : heightScale;

        if(oldScaleX != widthScale || oldScaleY != heightScale){
            for(UIElementChild child : children){
                child.element.onParentDimensionsChanged(newWidth != -1 ? newWidth - dimensions.x : 0, newHeight != -1 ? newHeight - dimensions.y : 0);
            }
        }

        return this;
    }

    public void onParentDimensionsChanged(int diffX, int diffY){
    }

    public int getWidth(){
        return (int) (dimensions.x * getWidthScaleMult());
    }
    public int getHeight(){
        return (int) (dimensions.y * getHeightScaleMult());
    }
    public IntegerVector2 getDimensions(){
        IntegerVector2 dimensions = this.dimensions.copy();
        dimensions.x = (int) (dimensions.x * getWidthScaleMult());
        dimensions.y = (int) (dimensions.y * getWidthScaleMult());
        return dimensions;
    }

    public int getWidthUnscaled(){
        return dimensions.x;
    }
    public int getHeightUnscaled(){
        return dimensions.y;
    }
    public IntegerVector2 getDimensionsUnscaled(){
        return dimensions.copy();
    }

    protected float getWidthScaleMult(){
        float scaleMult = widthScale;
        if(hasParent() && scalesWithParent()){
            scaleMult *= parent.getWidthScaleMult();
        }
        return scaleMult;
    }

    protected float getHeightScaleMult(){
        float scaleMult = heightScale;
        if(hasParent() && scalesWithParent()){
            scaleMult *= parent.getHeightScaleMult();
        }
        return scaleMult;
    }

    public UIElement setScaleWithParent(boolean scaleWithParent){
        this.scaleWithParent = scaleWithParent;
        return this;
    }

    public boolean scalesWithParent(){
        return scaleWithParent;
    }

    //endregion

    //region Animations

    public UIElement setEntryAnimation(UIAnimation entryAnimation){
        this.entryAnimation = entryAnimation;
        return this;
    }

    public UIElement setExitAnimation(UIAnimation exitAnimation){
        this.exitAnimation = exitAnimation;
        return this;
    }

    public UIElement setAnimation(UIAnimation animation){
        this.animation = animation;
        return this;
    }

    public void playAnimation(UIAnimation animation){
        if(playingAnimation != null){
            playingAnimation.finishInstantly();
        }

        playingAnimation = animation;
        if(playingAnimation != null){
            playingAnimation.start();
        }
    }

    //endregion

    //region Resource Management
    public void dispose(){
        for(UIElementChild child : children){
            child.element.dispose();
        }
    }
    //endregion

    //region Bounds Methods

    public boolean overlapsParent(){
        return parent != null && overlaps(parent);
    }
    public boolean overlaps(UIElement other){
        return overlaps(new IntegerVector4(other.getWorldPositionX(), other.getWorldPositionY(), other.getWidth(), other.getHeight()));
    }
    public boolean overlaps(IntegerVector4 other){
        IntegerVector2 thisWorldPos = getWorldPosition();
        IntegerVector2 thisDimensions = getDimensions();

        if (thisWorldPos.y + thisDimensions.y < other.y || thisWorldPos.y > other.y + other.h) {
            return false;
        }
        if (thisWorldPos.x + thisDimensions.x < other.x || thisWorldPos.x > other.x + other.w) {
            return false;
        }
        return true;
    }

    public boolean withinParent(){
        return parent != null && within(parent);
    }
    public boolean within(UIElement other){
        return within(new IntegerVector4(other.getWorldPositionX(), other.getWorldPositionY(), other.getWidth(), other.getHeight()));
    }
    public boolean within(IntegerVector4 other){
        IntegerVector2 thisWorldPos = getWorldPosition();

        IntegerVector2 thisDimensions = getDimensions();

        if (thisWorldPos.x < other.x || thisWorldPos.y < other.y) {
            return false;
        }

        if (thisWorldPos.x + thisDimensions.x > other.x + other.w || thisWorldPos.y + thisDimensions.y > other.y + other.h) {
            return false;
        }

        return true;
    }

    //endregion

    //region Masks

    public UIElement setElementMask(UIElement elementMask){
        this.elementMask = elementMask;
        return this;
    }

    public boolean hasMaskBounds(){
        return elementMask != null || (hasParent() && parent.hasMaskBounds());
    }

    public IntegerVector4 getMaskWorldBounds(){
        IntegerVector4 bounds = null;

        UIElement mask = elementMask;
        if(mask != null){
            bounds = new IntegerVector4(mask.getWorldPositionX(), mask.getWorldPositionY(), mask.getWidth(), mask.getHeight());
        }

        UIElement current = this;
        while(current.hasParent()){
            current = current.getParent();
            mask = current.elementMask;

            if(mask != null){
                if(bounds == null){
                    bounds = new IntegerVector4(mask.getWorldPositionX(), mask.getWorldPositionY(), mask.getWidth(), mask.getHeight());
                }
                else{
                    bounds = bounds.overlap(new IntegerVector4(mask.getWorldPositionX(), mask.getWorldPositionY(), mask.getWidth(), mask.getHeight()));
                }
            }
        }

        return bounds;
    }

    //endregion Masks

    //region Lifespan

    public void updateLifespan(){
        if(totalLifespan == -1) return;

        remainingLifespan -= Gdx.graphics.getDeltaTime();
        if(remainingLifespan <= 0){
            hideAndDisable();
            //TODO wait for animations to finish
            if(parent != null){
                parent.removeChild(this);
                dispose();
            }
            //TODO fire on death event
        }
    }

    public UIElement setLifespan(float lifespan){
        totalLifespan = lifespan;
        remainingLifespan = lifespan;
        return this;
    }

    public float getTotalLifespan(){
        return totalLifespan;
    }

    public float getRemainingLifespan(){
        return remainingLifespan;
    }

    //endregion

    //region Top-Level Display

    public void open(){
        open(true);
    }
    public void open(boolean closePrevious){
        UIManager.hideAllUIElements();
        UIManager.openUIElement(this);
    }

    public void close(){
        close(true, false);
    }
    public void close(boolean reopenPrevious, boolean closePermanently){
        UIManager.closeUIElement(this);
        UIManager.reopenPreviousUIElement();
    }

    //endregion

    //region String Tables

    protected String getUIStringsKey(){
        return null;
    }

    protected String uiString(int valIndex){
        if(stringTable != null){
            return stringTable.TEXT[valIndex];
        }
        else if(parent != null){
            return parent.uiString(valIndex);
        }
        else{
            return "MISSING STRING TABLE ENTRY";
        }
    }

    //endregion String Tables

    //endregion

    public static class UIElementChild{
        public UIElement element;
        public boolean isControllerSelectable;

        public UIElementChild(UIElement element, boolean isControllerSelectable){
            this.element = element;
            this.isControllerSelectable = isControllerSelectable;
        }
    }

    public static class UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public StringProperty id = new StringProperty(""){
            @Override
            public boolean isValidValue(String value) {
                return !value.isEmpty();
            }
        }.setName("Id");

        public IntegerVector2Property localPosition = new IntegerVector2Property(new IntegerVector2(0, 0)).setName("Local Position").setValueNames("X", "Y");
        public boolean dockedToParent = true;

        public IntegerVector2Property dimensions = new IntegerVector2Property(new IntegerVector2(1, 1)){
            @Override
            public void onValueChanged(IntegerVector2 oldValue, IntegerVector2 newValue) {
                super.onValueChanged(oldValue, newValue);

                for(UIElementData subElement : Reflection.getFieldValuesByClass(UIElementData.class, getSelf())){
                    subElement.dimensions.setValue(newValue.copy());
                };
            }
        }.setName("Dimensions").setValueNames("W", "H");
        public boolean scaleWithParent = true;

        public IntegerVector2 lowerLocalBound = new IntegerVector2(null, null);
        public IntegerVector2 upperLocalBound = new IntegerVector2(null, null);
        public IntegerVector2 lowerWorldBound = new IntegerVector2(null, null);
        public IntegerVector2 upperWorldBound = new IntegerVector2(null, null);
        public boolean boundWithinParent = false;
        public boolean borderToBorderBound = false;

        public BooleanProperty isVisible = new BooleanProperty(true).setName("Visible");
        public BooleanProperty isEnabled = new BooleanProperty(true).setName("Enabled");

        public String darkenedColor = Color.BLACK.toString();
        public float darkenedColorMultiplier = 0.25f;

        public MethodBinding onSelectionStateChangedBinding = new NoneMethodBinding();

        public boolean isSelectable;

        public UIElement makeUIElement(){
            return new UIElement(this);
        }

        public ArrayList<Property<?>> getEditableProperties(){
            ArrayList<Property<?>> properties = new ArrayList<>();

            for(Property<?> property : Reflection.getFieldValuesByClass(Property.class, this)){
                properties.add(property);
            }

            for(UIElementData subElement : Reflection.getFieldValuesByClass(UIElementData.class, this)){
                ArrayList<Property<?>> subProperties = subElement.getEditableProperties();
                subElement.filterInnerProperties(subProperties);
                properties.addAll(subProperties);
            }

            return properties;
        }

        public void filterInnerProperties(ArrayList<Property<?>> properties){
            properties.remove(id);
            properties.remove(localPosition);
            properties.remove(dimensions);
            properties.remove(isVisible);
            properties.remove(isEnabled);
        }

        public static UIElementData deserializeFromString(String s){
            byte[] data = Base64.getDecoder().decode(s);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                return (UIElementData) ois.readObject();
            }catch (Exception e){
                DLibLogger.log("Failed to deserialize AbstractScreenData due to " + e.getLocalizedMessage());
                e.printStackTrace();
            }

            return null;
        }

        public UIElementData getSelf(){
            return this;
        }
    }
}
