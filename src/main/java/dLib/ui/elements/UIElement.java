package dLib.ui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import dLib.modcompat.ModManager;
import dLib.patches.InputHelperHoverConsumer;
import dLib.properties.objects.*;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.animations.UIAnimation;
import dLib.ui.elements.components.UIElementComponent;
import dLib.ui.elements.prefabs.ItemBox;
import dLib.ui.screens.UIManager;
import dLib.util.*;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.StaticDimension;
import dLib.util.ui.padding.AbstractPadding;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import sayTheSpire.Output;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class UIElement {
    //region Variables

    protected String ID;

    protected UIElement parent;
    protected List<UIElementChild> children = new ArrayList<>();

    protected Hitbox hb;

    private AbstractPosition localPosX = Pos.px(0);
    private AbstractPosition localPosY = Pos.px(0);
    private Integer localPosXCache = null;
    private Integer localPosYCache = null;
    private Integer worldPosXCache = null;
    private Integer worldPosYCache = null;
    private ArrayList<Consumer<UIElement>> positionChangedConsumers = new ArrayList<>();

    private int localChildOffsetX = 0;
    private int localChildOffsetY = 0;

    private Alignment alignment = new Alignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);

    private AbstractDimension width = Dim.fill();
    private AbstractDimension height = Dim.fill();
    private Integer widthCache = null;
    private Integer heightCache = null;
    private IntegerVector2 lowerLocalBounds = new IntegerVector2(null, null);
    private IntegerVector2 upperLocalBounds = new IntegerVector2(null, null);
    private IntegerVector2 lowerWorldBounds = new IntegerVector2(null, null);
    private IntegerVector2 upperWorldBounds = new IntegerVector2(null, null);
    private boolean boundWithinParent = false;
    private boolean borderToBorderBound = false;

    private AbstractPadding paddingLeft = Padd.px(0);
    private AbstractPadding paddingBottom = Padd.px(0);
    private AbstractPadding paddingRight = Padd.px(0);
    private AbstractPadding paddingTop = Padd.px(0);

    private UIElement elementMask = null;

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    private Color darkenedColor = Color.BLACK;
    private float darkenedColorMultiplier = 0.4f;
    protected boolean isDarkened = false;

    private boolean selected;
    private ArrayList<Consumer<Boolean>> onSelectionStateChangedConsumers = new ArrayList<>();

    private boolean isPassthrough = false;

    private LinkedHashMap<UUID, Runnable> onHoveredEvents = new LinkedHashMap<>();
    private LinkedHashMap<UUID, Consumer<Float>> onHoverTickEvents = new LinkedHashMap<>();
    private LinkedHashMap<UUID, Runnable> onUnHoveredEvents = new LinkedHashMap<>();

    private LinkedHashMap<UUID, Runnable> onLeftClickEvents = new LinkedHashMap<>();
    private LinkedHashMap<UUID, Consumer<Float>> onLeftClickHeldEvents = new LinkedHashMap<>();
    private LinkedHashMap<UUID, Runnable> onLeftClickReleaseEvents = new LinkedHashMap<>();

    private LinkedHashMap<UUID, Runnable> onRightClickEvents = new LinkedHashMap<>();
    private LinkedHashMap<UUID, Consumer<Float>> onRightClickHeldEvents = new LinkedHashMap<>();
    private LinkedHashMap<UUID, Runnable> onRightClickReleaseEvents = new LinkedHashMap<>();

    private String onHoverLine; // Say the Spire mod compatibility

    protected String onSelectLine; // Say the Spire mod compatibility
    protected String onTriggeredLine; // Say the Spire mod compatibility

    private float totalHoverDuration;

    private float totalLeftClickDuration;
    private float totalRightClickDuration;

    private boolean holdingLeft;
    private boolean holdingRight;


    private ArrayList<UIElementComponent> components = new ArrayList<>();

    protected ArrayList<Runnable> delayedActions =new ArrayList<>();

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

    public UIElement(){
        this(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }
    public UIElement(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public UIElement(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        this.ID = getClass().getSimpleName() + "_" + UUID.randomUUID().toString().replace("-", "");
        this.localPosX = xPos;
        this.localPosY = yPos;
        this.width = width;
        this.height = height;

        String uiStrings = getUIStringsKey();
        if(uiStrings != null){
            stringTable = CardCrawlGame.languagePack.getUIString(uiStrings);
        }

        hb = new Hitbox(0, 0, 1, 1);

        GlobalEvents.subscribe(GlobalEvents.Events.PreLeftClickEvent.class, (event) -> {
            if(event.source != this && isSelected()){
                deselect();
            }
        });
    }

    public UIElement(UIElementData data){
        setID(data.id.getValue());

        setLocalPosition(data.localPosition.getXValue(), data.localPosition.getYValue());
        
        width = Dim.px(data.dimensions.getXValue());
        height = Dim.px(data.dimensions.getYValue());

        setLowerLocalBounds(data.lowerLocalBound.x, data.lowerLocalBound.y);
        setUpperLocalBounds(data.upperLocalBound.x, data.upperLocalBound.y);
        setLowerWorldBounds(data.lowerWorldBound.x, data.lowerWorldBound.y);
        setUpperWorldBounds(data.upperWorldBound.x, data.upperWorldBound.y);
        setBoundWithinParent(data.boundWithinParent);
        setBorderToBorderBound(data.borderToBorderBound);

        if(!data.isVisible.getValue() && !data.isEnabled.getValue()){
            hideAndDisableInstantly();
        }
        else if(!data.isVisible.getValue()){
            hideInstantly();
        }
        else if(!data.isEnabled.getValue()){
            disable();
        }

        this.darkenedColor = Color.valueOf(data.darkenedColor);
        this.darkenedColorMultiplier = data.darkenedColorMultiplier;

        onSelectionStateChangedConsumers.add(aBoolean -> data.onSelectionStateChangedBinding.executeBinding(aBoolean));

        hb = new Hitbox(0, 0, 1, 1);

        addOnHoveredEvent(() -> data.onHovered.executeBinding(getTopParent()));
        addOnHoverTickEvent((elapsedTime) -> data.onHoverTick.executeBinding(getTopParent(), elapsedTime));
        addOnUnHoveredEvent(() -> data.onUnhovered.executeBinding(getTopParent()));

        this.isPassthrough = data.isPassthrough;

        GlobalEvents.subscribe(GlobalEvents.Events.PreLeftClickEvent.class, (event) -> {
            if(event.source != this && isSelected()){
                deselect();
            }
        });
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
        //Update Components
        {
            for(UIElementComponent component : components){
                component.onUpdate(this);
            }
        }

        //Update Hitbox
        {
            boolean hbHoveredCache = this.hb.hovered || this.hb.justHovered;

            float targetHbX = getWorldPositionCenteredX() * Settings.xScale;
            float targetHbY = getWorldPositionCenteredY() * Settings.yScale;
            float targetHbWidth = getWidth() * Settings.xScale;
            float targetHbHeight = getHeight() * Settings.yScale;

            Bounds maskBounds = getMaskWorldBounds();
            if(maskBounds != null){
                Bounds myBounds = getBounds();
                if(getBounds().overlaps(maskBounds)){
                    if(!myBounds.within(maskBounds)){
                        if(getWorldPositionX() < maskBounds.left){
                            float newTargetHbX = (maskBounds.left + getWidth() * 0.5f) * Settings.xScale;
                            targetHbWidth -= newTargetHbX - targetHbX;
                            targetHbX = newTargetHbX;
                        }
                        if(getWorldPositionY() < maskBounds.bottom){
                            float newTargetHbY = (maskBounds.bottom + getHeight() * 0.5f) * Settings.yScale;
                            targetHbHeight -= newTargetHbY - targetHbY;
                            targetHbY = newTargetHbY;
                        }


                        if(getWorldPositionX() + getWidth() > maskBounds.right){
                            targetHbWidth = (maskBounds.right - getWorldPositionX()) * Settings.xScale;
                        }
                        if(getWorldPositionY() + getHeight() > maskBounds.top){
                            targetHbHeight = (maskBounds.top - getWorldPositionY()) * Settings.yScale;
                            targetHbY = (maskBounds.top - (targetHbHeight / Settings.yScale * 0.5f)) * Settings.yScale;
                        }
                    }
                }
                else{
                    targetHbWidth = 0;
                    targetHbHeight = 0;
                }
            }

            this.hb.resize(targetHbWidth, targetHbHeight);
            this.hb.move(targetHbX, targetHbY);
            this.hb.update();

            if((this.hb.justHovered || this.hb.hovered) && InputHelperHoverConsumer.alreadyHovered){
                this.hb.justHovered = false;
                this.hb.hovered = false;
            }

            if(isEnabled()){
                if(this.hb.justHovered) onHovered();
                if(this.hb.hovered){
                    totalHoverDuration += Gdx.graphics.getDeltaTime();
                    onHoverTick(totalHoverDuration);
                }
            }

            if(hbHoveredCache && (!this.hb.hovered && !this.hb.justHovered)){
                onUnhovered();
            }

            if(isEnabled()){
                if(isHovered()){
                    if(InputHelper.justClickedLeft){
                        clickLeft();
                        if(!isPassthrough()){
                            InputHelper.justClickedLeft = false;
                        }
                    }
                    if(InputHelper.justClickedRight){
                        clickRight();
                        if(!isPassthrough()) InputHelper.justClickedRight = false;
                    }

                }

                if(holdingLeft){
                    if(InputHelper.justReleasedClickLeft){
                        onLeftClickRelease();
                        return;
                    }

                    totalLeftClickDuration += Gdx.graphics.getDeltaTime();
                    onLeftClickHeld(totalLeftClickDuration);
                }

                if(holdingRight){
                    if(InputHelper.justReleasedClickRight){
                        onRightButtonRelease();
                        return;
                    }

                    totalRightClickDuration += Gdx.graphics.getDeltaTime();
                    onRightClickHeld(totalRightClickDuration);
                }
            }
        }

        //Update Playing Animation
        {
            if(playingAnimation != null){
                playingAnimation.update();
                if(!playingAnimation.isPlaying()){
                    playingAnimation.finishInstantly();
                    playingAnimation = null;
                }
            }
        }

        //Update Delayed Actions
        {
            while(!delayedActions.isEmpty()){
                delayedActions.remove(0).run();
            }
        }

        //Update Lifespan
        {
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
    }
    protected void updateChildren(){
        for(int i = children.size() - 1; i >= 0; i--){
            children.get(i).element.update();
        }
    }

    public final void render(SpriteBatch sb){
        if(!shouldRender()) return;

        boolean pushedScissors = false;
        Bounds maskBounds = getMaskWorldBounds();
        if(maskBounds != null){
            sb.flush();

            Rectangle scissors = new Rectangle();
            Rectangle mask = new Rectangle(maskBounds.left * Settings.xScale, maskBounds.bottom * Settings.yScale, (maskBounds.right - maskBounds.left) * Settings.xScale, (maskBounds.top - maskBounds.bottom) * Settings.yScale);

            OrthographicCamera camera = Reflection.getFieldValue("camera", Gdx.app.getApplicationListener());
            ScissorStack.calculateScissors(camera, sb.getTransformMatrix(), mask, scissors);
            pushedScissors = ScissorStack.pushScissors(scissors);
        }

        renderSelf(sb);

        if(pushedScissors){
            ScissorStack.popScissors();
            sb.flush();
        }

        renderChildren(sb);
    }

    protected void renderSelf(SpriteBatch sb){
        //Render Components
        {
            for(UIElementComponent component : components){
                component.onRender(this, sb);
            }
        }

        //Render Hitbox
        {
            hb.render(sb);
        }
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
    private UIElement setParent(UIElement parent){
        this.parent = parent;
        onParentChanged();
        return this;
    }

    public void reparent(UIElement newParent){
        boolean csSelectable = false;
        if(parent != null){
            for(UIElementChild child : parent.children){
                if(child.element == this){
                    csSelectable = child.isControllerSelectable;
                    parent.removeChild(this);
                    break;
                }
            }
        }

        newParent.addChild(this, csSelectable);
    }

    public <T extends UIElement> T getParent(){
        return (T) parent;
    }
    public boolean hasParent(){
        return parent != null;
    }

    public <T extends UIElement> T getTopParent(){
        if(parent == null) return (T) this;
        return parent.getTopParent();
    }

    public <T extends UIElement> T getParentOfType(Class<T> parentType){
        if(parent == null) return null;
        if(parentType.isAssignableFrom(parent.getClass())) return (T) parent;
        return parent.getParentOfType(parentType);
    }

    public void onParentChanged(){
        ensureElementWithinBounds();
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
    public UIElement replaceChild(UIElement original, UIElement replacement){
        for(UIElementChild child : children){
            if(Objects.equals(child.element, original)){
                child.element.setParent(null);
                child.element = replacement;
                replacement.setParent(this);
                return this;
            }
        }

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

    public ArrayList<UIElement> getAllChildren(){
        ArrayList<UIElement> allChildren = new ArrayList<>();
        for(UIElementChild child : children){
            allChildren.add(child.element);
            allChildren.addAll(child.element.getAllChildren());
        }
        return allChildren;
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
        return setLocalPosition(Pos.px(newPosition), getLocalPositionYRaw());
    }
    public UIElement setLocalPositionY(int newPosition){
        return setLocalPosition(getLocalPositionXRaw(), Pos.px(newPosition));
    }
    public UIElement setLocalPosition(int newPositionX, int newPositionY){
        return setLocalPosition(Pos.px(newPositionX), Pos.px(newPositionY));
    }
    public UIElement setLocalPosition(AbstractPosition newX, AbstractPosition newY){
        AbstractPosition oldPosX = localPosX;
        AbstractPosition oldPosY = localPosY;

        localPosX = newX;
        localPosY = newY;

        if(!oldPosX.equals(localPosX) || !oldPosY.equals(localPosY)){
            onPositionChanged();
        }

        ensureElementWithinBounds();

        return this;
    }

    public int getLocalPositionX(){
        if(localPosXCache == null){
            localPosXCache = localPosX.getLocalX(this) + paddingLeft.getHorizontal(this);
        }

        return localPosXCache;
    }
    public int getLocalPositionY(){
        if(localPosYCache == null){
            localPosYCache = localPosY.getLocalY(this) + paddingBottom.getVertical(this);
        }

        return localPosYCache;
    }
    public IntegerVector2 getLocalPosition(){
        return new IntegerVector2(getLocalPositionX(), getLocalPositionY());
    }

    public AbstractPosition getLocalPositionXRaw(){
        return localPosX;
    }
    public AbstractPosition getLocalPositionYRaw(){
        return localPosY;
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

    public int getLocalPositionCenteredX(){
        return getLocalPositionCentered().x;
    }
    public int getLocalPositionCenteredY(){
        return getLocalPositionCentered().y;
    }
    public IntegerVector2 getLocalPositionCentered(){
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

    public int getWorldPositionX(){
        worldPosXCache = null;
        if (worldPosXCache == null) {
            int parentWorldX = getParent() != null ?
                    getParent().getWorldPositionX() + (this instanceof ItemBox && !(getParent() instanceof ItemBox) ? 0 : getParent().getLocalChildOffsetX()) :
                    0;

            worldPosXCache = parentWorldX + getLocalPositionX();
        }

        return worldPosXCache;
    }
    public int getWorldPositionY(){
        worldPosYCache = null;
        if(worldPosYCache == null){
            int parentWorldY = getParent() != null ?
                    getParent().getWorldPositionY() + (this instanceof ItemBox && !(getParent() instanceof ItemBox) ? 0 : getParent().getLocalChildOffsetY()) :
                    0;

            worldPosYCache = parentWorldY + getLocalPositionY();
        }

        return worldPosYCache;
    }
    public IntegerVector2 getWorldPosition(){
        return new IntegerVector2(getWorldPositionX(), getWorldPositionY());
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

    public void onPositionChanged(){
        invalidateCachesForElementTree();

        for(Consumer<UIElement> consumer : positionChangedConsumers) consumer.accept(this);

        for(UIElementChild child : children){
            child.element.onParentPositionChanged();
        }
    }
    public UIElement addOnPositionChangedConsumer(Consumer<UIElement> consumer){
        positionChangedConsumers.add(consumer);
        return this;
    }

    protected void onParentPositionChanged(){
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

        Integer desiredWidth = null;
        Integer desiredHeight = null;

        int desiredPositionX = getLocalPositionX();
        int desiredPositionY = getLocalPositionY();

        if(width instanceof StaticDimension){
            int boundBoxUpperPosX = desiredPositionX + (borderToBorderBound ? 0 : getWidth());

            if(upperLocalBounds.x != null && boundBoxUpperPosX > upperLocalBounds.x){
                desiredPositionX = upperLocalBounds.x - (borderToBorderBound ? 0 : getWidth());
                boundBoxUpperPosX = desiredPositionX + (borderToBorderBound ? 0 : getWidth());
            }

            if(lowerLocalBounds.x != null && desiredPositionX < lowerLocalBounds.x){
                desiredPositionX = lowerLocalBounds.x;
                boundBoxUpperPosX = desiredPositionX + (borderToBorderBound ? 0 : getWidth());
            }

            desiredWidth = getWidth();

            if(upperLocalBounds.x != null && lowerLocalBounds.x != null && boundBoxUpperPosX > upperLocalBounds.x){
                desiredWidth = upperLocalBounds.x - lowerLocalBounds.x;
            }

        }

        if(height instanceof StaticDimension){
            int boundBoxUpperPosY = desiredPositionY + (borderToBorderBound ? 0 : getHeight());

            if(upperLocalBounds.y != null && boundBoxUpperPosY > upperLocalBounds.y){
                desiredPositionY = upperLocalBounds.y - (borderToBorderBound ? 0 : getHeight());
                boundBoxUpperPosY = desiredPositionY + (borderToBorderBound ? 0 : getHeight());
            }

            if(lowerLocalBounds.y != null && desiredPositionY < lowerLocalBounds.y){
                desiredPositionY = lowerLocalBounds.y;
                boundBoxUpperPosY = desiredPositionY + (borderToBorderBound ? 0 : getHeight());
            }

            desiredHeight = getHeight();

            if(upperLocalBounds.y != null && lowerLocalBounds.y != null && boundBoxUpperPosY > upperLocalBounds.y){
                desiredHeight = upperLocalBounds.y - lowerLocalBounds.y;
            }
        }

        if((desiredWidth != null && desiredWidth != getWidth()) || (desiredHeight != null && desiredHeight != getHeight())){
            setDimensions(desiredWidth == null ? width : Dim.px(desiredWidth),
                    desiredHeight == null ? height : Dim.px(desiredHeight));
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

    //region Caches

    private void invalidateCaches(){
        localPosXCache = null;
        localPosYCache = null;

        worldPosXCache = null;
        worldPosYCache = null;

        widthCache = null;
        heightCache = null;
    }

    private void invalidateCachesForElementTree(){
        invalidateCaches();
        invalidateParentCacheRecursive();
        invalidateChildrenCacheRecursive();
    }

    private void invalidateParentCacheRecursive(){
        if(hasParent()){
            parent.invalidateCaches();
            parent.invalidateParentCacheRecursive();
        }
    }

    private void invalidateChildrenCacheRecursive(){
        for(UIElementChild child : children){
            child.element.invalidateCaches();
            child.element.invalidateChildrenCacheRecursive();
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

        invalidateCachesForElementTree();
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

    //region Width & Height

    public UIElement setWidth(AbstractDimension newWidth){
        return setDimensions(newWidth, null);
    }
    public UIElement setHeight(AbstractDimension newHeight){
        return setDimensions(null, newHeight);
    }
    public UIElement setDimensions(AbstractDimension newWidth, AbstractDimension newHeight){
        AbstractDimension oldWidth = width;
        AbstractDimension oldHeight = height;

        width = newWidth == null ? width : newWidth;
        height = newHeight == null ? height : newHeight;

        if(!Objects.equals(oldWidth, width) || !Objects.equals(oldHeight, height)){
            onDimensionsChanged();
        }

        return this;
    }

    public UIElement setWidth(int newWidth){
        return setWidth(Dim.px(newWidth));
    }
    public UIElement setHeight(int newHeight){
        return setHeight(Dim.px(newHeight));
    }
    public UIElement setDimensions(int newWidth, int newHeight){
        return setDimensions(Dim.px(newWidth), Dim.px(newHeight));
    }

    public void onDimensionsChanged(){
        invalidateCachesForElementTree();
        for(UIElementChild child : children){
            child.element.onParentDimensionsChanged();
        }
    }

    public void onParentDimensionsChanged(){
    }

    public int getWidth(){
        if(widthCache == null || widthCache <= 0){
            widthCache = width.getWidth(this) - getPaddingRight();
        }

        return widthCache;
    }
    public int getHeight(){
        if(heightCache == null || heightCache <= 0){
            heightCache = height.getHeight(this) - getPaddingTop();
        }

        return heightCache;
    }
    public IntegerVector2 getDimensions(){
        return new IntegerVector2(getWidth(), getHeight());
    }

    public AbstractDimension getWidthRaw(){
        return width.cpy();
    }
    public AbstractDimension getHeightRaw(){
        return height.cpy();
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

    public Bounds getBounds(){
        return new Bounds(getWorldPositionX(), getWorldPositionY(), getWorldPositionX() + getWidth(), getWorldPositionY() + getHeight());
    }
    public Bounds getBoundsUnscrolled(){
        if(!hasParent()) return getBounds();
        return new Bounds(getWorldPositionX() - parent.getTotalLocalChildOffsetX(), getWorldPositionY() - parent.getTotalLocalChildOffsetY(), getWorldPositionX() - parent.getTotalLocalChildOffsetX() + getWidth(), getWorldPositionY() - parent.getTotalLocalChildOffsetY() + getHeight());
    }

    public Bounds getLocalBounds(){
        return new Bounds(getLocalPositionX(), getLocalPositionY(), getLocalPositionX() + getWidth(), getLocalPositionY() + getHeight());
    }

    public boolean overlapsParent(){
        return parent != null && overlaps(parent);
    }
    public boolean overlaps(UIElement other){
        return getBounds().overlaps(other.getBounds());
    }

    public Bounds getChildUnscrolledBounds(){
        return getChildUnscrolledBoundsRecursive(null);
    }
    private Bounds getChildUnscrolledBoundsRecursive(Bounds bounds){
        for(UIElementChild child : children){
            if(!(child.element.isActive())){
                continue;
            }

            Bounds childBounds = child.element.getBoundsUnscrolled();
            if(bounds == null){
                bounds = childBounds;
            }

            if(this instanceof ItemBox && getWidthRaw() instanceof AutoDimension){
                if(childBounds.left < bounds.left) bounds.left = childBounds.left;
                if(childBounds.right > bounds.right) bounds.right = childBounds.right;
            }

            if(this instanceof ItemBox && getHeightRaw() instanceof AutoDimension){
                if(childBounds.bottom < bounds.bottom) bounds.bottom = childBounds.bottom;
                if(childBounds.top > bounds.top) bounds.top = childBounds.top;
            }

            bounds = child.element.getChildUnscrolledBoundsRecursive(bounds);
        }

        return bounds;
    }

    public Bounds getFullChildLocalBounds(){
        Bounds fullChildBounds = null;
        for(UIElementChild child : children){
            if(!(child.element.isActive())){
                continue;
            }

            Bounds childBounds = child.element.getFullLocalBounds();
            if(fullChildBounds == null){
                fullChildBounds = childBounds;
                continue;
            }

            if(childBounds.left < fullChildBounds.left) fullChildBounds.left = childBounds.left;
            if(childBounds.right > fullChildBounds.right) fullChildBounds.right = childBounds.right;
            if(childBounds.bottom < fullChildBounds.bottom) fullChildBounds.bottom = childBounds.bottom;
            if(childBounds.top > fullChildBounds.top) fullChildBounds.top = childBounds.top;
        }
        return fullChildBounds;
    }
    public Bounds getFullLocalBounds(){
        Bounds myBounds = getLocalBounds();

        Bounds fullChildBounds = getFullChildLocalBounds();

        if(fullChildBounds != null){
            if(fullChildBounds.left < 0) myBounds.left += fullChildBounds.left;
            if(fullChildBounds.right > myBounds.right) myBounds.right += fullChildBounds.right - myBounds.right;
            if(fullChildBounds.bottom < 0) myBounds.bottom += fullChildBounds.bottom;
            if(fullChildBounds.top > myBounds.top) myBounds.top += fullChildBounds.top - myBounds.top;
        }

        return myBounds;
    }

    public boolean withinParent(){
        return parent != null && within(parent);
    }
    public boolean within(UIElement other){
        return getBounds().within(other.getBounds());
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

    public Bounds getMaskWorldBounds(){
        Bounds bounds = null;

        UIElement mask = elementMask;
        if(mask != null){
            return mask.getBounds();
        }

        UIElement current = this;
        while(current.hasParent()){
            current = current.getParent();
            mask = current.elementMask;

            if(mask != null){
                return mask.getBounds();
            }
        }

        return bounds;
    }

    //endregion Masks

    //region Lifespan

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

    //region Padding

    public UIElement setPadding(AbstractPadding all){
        return setPadding(all, all, all, all);
    }
    public UIElement setPadding(AbstractPadding leftRight, AbstractPadding topBottom){
        return setPadding(topBottom, leftRight, topBottom, leftRight);
    }
    public UIElement setPadding(AbstractPadding top, AbstractPadding right, AbstractPadding bottom, AbstractPadding left){
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
        this.paddingLeft = left;
        return this;
    }

    public UIElement setPaddingTop(AbstractPadding top){
        return setPadding(top, paddingRight, paddingBottom, paddingLeft);
    }
    public UIElement setPaddingRight(AbstractPadding right){
        return setPadding(paddingTop, right, paddingBottom, paddingLeft);
    }
    public UIElement setPaddingBottom(AbstractPadding bottom){
        return setPadding(paddingTop, paddingRight, bottom, paddingLeft);
    }
    public UIElement setPaddingLeft(AbstractPadding left){
        return setPadding(paddingTop, paddingRight, paddingBottom, left);
    }

    public int getPaddingTop(){
        return paddingTop.getVertical(this);
    }
    public int getPaddingRight(){
        return paddingRight.getHorizontal(this);
    }
    public int getPaddingBottom(){
        return paddingBottom.getVertical(this);
    }
    public int getPaddingLeft(){
        return paddingLeft.getHorizontal(this);
    }

    //endregion Padding

    //region Alignment

    public UIElement setHorizontalAlignment(Alignment.HorizontalAlignment horizontalAlignment){
        setAlignment(horizontalAlignment, alignment.verticalAlignment);
        return this;
    }
    public UIElement setVerticalAlignment(Alignment.VerticalAlignment verticalAlignment){
        setAlignment(alignment.horizontalAlignment, verticalAlignment);
        return this;
    }
    public UIElement setAlignment(Alignment.HorizontalAlignment horizontalAlignment, Alignment.VerticalAlignment verticalAlignment){
        alignment.horizontalAlignment = horizontalAlignment;
        alignment.verticalAlignment = verticalAlignment;
        return this;
    }

    public Alignment.HorizontalAlignment getHorizontalAlignment(){
        return alignment.horizontalAlignment;
    }
    public Alignment.VerticalAlignment getVerticalAlignment(){
        return alignment.verticalAlignment;
    }
    public Alignment getAlignment(){
        return alignment;
    }

    //endregion Alignment

    //region Components

    public <T extends UIElementComponent> T addComponent(T component){
        components.add(component);
        component.onRegisterComponent(this);
        return component;
    }

    public void removeComponent(UIElementComponent component){
        components.remove(component);
        component.onUnregisterComponent(this);
    }

    public <T extends UIElementComponent> T getComponent(Class<T> componentClass){
        for(UIElementComponent component : components){
            if(componentClass.isInstance(component)){
                return (T) component;
            }
        }
        return null;
    }

    public boolean hasComponent(Class<? extends UIElementComponent> componentClass){
        return getComponent(componentClass) != null;
    }

    //endregion Components

    //region Hb

    public Hitbox getHitbox(){
        return hb;
    }

    //endregion Hb

    //region Hover

    protected void onHovered(){
        totalHoverDuration = 0.f;

        if(getOnHoverLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnHoverLine(), true);
            }
        }

        for(Map.Entry<UUID, Runnable> entry : onHoveredEvents.entrySet()) entry.getValue().run();

        if(!isPassthrough()) InputHelperHoverConsumer.alreadyHovered = true;
    }
    protected void onHoverTick(float totalTickDuration){
        for(Map.Entry<UUID, Consumer<Float>> entry : onHoverTickEvents.entrySet()) entry.getValue().accept(totalTickDuration);

        if(!isPassthrough()) InputHelperHoverConsumer.alreadyHovered = true;
    }
    protected void onUnhovered(){
        totalHoverDuration = 0.f;

        for(Map.Entry<UUID, Runnable> entry : onUnHoveredEvents.entrySet()) entry.getValue().run();
    }

    public boolean isHovered(){ return (hb.hovered || hb.justHovered); }

    public UUID addOnHoveredEvent(Runnable event){
        UUID id = UUID.randomUUID();
        onHoveredEvents.put(id, event);
        return id;
    }
    public void removeOnHoveredEvent(UUID id){
        onHoveredEvents.remove(id);
    }

    public UUID addOnHoverTickEvent(Consumer<Float> event){
        UUID id = UUID.randomUUID();
        onHoverTickEvents.put(id, event);
        return id;
    }
    public void removeOnHoverTickEvent(UUID id){
        onHoverTickEvents.remove(id);
    }

    public UUID addOnUnHoveredEvent(Runnable event){
        UUID id = UUID.randomUUID();
        onUnHoveredEvents.put(id, event);
        return id;
    }
    public void removeOnUnHoveredEvent(UUID id){
        onUnHoveredEvents.remove(id);
    }

    public void setOnHoverLine(String newLine){
        this.onHoverLine = newLine;
    }
    public String getOnHoverLine(){
        return onHoverLine;
    }

    //endregion

    //region Clickthrough

    public void setPassthrough(boolean newValue){
        isPassthrough = newValue;
    }

    public boolean isPassthrough(){
        return isPassthrough || (!isVisible() && !isEnabled());
    }

    //endregion

    //region Left Click

    public void trigger(){ clickLeft(); }

    public void clickLeft(){
        onLeftClick();
    }

    protected void onLeftClick(){
        GlobalEvents.sendMessage(new GlobalEvents.Events.PreLeftClickEvent(this));

        totalLeftClickDuration = 0.f;
        holdingLeft = true;

        if(getOnTriggerLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggerLine(), true);
            }
        }

        select();

        for (Map.Entry<UUID, Runnable> event : onLeftClickEvents.entrySet()) event.getValue().run();
    }
    protected void onLeftClickHeld(float totalDuration){
        for(Map.Entry<UUID, Consumer<Float>> consumer : onLeftClickHeldEvents.entrySet()) consumer.getValue().accept(totalDuration);
    }
    protected void onLeftClickRelease(){
        holdingLeft = false;

        for(Map.Entry<UUID, Runnable> consumer : onLeftClickReleaseEvents.entrySet()) consumer.getValue().run();
    }

    public UUID addOnLeftClickEvent(Runnable consumer){
        UUID newId = UUID.randomUUID();
        onLeftClickEvents.put(newId, consumer);
        return newId;
    }
    public UUID addOnLeftClickHeldEvent(Consumer<Float> consumer){
        UUID newId = UUID.randomUUID();
        onLeftClickHeldEvents.put(newId, consumer);
        return newId;
    }
    public UUID addOnLeftClickReleaseEvent(Runnable consumer){
        UUID newId = UUID.randomUUID();
        onLeftClickReleaseEvents.put(newId, consumer);
        return newId;
    }

    public void removeOnLeftClickEvent(UUID id){
        onLeftClickEvents.remove(id);
    }
    public void removeOnLeftClickHeldEvent(UUID id){
        onLeftClickHeldEvents.remove(id);
    }
    public void removeOnLeftClickReleaseEvent(UUID id){
        onLeftClickReleaseEvents.remove(id);
    }

    //endregion

    //region Right Click

    public void clickRight(){
        onRightClick();
    }

    protected void onRightClick(){
        totalRightClickDuration = 0.f;
        holdingRight = true;

        if(getOnTriggerLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggerLine(), true);
            }
        }

        for(Map.Entry<UUID, Runnable> consumer : onRightClickEvents.entrySet()) consumer.getValue().run();
    }
    protected void onRightClickHeld(float totalDuration){
        for(Map.Entry<UUID, Consumer<Float>> consumer : onRightClickHeldEvents.entrySet()) consumer.getValue().accept(totalDuration);
    }
    protected void onRightButtonRelease(){
        holdingRight = false;

        for(Map.Entry<UUID, Runnable> consumer : onRightClickReleaseEvents.entrySet()) consumer.getValue().run();
    }

    public UUID addOnRightClickEvent(Runnable consumer){
        UUID newId = UUID.randomUUID();
        onRightClickEvents.put(newId, consumer);
        return newId;
    }
    public UUID addOnRightClickHeldEvent(Consumer<Float> consumer){
        UUID newId = UUID.randomUUID();
        onRightClickHeldEvents.put(newId, consumer);
        return newId;
    }
    public UUID addOnRightClickReleaseEvent(Runnable consumer){
        UUID newId = UUID.randomUUID();
        onRightClickReleaseEvents.put(newId, consumer);
        return newId;
    }

    public void removeOnRightClickEvent(UUID id){
        onRightClickEvents.remove(id);
    }
    public void removeOnRightClickHeldEvent(UUID id){
        onRightClickHeldEvents.remove(id);
    }
    public void removeOnRightClickReleaseEvent(UUID id){
        onRightClickReleaseEvents.remove(id);
    }

    //endregion

    //region Trigger Lines

    public void setOnSelectLine(String newLine){
        this.onSelectLine = newLine;
    }
    public String getOnSelectLine(){
        return onSelectLine;
    }

    public void setOnTriggerLine(String newLine) {
        this.onTriggeredLine = newLine;
    }
    public String getOnTriggerLine(){
        return onTriggeredLine;
    }

    //endregion

    //region Local Child Offset

    public void setLocalChildOffsetX(int offset){
        localChildOffsetX = offset;

        for(UIElementChild child : children){
            child.element.onPositionChanged();
        }
    }
    public void setLocalChildOffsetY(int offset){
        localChildOffsetY = offset;

        for(UIElementChild child : children){
            child.element.onPositionChanged();
        }
    }

    public int getLocalChildOffsetX(){
        return localChildOffsetX;
    }
    public int getLocalChildOffsetY(){
        return localChildOffsetY;
    }

    public final int getLocalChildOffsetXRaw(){
        return localChildOffsetX;
    }
    public final int getLocalChildOffsetYRaw(){
        return localChildOffsetY;
    }

    public int getTotalLocalChildOffsetX(){
        return localChildOffsetX + (hasParent() ? parent.getTotalLocalChildOffsetX() : 0);
    }

    public int getTotalLocalChildOffsetY(){
        return localChildOffsetY + (hasParent() ? parent.getTotalLocalChildOffsetY() : 0);
    }

    //endregion

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

        public StringProperty id = new StringProperty(getClass().getSimpleName() + "_" + UUID.randomUUID()){
            @Override
            public boolean isValidValue(String value) {
                return !value.isEmpty();
            }
        }
                .setName("Id")
                .setDescription("Internal ID of the element. Has to be unique.")
                .setCategory("General");

        public IntegerVector2Property localPosition = new IntegerVector2Property(new IntegerVector2(0, 0))
                .setName("Local Position")
                .setDescription("Local Position of the element relative to its parent. Can be:\n" +
                        "* Static: Fixed position in pixels.\n" +
                        "* Percentage: Position relative to the parent's dimensions.")
                .setCategory("Transform")
                .setValueNames("X", "Y");

        public IntegerVector2Property dimensions = new IntegerVector2Property(new IntegerVector2(1, 1)){
            @Override
            public void onValueChanged(IntegerVector2 oldValue, IntegerVector2 newValue) {
                super.onValueChanged(oldValue, newValue);

                for(UIElementData subElement : Reflection.getFieldValuesByClass(UIElementData.class, getSelf())){
                    subElement.dimensions.setValue(newValue.copy());
                };
            }
        }.setValueNames("W", "H"); //TODO make this dimensions

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

        public MethodBinding onHovered = new NoneMethodBinding();
        public MethodBinding onHoverTick = new NoneMethodBinding();
        public MethodBinding onUnhovered = new NoneMethodBinding();

        public boolean isPassthrough = false;

        public UIElement makeUIElement(){
            return new UIElement(this);
        }

        public ArrayList<TProperty<?, ?>> getEditableProperties(){
            ArrayList<TProperty<?, ?>> properties = new ArrayList<>();

            for(TProperty<?, ?> property : Reflection.getFieldValuesByClass(TProperty.class, this)){
                properties.add(property);
            }

            for(UIElementData subElement : Reflection.getFieldValuesByClass(UIElementData.class, this)){
                ArrayList<TProperty<?, ?>> subProperties = subElement.getEditableProperties();
                subElement.filterInnerProperties(subProperties);
                properties.addAll(subProperties);
            }

            return properties;
        }

        public void filterInnerProperties(ArrayList<TProperty<?, ?>> properties){
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
