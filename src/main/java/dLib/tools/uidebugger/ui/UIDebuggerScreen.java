package dLib.tools.uidebugger.ui;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class UIDebuggerScreen extends UIElement {
    public UIDebuggerToolbar toolbar;

    public UIDebuggerElementInfo elementInfo;

    public UIDebuggerScreen(){
        super();

        VerticalBox elementList = new VerticalBox(Pos.px(100), Pos.px(100), Dim.auto(), Dim.auto());
        elementList.setVerticalAlignment(Alignment.VerticalAlignment.TOP);
        elementList.setItemSpacing(30);
        elementList.setTexture(UICommonResources.white_pixel);
        {
            elementList.addChild(toolbar = new UIDebuggerToolbar());
            elementList.addChild(elementInfo = new UIDebuggerElementInfo());
        }
        addChild(elementList);
    }

    //region Subclasses

    public static class UIDebuggerToolbar extends HorizontalBox {
        public UIDebuggerToolbar(){
            super(Pos.px(0), Pos.px(0), Dim.auto(), Dim.auto());

            setVerticalAlignment(Alignment.VerticalAlignment.TOP);

            setItemSpacing(10);

            addChild(new TextButton("D", Dim.px(50), Dim.px(50)));

            TextButton informationButton = new TextButton("i", Dim.px(50), Dim.px(50));
            addChild(informationButton);
            informationButton.onLeftClickEvent.subscribeManaged(() -> {
                UIDebuggerScreen parent = getParentOfType(UIDebuggerScreen.class);
                if(parent.elementInfo.isActive()){
                    parent.elementInfo.hideAndDisable();
                }
                else{
                    parent.elementInfo.showAndEnable();
                }
            });
        }
    }

    public static class UIDebuggerElementInfo extends ImageTextBox {
        public UIDebuggerElementInfo(){
            super("", Dim.px(600), Dim.px(400));

            hideAndDisableInstantly();
        }
    }

    //endregion
}
