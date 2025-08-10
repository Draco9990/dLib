package dLib.gameplay.extensions;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dLib.DLib;
import dLib.util.DLibLogger;
import dLib.util.Reflection;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtMethod;
import javassist.NotFoundException;

public class AbstractMonsterExtensions {
    @SpirePatch2(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<String> currentState = new SpireField<>(() -> "");
    }

    public static String getCurrentState(AbstractMonster monster) {
        return Fields.currentState.get(monster);
    }

    public static class StateTracker {
        @SpirePatch(
                clz = CardCrawlGame.class,
                method = SpirePatch.CONSTRUCTOR
        )
        public static class DynamicPatch {
            public static void Raw(CtBehavior ctBehavior) throws NotFoundException {
                for(CtMethod method : Reflection.findMethodsFromClasses(ctBehavior, AbstractMonster.class, true, "changeState", new Class[]{String.class})){
                    try{
                        method.insertBefore("{" + StateTracker.class.getName() + ".onStateChange(($w)$0, $1);}");
                    }catch (CannotCompileException cannotCompileException){
                        DLibLogger.logError("DLib => Cannot dynamically patch changeState method due to " + cannotCompileException.getReason());
                        cannotCompileException.printStackTrace();
                    }
                }
            }
        }

        public static void onStateChange(AbstractMonster __instance, String newState){
            Fields.currentState.set(__instance, newState);
        }
    }
}
