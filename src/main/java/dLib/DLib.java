package dLib;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import dLib.commands.CommandManager;
import dLib.plugin.intellij.PluginManager;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UIThemeManager;
import dLib.util.FontManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
        ScreenManager.initializeGlobalStrings();
        UIThemeManager.initialize();
        FontManager.initialize();

        PluginManager.start();
    }

    public static void logError(String message){
        logger.error(message);
    }

    public static void log(String message){
        logger.info(message);
    }
}
