package dLib.modcompat;

import com.evacipated.cardcrawl.modthespire.Loader;
import sayTheSpire.Output;

public class ModManager {
    public static class TogetherInSpireMod{
        public static final String modId = "spireTogether";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }
    public static class TogetherInSpireBoosterPackMod{
        public static final String modId = "tisCardPack";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }

    public static class Downfall{
        public static final String modId = "downfall";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }
    public static class MarisaContinued{
        public static final String modId = "MarisaContinued";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }
    public static class SkulHeroSlayer{
        public static final String modId = "skulmod";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }
    public static class TheAbyssal{
        public static final String modId = "PirateMod";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }
    public static class TheUnchained{
        public static final String modId = "TheUnchainedMod";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }
    public static class ThePackmaster{
        public static final String modId = "anniv5";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }
    public static class DuelistMod{
        public static final String modId = "duelistmod";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }

    public static class SayTheSpire{
        public static final String modId = "Say_the_Spire";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }

        public static void outputCond(String message){
            if(ModManager.SayTheSpire.isActive() && message != null){
                Output.text(message, true);
            }
        }
    }

    public static class SpireLocations{
        public static final String modId = "spireLocations";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }

    public static class InfiniteSpire{
        public static final String modId = "infinitespire";

        public static boolean isActive(){
            return Loader.isModLoaded(modId);
        }
    }
}
