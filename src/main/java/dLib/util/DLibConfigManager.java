package dLib.util;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

public class DLibConfigManager {
    /** Variables */
    private static SpireConfig config;

    /** Definitions */
    public static String DEV_MODE = "DEV_MODE";

    /** Config */
    public static SpireConfig get(){
        if(config == null){
            initializeConfig();
        }

        return config;
    }

    private static void initializeConfig(){
        try{
            config = new SpireConfig("DLib", "config");
            config.load();
        }catch (Exception e){
            DLibLogger.log("Could not initialize the spire config file due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
