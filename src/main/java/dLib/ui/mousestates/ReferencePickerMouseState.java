package dLib.ui.mousestates;

import dLib.mousestates.MouseState;
import dLib.util.events.GlobalEvents;
import dLib.util.ui.events.PreUIHoverEvent;
import dLib.util.ui.events.PreUIUnhoverEvent;

import java.util.function.Consumer;

public class ReferencePickerMouseState extends MouseState {
    public ReferencePickerMouseState(){
        super("ReferencePicker");
    }

    @Override
    public void onStateEnter() {
        super.onStateEnter();

        GlobalEvents.subscribe(this, PreUIHoverEvent.class, preUIHoverEvent -> {

        });

        GlobalEvents.subscribe(this, PreUIUnhoverEvent.class, preUIUnhoverEvent -> {

        });
    }
}
