package dLib.tools.uicreator;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplate;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplateManager;
import dLib.tools.uicreator.ui.elements.UCERootElement;
import dLib.ui.DLibUIElements;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.*;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.ui.util.ESelectionMode;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public class UCEditor extends Renderable {
    public static final Color DARK_GRAY = Color.valueOf("#151515FF");
    public static final Color LIGHT_GRAY = Color.valueOf("#2B2B2BFF");

    public UC_EditorToolbar toolbar;
    public UC_EditorProperties properties;
    public UC_EditorMainScreen mainScreen;

    private UCERootElement rootElement;

    public UCEditor(){
        super(UITheme.whitePixel, Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill());
        setRenderColor(LIGHT_GRAY);

        HorizontalBox mainBox = new HorizontalBox(Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill());
        {
            VerticalBox firstColumn = new VerticalBox(Pos.perc(0), Pos.px(0), Dim.px(1536), Dim.fill());
            {
                firstColumn.addItem(toolbar = new UC_EditorToolbar());
                firstColumn.addItem(new Spacer(Dim.fill(), Dim.px(10)));
                firstColumn.addItem(mainScreen = new UC_EditorMainScreen());
                {
                    mainScreen.addChildNCS(rootElement = new UCERootElement());
                }
            }
            mainBox.addItem(firstColumn);
            mainBox.addItem(new Spacer(Dim.px(10), Dim.fill()));
            mainBox.addItem(properties = new UC_EditorProperties());
        }
        mainBox.setPadding(Padd.px(10));
        addChildNCS(mainBox);
    }

    //region Methods

    public void addNewEditorItem(UIElement item){
        rootElement.addChildNCS(item);
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

            HorizontalBox toolbar = new HorizontalBox(Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill());
            {
                toolbar.addItem(new Spacer(Dim.px(200), Dim.fill()));
                toolbar.addItem(new Spacer(Dim.px(10), Dim.fill()));

                VerticalBox propertiesOptions = new VerticalBox(Pos.px(0), Pos.px(0), Dim.px(200), Dim.fill());
                propertiesOptions.setDefaultItemHeight(30);
                propertiesOptions.setImage(null);
                propertiesOptions.setItemSpacing(10);
                propertiesOptions.disableItemWrapping();
                {
                    TextButton toolboxButton = new TextButton("Toolbox", Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.px(30));
                    toolboxButton.addOnLeftClickEvent(() -> {
                        getProperties().hideAll();
                        getProperties().toolbox.showAndEnableInstantly();
                    });
                    toolboxButton.getButton().setImage(UIThemeManager.getDefaultTheme().itemBoxVerticalItemBg);
                    propertiesOptions.addItem(toolboxButton);

                    TextButton elementListButton = new TextButton("Element List", Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.px(30));
                    elementListButton.addOnLeftClickEvent(() -> {
                        getProperties().hideAll();
                        getProperties().hierarchyViewer.showAndEnableInstantly();
                        getProperties().hierarchyViewer.loadForElement(((UCEditor)getTopParent()).rootElement);
                    });
                    elementListButton.getButton().setImage(UIThemeManager.getDefaultTheme().itemBoxVerticalItemBg);
                    propertiesOptions.addItem(elementListButton);

                    TextButton propertiesButton = new TextButton("Properties", Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.px(30));
                    propertiesButton.getButton().setImage(UIThemeManager.getDefaultTheme().itemBoxVerticalItemBg);
                    propertiesOptions.addItem(propertiesButton);
                }
                toolbar.addItem(propertiesOptions);
            }
            toolbar.setPadding(Padd.px(10));
            addChildNCS(toolbar);
        }

        public UC_EditorProperties getProperties(){
            return ((UCEditor)getTopParent()).properties;
        }
    }

    private static class UC_EditorProperties extends Renderable{
        public UC_EP_Toolbox toolbox;
        public HierarchyViewer hierarchyViewer;

        public UC_EditorProperties() {
            super(UITheme.whitePixel, Dim.fill(), Dim.fill());
            setRenderColor(DARK_GRAY);

            Scrollbox propertiesScrollbox = new Scrollbox(Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill());
            {
                propertiesScrollbox.addChildNCS(toolbox = new UC_EP_Toolbox());

                propertiesScrollbox.addChildNCS(hierarchyViewer = new HierarchyViewer());
                hierarchyViewer.hideAndDisableInstantly();
            }
            propertiesScrollbox.setIsHorizontal(false);
            addChildNCS(propertiesScrollbox);
        }

        public void hideAll(){
            toolbox.hideAndDisableInstantly();
            hierarchyViewer.hideAndDisableInstantly();
        }

        //region Subclasses

        private static class UC_EP_Toolbox extends VerticalListBox<UCEITemplate> {
            public UC_EP_Toolbox() {
                super(Dim.fill(), Dim.fill());
                setImage(null);

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
