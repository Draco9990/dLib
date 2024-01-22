package dLib.util;

import basemod.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import java.nio.file.Paths;
import java.util.HashMap;

public class AssetLoader {
    private static HashMap<String[], TextureAtlas> textureAtlasMap = new HashMap<>();

    public static TextureAtlas loadTextureAtlas(String atlasUrl, String resourcesUrl){
        if(resourcesUrl == null){
            resourcesUrl = Gdx.files.internal(atlasUrl).file().getParent();
            if(!resourcesUrl.endsWith("/")) resourcesUrl += "/";
        }

        String[] atlasInfoUrl = new String[]{atlasUrl, resourcesUrl};

        if(!textureAtlasMap.containsKey(atlasInfoUrl) || textureAtlasMap.get(atlasInfoUrl).getTextures().size == 0){
            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasUrl), Gdx.files.internal(resourcesUrl));
            textureAtlasMap.put(atlasInfoUrl, atlas);
        }

        return textureAtlasMap.get(atlasInfoUrl);
    }

    public static TextureAtlas.AtlasRegion makeAtlasRegion(Texture texture){
        return new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }
}
