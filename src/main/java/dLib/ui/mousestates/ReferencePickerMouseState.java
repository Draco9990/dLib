package dLib.ui.mousestates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.mousestates.MouseState;
import dLib.mousestates.MouseStateManager;
import dLib.ui.elements.UIElement;
import dLib.util.events.GlobalEvents;
import dLib.util.ui.events.PreUIHoverEvent;
import dLib.util.ui.events.PreUILeftClickEvent;
import dLib.util.ui.events.PreUIUnhoverEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class ReferencePickerMouseState extends MouseState {
    private HashMap<UIElement, UUID> renderEvents = new HashMap<>();

    private UIElement optionalRequiredParent;
    private Consumer<UIElement> onReferencePicked;

    public ReferencePickerMouseState(UIElement optionalRequiredParent, Consumer<UIElement> onReferencePicked) {
        super("ReferencePicker");

        this.optionalRequiredParent = optionalRequiredParent;
        this.onReferencePicked = onReferencePicked;
    }

    @Override
    public void onStateEnter() {
        super.onStateEnter();

        GlobalEvents.subscribe(this, PreUIHoverEvent.class, preUIHoverEvent -> {
            if(!preUIHoverEvent.source.isDescendantOf(optionalRequiredParent)){
                return;
            }

            UUID renderEventId = preUIHoverEvent.source.postRenderEvent.subscribe(ReferencePickerMouseState.this, sb -> {
                sb.setColor(Color.YELLOW);
                sb.draw(ImageMaster.DEBUG_HITBOX_IMG, preUIHoverEvent.source.getWorldPositionX() * Settings.xScale, preUIHoverEvent.source.getWorldPositionY() * Settings.yScale, preUIHoverEvent.source.getWidth() * Settings.xScale, preUIHoverEvent.source.getHeight() * Settings.yScale);
                sb.flush();
            });

            renderEvents.put(preUIHoverEvent.source, renderEventId);
        });

        GlobalEvents.subscribe(this, PreUIUnhoverEvent.class, preUIUnhoverEvent -> {
            if(renderEvents.containsKey(preUIUnhoverEvent.source)) {
                preUIUnhoverEvent.source.postRenderEvent.unsubscribeManaged(renderEvents.get(preUIUnhoverEvent.source));
            }
        });

        GlobalEvents.subscribe(this, PreUILeftClickEvent.class, preUILeftClickEvent -> {
            if(!preUILeftClickEvent.source.isDescendantOf(optionalRequiredParent)){
                return;
            }

            onReferencePicked.accept(preUILeftClickEvent.source);
            exitMouseState();

            for(UUID renderEventId : renderEvents.values()){
                preUILeftClickEvent.source.postRenderEvent.unsubscribeManaged(renderEventId);
            }
        });
    }

    @Override
    public void update() {
        super.update();

        if(InputHelper.justClickedRight){
            exitMouseState();
        }
    }
}
