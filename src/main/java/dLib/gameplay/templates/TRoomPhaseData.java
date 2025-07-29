package dLib.gameplay.templates;

import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import dLib.gameplay.SpireLocation;
import dLib.properties.objects.Property;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.serializableevents.SerializableBiConsumer;
import dLib.util.helpers.GameplayHelpers;

import java.io.Serializable;

public class TRoomPhaseData implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public final int infinityDepth;
    public final String act;
    public final int x;
    public final int y;
    public final int phase;

    // todo migrate to instance data
    public Property<String> roomType = new Property<>(null);

    public Property<RoomOutcome> outcome = new Property<>(RoomOutcome.NONE);

    //endregion Variables

    //region Constructors

    public TRoomPhaseData(int infinityDepth, String act, int x, int y, int phase){
        this.infinityDepth = infinityDepth;
        this.act = act;
        this.x = x;
        this.y = y;
        this.phase = phase;
    }

    //endregion Constructors

    //region Methods

    public SpireLocation getPartialLocation(){
        return SpireLocation.getPartial(this);
    }

    public void cleanForSave(){

    }

    //endregion Methods

    //region Patches

    public enum RoomOutcome {
        NONE,
        PLAYER_ESCAPED,
        COMBAT_CLEARED,
        COMBAT_ESCAPED,
    }

    //endregion Patches
}
