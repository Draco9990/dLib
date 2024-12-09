package dLib.tools.uicreator.ui.editoritems.templates;

import java.util.ArrayList;

public class UCEITemplateManager {
    private static ArrayList<UCEITemplate> templates = new ArrayList<>();

    public static void initialize(){
        //TODO expose
        templates.add(new UCEITImage());
    }

    public static ArrayList<UCEITemplate> getTemplates(){
        return templates;
    }
}
