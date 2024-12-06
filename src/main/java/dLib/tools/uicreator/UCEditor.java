package dLib.tools.uicreator;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplate;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplateManager;
import dLib.ui.DLibUIElements;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.*;
import dLib.ui.themes.UITheme;
import dLib.ui.util.ESelectionMode;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public class UCEditor extends Renderable {
    public static final Color DARK_GRAY = Color.valueOf("#151515FF");
    public static final Color LIGHT_GRAY = Color.valueOf("#2B2B2BFF");

    private static UC_EditorToolbar toolbar;
    private static UC_EditorProperties properties;
    private static UC_EditorMainScreen mainScreen;

    public UCEditor(){
        super(UITheme.whitePixel, Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill());
        setRenderColor(LIGHT_GRAY);

        HorizontalBox mainBox = new HorizontalBox(Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill(), true);
        {
            VerticalBox firstColumn = new VerticalBox(Pos.perc(0), Pos.px(0), Dim.px(1536), Dim.fill(), true);
            {
                firstColumn.addItem(toolbar = new UC_EditorToolbar());
                firstColumn.addItem(new Spacer(Dim.fill(), Dim.px(10)));
                firstColumn.addItem(mainScreen = new UC_EditorMainScreen());
            }
            mainBox.addItem(firstColumn);
            mainBox.addItem(new Spacer(Dim.px(10), Dim.fill()));
            mainBox.addItem(properties = new UC_EditorProperties());
        }
        mainBox.setPadding(Padd.px(10));
        addChildNCS(mainBox);
    }

    //region Methods

    public static void addNewEditorItem(UIElement item){
        mainScreen.addChildNCS(item);
    }

    //endregion

    //region Subclasses

    private static class UC_EditorMainScreen extends Renderable{

        public UC_EditorMainScreen() {
            super(DLibUIElements.UIEditorElements.transparentBg, Dim.px(1536), Dim.px(864));
        }
    }

    private static class UC_EditorToolbar extends Renderable{
        public UC_EditorToolbar() {
            super(UITheme.whitePixel, Dim.perc(100), Dim.fill());
            setRenderColor(DARK_GRAY);

            HorizontalBox toolbar = new HorizontalBox(Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill(), true);
            {
                toolbar.addItem(new Spacer(Dim.px(200), Dim.fill()));
                toolbar.addItem(new Spacer(Dim.px(10), Dim.fill()));

                VerticalListBox<String> propertiesOptions = new VerticalListBox<>(Pos.px(0), Pos.px(0), Dim.px(200), Dim.fill(), true);
                propertiesOptions.setDefaultItemHeight(30);
                propertiesOptions.getBackground().setImage(null);
                propertiesOptions.setItemSpacing(10);
                {
                    propertiesOptions.addItem("Toolbox");
                    propertiesOptions.addItem("Element List");
                    propertiesOptions.addItem("Properties");
                }
                toolbar.addItem(propertiesOptions);
            }
            toolbar.setPadding(Padd.px(10));
            addChildNCS(toolbar);
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

        private static class UC_EP_Toolbox extends VerticalListBox<UCEITemplate> {
            public UC_EP_Toolbox() {
                super(Dim.fill(), Dim.fill());
                getBackground().setImage(null);

                setSelectionMode(ESelectionMode.SINGLE_NOPERSIST);

                setItems(UCEITemplateManager.getTemplates());
            }

            @Override
            public void show() { //replace with onShowed
                super.show();
                setItems(UCEITemplateManager.getTemplates());
            }

            @Override
            public void onItemSelectionChanged(ArrayList<UCEITemplate> items) {
                super.onItemSelectionChanged(items);

                if(!items.isEmpty()){
                    ((UCEditor)getTopParent()).addNewEditorItem(items.get(0).makeEditorItem());
                }
            }
        }

        //endregion
    }

    //region Subclasses
}
