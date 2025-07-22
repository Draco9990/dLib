package dLib.ui.elements.components;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.util.events.localevents.RunnableEvent;

import java.util.UUID;

public class UIDraggableComponent extends AbstractUIElementComponent<UIElement> {
    //region Variables

    private UIElement owner;

    private boolean canDragX = true;
    private boolean canDragY = true;

    private float xDragOffset;
    private float yDragOffset;

    private UUID leftClickEventId;
    private UUID leftClickHeldEventId;

    public RunnableEvent onDraggedEvent = new RunnableEvent();

    //endregion

    //region Constructors

    @Override
    public void onRegisterComponent(UIElement owner) {
        this.owner = owner;

        leftClickEventId = owner.postLeftClickEvent.subscribeManaged(this::onLeftClick);
        leftClickHeldEventId = owner.postLeftClickHeldEvent.subscribeManaged(this::onLeftClickHeld);
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        owner.postLeftClickEvent.unsubscribeManaged(leftClickEventId);
        owner.postLeftClickHeldEvent.unsubscribeManaged(leftClickHeldEventId);
    }

    //endregion

    //region Methods

    //region Drag

    public void setCanDragX(boolean canDragX){
        this.canDragX = canDragX;
    }
    public void setCanDragY(boolean canDragY){
        this.canDragY = canDragY;
    }

    public boolean canDragX(){
        return canDragX;
    }
    public boolean canDragY(){
        return canDragY;
    }

    //endregion

    //region Click

    protected void onLeftClick() {
        xDragOffset = 0;
        yDragOffset = 0;

        if(canDragX) {
            xDragOffset = (InputHelper.mX - owner.getWorldPositionX() * Settings.xScale);
            xDragOffset = (xDragOffset / owner.getScaleX());
        }

        if(canDragY) {
            yDragOffset = (InputHelper.mY - owner.getWorldPositionY() * Settings.yScale);
            yDragOffset = (yDragOffset / owner.getScaleY());
        }
    }

    protected void onLeftClickHeld(float totalDuration) {
        float localDragOffsetX = 0;
        float localDragOffsetY = 0;

        if(xDragOffset != 0){
            localDragOffsetX = (InputHelper.mX - owner.getWorldPositionX() * Settings.xScale);
            localDragOffsetX = (localDragOffsetX / owner.getScaleX());
        }

        if(yDragOffset != 0){
            localDragOffsetY = (InputHelper.mY - owner.getWorldPositionY() * Settings.yScale);
            localDragOffsetY = (localDragOffsetY / owner.getScaleY());
        }

        owner.offset(((localDragOffsetX - xDragOffset) * owner.getScaleX()), ((localDragOffsetY - yDragOffset) * owner.getScaleY()));

        onDraggedEvent.invoke();
    }

    //endregion

    //endregion
}
