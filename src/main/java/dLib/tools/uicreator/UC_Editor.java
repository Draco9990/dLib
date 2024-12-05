package dLib.tools.uicreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.VerticalBox;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

public class UC_Editor extends Renderable {
    public static final Color DARK_GRAY = Color.valueOf("#151515FF");
    public static final Color LIGHT_GRAY = Color.valueOf("#2B2B2BFF");

    private static UC_EditorToolbar toolbar;
    private static UC_EditorProperties properties;
    private static UC_EditorMainScreen mainScreen;

    public UC_Editor(){
        super(UITheme.whitePixel, Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill());
        setRenderColor(LIGHT_GRAY);

        HorizontalBox mainBox = new HorizontalBox(Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill(), true);
        {
            VerticalBox firstColumn = new VerticalBox(Pos.perc(0), Pos.perc(0), Dim.perc(0.8f), Dim.fill(), true);
            {
                firstColumn.addItem(toolbar = new UC_EditorToolbar());
                toolbar.setPaddingBottom(Padd.px(10));
                firstColumn.addItem(mainScreen = new UC_EditorMainScreen());
            }
            mainBox.addItem(firstColumn);

            mainBox.addItem(properties = new UC_EditorProperties());
            properties.setPaddingLeft(Padd.px(10));
        }
        mainBox.setPadding(Padd.px(10));
        addChildNCS(mainBox);
    }

    //region Subclasses

    private static class UC_EditorMainScreen extends Renderable{

        public UC_EditorMainScreen() {
            super(UITheme.whitePixel, Dim.fill(), Dim.perc(0.8f));
            Color transparent = new Color(0, 0, 0, 0);
            //setRenderColor(transparent);
        }

        @Override
        public int getWidth() {
            int result = super.getWidth();
            return result;
        }

        @Override
        public int getHeight() {
            int result =  super.getHeight();
            return result;
        }
    }

    private static class UC_EditorToolbar extends Renderable{
        public UC_EditorToolbar() {
            super(UITheme.whitePixel, Dim.fill(), Dim.perc(0.2f));
            setRenderColor(DARK_GRAY);
        }
    }

    private static class UC_EditorProperties extends Renderable{
        public UC_EditorProperties() {
            super(UITheme.whitePixel, Dim.fill(), Dim.fill());
            setRenderColor(DARK_GRAY);
        }
    }

    //region Subclasses
}
