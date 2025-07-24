package dLib.achievements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.screens.stats.AchievementItem;
import dLib.util.AssetLoader;
import dLib.util.Reflection;

public class CustomAchievement extends AchievementItem {
    public CustomAchievement(String name, String description, Texture unlockedImg, Texture lockedImg, boolean hidden){
        this(name, name, description, unlockedImg, lockedImg, hidden);
    }

    public CustomAchievement(String name, String key, String description, Texture unlockedImg, Texture lockedImg, boolean hidden){
        super(name, description, null, key, hidden);

        if(isUnlocked){
            Reflection.setFieldValue("img", this, AssetLoader.makeAtlasRegion(unlockedImg));
        } else {
            Reflection.setFieldValue("img", this, AssetLoader.makeAtlasRegion(lockedImg));
        }
    }
}
