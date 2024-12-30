package dLib.ui.mousestates.events;

import dLib.mousestates.AbstractMouseState;
import dLib.util.events.globalevents.GlobalEvent;

public class PreExitMouseStateEvent extends GlobalEvent {
    public AbstractMouseState mouseState;

    public PreExitMouseStateEvent(AbstractMouseState mouseState) {
        this.mouseState = mouseState;
    }
}
