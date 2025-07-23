package dLib.achievements;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.stats.AchievementGrid;
import com.megacrit.cardcrawl.screens.stats.AchievementItem;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import dLib.util.Reflection;

import java.util.ArrayList;

public class AchievementManager {
    public static ArrayList<AchievementItem> customAchievements = new ArrayList<>();

    public static void registerCustomAchievement(AchievementItem achievement){
        customAchievements.add(achievement);
    }

    @SpirePatch2(clz = AchievementGrid.class, method = SpirePatch.CONSTRUCTOR)
    public static class MainPatch{
        public static void Postfix(AchievementGrid __instance){
            __instance.items.addAll(customAchievements);
        }
    }

    @SpirePatch2(clz = StatsScreen.class, method = "renderStatScreen")
    public static class StatScreenOffset{
        @SpireInsertPatch(rloc = 8, localvars = {"renderY"})
        public static void Insert(StatsScreen __instance, @ByRef float[] renderY){
            renderY[0] -= 180 * getNewRowCount() * Settings.scale;
        }
    }

    @SpirePatch2(clz = StatsScreen.class, method = "calculateScrollBounds")
    public static class StatScreenScrollbar {
        public static void Postfix(StatsScreen __instance) {
            Reflection.setFieldValue("scrollUpperBound", __instance, (float) Reflection.getFieldValue("scrollUpperBound", __instance) + 180 * getNewRowCount() * Settings.scale);
        }
    }

    private static int getNewRowCount(){
        int newAchievementCount = customAchievements.size();
        newAchievementCount -= 4; // Fill the remaining row.

        // For every five remaining achievements, we add a new row.
        if(newAchievementCount <= 0) {
            return 0;
        }

        int newRows = newAchievementCount / 5;
        if(newAchievementCount % 5 != 0) {
            newRows++;
        }
        return newRows;
    }
}
