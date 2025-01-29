package dLib.util.events.globalevents;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import dLib.util.DLibLogger;
import dLib.util.MatcherUtils;
import dLib.util.Reflection;
import javassist.CtBehavior;
import javassist.CtConstructor;
import javassist.Modifier;

public class PostConstructEvent extends GlobalEvent {
    public Constructable source;

    public PostConstructEvent(Constructable source){
        this.source = source;
    }

    //region Patches

    @SpirePatch(
            clz = CardCrawlGame.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class DynamicPatches {
        public static void Raw(CtBehavior ctBehavior) {
            for(CtConstructor constructor : Reflection.findConstructorsFromClasses(ctBehavior, Constructable.class, false)){
                if((constructor.getDeclaringClass().getModifiers() & Modifier.ABSTRACT) != 0){
                    continue;
                }

                if((constructor.getModifiers() & Modifier.ABSTRACT) != 0){
                    continue;
                }

                try{
                    if(MatcherUtils.callsSiblingConstructor(constructor)){
                        continue;
                    }

                    constructor.insertAfter(
                            "if($0.getClass() == " + constructor.getDeclaringClass().getName()  + ".class) { " +
                            "$0.postConstruct();" +
                            "dLib.util.events.GlobalEvents.sendMessage(new dLib.util.events.globalevents.PostConstructEvent($0));" +
                            "}");
                }
                catch (Exception e){
                    DLibLogger.logError("Failed to insert PostConstructEvent into constructor: " + constructor.getLongName() + " due to exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    //endregion
}
