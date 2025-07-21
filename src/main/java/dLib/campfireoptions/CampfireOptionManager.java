package dLib.campfireoptions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import dLib.util.Reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class CampfireOptionManager {
    private static ArrayList<Supplier<AbstractCampfireOption>> customOptions = new ArrayList<>();

    public static void registerCampfireOption(Supplier<AbstractCampfireOption> option){
        customOptions.add(option);
    }

    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class AddTradingOption{
        public static void Postfix(CampfireUI __instance){
            ArrayList<AbstractCampfireOption> buttons = Reflection.getFieldValue("buttons", __instance);

            for (Supplier<AbstractCampfireOption> optionSupplier : customOptions) {
                AbstractCampfireOption option = optionSupplier.get();
                if (option != null) {
                    buttons.add(option);
                }
            }

            Reflection.setFieldValue("buttons", __instance, buttons);
        }
    }
}
