package dLib.gameplay.extensions;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import dLib.util.IMetadataProvider;

import java.io.Serializable;
import java.util.HashMap;

public class AbstractGameActionExtensions {
    @SpirePatch2(clz = AbstractGameAction.class, method = SpirePatch.CLASS)
    private static class Fields{
        public static SpireField<HashMap<String, Serializable>> metadata = new SpireField<>(HashMap::new);
    }

    static HashMap<String, Serializable> getMetadataObject(AbstractGameAction action){
        return Fields.metadata.get(action);
    }

    public static void setMetadata(AbstractGameAction action, String key, Serializable value){
        getMetadataObject(action).put(key, value);
    }
    public static void unsetMetadata(AbstractGameAction action, String key){
        getMetadataObject(action).remove(key);
    }

    public static boolean hasMetadata(AbstractGameAction action, String key){
        return getMetadataObject(action).containsKey(key);
    }
    public static <T extends Serializable> T getMetadata(AbstractGameAction action, String key){
        return (T) getMetadataObject(action).get(key);
    }
}
