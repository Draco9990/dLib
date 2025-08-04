package dLib.util;

import java.io.Serializable;
import java.util.HashMap;

public interface IMetadataProvider {
    HashMap<String, Serializable> getMetadataObject();

    default void setMetadata(String key, Serializable value){
        getMetadataObject().put(key, value);
    }
    default void unsetMetadata(String key){
        getMetadataObject().remove(key);
    }

    default boolean hasMetadata(String key){
        return getMetadataObject().containsKey(key);
    }
    default <T extends Serializable> T getMetadata(String key){
        return (T) getMetadataObject().get(key);
    }
}
