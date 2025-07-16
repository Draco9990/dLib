package dLib.ui.elements;

import basemod.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import dLib.modcompat.ModManager;
import dLib.patches.InputHelpers;
import dLib.properties.objects.*;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.IEditableValue;
import dLib.tools.uicreator.ui.elements.interfaces.IGeneratedUIElement;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.animations.UIAnimation;
import dLib.ui.animations.exit.UIExitAnimation;
import dLib.ui.elements.components.AbstractUIElementComponent;
import dLib.ui.elements.components.GeneratedElementComponent;
import dLib.ui.elements.components.UIOverlayElementComponent;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.components.data.AbstractUIElementDataComponent;
import dLib.ui.elements.items.ContextMenu;
import dLib.ui.elements.items.Tooltip;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.layout.ILayoutProvider;
import dLib.ui.UIManager;
import dLib.util.DLibLogger;
import dLib.util.Reflection;
import dLib.util.bindings.string.AbstractStringBinding;
import dLib.util.bindings.string.Str;
import dLib.util.events.GlobalEvents;
import dLib.util.events.globalevents.Constructable;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.FunctionEvent;
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
import dLib.util.ui.position.Pos;
import org.lwjgl.util.vector.Vector4f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

//TODO add byproxy to all calls
public class UIElement implements Disposable, IEditableValue, Constructable {
    
    //region Variables

    protected String ID;

    protected UIElement parent;
    protected List<UIElement> children = Collections.synchronizedList(new ArrayList<>());
    public ConsumerEvent<UIElement> onChildAddedEvent = new ConsumerEvent<>();
    public ConsumerEvent<UIElement> onChildRemovedEvent = new ConsumerEvent<>();
    public RunnableEvent onChildrenChangedEvent = new RunnableEvent();
    public RunnableEvent onHierarchyChangedEvent = new RunnableEvent();

    public String rootOwnerId;

    protected Hitbox hb = new Hitbox(0, 0, 1, 1);

    private AbstractPosition localPosX = Pos.px(0);
    private AbstractPosition localPosY = Pos.px(0);
    private AbstractDimension width = Dim.fill();
    private AbstractDimension height = Dim.fill();

    private AbstractDimension minimumWidth = Dim.px(Integer.MIN_VALUE);
    private AbstractDimension maximumWidth = Dim.px(Integer.MAX_VALUE);
    private AbstractDimension minimumHeight = Dim.px(Integer.MIN_VALUE);
    private AbstractDimension maximumHeight = Dim.px(Integer.MAX_VALUE);

    public ConsumerEvent<UIElement> onPositionChangedEvent = new ConsumerEvent<>();

    private float offsetX = 0;
    private float offsetY = 0;
    private float childOffsetX = 0;
    private float childOffsetY = 0;

