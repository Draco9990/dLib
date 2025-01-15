package dLib.util.helpers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GameplayHelpers {
    public static boolean isInARun(){
        return CardCrawlGame.dungeon != null && AbstractDungeon.player != null;
    }
}
