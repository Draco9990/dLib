package dLib;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import dLib.external.ExternalEditorCommunicationManager;
import dLib.commands.CommandManager;
import dLib.custominput.CustomInputSetManager;
import dLib.shaders.ShaderManager;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplateManager;
import dLib.util.helpers.FontHelpers;
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
        CommandManager.initialize();

        FontHelpers.initialize();

        ExternalEditorCommunicationManager.start();

        UCEITemplateManager.initialize();

        ShaderManager.init();
    }

    public static void logError(String message){
        logger.error(message);
    }

    public static void log(String message){
        logger.info(message);
    }

    public static void registerCustomInputAction(String actionId, Function<String, String> getLocalizedDisplayName, InputAction inputAction, CInputAction cInputAction){
        CustomInputSetManager.register(actionId, getLocalizedDisplayName, inputAction, cInputAction);
    }

    public static InputAction getCustomInputAction(String actionId){
        return CustomInputSetManager.getAction(actionId);
    }

    public static CInputAction getCustomCInputAction(String actionId){
        return CustomInputSetManager.getCAction(actionId);
    }
}
