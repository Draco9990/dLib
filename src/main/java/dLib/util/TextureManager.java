package dLib.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.HashMap;
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

    private static Map<FileHandle, TextureAtlas> textureAtlases = new HashMap<>();

    public static TextureAtlas getTextureAtlas(FileHandle textureAtlasLocation) {
        if(textureAtlasLocation == null) return null;
        if(textureAtlases == null) textureAtlases = new HashMap<>();

        TextureAtlas t = textureAtlases.get(textureAtlasLocation);
        if(t != null) {
            return t;
        }

        textureAtlases.put(textureAtlasLocation, new TextureAtlas(textureAtlasLocation));
        return textureAtlases.get(textureAtlasLocation);
    }
}
