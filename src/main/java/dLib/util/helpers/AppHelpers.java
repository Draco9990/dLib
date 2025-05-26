package dLib.util.helpers;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class AppHelpers {
    public static float totalTime = 0f;

    @SpirePatch2(clz = CardCrawlGame.class, method = "update")
    public static class CardCrawlGameUpdatePatch {
        public static void Postfix(CardCrawlGame __instance) {
            totalTime += Gdx.graphics.getDeltaTime();
        }
    }

    public static float getTotalTime() {
        return totalTime;
    }
}
