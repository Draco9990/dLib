package dLib.gameplay.extensions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import dLib.util.ObservableArrayList;

public class CardGroupExtensions {
    @SpirePatch2(clz = CardGroup.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { CardGroup.CardGroupType.class })
    public static class ConstructorPatch1{
        @SpirePostfixPatch
        public static void Prefix(CardGroup __instance){
            __instance.group = new ObservableArrayList<>();
        }
    }

    @SpirePatch2(clz = CardGroup.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { CardGroup.class, CardGroup.CardGroupType.class })
    public static class ConstructorPatch2{
        @SpirePostfixPatch
        public static void Prefix(CardGroup __instance){
            __instance.group = new ObservableArrayList<>();
        }
    }

    public static <T extends Object> ObservableArrayList<T> getObservableCardGroup(CardGroup group) {
        if(!(group.group instanceof ObservableArrayList)){
            // For some reason our group wasn't an observable group, patch it
            group.group = new ObservableArrayList<>(group.group);
        }

        return (ObservableArrayList<T>) group.group;
    }
}
