package dLib.util;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TextureManager {
    private static Map<String, Texture> textures;

    public static Texture getTexture(String textureLocation) {
        if(textureLocation == null) return null;
        if(textures == null) textures = new HashMap<>();

        Texture t = textures.get(textureLocation);
        if(t != null) {
            if((int)(Reflection.getFieldValue("glHandle", t)) != 0) {
                return t;
            }
        }

        textures.put(textureLocation, ImageMaster.loadImage(textureLocation));
        return textures.get(textureLocation);
    }

    public static void disposeAll(){
        if(textures == null) return;
        Iterator tI = textures.values().iterator();
        while(tI.hasNext()){
            Texture t = (Texture) tI.next();
            if(t != null){
                t.dispose();
            }
        }
        textures.clear();
    }
}
