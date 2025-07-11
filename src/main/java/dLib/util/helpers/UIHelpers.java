package dLib.util.helpers;

import basemod.Pair;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.layout.ILayoutProvider;
import dLib.util.ui.dimensions.AutoDimension;

import java.util.UUID;

public class UIHelpers {
    public static Vector2 getMouseWorldPosition(){
        return new Vector2(getMouseWorldPositionX(), getMouseWorldPositionY());
    }

    public static float getMouseWorldPositionX(){
        return (InputHelper.mX / Settings.xScale);
    }

    public static float getMouseWorldPositionY(){
        return (InputHelper.mY / Settings.yScale);
    }

    public static String generateRandomElementId(){
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString.replaceAll("-", "");
    }

    public static Float getCalculatedParentWidthInHierarchy(UIElement forElement){
        return getCalculatedParentWidthInHierarchyWithParent(forElement).getKey();
    }
    public static Pair<Float, UIElement> getCalculatedParentWidthInHierarchyWithParent(UIElement forElement){
        float parentWidth = 1920;

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
                return new Pair<>(null, null);
            }

            parent = parent.getParent();
        }

        return new Pair<>(parentWidth, parent);
    }

    public static Float getCalculatedParentHeightInHierarchy(UIElement forElement){
        return getCalculatedParentHeightInHierarchyWithParent(forElement).getKey();
    }
    public static Pair<Float, UIElement> getCalculatedParentHeightInHierarchyWithParent(UIElement forElement){
        float parentHeight = 1080;

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
                return new Pair<>(null, null);
            }

            parent = parent.getParent();
        }

        return new Pair<>(parentHeight, parent);
    }
}
