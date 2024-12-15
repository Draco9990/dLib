package dLib.util.ui.events;

import dLib.ui.elements.UIElement;
import dLib.util.events.globalevents.GlobalEvent;

public class PreUIHoverEvent extends GlobalEvent {
    public UIElement source;

    public PreUIHoverEvent(UIElement source){
        this.source = source;
    }
}
