package dLib.ui.mousestates.events;

import dLib.mousestates.AbstractMouseState;
import dLib.util.events.globalevents.GlobalEvent;

public class PostEnterMouseStateEvent extends GlobalEvent {
    public AbstractMouseState mouseState;

    public PostEnterMouseStateEvent(AbstractMouseState mouseState) {
        this.mouseState = mouseState;
    }
}
