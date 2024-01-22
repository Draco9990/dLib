package dLib.ui.themes;

import java.util.HashMap;
import java.util.Map;

public class UIThemeManager {

    public static void initialize(){
        ThemeList.themeMap = new HashMap<>();

        ThemeList.basic = new UITheme("dlib_basic", "dLibResources/images/ui/themes/basic/");
        ThemeList.basic.load();
        registerTheme(ThemeList.basic);

        themeRegisterProcess();
    }

    private static void themeRegisterProcess(){
    }

    public static void registerTheme(UITheme theme){
        ThemeList.themeMap.put(theme.getId(), theme);
    }

    public static UITheme getThemeById(String id){
        return ThemeList.themeMap.get(id);
    }

    public static UITheme getDefaultTheme(){
        return ThemeList.basic;
    }

    public static class ThemeList{
        private static Map<String, UITheme> themeMap;

        public static UITheme basic;
    }
}
