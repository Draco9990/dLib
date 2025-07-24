package dLib.gameplay;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import dLib.util.DLibLogger;
import dLib.util.helpers.GameplayHelpers;

import java.io.Serializable;
import java.util.Objects;

public class PlayerLocation implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public int infinityCounter;

    public String act;

    public int x;
    public int y;

    public int phase;

    public String roomType;

    //endregion Variables

    //region Constructors

    private PlayerLocation(int infinityCounter, String act, MapRoomNode roomNode){
        this(infinityCounter, act, roomNode.x, roomNode.y, 0, roomNode.room.getClass().getSimpleName());
    }

    private PlayerLocation(int infinityCounter, String act, int x, int y, int phase, String roomType){
        this.infinityCounter = infinityCounter;
        this.act = act;
        this.x = x;
        this.y = y;
        this.phase = phase;
        this.roomType = roomType;
    }

    //endregion Constructors

    //region Methods

    //region Static Getters

    public static PlayerLocation getCurrent(){
        return getCurrent(true);
    }

    public static PlayerLocation getCurrent(boolean logFailure){
        MapRoomNode currMapNode = AbstractDungeon.getCurrMapNode();
        if(currMapNode == null){
            if(logFailure){
                DLibLogger.logError("Failed to get current map location, current map node was invalid.");
            }
            return null;
        }

        return new PlayerLocation(
                GameplayInformationTracker.getInfinityCycle(),
                GameplayHelpers.getCurrentActName(),
                currMapNode.x,
                currMapNode.y,
                GameplayInformationTracker.getRoomPhase(),
                currMapNode.room.getClass().getSimpleName()
        );
    }

    public static PlayerLocation getForRoomOnCurrentFloor(MapRoomNode roomNode){
        return new PlayerLocation(GameplayInformationTracker.getInfinityCycle(), GameplayHelpers.getCurrentActName(), roomNode);
    }

    //endregion Static Getters

    //region Room Type Information

    public boolean isNeowRoom() {
        return NeowRoom.class.getSimpleName().equals(roomType);
    }

    public boolean isBossRoom(){
        return MonsterRoomBoss.class.getSimpleName().equals(roomType);
    }

    public boolean isBossTreasureRoom(){
        return TreasureRoomBoss.class.getSimpleName().equals(roomType);
    }

    //endregion Room Type Information

    //region Comparisons

    public boolean inSameRoom(){
        return inSameRoomAs(getCurrent());
    }
    public boolean inSameRoomAs(PlayerLocation location){
        if(!GameplayHelpers.isInARun()) {
            return false;
        }

        return x == location.x &&
                y == location.y &&
                Objects.equals(act, location.act) &&
                infinityCounter == location.infinityCounter &&
                Objects.equals(roomType, location.roomType);
    }

    public boolean inSameRoomAndAction(){
        return inSameRoomAndActionAs(getCurrent());
    }
    public boolean inSameRoomAndActionAs(PlayerLocation location){
        return inSameRoomAs(location) &&
                phase == location.phase;
    }

    public boolean inSameAct(){
        return inSameActAs(getCurrent());
    }
    public boolean inSameActAs(PlayerLocation location){
        return act.equals(location.act) && infinityCounter == location.infinityCounter;
    }

    //endregion Comparisons

    @Override
    public String toString() {
        return infinityCounter + "[" + act + ":" + phase + "[" + x + ", " + y + "], " + roomType + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PlayerLocation)){
            return false;
        }

        return inSameRoomAndActionAs((PlayerLocation) obj);
    }

    //endregion Methods

}