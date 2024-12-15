package dLib.util.ui.events;

import dLib.ui.elements.UIElement;
import dLib.util.events.globalevents.GlobalEvent;

public class PreUIUnhoverEvent extends GlobalEvent {
    public UIElement source;

    public PreUIUnhoverEvent(UIElement source){
        this.source = source;
    }
}
