package dLib.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class InputHelperHoverConsumer {
    public static boolean alreadyHovered = false;

    @SpirePatch2(clz = CardCrawlGame.class, method = "render")
    public static class HoveredConsumerResetterPatch{
        @SpirePrefixPatch
        public static void Prefix(){
            alreadyHovered = false;
        }
    }
}
