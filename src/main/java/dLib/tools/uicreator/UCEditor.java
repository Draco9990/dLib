package dLib.tools.uicreator;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.uicreator.ui.components.data.UCEditorDataComponent;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplate;
import dLib.tools.uicreator.ui.editoritems.templates.UCEITemplateManager;
import dLib.tools.uicreator.ui.elements.RootElement;
import dLib.tools.uicreator.ui.elements.UCEHierarchyViewer;
import dLib.tools.uicreator.ui.elements.UCEPropertyEditor;
import dLib.ui.Alignment;
import dLib.ui.DLibUIElements;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.components.UIZoomableComponent;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.Spacer;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
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

        RootElement rootElement;

        HorizontalBox mainBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        {
            VerticalBox firstColumn = new VerticalBox(Pos.px(0), Pos.px(0), Dim.px(1536), Dim.fill());
            {
                firstColumn.addChild(toolbar = new UC_EditorToolbar());
                firstColumn.addChild(new Spacer(Dim.fill(), Dim.px(10)));
                firstColumn.addChild(mainScreen = new UC_EditorMainScreen());
                {
                    canvas = new Renderable(Tex.stat(DLibUIElements.UIEditorElements.transparentBg), Dim.px(10000), Dim.px(10000));
                    canvas.addComponent(new UIDraggableComponent());

                    canvas.setContainerBounds(Bound.element(mainScreen));
                    canvas.setContainerBoundCalculationType(BoundCalculationType.FILLS);

                    UIZoomableComponent zoomComp = canvas.addComponent(new UIZoomableComponent());
                    zoomComp.setMinScale(0.25f);

                    canvas.setElementMask(mainScreen);
                    canvas.setHorizontalAlignment(Alignment.HorizontalAlignment.CENTER);
                    canvas.setVerticalAlignment(Alignment.VerticalAlignment.CENTER);
                    //mainScreen.addChild(canvas);
                }
            }
            mainBox.addChild(firstColumn);
            mainBox.addChild(new Spacer(Dim.px(10), Dim.fill()));
            mainBox.addChild(properties = new UC_EditorProperties());
        }
        mainBox.setPadding(Padd.px(10));
        addChild(mainBox);

        UIElement canvasChild = new UIElement(Pos.px(0), Pos.px(0), Dim.px((int) (1920 * 0.8f)), Dim.px((int) (1080 * 0.8f)));
        canvasChild.setAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER);
        canvas.addChild(canvasChild);
        itemTree = new UCEditorItemTree(canvasChild);
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
            super(Tex.stat(UICommonResources.white_pixel), Dim.fill(), Dim.fill());
            setRenderColor(DARK_GRAY);

            HorizontalBox toolbar = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            {
                VerticalBox mainOptions = new VerticalBox(Pos.px(0), Pos.px(0), Dim.px(200), Dim.fill());
                mainOptions.setTexture(new TextureNoneBinding());
                mainOptions.setItemSpacing(10);
                {
                    TextButton toolboxButton = new TextButton("Close", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
                    toolboxButton.onLeftClickEvent.subscribeManaged(() -> {
                        getTopParent().close();
                    });
                    toolboxButton.setTexture(Tex.stat(UICommonResources.button03_square));
                    mainOptions.addChild(toolboxButton);
                }
                toolbar.addChild(mainOptions);
                toolbar.addChild(new Spacer(Dim.px(10), Dim.fill()));

                VerticalBox propertiesOptions = new VerticalBox(Pos.px(0), Pos.px(0), Dim.px(200), Dim.fill());
                propertiesOptions.setTexture(new TextureNoneBinding());
                propertiesOptions.setItemSpacing(10);
                {
                    TextButton toolboxButton = new TextButton("Toolbox", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
                    toolboxButton.onLeftClickEvent.subscribeManaged(() -> {
                        getProperties().hideAll();
                        getProperties().toolbarPropertiesScrollbox.showAndEnableInstantly();
                        getProperties().toolbox.showAndEnableInstantly();
                    });
                    toolboxButton.setTexture(Tex.stat(UICommonResources.button03_square));
                    propertiesOptions.addChild(toolboxButton);

                    TextButton elementListButton = new TextButton("Element List", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
                    elementListButton.onLeftClickEvent.subscribeManaged(() -> {
                        getProperties().hideAll();
                        getProperties().toolbarPropertiesScrollbox.showAndEnableInstantly();
                        getProperties().hierarchyViewer.showAndEnableInstantly();
                        getProperties().hierarchyViewer.loadForElement(((UCEditor)getTopParent()).itemTree.rootElementData.getComponent(UCEditorDataComponent.class).liveElement);
                    });
                    elementListButton.setTexture(Tex.stat(UICommonResources.button03_square));
                    propertiesOptions.addChild(elementListButton);

                    TextButton rootPropertiesButton = new TextButton("Root Properties", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(30));
                    rootPropertiesButton.onLeftClickEvent.subscribeManaged(() -> {
                        getProperties().hideAll();
                        getProperties().propertyEditor.showAndEnableInstantly();
                        getProperties().propertyEditor.setProperties(((UCEditor)getTopParent()).itemTree.rootElementData);
                    });
                    rootPropertiesButton.setTexture(Tex.stat(UICommonResources.button03_square));
                    propertiesOptions.addChild(rootPropertiesButton);
                }
                toolbar.addChild(propertiesOptions);
            }
            toolbar.setPadding(Padd.px(10));
            addChild(toolbar);
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
                toolbarPropertiesScrollbox.addChild(toolbox = new UC_EP_Toolbox());

                toolbarPropertiesScrollbox.addChild(hierarchyViewer = new UCEHierarchyViewer());
                hierarchyViewer.setAllowReordering(true);
                hierarchyViewer.hideAndDisableInstantly();
            }
            toolbarPropertiesScrollbox.setIsHorizontal(false);
            addChild(toolbarPropertiesScrollbox);

            addChild(propertyEditor = new UCEPropertyEditor(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
            propertyEditor.hideAndDisableInstantly();
        }

        public void hideAll(){
            toolbarPropertiesScrollbox.hideAndDisableInstantly();
            toolbox.hideAndDisableInstantly();
            hierarchyViewer.hideAndDisableInstantly();
            propertyEditor.hideAndDisableInstantly();
        }

        //region Subclasses

        public static class UC_EP_Toolbox extends VerticalDataBox<UCEITemplate> {
            public UC_EP_Toolbox() {
                super(Dim.fill(), Dim.fill());
                setTexture(new TextureNoneBinding());

                setSelectionMode(ESelectionMode.SINGLE_NOPERSIST);

                setChildren(UCEITemplateManager.getTemplates());
            }

            @Override
            public void show() { //replace with onShowed
                super.show();
                setChildren(UCEITemplateManager.getTemplates());
            }

            @Override
            public void onItemSelectionChanged() {
                super.onItemSelectionChanged();

                ArrayList<UCEITemplate> items = getCurrentlySelectedItems();
                if(!items.isEmpty()){
                    UIElementData elementData = items.get(0).makeElementData();
                    items.get(0).makeEditorItem(elementData);
                    ((UCEditor)getTopParent()).itemTree.addItem(elementData);

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
