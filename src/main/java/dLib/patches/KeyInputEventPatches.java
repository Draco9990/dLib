package dLib.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.helpers.input.ScrollInputProcessor;
import dLib.util.events.Event;
import dLib.util.events.localevents.ConsumerEvent;

import java.util.Objects;

public class KeyInputEventPatches {
    public static ConsumerEvent<Integer> onKeyPressed = new ConsumerEvent<>();

    private static InputProcessor initialProcessor;

    @SpirePatch2(clz = CardCrawlGame.class, method = "create")
    public static class KeyInputEventPatch {
        public static void Postfix(CardCrawlGame __instance) {
            initialProcessor = Gdx.input.getInputProcessor();
        }
    }

    @SpirePatch2(clz = ScrollInputProcessor.class, method = "keyDown")
    public static class KeyInputEventPatch2 {
        public static void Prefix(ScrollInputProcessor __instance, int keycode) {
            if (initialProcessor != null && Objects.equals(Gdx.input.getInputProcessor(), __instance)) {
                onKeyPressed.invoke(keycode);
            }
        }
    }
}
