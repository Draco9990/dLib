package dLib;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.screens.stats.AchievementItem;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import dLib.achievements.AchievementManager;
import dLib.betterscreens.ui.elements.items.PanelListScreen;
import dLib.campfireoptions.CampfireOptionManager;
import dLib.commands.CommandManager;
import dLib.custominput.CustomKeybindManager;
import dLib.developermode.DeveloperModeManager;
import dLib.external.ExternalEditorCommunicationManager;
import dLib.gameplay.GameplayInformationTracker;
import dLib.mainmenubuttons.MainMenuButtonManager;
import dLib.patchnotes.Patchnotes;
import dLib.patchnotes.PatchnotesManager;
import dLib.patchnotes.PatchnotesPatches;
import dLib.shaders.ShaderManager;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplateManager;
import dLib.ui.GeneratedUIManager;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.string.AbstractStringBinding;
import dLib.util.bindings.string.Str;
import dLib.util.bindings.texture.Tex;
import dLib.util.helpers.FontHelpers;
import dLib.util.helpers.SteamHelpers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

@SpireInitializer
public class DLib implements PostInitializeSubscriber{
    public static final Logger logger = LogManager.getLogger(DLib.class.getName());
    private static String modID;

    public DLib() {
        BaseMod.subscribe(this);
        setModID("dLib");
    }

    public static void initialize() {
        logger.info("<========================= Initializing DLib");
        DLib defaultmod = new DLib();
    }

    public static void setModID(String ID) {
        modID = ID;
    }
    
    public static String getModID() { // NO
        return modID;
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receivePostInitialize() {
        GameplayInformationTracker.init();

        GeneratedUIManager.initialize();

        SteamHelpers.init();

        CommandManager.initialize();

        FontHelpers.initialize();

        ExternalEditorCommunicationManager.start();

        UCEITemplateManager.initialize();

        ShaderManager.init();

        CustomKeybindManager.registerCommonEvents();

        DeveloperModeManager.init();

        PatchnotesPatches.init();

        Patchnotes test = new Patchnotes();

        Patchnotes.PatchnotesEntry testEntry = new Patchnotes.PatchnotesEntry(Str.stat("Test entry 1"), Str.stat("AAAAAAA\nBBBBBB"));
        test.entries.add(testEntry);
        Patchnotes.PatchnotesEntry testEntry2 = new Patchnotes.PatchnotesEntry(Str.stat("Test entry 2"), Str.stat("AAAAAAA\nBBBBBB"));
        test.entries.add(testEntry2);

        PatchnotesManager.registerCustomPatchnotes("dLib", () -> new PanelListScreen.Panel(Str.stat("DLib Test"), Tex.stat(UICommonResources.transparent_pixel), Str.stat("")), test);
    }

    public static void registerCustomKeybind(String actionId, Function<String, String> getLocalizedDisplayName, InputAction inputAction, CInputAction cInputAction){
        registerCustomKeybind(actionId, getLocalizedDisplayName, inputAction, cInputAction, () -> {});
    }

    public static void registerCustomKeybind(String actionId, Function<String, String> getLocalizedDisplayName, InputAction inputAction, CInputAction cInputAction, Runnable onKeyPressed){
        CustomKeybindManager.register(actionId, getLocalizedDisplayName, inputAction, cInputAction, onKeyPressed);
    }

    public static InputAction getCustomKeybind(String actionId){
        return CustomKeybindManager.getAction(actionId);
    }

    public static CInputAction getCustomCKeybind(String actionId){
        return CustomKeybindManager.getCAction(actionId);
    }

    //region Campfire Options

    public static void registerCustomCampfireOption(Supplier<AbstractCampfireOption> option) {
        CampfireOptionManager.registerCampfireOption(option);
    }

    //endregion Campfire Options

    //region Main Menu Button Manager

    public static void registerCustomMainMenuButton(MenuButton.ClickResult clickResult,
                                                    ArrayList<MenuButton.ClickResult> insertAfter,
                                                    AbstractStringBinding buttonLabel,
                                                    Runnable action,
                                                    Supplier<Boolean> isVisible) {
        MainMenuButtonManager.registerCustomMainMenuButton(clickResult, insertAfter, buttonLabel, action, isVisible);
    }
    public static void registerMainMenuButtonNameOverride(MenuButton.ClickResult result, AbstractStringBinding newName, Supplier<Boolean> isVisible) {
        MainMenuButtonManager.registerMainMenuButtonNameOverride(result, newName, isVisible);
    }
    public static void registerMainMenuButtonActionOverride(MenuButton.ClickResult result, Runnable action, Supplier<Boolean> isVisible) {
        MainMenuButtonManager.registerMainMenuButtonActionOverride(result, action, isVisible);
    }

    //endregion Main Menu Button Manager

    //region Achievements

    public static void registerCustomAchievement(AchievementItem achievement) {
        AchievementManager.registerCustomAchievement(achievement);
    }

    //endregion Achievements

    //region Patchnotes

    public static void registerCustomPatchnotes(String modDisplayName, Supplier<PanelListScreen.Panel> panel, Patchnotes patchnotes) {
        PatchnotesManager.registerCustomPatchnotes(modDisplayName, panel, patchnotes);
    }

    //endregion Patchnotes
}
