package dLib.ui.elements;

import basemod.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import dLib.modcompat.ModManager;
import dLib.modcompat.saythespire.SayTheSpireIntegration;
import dLib.patches.InputHelperHoverConsumer;
import dLib.properties.objects.*;
import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.IEditableValue;
import dLib.tools.uicreator.ui.properties.editors.UCRelativeUIElementBindingValueEditor;
import dLib.ui.Alignment;
import dLib.ui.animations.UIAnimation;
import dLib.ui.animations.exit.UIExitAnimation;
import dLib.ui.bindings.RelativeUIElementBinding;
import dLib.ui.elements.components.UIDebuggableComponent;
import dLib.ui.elements.components.UIElementComponent;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.screens.UIManager;
import dLib.util.*;
import dLib.util.events.Event;
import dLib.util.events.GlobalEvents;
import dLib.util.ui.bounds.AbstractBounds;
import dLib.util.ui.bounds.Bound;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.AbstractStaticDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.PixelDimension;
import dLib.util.ui.events.PreUIHoverEvent;
import dLib.util.ui.events.PreUILeftClickEvent;
import dLib.util.ui.events.PreUIUnhoverEvent;
import dLib.util.ui.padding.AbstractPadding;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.AbstractStaticPosition;
import dLib.util.ui.position.Pos;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UIElement implements Disposable, IEditableValue {
    //region Variables

    protected String ID;

    protected UIElement parent;
    protected List<UIElementChild> children = new ArrayList<>();

    protected Hitbox hb = new Hitbox(0, 0, 1, 1);

    private AbstractPosition localPosX = Pos.px(0);
    private AbstractPosition localPosY = Pos.px(0);
    private Integer localPosXCache = null;
    protected Integer localPosYCache = null;
    private Integer worldPosXCache = null;
    private Integer worldPosYCache = null;
    public Event<Consumer<UIElement>> onPositionChangedEvent = new Event<>();

    private int localChildOffsetX = 0;
    private int localChildOffsetY = 0;

    private Alignment alignment = new Alignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);

    private AbstractDimension width = Dim.fill();
    private AbstractDimension height = Dim.fill();
    private Integer widthCache = null;
    private Integer heightCache = null;
    private AbstractBounds containerBounds = null;
    private BoundCalculationType containerBoundCalculationType = BoundCalculationType.CONTAINS;
    public Event<Consumer<UIElement>> onDimensionsChangedEvent = new Event<>();

    private float xScale = 1f;
    private float yScale = 1f;
    public Event<Consumer<UIElement>> onScaleChangedEvent = new Event<>();

    private AbstractPadding paddingLeft = Padd.px(0);
    private AbstractPadding paddingBottom = Padd.px(0);
    private AbstractPadding paddingRight = Padd.px(0);
    private AbstractPadding paddingTop = Padd.px(0);

    private UIElement elementMask = null;

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    private boolean pendingHide = false;

    private Color darkenedColor = Color.BLACK;
    private float darkenedColorMultiplier = 0.4f;
    protected boolean isDarkened = false;

    private boolean selected;
    private ArrayList<Consumer<Boolean>> onSelectionStateChangedConsumers = new ArrayList<>();

    private boolean isPassthrough = true;

    //region Events

    public Event<Runnable> preUpdateEvent = new Event<>();
    public Event<Runnable> postUpdateEvent = new Event<>();
    public Event<Consumer<SpriteBatch>> preRenderEvent = new Event<>();
    public Event<Consumer<SpriteBatch>> postRenderEvent = new Event<>();

    public Event<Runnable> onHoveredEvent = new Event<>();
    public Event<Consumer<Float>> onHoverTickEvent = new Event<>();
    public Event<Runnable> onUnhoveredEvent = new Event<>();

    public Event<Consumer<UIElement>> onHoveredChildEvent = new Event<>();
    public Event<BiConsumer<UIElement, Float>> onHoverTickChildEvent = new Event<>();
    public Event<Consumer<UIElement>> onUnhoveredChildEvent = new Event<>();

    public Event<Runnable> onLeftClickEvent = new Event<>();
    public Event<Consumer<Float>> onLeftClickHeldEvent = new Event<>();
    public Event<Runnable> onLeftClickReleaseEvent = new Event<>();

    public Event<Runnable> onRightClickEvent = new Event<>();
    public Event<Consumer<Float>> onRightClickHeldEvent = new Event<>();
    public Event<Runnable> onRightClickReleaseEvent = new Event<>();

    //endregion Events

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
    private UIExitAnimation exitAnimation;
    private UIAnimation animation;

    private UIAnimation playingAnimation;

    private UIStrings stringTable = null;

    private boolean isContextual = false;
    private boolean isModal = false;

    private boolean drawFocusOnOpen = false;

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

        commonInitialize();
    }

    public UIElement(UIElementData data){
        setID(data.id.getValue());

        setLocalPosition(data.localPositionX.getValue(), data.localPositionY.getValue());
        
        width = data.width.getValue();
        height = data.height.getValue();

        setAlignment(data.alignment.getHorizontalAlignment(), data.alignment.getVerticalAlignment());

        if(!data.isVisible.getValue() && !data.isEnabled.getValue()){
            hideAndDisableInstantly();
        }
        else if(!data.isVisible.getValue()){
            hideInstantly();
        }
        else if(!data.isEnabled.getValue()){
            disable();
        }

        this.isPassthrough = data.isPassthrough.getValue();

        onHoveredEvent.subscribeManaged(() -> data.onHovered.getValue().executeBinding(getTopParent())); //* TODO replace with first non-native parent when nativity is added
        onHoverTickEvent.subscribeManaged((time) -> data.onHoverTick.getValue().executeBinding(getTopParent(), time)); //* TODO replace with first non-native parent when nativity is added
        onUnhoveredEvent.subscribeManaged(() -> data.onUnhovered.getValue().executeBinding(getTopParent())); //* TODO replace with first non-native parent when nativity is added

        onLeftClickEvent.subscribeManaged(() -> data.onLeftClick.getValue().executeBinding(getTopParent())); //* TODO replace with first non-native parent when nativity is added
        onLeftClickHeldEvent.subscribeManaged((time) -> data.onLeftClickHeld.getValue().executeBinding(getTopParent(), time)); //* TODO replace with first non-native parent when nativity is added
        onLeftClickReleaseEvent.subscribeManaged(() -> data.onLeftClickRelease.getValue().executeBinding(getTopParent())); //* TODO replace with first non-native parent when nativity is added

        onRightClickEvent.subscribeManaged(() -> data.onRightClick.getValue().executeBinding(getTopParent())); //* TODO replace with first non-native parent when nativity is added
        onRightClickHeldEvent.subscribeManaged((time) -> data.onRightClickHeld.getValue().executeBinding(getTopParent(), time)); //* TODO replace with first non-native parent when nativity is added
        onRightClickReleaseEvent.subscribeManaged(() -> data.onRightClickRelease.getValue().executeBinding(getTopParent())); //* TODO replace with first non-native parent when nativity is added

        commonInitialize();
    }

    private void commonInitialize(){
        registerCommonEvents();

        GlobalEvents.subscribeManaged(PreUILeftClickEvent.class, (event) -> {
            if(event.source != this && isSelected()){
                deselect();
            }

            if(this.isContextual() && event.source != this && !event.source.isDescendantOf(this)){
                dispose();
            }
        });

        /*GlobalEvents.subscribeManaged(GlobalEvents.Events.PreForceFocusChangeEvent.class, (event) -> {
            if(event.source != this && isSelected()){
                deselect();
            }
        });*/ //TODO

        GlobalEvents.subscribeManaged(PreUIHoverEvent.class, (event) -> {
            if(event.source != this && isHovered() && !event.source.isPassthrough()){
                this.hb.unhover();
                onUnhovered();
            }
        });

        addComponent(new UIDebuggableComponent());
    }

    private void registerCommonEvents(){
        //Region Hover
        {
            this.onHoveredEvent.subscribeManaged(() -> {
                if(hasParent()){
                    getParent().onHoveredChildEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(this));
                }

                if(getOnHoverLine() != null){
                    if(ModManager.SayTheSpire.isActive()){
                        SayTheSpireIntegration.Output(getOnHoverLine());
                    }
                }
            });
            this.onHoveredChildEvent.subscribeManaged(child -> {
                if(hasParent()){
                    getParent().onHoveredChildEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(child));
                }
            });

            this.onHoverTickEvent.subscribeManaged((time) -> {
                if(hasParent()){
                    getParent().onHoverTickChildEvent.invoke(uiElementFloatBiConsumer -> uiElementFloatBiConsumer.accept(UIElement.this, time));
                }
            });
            this.onHoveredChildEvent.subscribeManaged(child -> {
                if(hasParent()){
                    getParent().onHoverTickChildEvent.invoke(uiElementFloatBiConsumer -> uiElementFloatBiConsumer.accept(child, totalHoverDuration));
                }
            });

            this.onUnhoveredEvent.subscribeManaged(() -> {
                if(hasParent()){
                    getParent().onUnhoveredChildEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(this));
                }
            });
            this.onUnhoveredChildEvent.subscribeManaged(child -> {
                if(hasParent()){
                    getParent().onUnhoveredChildEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(child));
                }
            });
        }

        //Scale
        {
            this.onScaleChangedEvent.subscribeManaged(uiElementConsumer -> {
                uiElementConsumer.invalidateCachesForElementTree();
            });
        }
    }

    //endregion

    //region Destructors

    @Override
    public void dispose(){
        if(hasParent()){
            parent.removeChild(this);
        }
        else{
            close();
        }

        for (int i = 0; i < children.size(); i++) {
            UIElementChild child = children.get(i);
            child.element.dispose();
        }

        delayedActions.clear();
    }

    //endregion

    //region Methods

    //region Update & Render
    public final void update(){
        if(!shouldUpdate()) return;

        updateChildren();

        preUpdateEvent.invoke(Runnable::run);
        updateSelf();
        postUpdateEvent.invoke(Runnable::run);

        ensureElementWithinBounds();
    }
    protected void updateSelf(){
        //Update Visibility
        {
            if(pendingHide){
                boolean canHide = true;
                for(UIElement child : getChildren()){
                    if(child.isVisible() && child.playingAnimation instanceof UIExitAnimation){
                        canHide = false;
                        break;
                    }
                }
                if(canHide){
                    pendingHide = false;
                    setVisibility(false);
                }
            }
        }

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

            PositionBounds maskBounds = getMaskWorldBounds();
            if(maskBounds != null){
                PositionBounds myBounds = getWorldBounds();
                if(myBounds.overlaps(maskBounds)){
                    if(!myBounds.within(maskBounds)){
                        myBounds.clip(maskBounds);

                        targetHbWidth = myBounds.right - myBounds.left;
                        targetHbHeight = myBounds.top - myBounds.bottom;

                        targetHbX = myBounds.left + targetHbWidth * 0.5f;
                        targetHbY = myBounds.bottom + targetHbHeight * 0.5f;

                        targetHbWidth *= Settings.xScale;
                        targetHbHeight *= Settings.yScale;

                        targetHbX *= Settings.xScale;
                        targetHbY *= Settings.yScale;
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
                dispose();
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
        PositionBounds maskBounds = getMaskWorldBounds();
        if(maskBounds != null){
            sb.flush();

            Rectangle scissors = new Rectangle();
            Rectangle mask = new Rectangle(maskBounds.left * Settings.xScale, maskBounds.bottom * Settings.yScale, (maskBounds.right - maskBounds.left) * Settings.xScale, (maskBounds.top - maskBounds.bottom) * Settings.yScale);

            OrthographicCamera camera = Reflection.getFieldValue("camera", Gdx.app.getApplicationListener());
            ScissorStack.calculateScissors(camera, sb.getTransformMatrix(), mask, scissors);
            pushedScissors = ScissorStack.pushScissors(scissors);
        }

        preRenderEvent.invoke(spriteBatchConsumer -> spriteBatchConsumer.accept(sb));
        renderSelf(sb);
        postRenderEvent.invoke(spriteBatchConsumer -> spriteBatchConsumer.accept(sb));

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
                UIElement oldElement = child.element;
                oldElement.setParent(null);

                child.element = replacement;
                replacement.setParent(this);

                oldElement.dispose();
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

    protected void onChildrenChanged(){
        invalidateCachesForElementTree();
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

    public boolean isDescendantOf(UIElement parent){
        if(this.parent == null) return false;
        if(this.parent == parent) return true;
        return this.parent.isDescendantOf(parent);
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

        return this;
    }

    public int getLocalPositionX(){
        if(localPosXCache == null){
            localPosXCache = localPosX.getLocalX(this) + paddingLeft.getHorizontal(this);
            if(localPosX instanceof AbstractStaticPosition){
                localPosXCache = Math.round(localPosXCache / getParentScaleX());
            }
        }

        return localPosXCache;
    }
    public int getLocalPositionY(){
        if(localPosYCache == null){
            localPosYCache = localPosY.getLocalY(this) + paddingBottom.getVertical(this);
            if(localPosY instanceof AbstractStaticPosition){
                localPosYCache = Math.round(localPosYCache / getParentScaleY());
            }
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

    public Integer getLocalPositionXCache(){
        return localPosXCache;
    }
    public Integer getLocalPositionYCache(){
        return localPosYCache;
    }

    public UIElement setLocalPositionCenteredX(int newPos){
        return setLocalPositionX(newPos - (int)(getWidth() * 0.5f));
    }
    public UIElement setLocalPositionCenteredY(int newPos){
        return setLocalPositionY(newPos - (int)(getHeight() * 0.5f));
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
        AbstractPosition xCopy = getLocalPositionXRaw().cpy();
        AbstractPosition yCopy = getLocalPositionYRaw().cpy();

        xCopy.offsetHorizontal(this, xOffset);
        yCopy.offsetVertical(this, yOffset);

        setLocalPosition(xCopy, yCopy);

        return this;
    }
    //endregion

    //region Transforming

    public IntegerVector2 worldToLocal(IntegerVector2 worldPosition){
        return new IntegerVector2(worldPosition.x - getWorldPositionX(), worldPosition.y - getWorldPositionY());
    }
    public IntegerVector2 localToWorld(IntegerVector2 localPosition){
        return new IntegerVector2(localPosition.x + getWorldPositionX(), localPosition.y + getWorldPositionY());
    }


    //endregion

    public void onPositionChanged(){
        invalidateCachesForElementTree();

        onPositionChangedEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(UIElement.this));

        for(UIElementChild child : children){
            child.element.onParentPositionChanged();
        }
    }

    protected void onParentPositionChanged(){
    }

    //endregion

    //region Bounds

    public void setContainerBounds(AbstractBounds bounds){
        containerBounds = bounds;
    }

    public AbstractBounds getContainerBounds(){
        if(containerBounds != null){
            return containerBounds;
        }

        if(hasParent()){
            return parent.getContainerBounds();
        }

        return null;
    }

    public PositionBounds getLocalContainerBounds(){
        AbstractBounds containerBounds = getContainerBounds();
        if(containerBounds == null) return null;

        Integer worldLeft = containerBounds.getWorldLeft(this);
        Integer worldRight = containerBounds.getWorldRight(this);
        Integer worldTop = containerBounds.getWorldTop(this);
        Integer worldBottom = containerBounds.getWorldBottom(this);

        IntegerVector2 localBottomLeft = worldToLocal(new IntegerVector2(worldLeft, worldBottom));
        IntegerVector2 localTopRight = worldToLocal(new IntegerVector2(worldRight, worldTop));

        int horizontalOffset = 0;
        int verticalOffset = 0;

        if(containerBoundCalculationType == BoundCalculationType.CONTAINS_HALF){
            horizontalOffset = (int) (getWidth() * 0.5f);
            verticalOffset = (int) (getHeight() * 0.5f);
        }
        else if(containerBoundCalculationType == BoundCalculationType.OVERLAPS){
            horizontalOffset = getWidth();
            verticalOffset = getHeight();
        }

        return Bound.pos(localBottomLeft.x - horizontalOffset, localBottomLeft.y - verticalOffset, localTopRight.x + horizontalOffset, localTopRight.y + verticalOffset);
    }

    public void setContainerBoundCalculationType(BoundCalculationType type){
        containerBoundCalculationType = type;
    }

    //endregion

    private void ensureElementWithinBounds(){
        PositionBounds localContainerBounds = getLocalContainerBounds();
        if(localContainerBounds == null) return;

        Integer desiredWidth = null;
        Integer desiredHeight = null;

        int desiredPositionX = getLocalPositionX();
        int desiredPositionY = getLocalPositionY();

        if(width instanceof PixelDimension){
            int boundBoxUpperPosX = desiredPositionX + getWidth();
            
            if(localContainerBounds.right != null && boundBoxUpperPosX > localContainerBounds.right){
                desiredPositionX = localContainerBounds.right - getWidth();
                boundBoxUpperPosX = desiredPositionX + getWidth();
            }

            if(localContainerBounds.left != null && desiredPositionX < localContainerBounds.left){
                desiredPositionX = localContainerBounds.left;
                boundBoxUpperPosX = desiredPositionX + getWidth();
            }

            desiredWidth = getWidth();

            if(localContainerBounds.right != null && localContainerBounds.left != null && boundBoxUpperPosX > localContainerBounds.right){
                desiredWidth = localContainerBounds.right - localContainerBounds.left;
            }

        }

        if(height instanceof PixelDimension){
            int boundBoxUpperPosY = desiredPositionY + getHeight();

            if(localContainerBounds.top != null && boundBoxUpperPosY > localContainerBounds.top){
                desiredPositionY = localContainerBounds.top - getHeight();
                boundBoxUpperPosY = desiredPositionY + getHeight();
            }

            if(localContainerBounds.bottom != null && desiredPositionY < localContainerBounds.bottom){
                desiredPositionY = localContainerBounds.bottom;
                boundBoxUpperPosY = desiredPositionY + getHeight();
            }

            desiredHeight = getHeight();

            if(localContainerBounds.top != null && localContainerBounds.bottom != null && boundBoxUpperPosY > localContainerBounds.top){
                desiredHeight = localContainerBounds.top - localContainerBounds.bottom;
            }
        }

        if(desiredWidth == null) desiredWidth = getWidth();
        if(desiredHeight == null) desiredHeight = getHeight();

        if(desiredWidth != getWidth() || desiredHeight != getHeight()){
            resizeBy(desiredWidth - getWidth(), desiredHeight - getHeight());
        }
        else if(desiredPositionX != getLocalPositionX() || desiredPositionY != getLocalPositionY()){
            offset(desiredPositionX - getLocalPositionX(), desiredPositionY - getLocalPositionY());
        }
    }

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

        if(playingAnimation == null){
            widthCache = null;
            heightCache = null;
        }
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
            pendingHide = true;
            for(UIElement child : getChildren()){
                if(child.isVisible() && child.exitAnimation != null){
                    child.playAnimation(child.exitAnimation);
                }
            }
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
        pendingHide = false;
        playAnimation(entryAnimation);

        for(UIElement child : getChildren()){
            if(child.isVisible() && child.entryAnimation != null){
                child.playAnimation(child.entryAnimation);
            }
        }
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

        onDimensionsChangedEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(UIElement.this));
    }

    public void onParentDimensionsChanged(){
    }

    public int getWidth(){
        if(widthCache == null || widthCache <= 0){
            widthCache = width.getWidth(this) - getPaddingRight();
            if(width instanceof AbstractStaticDimension){
                widthCache = (int) (widthCache * getScaleX());
            }
        }

        return widthCache;
    }
    public int getHeight(){
        if(heightCache == null || heightCache <= 0){
            heightCache = height.getHeight(this) - getPaddingTop();
            if(height instanceof AbstractStaticDimension){
                heightCache = (int) (heightCache * getScaleY());
            }
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

    public Integer getWidthCache(){
        return widthCache;
    }
    public Integer getHeightCache(){
        return heightCache;
    }

    public void resizeBy(int widthDiff, int heightDiff){
        AbstractDimension widthCopy = width.cpy();
        AbstractDimension heightCopy = height.cpy();

        widthCopy.resizeWidthBy(this, widthDiff);
        heightCopy.resizeHeightBy(this, heightDiff);

        setDimensions(widthCopy, heightCopy);
    }

    //endregion

    //region Animations

    public void setEntryAnimation(UIAnimation entryAnimation){
        this.entryAnimation = entryAnimation;
    }

    public void setExitAnimation(UIExitAnimation exitAnimation){
        this.exitAnimation = exitAnimation;
    }

    public void setAnimation(UIAnimation animation){
        this.animation = animation;
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

    //region Bounds Methods

    public PositionBounds getWorldBounds(){
        return new PositionBounds(getWorldPositionX(), getWorldPositionY(), getWorldPositionX() + getWidth(), getWorldPositionY() + getHeight());
    }
    public PositionBounds getLocalBounds(){
        return new PositionBounds(getLocalPositionX(), getLocalPositionY(), getLocalPositionX() + getWidth(), getLocalPositionY() + getHeight());
    }

    public boolean overlapsParent(){
        return parent != null && overlaps(parent);
    }
    public boolean overlaps(UIElement other){
        return getWorldBounds().overlaps(other.getWorldBounds());
    }

    public PositionBounds getFullChildLocalBounds(){
        PositionBounds fullChildBounds = null;
        for(UIElementChild child : children){
            if(!(child.element.isActive())){
                continue;
            }

            PositionBounds childBounds = child.element.getFullLocalBounds();
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
    public PositionBounds getFullLocalBounds(){
        PositionBounds myBounds = getLocalBounds();

        PositionBounds fullChildBounds = getFullChildLocalBounds();

        if(fullChildBounds != null){
            if(fullChildBounds.left < 0) myBounds.left += fullChildBounds.left;
            if(fullChildBounds.right > myBounds.right - myBounds.left) myBounds.right += fullChildBounds.right - myBounds.right;
            if(fullChildBounds.bottom < 0) myBounds.bottom += fullChildBounds.bottom;
            if(fullChildBounds.top > myBounds.top - myBounds.bottom) myBounds.top += fullChildBounds.top - myBounds.top;
        }

        return myBounds;
    }

    public boolean withinParent(){
        return parent != null && within(parent);
    }
    public boolean within(UIElement other){
        return getWorldBounds().within(other.getWorldBounds());
    }

    public boolean hasHorizontalChildrenOOB(){
        Pair<Integer, Integer> OOBAmount = getHorizontalChildrenOOBAmount();
        return OOBAmount.getKey() > 0 || OOBAmount.getValue() > 0;
    }
    public Pair<Integer, Integer> getHorizontalChildrenOOBAmount(){
        PositionBounds myBounds = getLocalBounds();
        PositionBounds fullChildBounds = getFullChildLocalBounds();

        if (fullChildBounds == null) {
            return new Pair<>(0, 0);
        }

        int leftOOBAmount = 0;
        int rightOOBAmount = 0;

        if(fullChildBounds.left < 0) {
            leftOOBAmount = -fullChildBounds.left;
        }
        if(fullChildBounds.right > myBounds.right){
            rightOOBAmount = fullChildBounds.right - myBounds.right;
        }

        return new Pair<>(leftOOBAmount, rightOOBAmount);
    }

    public boolean hasVerticalChildrenOOB(){
        Pair<Integer, Integer> OOBAmount = getVerticalChildrenOOBAmount();
        return OOBAmount.getKey() > 0 || OOBAmount.getValue() > 0;
    }
    public Pair<Integer, Integer> getVerticalChildrenOOBAmount(){
        PositionBounds myBounds = getLocalBounds();
        PositionBounds fullChildBounds = getFullChildLocalBounds();

        if (fullChildBounds == null) {
            return new Pair<>(0, 0);
        }

        int bottomOOBAmount = 0;
        int topOOBAmount = 0;

        if(fullChildBounds.bottom < 0) {
            bottomOOBAmount = -fullChildBounds.bottom;
        }
        if(fullChildBounds.top > myBounds.top){
            topOOBAmount = fullChildBounds.top - myBounds.top;
        }

        return new Pair<>(bottomOOBAmount, topOOBAmount);
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
    public PositionBounds getMaskWorldBounds(){
        PositionBounds currentBounds = null;

        UIElement current = this;
        if(current.elementMask != null){
            currentBounds = current.elementMask.getWorldBounds();
        }

        while(current.hasParent()){
            current = current.getParent();
            if(current.elementMask != null){
                if(currentBounds == null){
                    currentBounds = current.elementMask.getWorldBounds();
                }
                else{
                    currentBounds.clip(current.elementMask.getWorldBounds());
                }
            }
        }

        return currentBounds;
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
        boolean shouldAnimate = isVisible();
        if(shouldAnimate){
            hideAndDisableInstantly();
        }
        UIManager.openUIElement(this);
        if(shouldAnimate){
            showAndEnable();
        }
    }

    public void close(){
        UIManager.closeUIElement(this);
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
        if(!isPassthrough()) InputHelperHoverConsumer.alreadyHovered = true;

        GlobalEvents.sendMessage(new PreUIHoverEvent(this));
        onHoveredEvent.invoke(uiElementConsumer -> uiElementConsumer.run());
    }
    protected void onHoverTick(float totalTickDuration){
        if(!isPassthrough()) InputHelperHoverConsumer.alreadyHovered = true;

        onHoverTickEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(totalTickDuration));
    }
    protected void onUnhovered(){
        totalHoverDuration = 0.f;

        GlobalEvents.sendMessage(new PreUIUnhoverEvent(this));
        onUnhoveredEvent.invoke(uiElementConsumer -> uiElementConsumer.run());
    }

    public boolean isHovered(){ return (hb.hovered || hb.justHovered); }

    public boolean isHoveredOrChildHovered(){
        if(isHovered()) return true;
        for(UIElementChild child : children){
            if(child.element.isHoveredOrChildHovered()) return true;
        }
        return false;
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
        GlobalEvents.sendMessage(new PreUILeftClickEvent(this));

        totalLeftClickDuration = 0.f;
        holdingLeft = true;

        if(getOnTriggerLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                SayTheSpireIntegration.Output(getOnTriggerLine());
            }
        }

        select();

        onLeftClickEvent.invoke(uiElementConsumer -> uiElementConsumer.run());
    }
    protected void onLeftClickHeld(float totalDuration){
        onLeftClickHeldEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(totalDuration));
    }
    protected void onLeftClickRelease(){
        holdingLeft = false;

        onLeftClickReleaseEvent.invoke(uiElementConsumer -> uiElementConsumer.run());
    }

    public boolean isHeld(){
        return holdingLeft;
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
                SayTheSpireIntegration.Output(getOnTriggerLine());
            }
        }

        onRightClickEvent.invoke(uiElementConsumer -> uiElementConsumer.run());
    }
    protected void onRightClickHeld(float totalDuration){
        onRightClickHeldEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(totalDuration));
    }
    protected void onRightButtonRelease(){
        holdingRight = false;

        onRightClickReleaseEvent.invoke(uiElementConsumer -> uiElementConsumer.run());
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

    //region Contextuality & Modality

    public void setContextual(boolean contextual){
        this.isContextual = contextual;

        if(isContextual){
            this.isModal = false;
        }
    }
    public boolean isContextual(){
        return isContextual;
    }

    public void setModal(boolean modal){
        this.isModal = modal;

        if(isModal){
            this.isContextual = false;
        }
    }
    public boolean isModal(){
        return isModal;
    }

    //endregion

    //region Focus Drawing

    public void setDrawFocusOnOpen(boolean drawControllerFocusOnOpen){
        this.drawFocusOnOpen = drawControllerFocusOnOpen;
    }
    public boolean shouldDrawFocusOnOpen(){
        return drawFocusOnOpen;
    }

    public void focus(){
        //TODO
    }

    //endregion

    //region Path

    public String getRelativePath(){
        if(!hasParent()) return getId();
        return parent.getRelativePath() + "." + getId();
    }

    public UIElement findChildFromPath(String path){
        if(path.isEmpty()) return null;

        path = path.replace(getRelativePath() + ".", "");

        String[] pathParts = path.split("\\.");
        UIElement currentElement = this;

        for(String pathPart : pathParts){
            currentElement = currentElement.findChildById(pathPart);
            if(currentElement == null) return null;
        }

        return currentElement;
    }

    //endregion Path

    //region Self Property Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new UCRelativeUIElementBindingValueEditor(new RelativeUIElementBinding(this));
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new UCRelativeUIElementBindingValueEditor((UCUIElementBindingProperty) property);
    }

    //endregion

    //region Scale

    public void setScaleX(float scaleX){
        this.xScale = scaleX;
        onScaleChanged();
    }
    public void setScaleY(float scaleY){
        this.yScale = scaleY;
        onScaleChanged();
    }
    public void setScale(float scale){
        setScaleX(scale);
        setScaleY(scale);
    }

    public void onScaleChanged(){
        onScaleChangedEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(this));
    }

    public float getScaleX(){
        return xScale * (hasParent() ? parent.getScaleX() : 1.0f);
    }
    public float getScaleY(){
        return yScale * (hasParent() ? parent.getScaleY() : 1.0f);
    }

    public float getParentScaleX(){
        return hasParent() ? parent.getScaleX() : 1.0f;
    }
    public float getParentScaleY(){
        return hasParent() ? parent.getScaleY() : 1.0f;
    }

    //endregion

    //endregion

    public enum BoundCalculationType{
        CONTAINS,
        CONTAINS_HALF,
        OVERLAPS,
    }

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
                .setDescription("Internal ID of the element. Has to be unique relative to its siblings.")
                .setCategory("Core");

        public PositionProperty localPositionX = new PositionProperty(Pos.px(0))
                .setName("Local X Position")
                .setDescription("Local X Position of the element relative to its parent. Can be:\n" +
                        "* Static: Fixed position in pixels.\n" +
                        "* Percentage: Position relative to the parent's dimensions.")
                .setCategory("Transform");
        public PositionProperty localPositionY = new PositionProperty(Pos.px(0))
                .setName("Local Y Position")
                .setDescription("Local Y Position of the element relative to its parent. Can be:\n" +
                        "* Static: Fixed position in pixels.\n" +
                        "* Percentage: Position relative to the parent's dimensions.")
                .setCategory("Transform");

        public DimensionProperty width = new DimensionProperty(Dim.px(1))
                .setName("Width")
                .setDescription("Width of the element. Can be:\n" +
                        "* Static: Fixed dimensions in pixels.\n" +
                        "* Percentage: Dimensions relative to the parent's dimensions.\n" +
                        "* Fill: Fills the parent's dimensions starting from it's position.\n" +
                        "* Auto: Automatically adjusts the dimensions based on the content size of its children.\n" +
                        "* Mirror height: Mirrors the value of the height dimension.")
                .setCategory("Transform");
        public DimensionProperty height = new DimensionProperty(Dim.px(1))
                .setName("Height")
                .setDescription("Height of the element. Can be:\n" +
                        "* Static: Fixed dimensions in pixels.\n" +
                        "* Percentage: Dimensions relative to the parent's dimensions.\n" +
                        "* Fill: Fills the parent's dimensions starting from it's position.\n" +
                        "* Auto: Automatically adjusts the dimensions based on the content size of its children.\n" +
                        "* Mirror width: Mirrors the value of the width dimension.")
                .setCategory("Transform");

        public AlignmentProperty alignment = new AlignmentProperty(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM)
                .setName("Alignment")
                .setDescription("Alignment of the element within its parent.")
                .setCategory("Transform");

        public BooleanProperty isVisible = new BooleanProperty(true)
                .setName("Visible")
                .setDescription("Whether or not the element is rendered/visible.")
                .setCategory("General");
        public BooleanProperty isEnabled = new BooleanProperty(true)
                .setName("Enabled")
                .setDescription("Whether or not the element is interactable with.")
                .setCategory("General");
        public BooleanProperty isPassthrough = new BooleanProperty(false)
                .setName("Passthrough")
                .setDescription("Whether or not the element allows interactions to pass through it to elements underneath it.")
                .setCategory("General");

        public MethodBindingProperty onHovered = new MethodBindingProperty()
                .setName("On Hovered")
                .setDescription("Method to call when the element is hovered.")
                .setCategory("Events");
        public MethodBindingProperty onHoverTick = new MethodBindingProperty()
                .setName("On Hover Tick")
                .setDescription("Method to call every tick the element is hovered.")
                .setCategory("Events")
                .setDynamicCreationParameters(new Pair<>("hoverDeltaTime", Float.class));
        public MethodBindingProperty onUnhovered = new MethodBindingProperty()
                .setName("On Unhovered")
                .setDescription("Method to call when the element is unhovered.")
                .setCategory("Events");

        public MethodBindingProperty onLeftClick = new MethodBindingProperty()
                .setName("On Left Click")
                .setDescription("Method to call when the element is left clicked.")
                .setCategory("Events");
        public MethodBindingProperty onLeftClickHeld = new MethodBindingProperty()
                .setName("On Left Click Held")
                .setDescription("Method to call every tick the element is left clicked.")
                .setCategory("Events")
                .setDynamicCreationParameters(new Pair<>("clickDuration", Float.class));
        public MethodBindingProperty onLeftClickRelease = new MethodBindingProperty()
                .setName("On Left Click Release")
                .setDescription("Method to call when the element is released after being left clicked.")
                .setCategory("Events");

        public MethodBindingProperty onRightClick = new MethodBindingProperty()
                .setName("On Right Click")
                .setDescription("Method to call when the element is right clicked.")
                .setCategory("Events");
        public MethodBindingProperty onRightClickHeld = new MethodBindingProperty()
                .setName("On Right Click Held")
                .setDescription("Method to call every tick the element is right clicked.")
                .setCategory("Events")
                .setDynamicCreationParameters(new Pair<>("clickDuration", Float.class));
        public MethodBindingProperty onRightClickRelease = new MethodBindingProperty()
                .setName("On Right Click Release")
                .setDescription("Method to call when the element is released after being right clicked.")
                .setCategory("Events");

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
            properties.remove(localPositionX);
            properties.remove(localPositionY);
            properties.remove(width);
            properties.remove(height);
            properties.remove(isVisible);
            properties.remove(isEnabled);
        }
    }
}
