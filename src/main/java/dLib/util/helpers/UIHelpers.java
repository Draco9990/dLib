package dLib.util.helpers;

import basemod.Pair;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TokenizedDescriptionBox;
import dLib.ui.layout.ILayoutProvider;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
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
        return getCalculatedParentWidthInHierarchyWithParent(forElement, true).getKey(); // TODO
    }
    public static Pair<Float, UIElement> getCalculatedParentWidthInHierarchyWithParent(UIElement forElement, boolean canBypassAutoParents){
        float parentWidth = 1920;

        UIElement parent = forElement.getParent();
        while(parent != null){
            if(!parent.getWidthRaw().needsRecalculation()){
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
            else if(!canBypassAutoParents || !(parent.getWidthRaw() instanceof AutoDimension)){
                return new Pair<>(null, null);
            }

            parent = parent.getParent();
        }

        return new Pair<>(parentWidth, parent);
    }

    public static Float getCalculatedParentHeightInHierarchy(UIElement forElement){
        return getCalculatedParentHeightInHierarchyWithParent(forElement, true).getKey();
    }
    public static Pair<Float, UIElement> getCalculatedParentHeightInHierarchyWithParent(UIElement forElement, boolean canBypassAutoParents){
        float parentHeight = 1080;

        UIElement parent = forElement.getParent();
        while(parent != null){
            if(!parent.getHeightRaw().needsRecalculation()){
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
            else if(!canBypassAutoParents || !(parent.getHeightRaw() instanceof AutoDimension)){
                return new Pair<>(null, null);
            }

            parent = parent.getParent();
        }

        return new Pair<>(parentHeight, parent);
    }

    public static ArrayList<UIElement> tokenizeStr(String text){
        return tokenizeStr(text, 16f); // Default font size
    }
    public static ArrayList<UIElement> tokenizeStr(String text, float fontSize){
        ArrayList<UIElement> elements = new ArrayList<>();

        String[] words = text.split(" ");
        for (String word : words) {
            if (word.isEmpty()) continue; // Skip empty strings

            if(word.startsWith("#r")) word = "[#" + "ff" + "66" + "66" + "ff]" + word.substring(2) + "[]";
            if(word.startsWith("#g")) word = "[#" + "66" + "ff" + "66" + "ff]" + word.substring(2) + "[]";
            if(word.startsWith("#b")) word = "[#" + "66" + "66" + "ff" + "ff]" + word.substring(2) + "[]";
            if(word.startsWith("#y")) word = "[#" + "ff" + "ff" + "66" + "ff]" + word.substring(2) + "[]";
            if(word.startsWith("#p")) word = "[#" + "ff" + "66" + "ff" + "ff]" + word.substring(2) + "[]";

            if (word.equals("[E]")) {
                Image energyImage = new Image(Tex.stat(UICommonResources.energyIconUniversal), Pos.px(0), Pos.px(0), Dim.px(fontSize), Dim.px(fontSize));
                elements.add(energyImage);
            }
            else if(word.equals("NL")){
                TokenizedDescriptionBox.NLBreak nlBreak = new TokenizedDescriptionBox.NLBreak();
                elements.add(nlBreak);
            }
            else {
                TextBox textBox = new TextBox(word, Pos.px(0), Pos.px(0), Dim.auto(), Dim.auto());
                textBox.setFontSize(fontSize);
                textBox.setUseSelfAsMask(false);
                elements.add(textBox);
            }
        }

        return elements;
    }

    public static Texture mergeTextures(Texture... textures) {
        if (textures == null || textures.length == 0) return null;

        int width = 0;
        int height = 0;

        // Assumes all textures are the same size (or at least uses the first one's size)
        width = textures[0].getWidth();
        height = textures[0].getHeight();

        Pixmap resultPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        for (Texture texture : textures) {
            // Get the texture's Pixmap
            TextureRegion region = new TextureRegion(texture);
            region.getTexture().getTextureData().prepare();
            Pixmap pixmap = region.getTexture().getTextureData().consumePixmap();

            resultPixmap.drawPixmap(pixmap, 0, 0);
            pixmap.dispose(); // Dispose the temporary Pixmap if you don't need it
        }

        Texture resultTexture = new Texture(resultPixmap);
        resultPixmap.dispose(); // Dispose if you no longer need the CPU-side data

        return resultTexture;
    }
}
