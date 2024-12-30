package dLib.tools.uicreator;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplate;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplateManager;
import dLib.tools.uicreator.ui.elements.UCEHierarchyViewer;
import dLib.tools.uicreator.ui.elements.UCEPropertyEditor;
import dLib.tools.uicreator.ui.elements.UCERootElement;
import dLib.ui.Alignment;
import dLib.ui.DLibUIElements;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.components.UIZoomableComponent;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.*;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.itembox.VerticalListBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;


import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.ui.bounds.Bound;
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
    public Renderable canvas;

    public UCEditorItemTree itemTree;

    public UCEditor(){
        super(Tex.stat(UICommonResources.white_pixel), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        setRenderColor(LIGHT_GRAY);

        UCERootElement rootElement;

        HorizontalBox mainBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        {
            VerticalBox firstColumn = new VerticalBox(Pos.px(0), Pos.px(0), Dim.px(1536), Dim.fill());
            {
                firstColumn.addItem(toolbar = new UC_EditorToolbar());
                firstColumn.addItem(new Spacer(Dim.fill(), Dim.px(10)));
                firstColumn.addItem(mainScreen = new UC_EditorMainScreen());
                {
                    canvas = new Renderable(Tex.stat(DLibUIElements.UIEditorElements.transparentBg), Dim.px(10000), Dim.px(10000));
                    {
                        canvas.addChildNCS(rootElement = new UCERootElement());
                        rootElement.setID("Root");
                    }
                    canvas.addComponent(new UIDraggableComponent());

                    canvas.setContainerBounds(Bound.element(mainScreen));
                    canvas.setContainerBoundCalculationType(BoundCalculationType.FILLS);

                    UIZoomableComponent zoomComp = canvas.addComponent(new UIZoomableComponent());
                    zoomComp.setMinScale(0.25f);

                    canvas.setElementMask(mainScreen);
                    canvas.setHorizontalAlignment(Alignment.HorizontalAlignment.CENTER);
                    canvas.setVerticalAlignment(Alignment.VerticalAlignment.CENTER);
                    mainScreen.addChildNCS(canvas);
                }
            }
            mainBox.addItem(firstColumn);
            mainBox.addItem(new Spacer(Dim.px(10), Dim.fill()));
            mainBox.addItem(properties = new UC_EditorProperties());
        }
        mainBox.setPadding(Padd.px(10));
        addChildNCS(mainBox);

        itemTree = new UCEditorItemTree(rootElement);
    }

    //region Methods

    //endregion

    //region Subclasses

    public static class UC_EditorMainScreen extends Renderable{

        public UC_EditorMainScreen() {
            super(Tex.stat(UICommonResources.white_pixel), Dim.px(1536), Dim.px(864));

            Color transparent = new Color(0, 0, 0, 0);
            setRenderColor(transparent);
        }
    }

    public static class UC_EditorToolbar extends Renderable{
        public UC_EditorToolbar() {
            super(Tex.stat(UICommonResources.white_pixel), Dim.perc(100), Dim.fill());
            setRenderColor(DARK_GRAY);

            HorizontalBox toolbar = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            {
                toolbar.addItem(new Spacer(Dim.px(200), Dim.fill()));
                toolbar.addItem(new Spacer(Dim.px(10), Dim.fill()));

                VerticalBox propertiesOptions = new VerticalBox(Pos.px(0), Pos.px(0), Dim.px(200), Dim.fill());
                propertiesOptions.setDefaultItemHeight(30);
                propertiesOptions.setImage(new TextureNoneBinding());
                propertiesOptions.setItemSpacing(10);
                propertiesOptions.disableItemWrapping();
                {
                    TextButton toolboxButton = new TextButton("Toolbox", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
                    toolboxButton.onLeftClickEvent.subscribeManaged(() -> {
                        getProperties().hideAll();
                        getProperties().toolbarPropertiesScrollbox.showAndEnableInstantly();
                        getProperties().toolbox.showAndEnableInstantly();
                    });
                    toolboxButton.setImage(Tex.stat(UICommonResources.itembox_itembg_horizontal));
                    propertiesOptions.addItem(toolboxButton);

                    TextButton elementListButton = new TextButton("Element List", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
                    elementListButton.onLeftClickEvent.subscribeManaged(() -> {
                        getProperties().hideAll();
                        getProperties().toolbarPropertiesScrollbox.showAndEnableInstantly();
                        getProperties().hierarchyViewer.showAndEnableInstantly();
                        getProperties().hierarchyViewer.loadForElement(((UCEditor)getTopParent()).itemTree.rootElement);
                    });
                    elementListButton.setImage(Tex.stat(UICommonResources.itembox_itembg_horizontal));
                    propertiesOptions.addItem(elementListButton);
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

    public static class UC_EditorProperties extends Renderable{
        public UC_EP_Toolbox toolbox;
        public UCEHierarchyViewer hierarchyViewer;
        public Scrollbox toolbarPropertiesScrollbox;

        public UCEPropertyEditor propertyEditor;

        public UC_EditorProperties() {
            super(Tex.stat(UICommonResources.white_pixel), Dim.fill(), Dim.fill());
            setRenderColor(DARK_GRAY);

            toolbarPropertiesScrollbox = new Scrollbox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            {
                toolbarPropertiesScrollbox.addChildNCS(toolbox = new UC_EP_Toolbox());

                toolbarPropertiesScrollbox.addChildNCS(hierarchyViewer = new UCEHierarchyViewer());
                hierarchyViewer.hideAndDisableInstantly();
            }
            toolbarPropertiesScrollbox.setIsHorizontal(false);
            addChildNCS(toolbarPropertiesScrollbox);

            addChildNCS(propertyEditor = new UCEPropertyEditor(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
            propertyEditor.hideAndDisableInstantly();
        }

        public void hideAll(){
            toolbarPropertiesScrollbox.hideAndDisableInstantly();
            toolbox.hideAndDisableInstantly();
            hierarchyViewer.hideAndDisableInstantly();
            propertyEditor.hideAndDisableInstantly();
        }

        //region Subclasses

        public static class UC_EP_Toolbox extends VerticalListBox<UCEITemplate> {
            public UC_EP_Toolbox() {
                super(Dim.fill(), Dim.fill());
                setImage(new TextureNoneBinding());

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
                    UIElementData elementData = items.get(0).makeElementData();
                    UIElement element = items.get(0).makeEditorItem(elementData);
                    ((UCEditor)getTopParent()).itemTree.addItem(element, elementData, items.get(0));

                    ((UCEditor)getTopParent()).properties.hideAll();
                    ((UCEditor)getTopParent()).properties.propertyEditor.showAndEnableInstantly();
                    ((UCEditor)getTopParent()).properties.propertyEditor.setProperties(elementData);
                }
            }
        }

        //endregion
    }

    //region Subclasses
}
