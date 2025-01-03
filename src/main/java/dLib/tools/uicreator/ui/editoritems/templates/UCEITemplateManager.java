package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.text.TextBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class UCEITemplateManager {
    private static LinkedHashMap<Class<? extends UIElement.UIElementData>, UCEITemplate> templates = new LinkedHashMap<>();

    public static void initialize(){
        //TODO expose
        templates.put(Image.ImageData.class, new UCEITImage());
        templates.put(TextBox.TextBoxData.class, new UCEITTextBox());
    }

    public static ArrayList<UCEITemplate> getTemplates(){
        return new ArrayList<>(templates.values());
    }

    public static UCEITemplate getBestTemplateFor(UIElement.UIElementData data){
        Class<?> bestClass = null;

        for(Class<? extends UIElement.UIElementData> clazz : templates.keySet()){
            if(clazz.isInstance(data)){
                if(bestClass == null || bestClass.isAssignableFrom(clazz)){
                    bestClass = clazz;
                }
            }
        }

        if(bestClass != null){
            return templates.get(bestClass);
        }

        return null;
    }
}