    private Alignment alignment = new Alignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);

    private AbstractBounds containerBounds = null;
    private BoundCalculationType containerBoundCalculationType = BoundCalculationType.CONTAINS;
    public ConsumerEvent<UIElement> onDimensionsChangedEvent = new ConsumerEvent<>();

    private float xScale = 1f;
    private float yScale = 1f;
    private boolean scaleWithParent = true;
    public ConsumerEvent<UIElement> onScaleChangedEvent = new ConsumerEvent<>();

    private AbstractPadding paddingLeft;
    private AbstractPadding paddingBottom;
    private AbstractPadding paddingRight;
    private AbstractPadding paddingTop;

    private UIElement elementMask = null;
    private boolean inheritMaskFromParent = true; //TODO: Expose

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    private boolean pendingHide = false;

    private Color darkenedColor = Color.BLACK;
    private float darkenedColorMultiplier = 0.4f;
    protected boolean isDarkened = false;

    private boolean controllerSelectable = false;
    private boolean selected;
    public ConsumerEvent<Boolean> postSelectionStateChangedEvent = new ConsumerEvent<>();                               public static BiConsumerEvent<UIElement, Boolean> postSelectionStateChangedEvent_Global = new BiConsumerEvent<>();

    private boolean isPassthrough = true;

    //region Events

    public RunnableEvent preUpdateEvent = new RunnableEvent();
    public RunnableEvent postUpdateEvent = new RunnableEvent();
    public ConsumerEvent<SpriteBatch> preRenderEvent = new ConsumerEvent<>();                                           public static BiConsumerEvent<UIElement, SpriteBatch> preRenderGlobalEvent = new BiConsumerEvent<>();
    public ConsumerEvent<SpriteBatch> postRenderEvent = new ConsumerEvent<>();                                          public static BiConsumerEvent<UIElement, SpriteBatch> postRenderGlobalEvent = new BiConsumerEvent<>();

    public RunnableEvent onHoveredEvent = new RunnableEvent();
                                                                                                                        public static ConsumerEvent<UIElement> postHoverGlobalEvent = new ConsumerEvent<>();
    public ConsumerEvent<Float> onHoverTickEvent = new ConsumerEvent<>();
    public RunnableEvent onUnhoveredEvent = new RunnableEvent();

    public ConsumerEvent<UIElement> onHoveredChildEvent = new ConsumerEvent<>();
    public BiConsumerEvent<UIElement, Float> onHoverTickChildEvent = new BiConsumerEvent<>();
    public ConsumerEvent<UIElement> onUnhoveredChildEvent = new ConsumerEvent<>();

                                                                                                                        public static ConsumerEvent<UIElement> preLeftClickGlobalEvent = new ConsumerEvent<>();
    public RunnableEvent onLeftClickEvent = new RunnableEvent();
    public ConsumerEvent<Float> onLeftClickHeldEvent = new ConsumerEvent<>();
    public RunnableEvent onLeftClickReleaseEvent = new RunnableEvent();

                                                                                                                        public static ConsumerEvent<UIElement> preRightClickGlobalEvent = new ConsumerEvent<>();
    public RunnableEvent onRightClickEvent = new RunnableEvent();
    public ConsumerEvent<Float> onRightClickHeldEvent = new ConsumerEvent<>();
    public RunnableEvent onRightClickReleaseEvent = new RunnableEvent();

    //endregion Events

    // Say the Spire mod compatibility
    protected AbstractStringBinding sayTheSpireElementName = Str.stat(null); // Say the Spire mod compatibility
    protected AbstractStringBinding sayTheSpireElementType = Str.stat(null); // Say the Spire mod compatibility
    protected AbstractStringBinding sayTheSpireElementValue = Str.stat(null); // Say the Spire mod compatibility

    protected AbstractStringBinding onEnabledLine = Str.stat(null); // Say the Spire mod compatibility
    protected AbstractStringBinding onHoverLine = Str.stat(null); // Say the Spire mod compatibility
    protected AbstractStringBinding onTriggeredLine = Str.stat(null); // Say the Spire mod compatibility

    private boolean controllerSelected = false;
    private boolean proxyHovered = false;

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
    private UIAnimation idleAnimation;

    private UIAnimation playingAnimation;

    private UIStrings stringTable = null;

    private boolean isContextual = false;
    private boolean isModal = false;

    private boolean drawFocusOnOpen = true;

    private boolean overridesBaseScreen = false;

    private transient boolean initialized = false;
    private transient boolean disposed = false;
    private transient boolean updating = false;
    private transient boolean rendering = false;

    private LinkedHashMap<UUID, ContextMenu.IContextMenuOption> contextMenuOptions = new LinkedHashMap<>();

    private UIElement tooltipObject = null;

    public FunctionEvent<Boolean, Boolean> onLeftInteractionEvent = new FunctionEvent<>();
    public FunctionEvent<Boolean, Boolean> onRightInteractionEvent = new FunctionEvent<>();
    public FunctionEvent<Boolean, Boolean> onUpInteractionEvent = new FunctionEvent<>();
    public FunctionEvent<Boolean, Boolean> onDownInteractionEvent = new FunctionEvent<>();
    public FunctionEvent<Boolean, Boolean> onConfirmInteractionEvent = new FunctionEvent<>();
    public FunctionEvent<Boolean, Boolean> onCancelInteractionEvent = new FunctionEvent<>();

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
        setLocalPosition_internal(xPos, yPos);
        setWidthRaw(width);
        setHeightRaw(height);
        setPadding(Padd.px(0));

        setMinimumWidth(Dim.px(Integer.MIN_VALUE + 1000000));
        setMinimumHeight(Dim.px(Integer.MIN_VALUE + 1000000));
        setMaximumWidth(Dim.px(Integer.MAX_VALUE - 1000000));
        setMaximumHeight(Dim.px(Integer.MAX_VALUE - 1000000));

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

        this.onHoverLine = data.onHoverLine.getValue();
        this.onTriggeredLine = data.onTriggeredLine.getValue();

        this.rootOwnerId = data.rootOwnerId;

        setPadding(Padd.px(0));

        setMinimumWidth(Dim.px(Integer.MIN_VALUE));
        setMinimumHeight(Dim.px(Integer.MIN_VALUE));
        setMaximumWidth(Dim.px(Integer.MAX_VALUE));
        setMaximumHeight(Dim.px(Integer.MAX_VALUE));

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
    }

    private void registerCommonEvents(){
        //Region Hover
        {
            this.onHoveredEvent.subscribeManaged(() -> {
                if(hasParent()){
                    getParent().onHoveredChildEvent.invoke(this);
                }

                ModManager.SayTheSpire.outputCond(getOnHoverLine());
            });
            this.onHoveredChildEvent.subscribeManaged(child -> {
                if(hasParent()){
                    getParent().onHoveredChildEvent.invoke(child);
                }
            });

            this.onHoverTickEvent.subscribeManaged((time) -> {
                if(hasParent()){
                    getParent().onHoverTickChildEvent.invoke(UIElement.this, time);
                }
            });
            this.onHoveredChildEvent.subscribeManaged(child -> {
                if(hasParent()){
                    getParent().onHoverTickChildEvent.invoke(child, totalHoverDuration);
                }
            });

            this.onUnhoveredEvent.subscribeManaged(() -> {
                if(hasParent()){
                    getParent().onUnhoveredChildEvent.invoke(this);
                }
            });
            this.onUnhoveredChildEvent.subscribeManaged(child -> {
                if(hasParent()){
                    getParent().onUnhoveredChildEvent.invoke(child);
                }
            });
        }

        //Context Menu
        {
            this.onRightClickEvent.subscribeManaged(() -> {
                if(contextMenuOptions.isEmpty()){
                    return;
                }

                Vector2 mousePos = UIHelpers.getMouseWorldPosition();

                ContextMenu contextMenu = new ContextMenu(Pos.px(mousePos.x), Pos.px(mousePos.y));
                for(Map.Entry<UUID, ContextMenu.IContextMenuOption> entry : contextMenuOptions.entrySet()){
                    contextMenu.addChild(entry.getValue());
                }
                contextMenu.open();
            });

            preLeftClickGlobalEvent.subscribe(this, (element) -> {
                if(this.isContextual() && element != this && !element.isDescendantOf(this)){
                    dispose();
                }
            });

            preRightClickGlobalEvent.subscribe(this, (element) -> {
                if(this.isContextual() && element != this && !element.isDescendantOf(this)){
                    dispose();
                }
            });
        }

        // Interactions
        {
            onConfirmInteractionEvent.subscribe(this, this::onLeftClick);
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
            parent.removeChildByInstance(this);
        }
        else{
            close();
        }

        delayedActions.clear();

        disposed = true;

        if(tooltipObject != null){
            tooltipObject.close();
            tooltipObject.dispose();
        }
    }

    //endregion

    //region Methods

    //region Update & Render
    public final void update(){
        if(!shouldUpdate()) return;

        updating = true;

        updateChildren();
        if(disposed) return;

        preUpdateEvent.invoke();
        updateSelf();
        postUpdateEvent.invoke();

        updating = false;

        initialized = true;
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
                if(playingAnimation instanceof UIExitAnimation){
                    canHide = false;
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
                if(this.hb.hovered || controllerSelected || proxyHovered){
                    totalHoverDuration += Gdx.graphics.getDeltaTime();
                    onHoverTick(totalHoverDuration);
                }
            }

            if(hbHoveredCache && (!this.hb.hovered && !this.hb.justHovered)){
                onUnhovered();
            }

            if(isEnabled()){
                if(isMouseHovered()){
                    if(InputHelper.justClickedLeft){
                        onLeftClick(false);
                        if(!isPassthrough()){
                            InputHelper.justClickedLeft = false;
                        }
                    }
                    if(InputHelper.justClickedRight){
                        onRightClick();
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
                if(playingAnimation.getAnimationState() == UIAnimation.EAnimationState.IDLE){
                    playingAnimation.start();
                }

                playingAnimation.update();

                if(playingAnimation.getAnimationState() == UIAnimation.EAnimationState.FINISHED){
                    playingAnimation.finishInstantly();
                    playingAnimation = null;
                }
            }
            else{
                if(idleAnimation != null && idleAnimation.getAnimationState() != UIAnimation.EAnimationState.FINISHED){
                    playAnimation(idleAnimation);
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
            if(totalLifespan != -1){
                remainingLifespan -= Gdx.graphics.getDeltaTime();
                if(remainingLifespan <= 0){
                    hideAndDisable();
                    //TODO wait for animations to finish
                    dispose();
                    //TODO fire on death event
                }
            }
        }

        //Update Tooltip
        {
            if(tooltipObject != null){
                if(isHovered() && totalHoverDuration > 0.5f){
                    tooltipObject.open();

                    Vector2 tooltipPos = UIHelpers.getMouseWorldPosition();
                    tooltipObject.setLocalPosition(tooltipPos.x, tooltipPos.y);

                    tooltipObject.show();
                }
                else{
                    tooltipObject.hide();
                }
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
        if(!initialized){
            return;
        }

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

        preRenderEvent.invoke(sb);
        preRenderGlobalEvent.invoke(this, sb);
        renderSelf(sb);
        postRenderGlobalEvent.invoke(this, sb);
        postRenderEvent.invoke(sb);

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
                removeChildByInstance(child);
                parent.addChild(child);
            }

            parent.removeChildByInstance(this);
            newParent.addChild(this);
        }
        else {
            if(parent != null){
                parent.removeChildByInstance(this);
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

    public float getParentWidthSafe(){
        return parent != null ? parent.getWidth() : 1920;
    }
    public float getParentHeightSafe(){
        return parent != null ? parent.getHeight() : 1080;
    }

    //endregion

    //region Children
    public void addChild(UIElement child){
        if(children.contains(child)){
            return;
        }

        this.children.add(child);
        child.setParent(this);

        if(!child.hasComponent(UITransientElementComponent.class)){
            onChildAddedEvent.invoke(child);
            onChildrenChanged();
        }
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

        if(!child.hasComponent(UITransientElementComponent.class)){
            onChildAddedEvent.invoke(child);
            onChildrenChanged();
        }
    }

    public void swapChildren(int index1, int index2){
        Collections.swap(this.children, index1, index2);
    }

    public boolean hasChild(UIElement child){
        return children.contains(child);
    }

    public void removeChildByInstance(UIElement child){
        if(children.remove(child)){
            child.setParent(null);

            if(!child.hasComponent(UITransientElementComponent.class)){
                onChildRemovedEvent.invoke(child);
                onChildrenChanged();
            }
        }
    }
    public void removeChildById(String childId){
        UIElement child = findChildById(childId);
        if(child == null){
            return;
        }

        removeChildByInstance(child);
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

        if(!original.hasComponent(UITransientElementComponent.class)){
            onChildRemovedEvent.invoke(original);
        }

        if(!replacement.hasComponent(UITransientElementComponent.class)){
            onChildAddedEvent.invoke(replacement);
            onChildrenChanged();
        }
    }
    public void clearChildren(){
        //TODO this is currently called from both dispose (where the parent is already cleared) and from when it isnt, fix
        for(UIElement child : children) {
            child.setParent(null);
            onChildRemovedEvent.invoke(child);
        }

        children.clear();
        onChildrenChanged();
    }

    protected void onChildrenChanged(){
        onHierarchyChangedEvent.invoke();
        onChildrenChangedEvent.invoke();

        if(getWidthRaw() instanceof AutoDimension){
            getWidthRaw().requestRecalculation();
            onDimensionsChanged();
        }
        if(getHeightRaw() instanceof AutoDimension){
            getHeightRaw().requestRecalculation();
            onDimensionsChanged();
        }
    }

    public UIElement getFirstChild(){
        if(children.isEmpty()) return null;
        return children.get(0);
    }
    public UIElement getLastChild(){
        if(children.isEmpty()) return null;
        return children.get(children.size() - 1);
    }
    public UIElement getChildByIndex(int index){
        if(index < 0 || index >= children.size()){
            return null;
        }
        return children.get(index);
    }
    public ArrayList<UIElement> getChildren(){
        return new ArrayList<>(children);
    }
    public <T extends UIElement> ArrayList<T> getChildren(Class<T> desiredType){
        ArrayList<T> children = new ArrayList<>();
        for(UIElement child : this.children){
            if(desiredType.isAssignableFrom(child.getClass())){
                children.add((T) child);
            }
        }
        return children;
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
    public void setLocalPositionX(float newPosition){
        setLocalPosition(Pos.px(newPosition), getLocalPositionYRaw());
    }
    public void setLocalPositionY(float newPosition){
        setLocalPosition(getLocalPositionXRaw(), Pos.px(newPosition));
    }
    public void setLocalPosition(float newPositionX, float newPositionY){
        setLocalPosition(Pos.px(newPositionX), Pos.px(newPositionY));
    }
    public void setLocalPosition(AbstractPosition newX, AbstractPosition newY){
        setLocalPosition_internal(newX, newY);
    }
    private final void setLocalPosition_internal(AbstractPosition newX, AbstractPosition newY){
        AbstractPosition oldPosX = localPosX;
        AbstractPosition oldPosY = localPosY;

        localPosX = newX;
        localPosX.setReference(AbstractPosition.ReferencePosition.X);
        localPosY = newY;
        localPosY.setReference(AbstractPosition.ReferencePosition.Y);

        if(oldPosX != null && oldPosY != null && (!oldPosX.equals(localPosX) || !oldPosY.equals(localPosY))){
            onPositionChanged();
        }
    }

    public float getLocalPositionX(){
        return getLocalPositionXRaw().getCalculatedValue();
    }
    public float getLocalPositionY(){
        return getLocalPositionYRaw().getCalculatedValue();
    }
    public Vector2 getLocalPosition(){
        return new Vector2(getLocalPositionX(), getLocalPositionY());
    }

    public AbstractPosition getLocalPositionXRaw(){
        return localPosX;
    }
    public AbstractPosition getLocalPositionYRaw(){
        return localPosY;
    }

    public void setLocalPositionCenteredX(int newPos){
        setLocalPositionX(newPos - (getWidth() * 0.5f));
    }
    public void setLocalPositionCenteredY(int newPos){
        setLocalPositionY(newPos - (getHeight() * 0.5f));
    }
    public void setLocalPositionCentered(int newPosX, int newPosY){
        float wHalf = (getWidth() * 0.5f);
        float hHalf = (getHeight() * 0.5f);
        setLocalPosition(newPosX - wHalf, newPosY - hHalf);
    }

    public float getLocalPositionCenteredX(){
        return getLocalPositionCentered().x;
    }
    public float getLocalPositionCenteredY(){
        return getLocalPositionCentered().y;
    }
    public Vector2 getLocalPositionCentered(){
        Vector2 localPosition = getLocalPosition();
        localPosition.x += (getWidth() * 0.5f);
        localPosition.y += (getHeight() * 0.5f);
        return localPosition;
    }

    //endregion

    //region World Position

    public void setWorldPositionX(float newPos){
        setWorldPosition(newPos, getWorldPositionY());
    }
    public void setWorldPositionY(float newPos){
        setWorldPosition(getWorldPositionX(), newPos);
    }
    public void setWorldPosition(float newPosX, float newPosY){
        float localPosX = newPosX - (getParent() != null ? getParent().getWorldPositionX() : 0);
        localPosX -= getPaddingLeft();
        localPosX -= getOffsetX();

        float localPosY = newPosY - (getParent() != null ? getParent().getWorldPositionY() : 0);
        localPosY -= getPaddingBottom();
        localPosY -= getOffsetY();

        if(localPosX != getLocalPositionX() && localPosY != getLocalPositionY()){
            setLocalPosition(localPosX, localPosY);
        }
        else{
            if(localPosX != getLocalPositionX()){
                setLocalPositionX(localPosX);
            }
            if(localPosY != getLocalPositionY()){
                setLocalPositionY(localPosY);
            }
        }
    }

    public float getWorldPositionX(){
        float parentWorldX = getParent() != null ?
                getParent().getWorldPositionX() + (this instanceof ItemBox && !(getParent() instanceof ItemBox) ? 0 : getParent().getChildOffsetX()) :
                0;

        return parentWorldX + getLocalPositionX();
    }
    public float getWorldPositionY(){
        float parentWorldY = getParent() != null ?
                getParent().getWorldPositionY() + (this instanceof ItemBox && !(getParent() instanceof ItemBox) ? 0 : getParent().getChildOffsetY()) :
                0;

        return parentWorldY + getLocalPositionY();
    }
    public Vector2 getWorldPosition(){
        return new Vector2(getWorldPositionX(), getWorldPositionY());
    }


    public final float getWorldPositionCenteredX(){
        return getWorldPositionCentered().x;
    }
    public final float getWorldPositionCenteredY(){
        return getWorldPositionCentered().y;
    }
    public final Vector2 getWorldPositionCentered(){
        Vector2 worldPosition = getWorldPosition();
        worldPosition.x += getWidth() * 0.5f;
        worldPosition.y += getHeight() * 0.5f;
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
    public void offset(float xOffset, float yOffset){
        if(xOffset == 0 && yOffset == 0) return;

        offsetX += xOffset;
        offsetY += yOffset;

        if(!isWithinBounds()){
            offsetX -= xOffset;
            offsetY -= yOffset;
            return;
        }

        onPositionChanged();
    }

    //endregion

    //region Transforming

    public Vector2 worldToLocal(Vector2 worldPosition){
        return new Vector2((worldPosition.x - getWorldPositionX()), worldPosition.y - getWorldPositionY());
    }
    public Vector2 localToWorld(Vector2 localPosition){
        return new Vector2(localPosition.x + getWorldPositionX(), localPosition.y + getWorldPositionY());
    }

    public Vector2 worldToLocalUnscaled(Vector2 worldPosition){
        return new Vector2((worldPosition.x - getWorldPositionX()) / getScaleX(), (worldPosition.y - getWorldPositionY()) / getScaleY());
    }
    public Vector2 localToWorldUnscaled(Vector2 localPosition){
        return new Vector2(localPosition.x * getScaleX() + getWorldPositionX(), (localPosition.y * getScaleY() + getWorldPositionY()));
    }


    //endregion

    public void onPositionChanged(){
        onPositionChangedEvent.invoke(this);

        requestLocalPositionXRecalculation();
        requestLocalPositionYRecalculation();

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
            requestWidthRecalculation();
        }

        if(getHeightRaw() instanceof AutoDimension){
            requestHeightRecalculation();
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

        Float worldLeft = containerBounds.getWorldLeft(this);
        Float worldRight = containerBounds.getWorldRight(this);
        Float worldTop = containerBounds.getWorldTop(this);
        Float worldBottom = containerBounds.getWorldBottom(this);

        Vector2 localBottomLeft;
        Vector2 localTopRight;

        if(hasParent()){
            localBottomLeft = parent.worldToLocal(new Vector2(worldLeft, worldBottom));
            localTopRight = parent.worldToLocal(new Vector2(worldRight, worldTop));
        }
        else{
            localBottomLeft = new Vector2(worldLeft, worldBottom);
            localTopRight = new Vector2(worldRight, worldTop);
        }

        float horizontalOffset = 0;
        float verticalOffset = 0;

        if(containerBoundCalculationType == BoundCalculationType.CONTAINS_HALF){
            horizontalOffset = (getWidth() * 0.5f);
            verticalOffset = (getHeight() * 0.5f);
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

    public void ensureElementWithinBounds(){
        Vector4f desiredBounds = calculateDesiredBounds();
        if(desiredBounds == null) return;

        float desiredPositionX = desiredBounds.x;
        float desiredPositionY = desiredBounds.y;
        float desiredWidth = desiredBounds.z;
        float desiredHeight = desiredBounds.w;

        if(desiredWidth != getWidth() || desiredHeight != getHeight() || desiredPositionX != getLocalPositionX() || desiredPositionY != getLocalPositionY()){
            getLocalPositionXRaw().overrideCalculatedValue(desiredPositionX);
            getLocalPositionYRaw().overrideCalculatedValue(desiredPositionY);
            getWidthRaw().overrideCalculatedValue(desiredWidth);
            getHeightRaw().overrideCalculatedValue(desiredHeight);
        }
    }

    public Vector4f calculateDesiredBounds(){
        PositionBounds localContainerBounds = getLocalContainerBounds();
        if(localContainerBounds == null) return null;

        Float desiredWidth = null;
        Float desiredHeight = null;

        float desiredPositionX = getLocalPositionX();
        float desiredPositionY = getLocalPositionY();

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
                float boundBoxUpperPosX = desiredPositionX + getWidth();

                if(boundBoxUpperPosX > localContainerBounds.right){
                    desiredPositionX = localContainerBounds.right - getWidth();
                    boundBoxUpperPosX = desiredPositionX + getWidth();
                }

                if(desiredPositionX < localContainerBounds.left){
                    desiredPositionX = localContainerBounds.left;
                    boundBoxUpperPosX = desiredPositionX + getWidth();
                }

                desiredWidth = getWidth();

                if(boundBoxUpperPosX > localContainerBounds.right){
                    desiredWidth = (localContainerBounds.right - localContainerBounds.left);
                }

            }

            if(height instanceof PixelDimension){
                float boundBoxUpperPosY = desiredPositionY + getHeight();

                if(boundBoxUpperPosY > localContainerBounds.top){
                    desiredPositionY = localContainerBounds.top - getHeight();
                    boundBoxUpperPosY = desiredPositionY + getHeight();
                }

                if(desiredPositionY < localContainerBounds.bottom){
                    desiredPositionY = localContainerBounds.bottom;
                    boundBoxUpperPosY = desiredPositionY + getHeight();
                }

                desiredHeight = getHeight();

                if(boundBoxUpperPosY > localContainerBounds.top){
                    desiredHeight = (localContainerBounds.top - localContainerBounds.bottom);
                }
            }
        }

        if(desiredWidth == null) desiredWidth = getWidth();
        if(desiredHeight == null) desiredHeight = getHeight();

        return new Vector4f(desiredPositionX, desiredPositionY, desiredWidth, desiredHeight);
    }

    private boolean isWithinBounds(){
        Vector4f desiredBounds = calculateDesiredBounds();
        if(desiredBounds == null) return true;

        return getLocalPositionX() == desiredBounds.x && getLocalPositionY() == desiredBounds.y && getWidth() == desiredBounds.z && getHeight() == desiredBounds.w;
    }

    //endregion

    //region Interactions
    public boolean onLeftInteraction(boolean byProxy){
        ArrayList<Boolean> interactionResults = onLeftInteractionEvent.invokeWhile(byProxy, (invocationResult) -> !invocationResult);
        return interactionResults.stream().anyMatch(result -> result);
    }
    public boolean onRightInteraction(boolean byProxy){
        ArrayList<Boolean> interactionResults = onRightInteractionEvent.invokeWhile(byProxy, (invocationResult) -> !invocationResult);
        return interactionResults.stream().anyMatch(result -> result);
    }
    public boolean onUpInteraction(boolean byProxy){
        ArrayList<Boolean> interactionResults = onUpInteractionEvent.invokeWhile(byProxy, (invocationResult) -> !invocationResult);
        return interactionResults.stream().anyMatch(result -> result);
    }
    public boolean onDownInteraction(boolean byProxy){
        ArrayList<Boolean> interactionResults = onDownInteractionEvent.invokeWhile(byProxy, (invocationResult) -> !invocationResult);
        return interactionResults.stream().anyMatch(result -> result);
    }

    public boolean onConfirmInteraction(boolean byProxy){
        ArrayList<Boolean> interactionResults = onConfirmInteractionEvent.invokeWhile(byProxy, (invocationResult) -> !invocationResult);
        return interactionResults.stream().anyMatch(result -> result);
    }
    public boolean onCancelInteraction(boolean byProxy){
        ArrayList<Boolean> interactionResults = onCancelInteractionEvent.invokeWhile(byProxy, (invocationResult) -> !invocationResult);
        return interactionResults.stream().anyMatch(result -> result);
    }
    //endregion

    //region Controller Selection
    public void disableControllerSelectionAndSayTheSpireIntegration(){
        setControllerSelectable(false);
        setSayTheSpireElementName((String)null);
        setSayTheSpireElementType((String)null);
    }
    public void setControllerSelectable(boolean controllerSelectable){
        this.controllerSelectable = controllerSelectable;
    }
    public boolean isControllerSelectable(){
        return controllerSelectable;
    }

    public ArrayList<UIElement> getAllSelectableChildren(){
        ArrayList<UIElement> allChildren = new ArrayList<>();
        for(UIElement child : children){
            if((child.isControllerSelectable() && child.isEnabled()) || child.isSelected()){
                allChildren.add(child);
            }
            allChildren.addAll(child.getAllSelectableChildren());
        }
        return allChildren;
    }

    public void select(boolean byController){
        if(byController){
            controllerSelected = true;
            onHovered();
        }

        setSelected(true);
    }
    public void deselect(){
        setSelected(false);

        if(controllerSelected){
            onUnhovered();
            controllerSelected = false;
        }
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

    public boolean isControllerSelected(){
        return controllerSelected;
    }

    public void onSelectionStateChanged(){
        postSelectionStateChangedEvent.invoke(isSelected());
        postSelectionStateChangedEvent_Global.invoke(this, isSelected());
    }

    //endregion

    //region Visible & Enabled States

    //region Visibility

    public void hide(){
        if(!isVisible || pendingHide) return;

        if(exitAnimation == null){
            for(UIElement child : getChildren()){
                if(child.isVisible() && child.exitAnimation != null){
                    pendingHide = true;
                    child.playAnimation(child.exitAnimation);
                }
            }
        }
        else{
            pendingHide = true;
            playAnimation(exitAnimation);
        }

        if(!pendingHide){
            setVisibility(false);
        }
    }
    public void hideInstantly(){
        if(!isVisible) return;

        setVisibility(false);
    }

    public void show(){
        if(isVisible && !pendingHide) {
            if(shouldDrawFocusOnOpen()){
                UIManager.drawControllerFocusCond(this);
            }
        }

        setVisibility(true);
        pendingHide = false;

        if(entryAnimation == null){
            for(UIElement child : getChildren()){
                if(child.isVisible() && child.entryAnimation != null){
                    child.playAnimation(child.entryAnimation);
                }
            }
        }
        else{
            playAnimation(entryAnimation);
        }
    }
    public void showInstantly(){
        if(isVisible) return;

        setVisibility(true);
    }

    public void toggleVisibility(){
        if(isVisible()){
            hide();
        }
        else{
            show();
        }
    }
    public void toggleVisibilityInstantly(){
        if(isVisible()){
            hideInstantly();
        }
        else{
            showInstantly();
        }
    }

    protected void setVisibility(boolean visible){
        boolean isActive = isActive();

        if(isVisible == visible) return;
        isVisible = visible;

        onVisiblityChanged();

        if(isActive != isActive()){
            onActiveStateChanged();
        }
    }

    public boolean isVisible(){
        if(hasParent() && !parent.isVisible()) return false;
        return isVisible;
    }
    public boolean isVisibleRaw(){
        return isVisible;
    }

    protected void onVisiblityChanged(){
        if(getParent() != null) getParent().onChildVisibilityChanged(this);
        for(UIElement child : children){
            child.onParentVisibilityChanged();
        }
    }
    protected void onParentVisibilityChanged(){

    }
    protected void onChildVisibilityChanged(UIElement changedChild){
        if(getWidthRaw() instanceof AutoDimension){
            getWidthRaw().requestRecalculation();
        }

        if(getHeightRaw() instanceof AutoDimension){
            getHeightRaw().requestRecalculation();
        }
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
        if(isEnabled == enabled) return;

        isEnabled = enabled;
        onEnabledStatusChanged();
    }

    public boolean isEnabled(){
        if(hasParent() && !parent.isEnabled()) return false;
        return isEnabled && (getParent() == null || !(getParent() instanceof ILayoutProvider) || ((ILayoutProvider) getParent()).isChildEnabled(this));
    }
    public boolean isEnabledRaw(){
        return isEnabled;
    }

    protected void onEnabledStatusChanged(){
        if(isControllerModal()){
            if(isEnabled()){
                UIManager.drawControllerFocusCond(this);

                ModManager.SayTheSpire.outputCond(getOnEnabledLine());
            }
            else{
                UIManager.loseFocus(this);
            }
        }

        boolean isActive = isActive();

        if(getParent() != null) getParent().onChildEnabledStatusChanged(this);
        for(UIElement child : children){
            child.onParentEnabledStatusChanged();
        }

        if(isActive != isActive()){
            onActiveStateChanged();
        }
    }
    protected void onParentEnabledStatusChanged(){
        if(isControllerModal()){
            if(isEnabled()){
                UIManager.drawControllerFocusCond(this);

                ModManager.SayTheSpire.outputCond(getOnEnabledLine());
            }
            else{
                UIManager.loseFocus(this);
            }
        }
    }
    protected void onChildEnabledStatusChanged(UIElement changedChild){

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

    public boolean isActiveRaw(){
        return isVisibleRaw() || isEnabledRaw();
    }

    public void onActiveStateChanged(){
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
        width.setReference(AbstractDimension.ReferenceDimension.WIDTH);
    }
    private final void setHeightRaw(AbstractDimension newHeight){
        height = newHeight;
        height.setReference(AbstractDimension.ReferenceDimension.HEIGHT);
    }

    public void setWidth(float newWidth){
        setWidth(Dim.px(newWidth));
    }
    public void setHeight(float newHeight){
        setHeight(Dim.px(newHeight));
    }
    public void setDimensions(float newWidth, float newHeight){
        setDimensions(Dim.px(newWidth), Dim.px(newHeight));
    }

    public void onDimensionsChanged(){
        if(getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER || getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            requestLocalPositionXRecalculation();
        }
        if(getVerticalAlignment() == Alignment.VerticalAlignment.CENTER || getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            requestLocalPositionYRecalculation();
        }

        for(UIElement child : children){
            child.onParentDimensionsChanged();
        }
        if(hasParent()){
            getParent().onChildDimensionsChanged(this);
        }

        onDimensionsChangedEvent.invoke(this);
    }

    protected void onParentDimensionsChanged(){
        if(alignment.horizontalAlignment == Alignment.HorizontalAlignment.RIGHT || alignment.horizontalAlignment == Alignment.HorizontalAlignment.CENTER){
            requestLocalPositionXRecalculation();
        }
        if(alignment.verticalAlignment == Alignment.VerticalAlignment.TOP || alignment.verticalAlignment == Alignment.VerticalAlignment.CENTER){
            requestLocalPositionYRecalculation();
        }
    }
    public void onChildDimensionsChanged(UIElement child){
        if(getWidthRaw() instanceof AutoDimension || getWidthRaw() instanceof MirrorDimension){
            requestWidthRecalculation();
            onDimensionsChanged();
        }

        if(getHeightRaw() instanceof AutoDimension || getHeightRaw() instanceof MirrorDimension){
            requestHeightRecalculation();
            onDimensionsChanged();
        }
    }

    public float getWidth(){
        float width = getWidthRaw().getCalculatedValue();
        float minWidth = getMinimumWidthRaw().getCalculatedValue();
        float maxWidth = getMaximumWidthRaw().getCalculatedValue();

        return Math.max(minWidth, Math.min(maxWidth, width));
    }
    public float getHeight(){
        float height = getHeightRaw().getCalculatedValue();
        float minHeight = getMinimumHeightRaw().getCalculatedValue();
        float maxHeight = getMaximumHeightRaw().getCalculatedValue();

        return Math.max(minHeight, Math.min(maxHeight, height));
    }
    public Vector2 getDimensions(){
        return new Vector2(getWidth(), getHeight());
    }

    public AbstractDimension getWidthRaw(){
        return width;
    }
    public AbstractDimension getHeightRaw(){
        return height;
    }

    public void resizeBy(float widthDiff, float heightDiff){
        AbstractDimension widthCopy = width.cpy();
        AbstractDimension heightCopy = height.cpy();

        if(widthDiff != 0) widthCopy.resizeBy(this, widthDiff);
        if(heightDiff != 0) heightCopy.resizeBy(this, heightDiff);

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

    public void setIdleAnimation(UIAnimation idleAnimation){
        this.idleAnimation = idleAnimation;
    }
    public UIAnimation getIdleAnimation(){
        return idleAnimation;
    }

    public void playAnimation(UIAnimation animation){
        if(playingAnimation != null){
            playingAnimation.finishInstantly();
        }

        playingAnimation = animation;
        playingAnimation.setAnimationState(UIAnimation.EAnimationState.IDLE);
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
        for(UIElement child : children){
            if(!(child.isActive()) || child.hasComponent(UIOverlayElementComponent.class)){
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

    public boolean withinParent(){
        return parent != null && within(parent);
    }
    public boolean within(UIElement other){
        return getWorldBounds().within(other.getWorldBounds());
    }

    public boolean hasHorizontalChildrenOOB(){
        Pair<Float, Float> OOBAmount = getHorizontalChildrenOOBAmount();
        return OOBAmount.getKey() > 0 || OOBAmount.getValue() > 0;
    }
    public Pair<Float, Float> getHorizontalChildrenOOBAmount(){
        PositionBounds myBounds = getLocalBounds();
        PositionBounds fullChildBounds = getFullChildLocalBounds();

        if (fullChildBounds == null) {
            return new Pair<>(0f, 0f);
        }

        float leftOOBAmount = 0;
        float rightOOBAmount = 0;

        if(fullChildBounds.left < 0) {
            leftOOBAmount = -fullChildBounds.left;
        }
        if(fullChildBounds.right > myBounds.right){
            rightOOBAmount = fullChildBounds.right - myBounds.right;
        }

        return new Pair<>(leftOOBAmount, rightOOBAmount);
    }

    public boolean hasVerticalChildrenOOB(){
        Pair<Float, Float> OOBAmount = getVerticalChildrenOOBAmount();
        return OOBAmount.getKey() > 0 || OOBAmount.getValue() > 0;
    }
    public Pair<Float, Float> getVerticalChildrenOOBAmount(){
        PositionBounds myBounds = getLocalBounds();
        PositionBounds fullChildBounds = getFullChildLocalBounds();

        if (fullChildBounds == null) {
            return new Pair<>(0f, 0f);
        }

        float bottomOOBAmount = 0;
        float topOOBAmount = 0;

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
        if(isOpen()) return;

        UIManager.openUIElement(this);
    }

    public boolean isOpen(){
        return UIManager.isOpen(this);
    }

    public void close(){
        if(!isOpen()) return;

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
        setPadding(all.cpy(), all.cpy(), all.cpy(), all.cpy());
    }
    public void setPadding(AbstractPadding leftRight, AbstractPadding topBottom){
        setPadding(topBottom.cpy(), leftRight.cpy(), topBottom.cpy(), leftRight.cpy());
    }
    public void setPadding(AbstractPadding top, AbstractPadding right, AbstractPadding bottom, AbstractPadding left){
        this.paddingTop = top;
        this.paddingTop.setReference(AbstractPadding.ReferenceDimension.VERTICAL);
        this.paddingRight = right;
        this.paddingRight.setReference(AbstractPadding.ReferenceDimension.HORIZONTAL);
        this.paddingBottom = bottom;
        this.paddingBottom.setReference(AbstractPadding.ReferenceDimension.VERTICAL);
        this.paddingLeft = left;
        this.paddingLeft.setReference(AbstractPadding.ReferenceDimension.HORIZONTAL);
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

    public float getPaddingTop(){
        return paddingTop.getCalculatedValue();
    }
    public float getPaddingRight(){
        return paddingRight.getCalculatedValue();
    }
    public float getPaddingBottom(){
        return paddingBottom.getCalculatedValue();
    }
    public float getPaddingLeft(){
        return paddingLeft.getCalculatedValue();
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

    public void hover(){
        if(isHovered()) return;

        onHovered();
    }

    public void unhover(){
        if(!isHovered()) return;

        onUnhovered();
    }

    public void proxyHover(){
        boolean hoveredExternally = isHovered();

        if(!proxyHovered){
            this.proxyHovered = true;
        }

        if(!hoveredExternally){
            onHovered();
        }
    }

    public void proxyUnhover(){
        if(!proxyHovered) return;

        proxyHovered = false;

        if(!isHovered()){
            onUnhovered();
        }
    }

    protected void onHovered(){
        totalHoverDuration = 0.f;
        if(!isPassthrough()) InputHelpers.alreadyHovered = true;

        GlobalEvents.sendMessage(new PreUIHoverEvent(this));
        onHoveredEvent.invoke();

        postHoverGlobalEvent.invoke(this);
    }
    protected void onHoverTick(float totalTickDuration){
        if(!isPassthrough()) InputHelpers.alreadyHovered = true;

        onHoverTickEvent.invoke(totalTickDuration);
    }
    protected void onUnhovered(){
        totalHoverDuration = 0.f;

        GlobalEvents.sendMessage(new PreUIUnhoverEvent(this));
        onUnhoveredEvent.invoke();
    }

    public boolean isHovered(){ return (hb.hovered || hb.justHovered || controllerSelected || proxyHovered); }
    public boolean isMouseHovered(){ return (hb.hovered || hb.justHovered); }

    public boolean isProxyHovered() { return proxyHovered; }

    public boolean isHoveredOrChildHovered(){
        if(isHovered()) return true;
        for(UIElement child : children){
            if(child.isHoveredOrChildHovered()) return true;
        }
        return false;
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

    protected boolean onLeftClick(boolean byProxy){
        preLeftClickGlobalEvent.invoke(this);
        GlobalEvents.sendMessage(new PreUILeftClickEvent(this));

        totalLeftClickDuration = 0.f;
        holdingLeft = true;

        ModManager.SayTheSpire.outputCond(getOnTriggerLine());

        if(!controllerSelected && !byProxy){
            select(false);
        }

        onLeftClickEvent.invoke();

        return onLeftClickEvent.count() > 0;
    }
    protected void onLeftClickHeld(float totalDuration){
        onLeftClickHeldEvent.invoke(totalDuration);
    }
    protected void onLeftClickRelease(){
        holdingLeft = false;

        onLeftClickReleaseEvent.invoke();
    }

    public boolean isHeld(){
        return holdingLeft;
    }

    //endregion

    //region Right Click

    protected void onRightClick(){
        preRightClickGlobalEvent.invoke(this);

        totalRightClickDuration = 0.f;
        holdingRight = true;

        ModManager.SayTheSpire.outputCond(getOnTriggerLine());

        onRightClickEvent.invoke();
    }
    protected void onRightClickHeld(float totalDuration){
        onRightClickHeldEvent.invoke(totalDuration);
    }
    protected void onRightButtonRelease(){
        holdingRight = false;

        onRightClickReleaseEvent.invoke();
    }

    //endregion

    //region Say the Spire

    //region Element Name

    public void setSayTheSpireElementName(AbstractStringBinding binding){
        this.sayTheSpireElementName = binding;
    }
    public void setSayTheSpireElementName(String newLine){
        this.sayTheSpireElementName = Str.stat(newLine);
    }
    public void setSayTheSpireElementType(AbstractStringBinding binding){
        this.sayTheSpireElementType = binding;
    }
    public void setSayTheSpireElementType(String newLine){
        this.sayTheSpireElementType = Str.stat(newLine);
    }
    public void setSayTheSpireElementValue(AbstractStringBinding binding){
        this.sayTheSpireElementValue = binding;
    }
    public void setSayTheSpireElementValue(String newLine){
        this.sayTheSpireElementValue = Str.stat(newLine);
    }
    public String getSayTheSpireElementNameAndType(boolean withValue){
        String elementName = sayTheSpireElementName.getBoundObject();
        String elementType = sayTheSpireElementType.getBoundObject();
        String elementValue = sayTheSpireElementValue.getBoundObject();

        if(elementName == null && elementType == null) return null;

        String sentence = "";
        if(elementName != null) sentence += elementName + " ";
        if(elementType != null) sentence += elementType + " ";
        if(withValue && elementValue != null && !elementValue.isEmpty()) sentence += "with value of " + elementValue;
        return sentence;
    }

    //endregion

    //region Hover Lines

    public void setOnHoverLine(AbstractStringBinding binding){
        this.onHoverLine = binding;
    }
    public void setOnHoverLine(String newLine){
        this.onHoverLine = Str.stat(newLine);
    }
    public String getOnHoverLine(){
        String binding = onHoverLine.getBoundObject();
        if(binding != null) return binding;

        return getSayTheSpireElementNameAndType(true);
    }

    //endregion Hover Lines

    //region Trigger Lines

    public void setOnTriggerLine(AbstractStringBinding binding){
        this.onTriggeredLine = binding;
    }
    public void setOnTriggerLine(String newLine) {
        this.onTriggeredLine = Str.stat(newLine);
    }
    public String getOnTriggerLine(){
        return onTriggeredLine.getBoundObject();
    }

    //endregion

    //region On Active Lines

    public void setOnEnabledLine(AbstractStringBinding binding){
        this.onEnabledLine = binding;
    }
    public void setOnActivateLine(String newLine) {
        this.onEnabledLine = Str.stat(newLine);
    }
    public String getOnEnabledLine(){
        return onEnabledLine.getBoundObject();
    }

    //endregion

    //endregion Say the Spire

    //region Local Child Offset

    public void setChildOffsetX(float offset){
        if(childOffsetX == offset) return;

        childOffsetX = offset;

        for(UIElement child : children){
            child.onPositionChanged();
        }
    }
    public void setChildOffsetY(float offset){
        if(childOffsetY == offset) return;

        childOffsetY = offset;

        for(UIElement child : children){
            child.onPositionChanged();
        }
    }

    public float getChildOffsetX(){
        return childOffsetX;
    }
    public float getChildOffsetY(){
        return childOffsetY;
    }

    public final float getLocalChildOffsetXRaw(){
        return childOffsetX;
    }
    public final float getLocalChildOffsetYRaw(){
        return childOffsetY;
    }

    public float getTotalLocalChildOffsetX(){
        return childOffsetX + (hasParent() ? parent.getTotalLocalChildOffsetX() : 0);
    }

    public float getTotalLocalChildOffsetY(){
        return childOffsetY + (hasParent() ? parent.getTotalLocalChildOffsetY() : 0);
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

    public boolean isControllerModal(){
        return isModal() || isContextual();
    }
    public UIElement getControllerModalParent(){
        if(isControllerModal()) return this;

        if(hasParent()){
            return parent.getControllerModalParent();
        }

        return null;
    }

    //endregion

    //region Focus Drawing

    public void setDrawFocusOnOpen(boolean drawControllerFocusOnOpen){
        this.drawFocusOnOpen = drawControllerFocusOnOpen;
    }
    public boolean shouldDrawFocusOnOpen(){
        return drawFocusOnOpen || isControllerModal();
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

    protected void onScaleChanged(){
        onScaleChangedEvent.invoke(this);

        if(getParent() != null) getParent().onChildScaleChanged(this);
        for (UIElement child : getChildren()) child.onParentScaleChanged();
    }
    protected void onParentScaleChanged(){

    }
    protected void onChildScaleChanged(UIElement child){

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

    //region Context Menu Options

    public UUID addContextMenuOption(ContextMenu.IContextMenuOption action){
        UUID id = UUID.randomUUID();
        contextMenuOptions.put(id, action);
        return id;
    }

    public void removeContextMenuOption(UUID optionId){
        contextMenuOptions.remove(optionId);
    }

    //endregion

    //region Tooltip

    public void setTooltip(String tooltip){
        if(tooltipObject != null){
            tooltipObject.dispose();
        }

        tooltipObject = new Tooltip(tooltip);
    }

    public void setTooltipObject(UIElement tooltipObject){
        if(this.tooltipObject != null){
            this.tooltipObject.dispose();
        }

        this.tooltipObject = tooltipObject;
    }

    public UIElement getTooltipObject(){
        return tooltipObject;
    }

    //endregion

    //region Position & Dimension Calculations

    public ArrayList<Pair<Integer, ElementCalculationManager.ElementCalculationInstruction>> collectCalculationInstructions(){
        ArrayList<Pair<Integer, ElementCalculationManager.ElementCalculationInstruction>> calculationInstructions = new ArrayList<>();

        if(getLocalPositionXRaw().needsRecalculation()) {
            calculationInstructions.add(getLocalPositionXRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getLocalPositionXRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getLocalPositionYRaw().needsRecalculation()){
            calculationInstructions.add(getLocalPositionYRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getLocalPositionYRaw().getCalculationInstruction(this); //* For Debug
            }
        }

        if(getMinimumWidthRaw().needsRecalculation()) {
            calculationInstructions.add(getMinimumWidthRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getMinimumWidthRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getMinimumHeightRaw().needsRecalculation()) {
            calculationInstructions.add(getMinimumHeightRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getMinimumHeightRaw().getCalculationInstruction(this); //* For Debug
            }
        }

        if(getMaximumWidthRaw().needsRecalculation()) {
            calculationInstructions.add(getMaximumWidthRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getMaximumWidthRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getMaximumHeightRaw().needsRecalculation()) {
            calculationInstructions.add(getMaximumHeightRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getMaximumHeightRaw().getCalculationInstruction(this); //* For Debug
            }
        }

        if(needsWidthCalculation()) {
            calculationInstructions.add(getWidthRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getWidthRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(needsHeightCalculation()) {
            calculationInstructions.add(getHeightRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getHeightRaw().getCalculationInstruction(this); //* For Debug
            }
        }

        if(getPaddingLeftRaw().needsRecalculation()){
            calculationInstructions.add(getPaddingLeftRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getPaddingLeftRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getPaddingBottomRaw().needsRecalculation()){
            calculationInstructions.add(getPaddingBottomRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getPaddingBottomRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getPaddingRightRaw().needsRecalculation()){
            calculationInstructions.add(getPaddingRightRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getPaddingRightRaw().getCalculationInstruction(this); //* For Debug
            }
        }
        if(getPaddingTopRaw().needsRecalculation()){
            calculationInstructions.add(getPaddingTopRaw().getCalculationInstruction(this));

            if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                getPaddingTopRaw().getCalculationInstruction(this); //* For Debug
            }
        }

        return calculationInstructions;
    }

    //endregion

    //endregion

    public void requestLocalPositionXRecalculation(){
        getLocalPositionXRaw().requestRecalculation();
    }
    public void requestLocalPositionYRecalculation(){
        getLocalPositionYRaw().requestRecalculation();
    }
    public void requestWidthRecalculation(){
        getWidthRaw().requestRecalculation();
    }
    public void requestHeightRecalculation(){
        getHeightRaw().requestRecalculation();
    }

    public float getOffsetX(){
        return offsetX;
    }
    public float getOffsetY(){
        return offsetY;
    }

    public void setOffsetX(float offsetX){
        this.offsetX = offsetX;
        this.getLocalPositionXRaw().requestRecalculation();
    }
    public void setOffsetY(float offsetY){
        this.offsetY = offsetY;
        this.getLocalPositionYRaw().requestRecalculation();
    }

    public ArrayList<UIElement> getHierarchyForUpdateOrder(){
        ArrayList<UIElement> hierarchy = new ArrayList<>();

        for(UIElement child : children){
            hierarchy.addAll(child.getHierarchyForUpdateOrder());
        }

        hierarchy.add(this);

        return hierarchy;
    }

    public ArrayList<UIElement> getSiblings(){
        if(hasParent()){
            return parent.getChildren().stream().filter(element -> element != this).collect(Collectors.toCollection(ArrayList::new));
        }
        return new ArrayList<>();
    }

    public AbstractPadding getPaddingLeftRaw(){
        return paddingLeft;
    }
    public AbstractPadding getPaddingRightRaw(){
        return paddingRight;
    }
    public AbstractPadding getPaddingTopRaw(){
        return paddingTop;
    }
    public AbstractPadding getPaddingBottomRaw(){
        return paddingBottom;
    }

    public void setMinimumWidth(AbstractDimension minimumWidth){
        this.minimumWidth = minimumWidth;
        this.minimumWidth.setReference(AbstractDimension.ReferenceDimension.WIDTH);
    }
    public void setMinimumHeight(AbstractDimension minimumHeight){
        this.minimumHeight = minimumHeight;
        this.minimumHeight.setReference(AbstractDimension.ReferenceDimension.HEIGHT);
    }

    public void setMaximumWidth(AbstractDimension maximumWidth){
        this.maximumWidth = maximumWidth;
        this.maximumWidth.setReference(AbstractDimension.ReferenceDimension.WIDTH);
    }
    public void setMaximumHeight(AbstractDimension maximumHeight){
        this.maximumHeight = maximumHeight;
        this.maximumHeight.setReference(AbstractDimension.ReferenceDimension.HEIGHT);
    }

    public AbstractDimension getMinimumWidthRaw(){
        return minimumWidth;
    }
    public AbstractDimension getMinimumHeightRaw(){
        return minimumHeight;
    }

    public AbstractDimension getMaximumWidthRaw(){
        return maximumWidth;
    }
    public AbstractDimension getMaximumHeightRaw(){
        return maximumHeight;
    }

    public boolean needsWidthCalculation(){
        return getWidthRaw().needsRecalculation() || getMinimumWidthRaw().needsRecalculation() || getMaximumWidthRaw().needsRecalculation();
    }
    public boolean needsHeightCalculation(){
        return getHeightRaw().needsRecalculation() || getMinimumHeightRaw().needsRecalculation() || getMaximumHeightRaw().needsRecalculation();
    }

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
                .setCategory("Events")
                .setDynamicCreationParameters(new Pair<>("byProxy", Boolean.class));
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

        public StringBindingProperty onHoverLine = new StringBindingProperty(Str.stat(""))
                .setName("On Hover/Select Line")
                .setDescription("Line to say when the element is hovered/selected.")
                .setCategory("Say the Spire");
        public StringBindingProperty onTriggeredLine = new StringBindingProperty(Str.stat(""))
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
