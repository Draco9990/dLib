package dLib.gameplay;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import dLib.gameplay.templates.TDungeonCycleData;
import dLib.gameplay.templates.TDungeonData;
import dLib.gameplay.templates.TRoomData;
import dLib.gameplay.templates.TRoomPhaseData;
import dLib.util.DLibLogger;
import dLib.util.utils.GameplayUtils;

import java.io.Serializable;
import java.rmi.AccessException;
import java.util.Objects;

public class SpireLocation implements Serializable {
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

    protected SpireLocation(int infinityCounter, String act, MapRoomNode roomNode){
        this(infinityCounter, act, roomNode.x, roomNode.y, 0, roomNode.room.getClass().getSimpleName());
    }

    protected SpireLocation(int infinityCounter, String act, int x, int y, int phase, String roomType){
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

    public static SpireLocation getCurrent(){
        return getCurrent(true);
    }

    public static SpireLocation getCurrent(boolean logFailure){
        MapRoomNode currMapNode = AbstractDungeon.getCurrMapNode();
        if(currMapNode == null){
            if(logFailure){
                DLibLogger.logError("Failed to get current map location, current map node was invalid.");
            }
            return null;
        }

        return new SpireLocation(
                GameplayUtils.getInfinityCycle(),
                GameplayUtils.getCurrentActName(),
                currMapNode.x,
                currMapNode.y,
                GameplayUtils.getRoomPhase(),
                currMapNode.room.getClass().getSimpleName()
        );
    }

    public static SpireLocation getFor(MapRoomNode roomNode){
        return new SpireLocation(GameplayUtils.getInfinityCycle(), GameplayUtils.getCurrentActName(), roomNode);
    }

    public static <T extends Throwable> SpireLocation getFor(TRoomPhaseData roomPhase) throws T{
        if(roomPhase.roomType.getValue() == null){
            throw (T) new AccessException("Tried to generate SpireLocation for TRoomPhaseData that was incomplete");
        }
        return new SpireLocation(roomPhase.infinityDepth, roomPhase.act, roomPhase.x, roomPhase.y, roomPhase.phase, roomPhase.roomType.getValue());
    }

    public static <T extends Throwable> SpireLocation getPartial(TDungeonCycleData dungeonCycle) {
        return new SpireLocation(dungeonCycle.infinityDepth, null, -1, -1, -1, null);
    }
    public static <T extends Throwable> SpireLocation getPartial(TDungeonData dungeon) {
        return new SpireLocation(dungeon.infinityDepth, dungeon.act, -1, -1, -1, null);
    }
    public static <T extends Throwable> SpireLocation getPartial(TRoomData room) {
        return new SpireLocation(room.infinityDepth, room.act, room.x, room.y, -1, null);
    }
    public static <T extends Throwable> SpireLocation getPartial(TRoomPhaseData roomPhase) {
        return new SpireLocation(roomPhase.infinityDepth, roomPhase.act, roomPhase.x, roomPhase.y, roomPhase.phase, roomPhase.roomType.getValue());
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
    public boolean inSameRoomAs(SpireLocation location){
        if(!GameplayUtils.isInARun()) {
            return false;
        }

        return x == location.x &&
                y == location.y &&
                Objects.equals(act, location.act) &&
                infinityCounter == location.infinityCounter &&
                Objects.equals(roomType, location.roomType);
    }

    public boolean inSameRoomAndPhase(){
        return inSameRoomAndPhaseAs(getCurrent());
    }
    public boolean inSameRoomAndPhaseAs(SpireLocation location){
        return inSameRoomAs(location) &&
                phase == location.phase;
    }

    public boolean inSameAct(){
        return inSameActAs(getCurrent());
    }
    public boolean inSameActAs(SpireLocation location){
        return act.equals(location.act) && infinityCounter == location.infinityCounter;
    }

    public boolean sameAs(TDungeonCycleData dungeonCycle){
        return infinityCounter == dungeonCycle.infinityDepth;
    }
    public boolean sameAs(TDungeonData dungeon){
        return infinityCounter == dungeon.infinityDepth &&
                act.equals(dungeon.act);
    }
    public boolean sameAs(TRoomData room){
        return infinityCounter == room.infinityDepth &&
                act.equals(room.act) &&
                x == room.x &&
                y == room.y;
    }
    public boolean sameAs(TRoomPhaseData roomPhase){
        return infinityCounter == roomPhase.infinityDepth &&
                act.equals(roomPhase.act) &&
                x == roomPhase.x &&
                y == roomPhase.y &&
                phase == roomPhase.phase;
    }

    //endregion Comparisons

    @Override
    public String toString() {
        return infinityCounter + "[" + act + "[" + x + ", " + y + "]:[" + phase + "] (" + roomType + ")]";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SpireLocation)){
            return false;
        }

        return inSameRoomAndPhaseAs((SpireLocation) obj);
    }

    //endregion Methods

}