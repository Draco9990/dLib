package dLib.gameplay.templates;

import com.badlogic.gdx.math.Vector2;
import dLib.gameplay.DungeonData;
import dLib.gameplay.SpireLocation;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class TDungeonData<TRoomDataDef extends TRoomData> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public final int infinityDepth;
    public final String act;

    public LinkedHashMap<Vector2, TRoomDataDef> rooms = new LinkedHashMap<>();

    private HashMap<String, Serializable> metadata = new HashMap<>();
    public BiConsumerEvent<String, Serializable> postMetadataChangedEvent = new BiConsumerEvent<>();                    public TriConsumerEvent<TDungeonData, String, Serializable> postMetadataChangedGlobalEvent = new TriConsumerEvent<>();

    //endregion Variables

    //region Constructors

    public TDungeonData(int infinityDepth, String act){
        this.infinityDepth = infinityDepth;
        this.act = act;
    }

    //endregion Constructors

    //region Methods

    public abstract TRoomDataDef makeRoomData(int forX, int forY);

    public TRoomDataDef getRoom(int x, int y) {
        return rooms.computeIfAbsent(new Vector2(x, y), k -> makeRoomData(x, y));
    }

    public SpireLocation getPartialLocation(){
        return SpireLocation.getPartial(this);
    }

    public void cleanForSave(){
        rooms.values().forEach(TRoomData::cleanForSave);
    }

    //region Metadata

    public void updateMetadata(String key, Serializable value){
        metadata.put(key, value);
        postMetadataChangedEvent.invoke(key, value);
        postMetadataChangedGlobalEvent.invoke(this, key, value);
    }

    public void removeMetadata(String key){
        if(metadata.containsKey(key)){
            metadata.remove(key);
            postMetadataChangedEvent.invoke(key, null);
            postMetadataChangedGlobalEvent.invoke(this, key, null);
        }
    }

    //endregion Metadata

    //endregion Methods
}
