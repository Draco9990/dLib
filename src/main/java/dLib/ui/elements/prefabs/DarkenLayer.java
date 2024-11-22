package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.animations.entry.UIAnimation_FadeIn;
import dLib.ui.animations.exit.UIAnimation_FadeOut;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;

public class DarkenLayer extends Image {
    public DarkenLayer(){
        super(UITheme.whitePixel,0, 0, 1920, 1080);

        init();
    }

    public DarkenLayer(int xPos, int yPos, int width, int height){
        super(UITheme.whitePixel, xPos, yPos, width, height);

        init();
    }

    private void init(){
        setRenderColor(new Color(0, 0, 0, 0.85f));
        setDarkenedColor(new Color(0, 0, 0, 0.85f));
        setHoveredColor(new Color(0, 0, 0, 0.85f));
        setDarkenedColorMultiplier(0);
        setHoveredColorMultiplier(0);
        //setDarkenedColor(new Color(0, 0, 0, 0.85f));
        hideInstantly();
        disable();

        setEntryAnimation(new UIAnimation_FadeIn(this));
        setExitAnimation(new UIAnimation_FadeOut(this));

        setClickthrough(false);
    }

    public void darken(){
        showAndEnable();
    }

    public void lighten(){
        hideAndDisable();
    }

    @Override
    public boolean isClickthrough() {
        return super.isClickthrough();
    }
}
