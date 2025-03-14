package dLib.util.helpers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.util.IntegerVector2;
import dLib.util.ui.dimensions.AutoDimension;

import java.util.UUID;

public class UIHelpers {
    public static IntegerVector2 getMouseWorldPosition(){
        return new IntegerVector2((int) (InputHelper.mX / Settings.xScale), (int) (InputHelper.mY / Settings.yScale));
    }

    public static String generateRandomElementId(){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString.replaceAll("-", "");
    }

    public static Integer getCalculatedParentWidthInHierarchy(UIElement forElement){
        Integer parentWidth = 1920;

        UIElement parent = forElement.getParent();
        while(parent != null){
            if(!parent.getWidthRaw().needsRecalculation()){
                parentWidth = parent.getWidth();
                break;
            }
            else if(!(parent.getWidthRaw() instanceof AutoDimension)){
                return null;
            }

            parent = parent.getParent();
        }

        return parentWidth;
    }

    public static Integer getCalculatedParentHeightInHierarchy(UIElement forElement){
        Integer parentHeight = 1080;

        UIElement parent = forElement.getParent();
        while(parent != null){
            if(!parent.getHeightRaw().needsRecalculation()){
                parentHeight = parent.getHeight();
                break;
            }
            else if(!(parent.getHeightRaw() instanceof AutoDimension)){
                return null;
            }

            parent = parent.getParent();
        }

        return parentHeight;
    }
}
