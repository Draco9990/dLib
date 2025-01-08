package dLib.ui.elements.components;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.util.events.Event;

import java.util.UUID;

public class UIDraggableComponent extends AbstractUIElementComponent<UIElement> {
    //region Variables

    private UIElement owner;

    private boolean canDragX = true;
    private boolean canDragY = true;

    private int xDragOffset;
    private int yDragOffset;

    private UUID leftClickEventId;
    private UUID leftClickHeldEventId;

    public Event<Runnable> onDraggedEvent = new Event<>();

    //endregion

    //region Constructors

    @Override
    public void onRegisterComponent(UIElement owner) {
        this.owner = owner;

        leftClickEventId = owner.onLeftClickEvent.subscribeManaged(this::onLeftClick);
        leftClickHeldEventId = owner.onLeftClickHeldEvent.subscribeManaged(this::onLeftClickHeld);
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        owner.onLeftClickEvent.unsubscribeManaged(leftClickEventId);
        owner.onLeftClickHeldEvent.unsubscribeManaged(leftClickHeldEventId);
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
            xDragOffset = (int) (InputHelper.mX - owner.getWorldPositionX() * Settings.xScale);
            xDragOffset = (int) (xDragOffset / owner.getScaleX());
        }

        if(canDragY) {
            yDragOffset = (int) (InputHelper.mY - owner.getWorldPositionY() * Settings.yScale);
            yDragOffset = (int) (yDragOffset / owner.getScaleY());
        }
    }

    protected void onLeftClickHeld(float totalDuration) {
        int localDragOffsetX = 0;
        int localDragOffsetY = 0;

        if(xDragOffset != 0){
            localDragOffsetX = (int) (InputHelper.mX - owner.getWorldPositionX() * Settings.xScale);
            localDragOffsetX = (int) (localDragOffsetX / owner.getScaleX());
        }

        if(yDragOffset != 0){
            localDragOffsetY = (int) (InputHelper.mY - owner.getWorldPositionY() * Settings.yScale);
            localDragOffsetY = (int) (localDragOffsetY / owner.getScaleY());
        }

        owner.offset((int) ((localDragOffsetX - xDragOffset) * owner.getScaleX()), (int) ((localDragOffsetY - yDragOffset) * owner.getScaleY()));

        onDraggedEvent.invoke(Runnable::run);
    }

    //endregion

    //endregion
}
