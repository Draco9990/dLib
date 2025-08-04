package dLib.gameplay.extensions;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.io.Serializable;
import java.util.HashMap;

public class AbstractGameEffectExtensions {
    @SpirePatch2(clz = AbstractGameEffect.class, method = SpirePatch.CLASS)
    private static class Fields{
        public static SpireField<HashMap<String, Serializable>> metadata = new SpireField<>(HashMap::new);
    }

    static HashMap<String, Serializable> getMetadataObject(AbstractGameEffect action){
        return Fields.metadata.get(action);
    }

    public static void setMetadata(AbstractGameEffect action, String key, Serializable value){
        getMetadataObject(action).put(key, value);
    }
    public static void unsetMetadata(AbstractGameEffect action, String key){
        getMetadataObject(action).remove(key);
    }

    public static boolean hasMetadata(AbstractGameEffect action, String key){
        return getMetadataObject(action).containsKey(key);
    }
    public static <T extends Serializable> T getMetadata(AbstractGameEffect action, String key){
        return (T) getMetadataObject(action).get(key);
    }
}
