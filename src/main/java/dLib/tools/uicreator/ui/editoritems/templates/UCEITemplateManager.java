package dLib.tools.uicreator.ui.editoritems.templates;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.itembox.GridItemBox;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.scroll.HorizontalScrollbar;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.scroll.VerticalScrollbar;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TextButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class UCEITemplateManager {
    private static LinkedHashMap<Class<? extends UIElement.UIElementData>, UCEITemplate> templates = new LinkedHashMap<>();

    public static void initialize(){
        //TODO expose
        templates.put(TextBox.TextBoxData.class, new UCEITTextBox());
        templates.put(Image.ImageData.class, new UCEITImage());
        templates.put(Button.ButtonData.class, new UCEITButton());
        templates.put(TextButton.TextButtonData.class, new UCEITTextButton());
        templates.put(GridItemBox.GridItemBoxData.class, new UCEITGridBox());
        templates.put(VerticalBox.VerticalBoxData.class, new UCEITVerticalBox());
        templates.put(HorizontalBox.HorizontalBoxData.class, new UCEITHorizontalBox());

        templates.put(VerticalScrollbar.VerticalScrollbarData.class, new UCEITVerticalScrollbar());
        templates.put(HorizontalScrollbar.HorizontalScrollbarData.class, new UCEITHorizontalScrollbar());

        templates.put(Scrollbox.ScrollboxData.class, new UCEITScrollbox());
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
