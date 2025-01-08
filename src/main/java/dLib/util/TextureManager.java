package dLib.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private static AssetManager assetManager = new AssetManager();

    public static Texture getTexture(String textureLocation) {
        return getResource(textureLocation, Texture.class);
    }
    public static TextureAtlas getTextureAtlas(String textureAtlasLocation) {
        return getResource(textureAtlasLocation, TextureAtlas.class);
    }
    public static <T> T getResource(String resourceLocation, Class<T> resource) {
        if(!assetManager.isLoaded(resourceLocation, resource)){
            assetManager.load(resourceLocation, resource);
            assetManager.finishLoading();
        }

        return assetManager.get(resourceLocation, resource);
    }
}
