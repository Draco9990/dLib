package dLib;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import dLib.commands.CommandManager;
import dLib.custominput.CustomKeybindManager;
import dLib.external.ExternalEditorCommunicationManager;
import dLib.shaders.ShaderManager;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplateManager;
import dLib.ui.GeneratedUIManager;
import dLib.util.helpers.FontHelpers;
import dLib.util.helpers.SteamHelpers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

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
        GeneratedUIManager.initialize();

        SteamHelpers.init();

        CommandManager.initialize();

        FontHelpers.initialize();

        ExternalEditorCommunicationManager.start();

        UCEITemplateManager.initialize();

        ShaderManager.init();

        CustomKeybindManager.registerCommonEvents();
    }

    public static void logError(String message){
        logger.error(message);
    }

    public static void log(String message){
        logger.info(message);
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
}
