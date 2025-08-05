package dLib.room;

import basemod.CustomEventRoom;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;

public class PredefinedEventRoom extends CustomEventRoom {
    private String eventId;

    public PredefinedEventRoom(String eventId) {
        this.eventId = eventId;
    }

    public PredefinedEventRoom(AbstractEvent event) {
        this.event = event;
        eventId = null;
    }

    @Override
    public void onPlayerEntry() {
        if(eventId != null){
            AbstractDungeon.overlayMenu.proceedButton.hide();
            this.event = EventHelper.getEvent(eventId);
        }

        this.event.onEnterRoom();
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "generateRoom")
    public static class PredefinedEventRoomPatcher {
        @SpirePrefixPatch
        public static SpireReturn Prefix(){
            if(AbstractDungeon.nextRoom.room instanceof PredefinedEventRoom){
                return SpireReturn.Return(AbstractDungeon.nextRoom.room);
            }

            return SpireReturn.Continue();
        }
    }
}
