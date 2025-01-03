package dLib.util;

import com.megacrit.cardcrawl.audio.Sfx;

import java.util.HashMap;
import java.util.Map;

public class StaticResourceLoader {
    private static Map<String, Sfx> sfx;

    public static Sfx getSfx(String sfxPath) {
        if(sfxPath == null) return null;
        if(sfx == null) sfx = new HashMap<>();

        Sfx t = sfx.get(sfxPath);
        if(t != null) {
            return t;
        }

        sfx.put(sfxPath, new Sfx(sfxPath));
        return sfx.get(sfxPath);
    }
}
