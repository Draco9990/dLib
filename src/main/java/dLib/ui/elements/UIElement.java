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
import dLib.patches.InputHelpers;
import dLib.properties.objects.*;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.IEditableValue;
import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.tools.uicreator.ui.elements.interfaces.IGeneratedUIElement;
import dLib.tools.uicreator.ui.properties.editors.UCRelativeUIElementBindingValueEditor;
import dLib.ui.Alignment;
import dLib.ui.animations.UIAnimation;
import dLib.ui.animations.exit.UIExitAnimation;
import dLib.ui.bindings.AbstractUIElementBinding;
import dLib.ui.bindings.UIElementRelativePathBinding;
import dLib.ui.elements.components.GeneratedElementComponent;
import dLib.ui.elements.components.UIDebuggableComponent;
import dLib.ui.elements.components.AbstractUIElementComponent;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.components.data.AbstractUIElementDataComponent;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.screens.UIManager;
import dLib.util.DLibLogger;
import dLib.util.IntegerVector2;
import dLib.util.Reflection;
import dLib.util.events.Event;
import dLib.util.events.GlobalEvents;
import dLib.util.events.globalevents.Constructable;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.helpers.UIHelpers;
import dLib.util.ui.bounds.AbstractBounds;
import dLib.util.ui.bounds.Bound;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.*;
import dLib.util.ui.events.PreUIHoverEvent;
import dLib.util.ui.events.PreUILeftClickEvent;
import dLib.util.ui.events.PreUISelectEvent;
import dLib.util.ui.events.PreUIUnhoverEvent;
import dLib.util.ui.padding.AbstractPadding;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.AbstractStaticPosition;
import dLib.util.ui.position.Pos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UIElement implements Disposable, IEditableValue, Constructable {
    //region Variables

    protected String ID;

    protected UIElement parent;
    protected List<UIElement> children = new ArrayList<>();
    public ConsumerEvent<UIElement> onChildAddedEvent = new ConsumerEvent<>();
    public ConsumerEvent<UIElement> onChildRemovedEvent = new ConsumerEvent<>();
    public RunnableEvent onChildrenChangedEvent = new RunnableEvent();
    public RunnableEvent onHierarchyChangedEvent = new RunnableEvent();

    public String rootOwnerId;

    protected Hitbox hb = new Hitbox(0, 0, 1, 1);

    private AbstractPosition localPosX = Pos.px(0);
    private AbstractPosition localPosY = Pos.px(0);
    private Integer localPosXCache = null;
    protected Integer localPosYCache = null;
    private Integer worldPosXCache = null;
    private Integer worldPosYCache = null;
    public ConsumerEvent<UIElement> onPositionChangedEvent = new ConsumerEvent<>();

    private int localChildOffsetX = 0;
    private int localChildOffsetY = 0;

    private Alignment alignment = new Alignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);

    private AbstractDimension width = Dim.fill();
    private AbstractDimension height = Dim.fill();
    private Integer widthCache = null;
    private Integer heightCache = null;
    private AbstractBounds containerBounds = null;
    private BoundCalculationType containerBoundCalculationType = BoundCalculationType.CONTAINS;
    public ConsumerEvent<UIElement> onDimensionsChangedEvent = new ConsumerEvent<>();

    private float xScale = 1f;
    private float yScale = 1f;
    private boolean scaleWithParent = true;
    public ConsumerEvent<UIElement> onScaleChangedEvent = new ConsumerEvent<>();

    private AbstractPadding paddingLeft = Padd.px(0);
    private AbstractPadding paddingBottom = Padd.px(0);
    private AbstractPadding paddingRight = Padd.px(0);
    private AbstractPadding paddingTop = Padd.px(0);

    private UIElement elementMask = null;
    private boolean inheritMaskFromParent = true; //TODO: Expose

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    private boolean pendingHide = false;

    private Color darkenedColor = Color.BLACK;
    private float darkenedColorMultiplier = 0.4f;
    protected boolean isDarkened = false;

    protected boolean controllerSelectable = false;
    private boolean selected;
    public ConsumerEvent<Boolean> onSelectionStateChangedEvent = new ConsumerEvent<>();

    private boolean isPassthrough = true;

    //region Events

    public RunnableEvent preUpdateEvent = new RunnableEvent();
    public RunnableEvent postUpdateEvent = new RunnableEvent();
    public ConsumerEvent<SpriteBatch> preRenderEvent = new ConsumerEvent<>();
    public ConsumerEvent<SpriteBatch> postRenderEvent = new ConsumerEvent<>();

    public RunnableEvent onHoveredEvent = new RunnableEvent();
    public ConsumerEvent<Float> onHoverTickEvent = new ConsumerEvent<>();
    public RunnableEvent onUnhoveredEvent = new RunnableEvent();

    public ConsumerEvent<UIElement> onHoveredChildEvent = new ConsumerEvent<>();
    public BiConsumerEvent<UIElement, Float> onHoverTickChildEvent = new BiConsumerEvent<>();
    public ConsumerEvent<UIElement> onUnhoveredChildEvent = new ConsumerEvent<>();

                                                                                                                        public static ConsumerEvent<UIElement> preLeftClickGlobalEvent = new ConsumerEvent<>();
    public RunnableEvent onLeftClickEvent = new RunnableEvent();
    public ConsumerEvent<Float> onLeftClickHeldEvent = new ConsumerEvent<>();
    public RunnableEvent onLeftClickReleaseEvent = new RunnableEvent();

    public RunnableEvent onRightClickEvent = new RunnableEvent();
    public ConsumerEvent<Float> onRightClickHeldEvent = new ConsumerEvent<>();
    public RunnableEvent onRightClickReleaseEvent = new RunnableEvent();

    //endregion Events

    private String onHoverLine; // Say the Spire mod compatibility
    protected String onSelectLine; // Say the Spire mod compatibility
    protected String onTriggeredLine; // Say the Spire mod compatibility

    private float totalHoverDuration;

    private float totalLeftClickDuration;
    private float totalRightClickDuration;

    private boolean holdingLeft;
    private boolean holdingRight;

    private ArrayList<AbstractUIElementComponent<?>> components = new ArrayList<>();

    protected ArrayList<Runnable> delayedActions =new ArrayList<>();

    private float totalLifespan = -1f;
    private float remainingLifespan = -1f;

    public RunnableEvent onCloseEvent = new RunnableEvent();

    //TODO: Expose to data and screen editor
    private UIAnimation entryAnimation;
    private UIAnimation reentryAnimation; //TODO
    private UIExitAnimation exitAnimation;
    private UIAnimation animation;

    private UIAnimation playingAnimation;

    private UIStrings stringTable = null;

    private boolean isContextual = false;
    private boolean isModal = false;

    private boolean drawFocusOnOpen = true;

    private boolean overridesBaseScreen = false;

    private transient boolean disposed = false;
    private transient boolean updating = false;
    private transient boolean rendering = false;

    //endregion

    //region Statics

    public static OrthographicCamera camera = Reflection.getFieldValue("camera", Gdx.app.getApplicationListener());

    //endregion

    //region Constructors

    public UIElement(){
        this(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }
    public UIElement(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public UIElement(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public UIElement(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        this.ID = getClass().getSimpleName() + "_" + UUID.randomUUID().toString().replace("-", "");
        this.localPosX = xPos;
        this.localPosY = yPos;
        setWidthRaw(width);
        setHeightRaw(height);

        String uiStrings = getUIStringsKey();
        if(uiStrings != null){
            stringTable = CardCrawlGame.languagePack.getUIString(uiStrings);
        }
    }

    public UIElement(UIElementData data){
        setID(data.id.getValue());

        setLocalPosition(data.localPositionX.getValue(), data.localPositionY.getValue());

        setWidthRaw(data.width.getValue());
        setHeightRaw(data.height.getValue());

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

        this.controllerSelectable = data.isControllerSelectable.getValue();

        onHoveredEvent.subscribeManaged(() -> data.onHovered.getValue().executeBinding(this));
        onHoverTickEvent.subscribeManaged((time) -> data.onHoverTick.getValue().executeBinding(this, time));
        onUnhoveredEvent.subscribeManaged(() -> data.onUnhovered.getValue().executeBinding(this));

        onLeftClickEvent.subscribeManaged(() -> data.onLeftClick.getValue().executeBinding(this));
        onLeftClickHeldEvent.subscribeManaged((time) -> data.onLeftClickHeld.getValue().executeBinding(this, time));
        onLeftClickReleaseEvent.subscribeManaged(() -> data.onLeftClickRelease.getValue().executeBinding(this));

        onRightClickEvent.subscribeManaged(() -> data.onRightClick.getValue().executeBinding(this));
        onRightClickHeldEvent.subscribeManaged((time) -> data.onRightClickHeld.getValue().executeBinding(this, time));
        onRightClickReleaseEvent.subscribeManaged(() -> data.onRightClickRelease.getValue().executeBinding(this));

        this.onSelectLine = data.onSelectLine.getValue();
        this.onTriggeredLine = data.onTriggeredLine.getValue();

        this.rootOwnerId = data.rootOwnerId;

        addComponent(new GeneratedElementComponent(data));
        for(UIElementData childData : data.children){
            UIElement child = childData.makeUIElement();
            addChild(child);
        }
    }

    @Override
    public void postConstruct(){
        if(this instanceof IGeneratedUIElement){
            ((IGeneratedUIElement)this).loadGeneratedData();
        }

        registerCommonEvents();

        preLeftClickGlobalEvent.subscribe(this, (element) -> {
            if(this.isContextual() && element != this && !element.isDescendantOf(this)){
                dispose();
            }
        });

        GlobalEvents.subscribe(this, PreUIHoverEvent.class, (event) -> {
            if(event.source.isPassthrough() || event.source == this){
                return;
            }

            if(isHovered()){
                this.hb.unhover();
                onUnhovered();
            }
        });

        GlobalEvents.subscribe(this, PreUISelectEvent.class, (event) -> {
            if(event.source.isPassthrough() || event.source == this){
                return;
            }

            if(isSelected()){
                deselect();
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
        while(!children.isEmpty()){
            UIElement child = children.get(0);

            int elementCountBefore = children.size();

            child.dispose();


            if(children.size() == elementCountBefore){
                int i = 0;
            }
        }

        if(hasParent()){
            parent.removeChild(this);
        }
        else{
            close();
        }

        delayedActions.clear();

        disposed = true;
    }

    //endregion

    //region Methods

    //region Update & Render
    public final void update(){
        if(!shouldUpdate()) return;

        updating = true;

        updateChildren();
        if(disposed) return;

        preUpdateEvent.invoke(Runnable::run);
        updateSelf();
        postUpdateEvent.invoke(Runnable::run);

        ensureElementWithinBounds();

        updating = false;
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
            for(AbstractUIElementComponent component : components){
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

            if((this.hb.justHovered || this.hb.hovered) && InputHelpers.alreadyHovered){
                this.hb.justHovered = false;
                this.hb.hovered = false;
            }

            if(isEnabled()){
                if(this.hb.justHovered) onHovered();
                if(this.hb.hovered || (isSelected() && Settings.isControllerMode)){
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
            children.get(i).update();

            if(disposed){ //!If any of the children disposes us and themselves, we should stop updating the children
                return;
            }
        }
    }

    public final void render(SpriteBatch sb){
        if(!shouldRender()) return;

        rendering = true;

        boolean pushedScissors = false;
        PositionBounds maskBounds = getMaskWorldBounds();
        if(maskBounds != null){
            sb.flush();

            Rectangle scissors = new Rectangle();
            Rectangle mask = new Rectangle(maskBounds.left * Settings.xScale, maskBounds.bottom * Settings.yScale, (maskBounds.right - maskBounds.left) * Settings.xScale, (maskBounds.top - maskBounds.bottom) * Settings.yScale);

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

        rendering = false;
    }

    protected void renderSelf(SpriteBatch sb){
        //Render Components
        {
            for(AbstractUIElementComponent component : components){
                component.onRender(this, sb);
            }
        }

        //Render Hitbox
        {
            hb.render(sb);
        }
    }

    protected void renderChildren(SpriteBatch sb){
        for(UIElement child : children){
            child.render(sb);
        }
    }

    protected boolean shouldUpdate(){
        return isActive() && (isEnabled() || isVisible()) && !disposed;
    }
    protected boolean shouldRender(){
        return isActive() && isVisible() && !disposed;
    }
    //endregion

    //region Id
    public void setID(String newId){
        this.ID = newId;
    }
    public String getId(){
        return ID;
    }
    //endregion

    //region Parent & Children

    //region Parent
    private void setParent(UIElement parent){
        if(parent == this.parent){
            return;
        }

        this.parent = parent;
        onParentChanged();
    }

    public void reparent(UIElement newParent){
        if(newParent == this.parent){
            return;
        }

        if (newParent.isDescendantOf(this)) {
            if(parent == null){
                throw new UnsupportedOperationException("Cannot reparent root element to a child element");
            }

            while(!children.isEmpty()){
                UIElement child = children.get(0);
                removeChild(child);
                parent.addChild(child);
            }

            parent.removeChild(this);
            newParent.addChild(this);
        }
        else {
            if(parent != null){
                parent.removeChild(this);
            }

            newParent.addChild(this);
        }
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
        onHierarchyChangedEvent.invoke();
    }

    //endregion

    //region Children
    public void addChild(UIElement child){
        if(children.contains(child)){
            return;
        }

        this.children.add(child);
        child.setParent(this);
        onChildAddedEvent.invoke(child);
        onChildrenChanged();
    }
    public void setChildren(ArrayList<UIElement> children){
        clearChildren();
        for(UIElement child : children){
            addChild(child);
        }
    }
    public void insertChild(int index, UIElement child){
        if(children.contains(child)){
            return;
        }

        this.children.add(index, child);
        child.setParent(this);
        onChildAddedEvent.invoke(child);
        onChildrenChanged();
    }

    public void swapChildren(int index1, int index2){
        Collections.swap(this.children, index1, index2);
    }

    public boolean hasChild(UIElement child){
        return children.contains(child);
    }

    public void removeChild(UIElement child){
        if(children.remove(child)){
            child.setParent(null);
            onChildRemovedEvent.invoke(child);
            onChildrenChanged();
        }
    }
    public void replaceChild(UIElement original, UIElement replacement){
        int childIndex = children.indexOf(original);
        if(childIndex == -1){
            return;
        }

        children.set(childIndex, replacement);
        original.setParent(null);
        original.dispose();

        replacement.setParent(this);
        onChildRemovedEvent.invoke(original);
        onChildAddedEvent.invoke(replacement);
        onChildrenChanged();
    }
    public void clearChildren(){
        for(UIElement child : children){
            child.setParent(null);
            onChildRemovedEvent.invoke(child);
        }
        children.clear();
        onChildrenChanged();
    }

    protected void onChildrenChanged(){
        invalidateCachesForElementTree();
        onHierarchyChangedEvent.invoke();
        onChildrenChangedEvent.invoke();
    }

    public UIElement getFirstChild(){
        if(children.isEmpty()) return null;
        return children.get(0);
    }
    public UIElement getLastChild(){
        if(children.isEmpty()) return null;
        return children.get(children.size() - 1);
    }
    public ArrayList<UIElement> getChildren(){
        return new ArrayList<>(children);
    }

    public ArrayList<UIElement> getAllChildren(){
        ArrayList<UIElement> allChildren = new ArrayList<>();
        for(UIElement child : children){
            allChildren.add(child);
            allChildren.addAll(child.getAllChildren());
        }
        return allChildren;
    }

    public UIElement getSelectedChild(){
        for(UIElement child : children){
            if(child.isSelected()){
                return child;
            }

            UIElement selectedChild = child.getSelectedChild();
            if(selectedChild != null){
                return selectedChild;
            }
        }

        return null;
    }

    public <T extends UIElement> T findChildById(String elementId){
        for(UIElement child : children){
            if(child.getId().equals(elementId)){
                return (T) child;
            }
        }

        return null;
    }
    public UIElement findParentById(String elementId){
        if(getId().equals(elementId)){
            return this;
        }

        if(parent == null){
            return null;
        }

        return parent.findParentById(elementId);
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
    public void setLocalPositionX(int newPosition){
        setLocalPosition(Pos.px(newPosition), getLocalPositionYRaw());
    }
    public void setLocalPositionY(int newPosition){
        setLocalPosition(getLocalPositionXRaw(), Pos.px(newPosition));
    }
    public void setLocalPosition(int newPositionX, int newPositionY){
        setLocalPosition(Pos.px(newPositionX), Pos.px(newPositionY));
    }
    public void setLocalPosition(AbstractPosition newX, AbstractPosition newY){
        setLocalPosition_internal(newX, newY);
        ensureElementWithinBounds();
    }
    private final void setLocalPosition_internal(AbstractPosition newX, AbstractPosition newY){
        AbstractPosition oldPosX = localPosX;
        AbstractPosition oldPosY = localPosY;

        localPosX = newX;
        localPosY = newY;

        if(!oldPosX.equals(localPosX) || !oldPosY.equals(localPosY)){
            onPositionChanged();
        }
    }

    public int getLocalPositionX(){
        if(localPosXCache == null){
            localPosXCache = localPosX.getLocalX(this);
        }

        int toReturn = localPosXCache;
        toReturn += getPaddingLeft();
        if(localPosX instanceof AbstractStaticPosition){
            toReturn = Math.round(toReturn * getParentScaleX());
        }

        return toReturn;
    }
    public int getLocalPositionY(){
        if(localPosYCache == null){
            localPosYCache = localPosY.getLocalY(this);
        }

        int toReturn = localPosYCache;
        toReturn += getPaddingBottom();
        if(localPosY instanceof AbstractStaticPosition){
            toReturn = Math.round(toReturn * getParentScaleY());
        }

        return toReturn;
    }
    public IntegerVector2 getLocalPosition(){
        return new IntegerVector2(getLocalPositionX(), getLocalPositionY());
    }

    public int getLocalPositionXUnpadded(){
        if(localPosXCache == null){
            localPosXCache = localPosX.getLocalX(this);
        }

        int toReturn = localPosXCache;
        if(localPosX instanceof AbstractStaticPosition){
            toReturn = Math.round(toReturn * getParentScaleX());
        }

        return toReturn;
    }
    public int getLocalPositionYUnpadded(){
        if(localPosYCache == null){
            localPosYCache = localPosY.getLocalY(this);
        }

        int toReturn = localPosYCache;
        if(localPosY instanceof AbstractStaticPosition){
            toReturn = Math.round(toReturn * getParentScaleY());
        }

        return toReturn;
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

    public void setLocalPositionCenteredX(int newPos){
        setLocalPositionX(newPos - (int)(getWidth() * 0.5f));
    }
    public void setLocalPositionCenteredY(int newPos){
        setLocalPositionY(newPos - (int)(getHeight() * 0.5f));
    }
    public void setLocalPositionCentered(int newPosX, int newPosY){
        int wHalf = (int)(getWidth() * 0.5f);
        int hHalf = (int)(getHeight() * 0.5f);
        setLocalPosition(newPosX - wHalf, newPosY - hHalf);
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
    public void setWorldPositionX(int newPos){
        setWorldPosition(newPos, getWorldPositionY());
    }
    public void setWorldPositionY(int newPos){
        setWorldPosition(getWorldPositionX(), newPos);
    }
    public void setWorldPosition(int newPosX, int newPosY){
        int xDiff = newPosX - getWorldPositionX();
        int yDiff = newPosY - getWorldPositionY();

        DLibLogger.log("Offsetting by " + xDiff + ", " + yDiff);

        offset(xDiff, yDiff);
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

    public void setWorldPositionCenteredX(int newPos){
        setWorldPositionCentered(newPos, getWorldPositionCenteredY());
    }
    public void setWorldPositionCenteredY(int newPos){
        setWorldPositionCentered(getWorldPositionCenteredX(), newPos);
    }
    public void setWorldPositionCentered(int newPosX, int newPosY){
        int wHalf = (int)(getWidth() * 0.5f);
        int hHalf = (int)(getHeight() * 0.5f);
        setWorldPosition(newPosX - wHalf, newPosY - hHalf);
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
    public void offsetX(int xOffset){
        offset(xOffset, 0);
    }
    public void offsetY(int yOffset){
        offset(0, yOffset);
    }
    public void offset(int xOffset, int yOffset){
        AbstractPosition xCopy = getLocalPositionXRaw().cpy();
        AbstractPosition yCopy = getLocalPositionYRaw().cpy();

        xCopy.offsetHorizontal(this, xOffset);
        yCopy.offsetVertical(this, yOffset);

        setLocalPosition(xCopy, yCopy);
    }
    //endregion

    //region Transforming

    public IntegerVector2 worldToLocal(IntegerVector2 worldPosition){
        return new IntegerVector2((worldPosition.x - getWorldPositionX()), worldPosition.y - getWorldPositionY());
    }
    public IntegerVector2 localToWorld(IntegerVector2 localPosition){
        return new IntegerVector2(localPosition.x + getWorldPositionX(), localPosition.y + getWorldPositionY());
    }

    public IntegerVector2 worldToLocalUnscaled(IntegerVector2 worldPosition){
        return new IntegerVector2((int) ((worldPosition.x - getWorldPositionX()) / getScaleX()), (int) ((worldPosition.y - getWorldPositionY()) / getScaleY()));
    }
    public IntegerVector2 localToWorldUnscaled(IntegerVector2 localPosition){
        return new IntegerVector2((int) (localPosition.x * getScaleX() + getWorldPositionX()), (int) (localPosition.y * getScaleY() + getWorldPositionY()));
    }


    //endregion

    public void onPositionChanged(){
        invalidateCachesForElementTree();

        onPositionChangedEvent.invoke(this);

        for(UIElement child : children){
            child.onParentPositionChanged();
        }
        if(hasParent()){
            getParent().onChildPositionChanged(this);
        }
    }

    protected void onParentPositionChanged(){
    }
    protected void onChildPositionChanged(UIElement child){
        if(getWidthRaw() instanceof AutoDimension){
            invalidateCaches();
        }

        if(getHeightRaw() instanceof AutoDimension){
            invalidateCaches();
        }
    }

    //endregion

    //region Bounds

    public void setContainerBounds(AbstractBounds bounds){
        containerBounds = bounds;
    }

    public AbstractBounds getContainerBounds(){
       return containerBounds;
    }

    public PositionBounds getLocalContainerBounds(){
        AbstractBounds containerBounds = getContainerBounds();
        if(containerBounds == null) return null;

        Integer worldLeft = containerBounds.getWorldLeft(this);
        Integer worldRight = containerBounds.getWorldRight(this);
        Integer worldTop = containerBounds.getWorldTop(this);
        Integer worldBottom = containerBounds.getWorldBottom(this);

        IntegerVector2 localBottomLeft;
        IntegerVector2 localTopRight;

        if(hasParent()){
            localBottomLeft = parent.worldToLocal(new IntegerVector2(worldLeft, worldBottom));
            localTopRight = parent.worldToLocal(new IntegerVector2(worldRight, worldTop));
        }
        else{
            localBottomLeft = new IntegerVector2(worldLeft, worldBottom);
            localTopRight = new IntegerVector2(worldRight, worldTop);
        }

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

        if(containerBoundCalculationType == BoundCalculationType.FILLS){
            if(desiredPositionX > localContainerBounds.left){
                desiredPositionX = localContainerBounds.left;
            }
            if(desiredPositionY > localContainerBounds.bottom){
                desiredPositionY = localContainerBounds.bottom;
            }

            if(desiredPositionX + getWidth() < localContainerBounds.right){
                desiredPositionX = localContainerBounds.right - getWidth();
            }
            if(desiredPositionY + getHeight() < localContainerBounds.top){
                desiredPositionY = localContainerBounds.top - getHeight();
            }
        }
        else{
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
        for(UIElement child : children) hasInteraction = hasInteraction || child.onLeftInteraction();
        return hasInteraction;
    }
    public boolean onRightInteraction(){
        boolean hasInteraction = false;
        for(UIElement child : children) hasInteraction = hasInteraction || child.onRightInteraction();
        return hasInteraction;
    }
    public boolean onUpInteraction(){
        boolean hasInteraction = false;
        for(UIElement child : children) hasInteraction = hasInteraction || child.onUpInteraction();
        return hasInteraction;
    }
    public boolean onDownInteraction(){
        boolean hasInteraction = false;
        for(UIElement child : children) hasInteraction = hasInteraction || child.onDownInteraction();
        return hasInteraction;
    }

    public boolean onConfirmInteraction(){
        boolean hasInteraction = false;
        for(UIElement child : children) hasInteraction = hasInteraction || child.onConfirmInteraction();
        return hasInteraction;
    }
    public boolean onCancelInteraction(){
        boolean hasInteraction = false;
        for(UIElement child : children) hasInteraction = hasInteraction || child.onCancelInteraction();
        return hasInteraction;
    }
    //endregion

    //region Controller Selection
    public void setControllerSelectable(boolean controllerSelectable){
        this.controllerSelectable = controllerSelectable;
    }
    public boolean isControllerSelectable(){
        return controllerSelectable;
    }

    public ArrayList<UIElement> getAllSelectableChildren(){
        ArrayList<UIElement> allChildren = new ArrayList<>();
        for(UIElement child : children){
            if(child.isControllerSelectable() && child.isEnabled()){
                allChildren.add(child);
            }
            allChildren.addAll(child.getAllSelectableChildren());
        }
        return allChildren;
    }

    public void select(){
        setSelected(true);
    }
    public void deselect(){
        setSelected(false);
    }
    public void setSelected(boolean selected){
        if(this.selected == selected) {
            return;
        }

        if(selected){
            GlobalEvents.sendMessage(new PreUISelectEvent(this));
        }

        this.selected = selected;
        onSelectionStateChanged();
    }

    public boolean isSelected(){
        return selected;
    }

    public void onSelectionStateChanged(){
        if(Settings.isControllerMode){
            if(selected){
                onHovered();
            }
            else if (isHovered()){
                onUnhovered();
            }
        }

        onSelectionStateChangedEvent.invoke(isSelected());
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
        for(UIElement child : children){
            child.invalidateCaches();
            child.invalidateChildrenCacheRecursive();
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

    public void setDarkenedColor(Color darkenedColor){
        this.darkenedColor = darkenedColor;
    }
    public Color getDarkenedColor(){
        return darkenedColor;
    }

    public void setDarkenedColorMultiplier(float darkenedColorMultiplier){
        this.darkenedColorMultiplier = darkenedColorMultiplier;
        if(this.darkenedColorMultiplier > 1.0f) this.darkenedColorMultiplier = 1.0f;
    }
    public Float getDarkenedColorMultiplier(){
        return darkenedColorMultiplier;
    }

    public void darkenInstantly(){
        setDarkened(true);
    }
    public void lightenInstantly(){
        setDarkened(false);
    }
    private void setDarkened(boolean darkened){
        this.isDarkened = darkened;
    }

    public boolean isDarkened(){
        return isDarkened || (hasParent() && parent.isDarkened());
    }

    //endregion

    //region Width & Height

    public void setWidth(AbstractDimension newWidth){
        setDimensions(newWidth, null);
    }
    public void setHeight(AbstractDimension newHeight){
        setDimensions(null, newHeight);
    }
    public void setDimensions(AbstractDimension newWidth, AbstractDimension newHeight){
        setDimensions_internal(newWidth, newHeight);
        ensureElementWithinBounds();
    }
    private final void setDimensions_internal(AbstractDimension newWidth, AbstractDimension newHeight){
        AbstractDimension oldWidth = width;
        AbstractDimension oldHeight = height;

        if(newWidth != null) setWidthRaw(newWidth);
        if(newHeight != null) setHeightRaw(newHeight);

        if(!Objects.equals(oldWidth, width) || !Objects.equals(oldHeight, height)){
            onDimensionsChanged();
        }
    }

    private final void setWidthRaw(AbstractDimension newWidth){
        width = newWidth;
        width.setReferenceDimension(AbstractDimension.ReferenceDimension.WIDTH);
    }
    private final void setHeightRaw(AbstractDimension newHeight){
        height = newHeight;
        height.setReferenceDimension(AbstractDimension.ReferenceDimension.HEIGHT);
    }

    public void setWidth(int newWidth){
        setWidth(Dim.px(newWidth));
    }
    public void setHeight(int newHeight){
        setHeight(Dim.px(newHeight));
    }
    public void setDimensions(int newWidth, int newHeight){
        setDimensions(Dim.px(newWidth), Dim.px(newHeight));
    }

    public void onDimensionsChanged(){
        invalidateCachesForElementTree();

        for(UIElement child : children){
            child.onParentDimensionsChanged();
        }
        if(hasParent()){
            getParent().onChildDimensionsChanged(this);
        }

        onDimensionsChangedEvent.invoke(this);
    }

    protected void onParentDimensionsChanged(){
    }
    protected void onChildDimensionsChanged(UIElement child){
        if(getWidthRaw() instanceof AutoDimension || getWidthRaw() instanceof MirrorDimension){
            invalidateCaches();
        }

        if(getHeightRaw() instanceof AutoDimension || getHeightRaw() instanceof MirrorDimension){
            invalidateCaches();
        }
    }

    public int getWidth(){
        if(widthCache == null || widthCache <= 0){
            widthCache = width.calculateDimension(this);
        }

        int toReturn = widthCache;

        toReturn -= getPaddingRight();
        if(width instanceof AbstractStaticDimension){
            toReturn = (int) (toReturn * getScaleX());
        }

        return toReturn;
    }
    public int getHeight(){
        if(heightCache == null || heightCache <= 0){
            heightCache = height.calculateDimension(this);
        }

        int toReturn = heightCache;

        toReturn -= getPaddingTop();
        if(height instanceof AbstractStaticDimension){
            toReturn = (int) (toReturn * getScaleY());
        }

        return toReturn;
    }
    public IntegerVector2 getDimensions(){
        return new IntegerVector2(getWidth(), getHeight());
    }

    public int getWidthUnpadded(){
        if(widthCache == null || widthCache <= 0){
            widthCache = width.calculateDimension(this);
        }

        int toReturn = widthCache;
        if(width instanceof AbstractStaticDimension){
            toReturn = (int) (toReturn * getScaleX());
        }

        return toReturn;
    }
    public int getHeightUnpadded(){
        if(heightCache == null || heightCache <= 0){
            heightCache = height.calculateDimension(this);
        }

        int toReturn = heightCache;
        if(height instanceof AbstractStaticDimension){
            toReturn = (int) (toReturn * getScaleY());
        }

        return toReturn;
    }

    public int getWidthUnscaled(){
        return (int) (getWidth() / getScaleX());
    }
    public int getHeightUnscaled(){
        return (int) (getHeight() / getScaleY());
    }

    public int getWidthLocalScaled(){
        return (int) (getWidth() / getParentScaleX());
    }
    public int getHeightLocalScaled(){
        return (int) (getHeight() / getParentScaleY());
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

        widthCopy.resizeBy(this, widthDiff);
        heightCopy.resizeBy(this, heightDiff);

        setDimensions(widthCopy, heightCopy);
    }

    public void setPositionAndDimensionsFromWorldBounds(PositionBounds bounds){

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

    public PositionBounds getLocalBoundsUnpadded(){
        return new PositionBounds(getLocalPositionXUnpadded(), getLocalPositionYUnpadded(), getLocalPositionXUnpadded() + getWidthUnpadded(), getLocalPositionYUnpadded() + getHeightUnpadded());
    }

    public boolean overlapsParent(){
        return parent != null && overlaps(parent);
    }
    public boolean overlaps(UIElement other){
        return getWorldBounds().overlaps(other.getWorldBounds());
    }

    public PositionBounds getFullChildLocalBounds(){
        PositionBounds fullChildBounds = null;
        for(UIElement child : children){
            if(!(child.isActive()) || child.hasComponent(UITransientElementComponent.class)){
                continue;
            }

            PositionBounds childBounds = child.getFullLocalBounds();
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

    public PositionBounds getFullChildLocalBoundsForAutoDim(){
        PositionBounds fullChildBounds = null;
        for(UIElement child : children){
            if(!(child.isActive()) || child.hasComponent(UITransientElementComponent.class)){
                continue;
            }

            PositionBounds childBounds = child.getFullLocalBoundsForAutoDim();
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
    public PositionBounds getFullLocalBoundsForAutoDim(){
        PositionBounds myBounds = getLocalBounds();
        myBounds.bottom -= getPaddingBottom();
        myBounds.left -= getPaddingLeft();

        PositionBounds fullChildBounds = getFullChildLocalBoundsForAutoDim();

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

    public void setElementMask(UIElement elementMask){
        this.elementMask = elementMask;
    }

    public boolean hasMaskBounds(){
        return elementMask != null || (inheritMaskFromParent && (hasParent() && parent.hasMaskBounds()));
    }
    public PositionBounds getMaskWorldBounds(){
        PositionBounds currentBounds = null;

        UIElement current = this;
        if(current.elementMask != null){
            currentBounds = current.elementMask.getWorldBounds();
        }

        if(inheritMaskFromParent){
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
        }

        return currentBounds;
    }

    public void setInheritMaskFromParent(boolean inheritMaskFromParent){
        this.inheritMaskFromParent = inheritMaskFromParent;
    }

    public boolean inheritsMaskFromParent(){
        return inheritMaskFromParent;
    }

    //endregion Masks

    //region Lifespan

    public void setLifespan(float lifespan){
        totalLifespan = lifespan;
        remainingLifespan = lifespan;
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

        onCloseEvent.invoke();
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

    public void setPadding(AbstractPadding all){
        setPadding(all, all, all, all);
    }
    public void setPadding(AbstractPadding leftRight, AbstractPadding topBottom){
        setPadding(topBottom, leftRight, topBottom, leftRight);
    }
    public void setPadding(AbstractPadding top, AbstractPadding right, AbstractPadding bottom, AbstractPadding left){
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
        this.paddingLeft = left;
    }

    public void setPaddingHorizontal(AbstractPadding horizontal){
        setPaddingLeft(horizontal);
        setPaddingRight(horizontal);
    }
    public void setPaddingVertical(AbstractPadding vertical){
        setPaddingTop(vertical);
        setPaddingBottom(vertical);
    }

    public void setPaddingTop(AbstractPadding top){
        setPadding(top, paddingRight, paddingBottom, paddingLeft);
    }
    public void setPaddingRight(AbstractPadding right){
        setPadding(paddingTop, right, paddingBottom, paddingLeft);
    }
    public void setPaddingBottom(AbstractPadding bottom){
        setPadding(paddingTop, paddingRight, bottom, paddingLeft);
    }
    public void setPaddingLeft(AbstractPadding left){
        setPadding(paddingTop, paddingRight, paddingBottom, left);
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

    public void setHorizontalAlignment(Alignment.HorizontalAlignment horizontalAlignment){
        setAlignment(horizontalAlignment, alignment.verticalAlignment);
    }
    public void setVerticalAlignment(Alignment.VerticalAlignment verticalAlignment){
        setAlignment(alignment.horizontalAlignment, verticalAlignment);
    }
    public void setAlignment(Alignment.HorizontalAlignment horizontalAlignment, Alignment.VerticalAlignment verticalAlignment){
        alignment.horizontalAlignment = horizontalAlignment;
        alignment.verticalAlignment = verticalAlignment;
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

    public <T extends AbstractUIElementComponent> T addComponent(T component){
        components.add(component);
        component.onRegisterComponent(this);
        return component;
    }

    public void removeComponent(AbstractUIElementComponent component){
        components.remove(component);
        component.onUnregisterComponent(this);
        component.dispose();
    }

    public <T extends AbstractUIElementComponent> T getComponent(Class<T> componentClass){
        for(AbstractUIElementComponent component : components){
            if(componentClass.isInstance(component)){
                return (T) component;
            }
        }
        return null;
    }

    public boolean hasComponent(Class<? extends AbstractUIElementComponent> componentClass){
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
        if(!isPassthrough()) InputHelpers.alreadyHovered = true;

        GlobalEvents.sendMessage(new PreUIHoverEvent(this));
        onHoveredEvent.invoke(uiElementConsumer -> uiElementConsumer.run());
    }
    protected void onHoverTick(float totalTickDuration){
        if(!isPassthrough()) InputHelpers.alreadyHovered = true;

        onHoverTickEvent.invoke(uiElementConsumer -> uiElementConsumer.accept(totalTickDuration));
    }
    protected void onUnhovered(){
        totalHoverDuration = 0.f;

        GlobalEvents.sendMessage(new PreUIUnhoverEvent(this));
        onUnhoveredEvent.invoke();
    }

    public boolean isHovered(){ return (hb.hovered || hb.justHovered); }

    public boolean isHoveredOrChildHovered(){
        if(isHovered()) return true;
        for(UIElement child : children){
            if(child.isHoveredOrChildHovered()) return true;
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
        preLeftClickGlobalEvent.invoke(this);
        GlobalEvents.sendMessage(new PreUILeftClickEvent(this));

        totalLeftClickDuration = 0.f;
        holdingLeft = true;

        if(getOnTriggerLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                SayTheSpireIntegration.Output(getOnTriggerLine());
            }
        }

        if(!Settings.isControllerMode){
            select();
        }

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

        for(UIElement child : children){
            child.onPositionChanged();
        }
    }
    public void setLocalChildOffsetY(int offset){
        localChildOffsetY = offset;

        for(UIElement child : children){
            child.onPositionChanged();
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

    public String getRelativePathToElement(UIElement element){
        //First, find common parent
        UIElement commonParent = findCommonParentWith(element);
        if(commonParent == null) {
            return null;
        }

        String path = "";

        UIElement iterating = this;
        while(iterating != commonParent){
            path += "../";
            iterating = iterating.getParent();
        }

        String reversePath = "";
        iterating = element;
        while(iterating != commonParent){
            reversePath = iterating.getId() + "/" + reversePath;
            iterating = iterating.getParent();
        }

        return path + reversePath;
    }

    public UIElement findCommonParentWith(UIElement element){
        UIElement currentElement = this;
        while(currentElement != null){
            if(element == currentElement){
                return currentElement;
            }
            if(element.isDescendantOf(currentElement)){
                return currentElement;
            }
            currentElement = currentElement.getParent();
        }
        return null;
    }

    public UIElement getElementFromRelativePath(String relativePath){
        String[] pathParts = relativePath.split("/");
        UIElement currentElement = this;
        for(String pathPart : pathParts){
            if(pathPart.equals("..")){
                currentElement = currentElement.getParent();
            }
            else{
                currentElement = currentElement.findChildById(pathPart);
            }
        }
        return currentElement;
    }

    //endregion Path

    //region Self Property Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        throw new UnsupportedOperationException("UIElement does not support self property editing.");
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        throw new UnsupportedOperationException("UIElement does not support self property editing.");
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
        onScaleChangedEvent.invoke(this);
    }

    public float getScaleX(){
        if(!scaleWithParent) return xScale;
        else return xScale * getParentScaleX();
    }
    public float getScaleY(){
        if(!scaleWithParent) return yScale;
        else return yScale * getParentScaleY();
    }

    public float getParentScaleX(){
        return hasParent() ? parent.getScaleX() : 1.0f;
    }
    public float getParentScaleY(){
        return hasParent() ? parent.getScaleY() : 1.0f;
    }

    public void setScaleWithParent(boolean scaleWithParent){
        this.scaleWithParent = scaleWithParent;
    }

    //endregion

    //region Overrides Base Screen

    public void setOverridesBaseScreen(boolean overridesBaseScreen){
        this.overridesBaseScreen = overridesBaseScreen;
    }

    public boolean overridesBaseScreen(){
        return overridesBaseScreen;
    }

    //endregion Overrides Base Screen

    //endregion

    public enum BoundCalculationType{
        CONTAINS,
        CONTAINS_HALF,
        OVERLAPS,
        FILLS
    }

    public static class UIElementData implements Serializable, Constructable {
        private static final long serialVersionUID = 1L;

        //region Properties

        public StringProperty id = new StringProperty(getClass().getSimpleName() + "_" + UIHelpers.generateRandomElementId()){
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

        public DimensionProperty width = new DimensionProperty(Dim.px(1920))
                .setName("Width")
                .setDescription("Width of the element. Can be:\n" +
                        "* Static: Fixed dimensions in pixels.\n" +
                        "* Percentage: Dimensions relative to the parent's dimensions.\n" +
                        "* Fill: Fills the parent's dimensions starting from it's position.\n" +
                        "* Auto: Automatically adjusts the dimensions based on the content size of its children.\n" +
                        "* Mirror height: Mirrors the value of the height dimension.")
                .setCategory("Transform");
        public DimensionProperty height = new DimensionProperty(Dim.px(1080))
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
        public BooleanProperty isControllerSelectable = new BooleanProperty(false)
                .setName("KB/Controller Selectable")
                .setDescription("Whether or not the element can be selected by a keyboard or a controller setup.")
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

        public StringProperty onSelectLine = new StringProperty("")
                .setName("On Hover/Select Line")
                .setDescription("Line to say when the element is hovered/selected.")
                .setCategory("Say the Spire");
        public StringProperty onTriggeredLine = new StringProperty("")
                .setName("On Trigger Line")
                .setDescription("Line to say when the element is triggered.")
                .setCategory("Say the Spire");

        //endregion

        //region Variables

        public String rootOwnerId;
        public ArrayList<UIElementData> children = new ArrayList<>();

        private transient ArrayList<AbstractUIElementDataComponent> components = new ArrayList<>();

        //endregion

        //region Constructor

        @Override
        public void postConstruct() {
            bindCommonEvents();
        }

        //endregion

        //region Methods

        //region UI Element Creation

        public final UIElement makeUIElement(){
            return makeUIElement_internal();
        }

        public UIElement makeUIElement_internal(){
            return new UIElement(this);
        }

        //endregion

        //region Components

        public <T extends AbstractUIElementDataComponent> T getOrAddComponent(T component){
            T existingComponent = (T) getComponent(component.getClass());
            if(existingComponent != null){
                return existingComponent;
            }

            components.add(component);
            return component;
        }

        public void removeComponent(AbstractUIElementDataComponent component){
            components.remove(component);
        }

        public <T extends AbstractUIElementDataComponent> T getComponent(Class<T> componentClass){
            for(AbstractUIElementDataComponent component : components){
                if(componentClass.isInstance(component)){
                    return (T) component;
                }
            }
            return null;
        }

        public boolean hasComponent(Class<? extends AbstractUIElementDataComponent> componentClass){
            return getComponent(componentClass) != null;
        }

        //endregion

        //endregion

        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            ois.defaultReadObject();

            verifyNullProperties();
            bindCommonEvents();
            components = new ArrayList<>();
        }

        protected void bindCommonEvents(){
            ArrayList<TProperty> properties = Reflection.getFieldValuesByClass(TProperty.class, this);
            for(TProperty property : properties){
                property.setOwningContainer(this);
            }
        }

        public ArrayList<TProperty<?, ?>> getEditableProperties(){
            ArrayList<TProperty<?, ?>> properties = new ArrayList<>();

            for(TProperty<?, ?> property : Reflection.getFieldValuesByClass(TProperty.class, this)){
                properties.add(property);
            }

            return properties;
        }

        private void verifyNullProperties(){
            ArrayList<Field> propertyFields = Reflection.getFieldsByClass(TProperty.class, this.getClass());

            UIElementData cpy = null;
            for(Field field : propertyFields){
                try {
                    if(field.get(this) == null){
                        if(cpy == null) {
                            cpy = makeGenericCopy();
                        }

                        Reflection.setFieldValue(field, this, Reflection.getFieldValue(field, cpy));
                    }
                } catch (Exception e) {
                    DLibLogger.logError("Failed to verify null properties for field " + field.getName() + " due to: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        private <T extends UIElementData> T makeGenericCopy(){
            try{
                return (T) this.getClass().getConstructor().newInstance();
            }catch (Exception e){
                DLibLogger.logError("Failed to make generic copy of UIElementData due to: " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }
    }
}
