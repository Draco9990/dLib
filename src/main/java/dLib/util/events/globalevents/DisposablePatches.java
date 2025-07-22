package dLib.util.events.globalevents;

import com.badlogic.gdx.utils.Disposable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import dLib.util.DLibLogger;
import dLib.util.Reflection;
import dLib.util.events.Event;
import dLib.util.events.localevents.ConsumerEvent;
import javassist.CtBehavior;
import javassist.CtMethod;
import javassist.Modifier;
import org.clapper.util.classutil.ClassInfo;

import java.util.ArrayList;

public class DisposablePatches {
    public static ConsumerEvent<Disposable> postDisposedGlobalEvent = new ConsumerEvent<>();

    //region Patches

    @SpirePatch(
            clz = CardCrawlGame.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class DynamicPatches {
        public static void Raw(CtBehavior ctBehavior) {
            ArrayList<ClassInfo> disposableClasses = Reflection.findClassInfosOfType(Disposable.class, false);
            for(CtMethod method : Reflection.findMethodsFromClasses(ctBehavior, disposableClasses, "dispose")){
                if((method.getModifiers() & Modifier.ABSTRACT) != 0){
                    continue;
                }

                try{
                    method.insertAfter("dLib.util.events.globalevents.DisposablePatches.postDispose($0);");
                }
                catch (Exception e){
                    DLibLogger.logError("Failed to insert PostDisposeEvent into method: " + method.getLongName() + " due to exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void postDispose(Disposable source) {
        postDisposedGlobalEvent.invoke(source);
        for(Event<?> event : Event.allRegisteredEvents) {
            event.postObjectDisposed(source);
        }
    }

    //endregion
}
