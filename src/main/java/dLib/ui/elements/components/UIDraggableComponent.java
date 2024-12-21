package dLib.ui.elements.components;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;

import java.util.HashMap;
import java.util.UUID;

public class UIDraggableComponent extends UIElementComponent<UIElement> {
    //region Variables

    private UIElement owner;

    private boolean canDragX = true;
    private boolean canDragY = true;

    private int xDragOffset;
    private int yDragOffset;

    private UUID leftClickEventId;
    private UUID leftClickHeldEventId;

    private HashMap<UUID, Runnable> onDraggedEvents = new HashMap<>();

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
        if(canDragX) xDragOffset = (int) (InputHelper.mX - owner.getWorldPositionX() * Settings.xScale);
        if(canDragY) yDragOffset = (int) (InputHelper.mY - owner.getWorldPositionY() * Settings.yScale);
    }

    protected void onLeftClickHeld(float totalDuration) {
        int xPos = canDragX ? (int) ((InputHelper.mX - xDragOffset) / Settings.xScale) : owner.getWorldPositionX();
        int yPos = canDragY ? (int) ((InputHelper.mY - yDragOffset) / Settings.yScale) : owner.getWorldPositionY();

        owner.setWorldPosition(xPos, yPos);

        onDraggedEvents.forEach((uuid, runnable) -> runnable.run());
    }

    //endregion

    //region Events

    public UUID addOnDraggedEvent(Runnable event){
        UUID id = UUID.randomUUID();
        onDraggedEvents.put(id, event);
        return id;
    }

    public void removeOnDraggedEvent(UUID id){
        onDraggedEvents.remove(id);
    }

    //endregion

    //endregion
}
