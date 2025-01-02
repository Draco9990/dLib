package dLib.util.ui.events;

import dLib.ui.elements.UIElement;
import dLib.util.events.globalevents.GlobalEvent;

public class PreUISelectEvent extends GlobalEvent {
    public UIElement source;

    public PreUISelectEvent(UIElement source){
        this.source = source;
    }
}
