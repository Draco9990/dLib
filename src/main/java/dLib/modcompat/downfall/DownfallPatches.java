package dLib.modcompat.downfall;

import basemod.CustomEventRoom;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dLib.gameplay.GameplayInformationTracker;
import dLib.modcompat.ModManager;
import dLib.util.Reflection;
import dLib.util.helpers.GameplayHelpers;
import downfall.events.GremlinWheelGame_Rest;
import downfall.rooms.HeartShopRoom;

public class DownfallPatches {
    @SpirePatch2(clz = HeartShopRoom.class, method = "showHeartMerchant", requiredModId = ModManager.Downfall.modId, optional = true)
    public static class HeartShopRoomPatch {
        public static void Postfix(){
            GameplayInformationTracker.increaseRoomPhase();
        }
    }

    @SpirePatch2(clz = GremlinWheelGame_Rest.class, method = "buttonEffect", requiredModId = ModManager.Downfall.modId, optional = true)
    public static class RestSwapTracker {
        @SpirePrefixPatch
        public static void Prefix(){
            Reflection.setFieldValue("enteringDoubleBoss", GameplayHelpers.class, true);
        }

        @SpirePostfixPatch
        public static void Postfix(){
            Reflection.setFieldValue("enteringDoubleBoss", GameplayHelpers.class, false);
        }
    }
}
