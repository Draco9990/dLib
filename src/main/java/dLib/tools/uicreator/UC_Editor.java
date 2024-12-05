package dLib.tools.uicreator;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditorold.ui.items.editoritems.ScreenEditorItem;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.VerticalBox;
import dLib.ui.elements.prefabs.VerticalListBox;
import dLib.ui.themes.UITheme;
import dLib.ui.util.ESelectionMode;
import dLib.util.Reflection;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;
import sun.reflect.generics.tree.ClassTypeSignature;
import sun.reflect.generics.tree.FieldTypeSignature;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

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
            VerticalBox firstColumn = new VerticalBox(Pos.perc(0), Pos.perc(0), Dim.perc(0.8f), Dim.fill(), true){
                @Override
                public int getWidth() {
                    int res = super.getWidth();
                    return res;
                }

                @Override
                public int getHeight() {
                    int res = super.getHeight();
                    return res;
                }

                @Override
                public int getLocalPositionX() {
                    int res = super.getLocalPositionX();
                    return res;
                }

                @Override
                public int getLocalPositionY() {
                    int res = super.getLocalPositionY();
                    return res;
                }
            };
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
            setRenderColor(transparent);
        }
    }

    private static class UC_EditorToolbar extends Renderable{
        public UC_EditorToolbar() {
            super(UITheme.whitePixel, Dim.fill(), Dim.perc(0.2f));
            setRenderColor(DARK_GRAY);
        }
    }

    private static class UC_EditorProperties extends Renderable{
        public UC_EP_Toolbox toolbox;

        public UC_EditorProperties() {
            super(UITheme.whitePixel, Dim.fill(), Dim.fill());
            setRenderColor(DARK_GRAY);

            addChildNCS(toolbox = new UC_EP_Toolbox());
        }

        //region Subclasses

        private static class UC_EP_Toolbox extends VerticalListBox<Class<? extends ScreenEditorItem>> {
            public UC_EP_Toolbox() {
                super(Dim.fill(), Dim.fill());
                getBackground().setImage(null);

                setSelectionMode(ESelectionMode.SINGLE_NOPERSIST);

                setItems(Reflection.findClassesOfType(ScreenEditorItem.class, false));
            }

            @Override
            public String itemToString(Class<? extends ScreenEditorItem> item) {
                ParameterizedType type = (ParameterizedType) item.getGenericSuperclass();
                Type actualType = type.getActualTypeArguments()[0];

                try{
                    String actualTypeName = actualType.getTypeName().split("<")[0];
                    Class<?> typeClass = Class.forName(actualTypeName);
                    return typeClass.getSimpleName();
                }
                catch (Exception e){
                    return "";
                }
            }

            @Override
            public void show() { //replace with onShowed
                super.show();
                setItems(Reflection.findClassesOfType(ScreenEditorItem.class, false));
            }
        }

        //endregion
    }

    //region Subclasses
}
