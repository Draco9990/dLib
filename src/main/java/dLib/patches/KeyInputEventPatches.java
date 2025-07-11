package dLib.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.ScrollInputProcessor;
import dLib.util.HistoryProperty;
import dLib.util.events.localevents.ConsumerEvent;

import java.util.Objects;

public class KeyInputEventPatches {
    public static ConsumerEvent<Integer> onKeyPressed = new ConsumerEvent<>();

    private static InputProcessor initialProcessor;

    public static HistoryProperty<Boolean> linkProxyInput = new HistoryProperty<>(false);

    @SpirePatch2(clz = CardCrawlGame.class, method = "create")
    public static class KeyInputEventPatch {
        public static void Postfix(CardCrawlGame __instance) {
            initialProcessor = Gdx.input.getInputProcessor();
        }
    }

    @SpirePatch2(clz = ScrollInputProcessor.class, method = "keyDown")
    public static class KeyInputEventPatch2 {
        public static void Prefix(ScrollInputProcessor __instance, int keycode) {
            if (suppressingProxyInput()) {
                onKeyPressed.invoke(keycode);
            }
        }
    }

    @SpirePatch2(clz = InputAction.class, method = "isJustPressed")
    public static class IA_IsJustPressed{
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(InputAction __instance){
            if(suppressingProxyInput()){
                return SpireReturn.Return(false);
            }

            return SpireReturn.Continue();
        }
    }
    @SpirePatch2(clz = InputAction.class, method = "isPressed")
    public static class IA_IsPressed{
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(InputAction __instance){
            if(suppressingProxyInput()){
                return SpireReturn.Return(false);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = CInputAction.class, method = "isJustPressed")
    public static class CIA_IsJustPressed{
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(CInputAction __instance){
            if(suppressingProxyInput()){
                return SpireReturn.Return(false);
            }

            return SpireReturn.Continue();
        }
    }
    @SpirePatch2(clz = CInputAction.class, method = "isJustReleased")
    public static class IA_IsJustReleased{
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(CInputAction __instance){
            if(suppressingProxyInput()){
                return SpireReturn.Return(false);
            }

            return SpireReturn.Continue();
        }
    }
    @SpirePatch2(clz = CInputAction.class, method = "isPressed")
    public static class CIA_IsPressed{
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(CInputAction __instance){
            if(suppressingProxyInput()){
                return SpireReturn.Return(false);
            }

            return SpireReturn.Continue();
        }
    }

    public static boolean suppressingProxyInput() {
        return !linkProxyInput.get() && initialProcessor != null && !Objects.equals(Gdx.input.getInputProcessor(), initialProcessor);
    }
}
