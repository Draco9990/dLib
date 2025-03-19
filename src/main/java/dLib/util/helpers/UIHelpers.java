package dLib.util.helpers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.layout.ILayoutProvider;
import dLib.util.IntegerVector2;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.FillDimension;
import dLib.util.ui.dimensions.PercentageDimension;

import java.util.UUID;

public class UIHelpers {
    public static IntegerVector2 getMouseWorldPosition(){
        return new IntegerVector2(getMouseWorldPositionX(), getMouseWorldPositionY());
    }

    public static int getMouseWorldPositionX(){
        return (int) Math.floor(InputHelper.mX / Settings.xScale);
    }

    public static int getMouseWorldPositionY(){
        return (int) Math.floor(InputHelper.mY / Settings.yScale);
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
            if(!parent.needsWidthCalculation()){
                parentWidth = parent.getWidth();
                if(parent.getWidthRaw() instanceof AutoDimension){
                    parentWidth = ((AutoDimension) parent.getWidthRaw()).getCalculatedValueForChildren();
                }

                if(parent instanceof ILayoutProvider){
                    parentWidth -= ((ILayoutProvider) parent).getContentPaddingLeft();
                    parentWidth -= ((ILayoutProvider) parent).getContentPaddingRight();
                }

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
            if(!parent.needsHeightCalculation()){
                parentHeight = parent.getHeight();
                if(parent.getHeightRaw() instanceof AutoDimension){
                    parentHeight = ((AutoDimension) parent.getHeightRaw()).getCalculatedValueForChildren();
                }

                if(parent instanceof ILayoutProvider){
                    parentHeight -= ((ILayoutProvider) parent).getContentPaddingTop();
                    parentHeight -= ((ILayoutProvider) parent).getContentPaddingBottom();
                }

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
