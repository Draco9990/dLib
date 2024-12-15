package dLib.util.ui.events;

import dLib.ui.elements.UIElement;
import dLib.util.events.globalevents.GlobalEvent;

public class PreUILeftClickEvent extends GlobalEvent {
    public UIElement source;

    public PreUILeftClickEvent(UIElement source){
        this.source = source;
    }
}
