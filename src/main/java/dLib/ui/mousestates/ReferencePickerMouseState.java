package dLib.ui.mousestates;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.mousestates.AbstractMouseState;
import dLib.mousestates.MouseStateManager;
import dLib.ui.elements.UIElement;
import dLib.util.events.localevents.ConsumerEvent;

import java.util.HashMap;
import java.util.UUID;

public class ReferencePickerMouseState extends AbstractMouseState {
    private HashMap<UIElement, UUID> renderEvents = new HashMap<>();

    private UIElement optionalRequiredParent;

    public ConsumerEvent<UIElement> onReferencePickedEvent = new ConsumerEvent<>();

    public ReferencePickerMouseState(UIElement optionalRequiredParent) {
        super("ReferencePicker");

        this.optionalRequiredParent = optionalRequiredParent;
    }

    @Override
    public void onStateEnter() {
        super.onStateEnter();

        UIElement.preHoveredGlobalEvent.subscribe(this, source -> {
            if(!source.isDescendantOf(optionalRequiredParent)){
                return;
            }

            UUID renderEventId = source.postRenderEvent.subscribe(ReferencePickerMouseState.this, sb -> {
                sb.setColor(Color.YELLOW);
                sb.draw(ImageMaster.DEBUG_HITBOX_IMG, source.getWorldPositionX() * Settings.xScale, source.getWorldPositionY() * Settings.yScale, source.getWidth() * Settings.xScale, source.getHeight() * Settings.yScale);
                sb.flush();
            });

            renderEvents.put(source, renderEventId);
        });

        UIElement.preUnhoveredGlobalEvent.subscribe(this, source -> {
            if(renderEvents.containsKey(source)) {
                source.postRenderEvent.unsubscribeManaged(renderEvents.get(source));
            }
        });

        UIElement.preLeftClickGlobalEvent.subscribe(this, source -> {
            if(!source.isDescendantOf(optionalRequiredParent)){
                return;
            }

            onReferencePickedEvent.invoke(source);
            MouseStateManager.exitMouseState();

            for(UUID renderEventId : renderEvents.values()){
                source.postRenderEvent.unsubscribeManaged(renderEventId);
            }
        });
    }

    @Override
    public void update() {
        super.update();

        if(InputHelper.justClickedRight){
            MouseStateManager.exitMouseState();
        }
    }
}
