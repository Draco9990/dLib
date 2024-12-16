package dLib.util.bindings.texture.textureresource;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.util.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

public class TextureResourceManager {
    private static ArrayList<Texture> basegameTextures = new ArrayList<>();
    private static ArrayList<TextureAtlas.AtlasRegion> basegameAtlasRegion = new ArrayList<>();

    private static HashMap<Class<? extends ITextureResource>, ArrayList<Texture>> textureResourceMap = new HashMap<>();
    private static HashMap<Class<? extends ITextureResource>, ArrayList<TextureAtlas.AtlasRegion>> atlasRegionResourceMap = new HashMap<>();

    public static void initialize(){
        ArrayList<Class<? extends ITextureResource>> textureResources = Reflection.findClassesOfType(ITextureResource.class, false);

        for(Class<? extends ITextureResource> textureResource : textureResources){
            ArrayList<Texture> classTextures = Reflection.getFieldValuesByClass(Texture.class, textureResource);
            textureResourceMap.put(textureResource, classTextures);

            ArrayList<TextureAtlas.AtlasRegion> classAtlasRegions = Reflection.getFieldValuesByClass(TextureAtlas.AtlasRegion.class, textureResource);
            atlasRegionResourceMap.put(textureResource, classAtlasRegions);
        }

        basegameTextures = Reflection.getFieldValuesByClass(Texture.class, ImageMaster.class);
        basegameAtlasRegion = Reflection.getFieldValuesByClass(TextureAtlas.AtlasRegion.class, ImageMaster.class);
    }
}
