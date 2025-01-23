package dLib.tools.uidebugger.ui;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class UIDebuggerScreen extends UIElement {
    public UIDebuggerElementList elementList;

    public UIDebuggerScreen(){
        super();

        addChild(elementList = new UIDebuggerElementList());
    }

    //region Subclasses

    public static class UIDebuggerElementList extends VerticalBox {
        public UIDebuggerToolbar toolbar;
        public UIDebuggerElementInfo elementInfo;

        public UIDebuggerElementList(){
            super(Pos.px(100), Pos.px(100), Dim.auto(), Dim.auto());

            setVerticalAlignment(Alignment.VerticalAlignment.TOP);
            setItemSpacing(30);

            addChild(toolbar = new UIDebuggerToolbar());
            addChild(elementInfo = new UIDebuggerElementInfo());
        }

        public static class UIDebuggerToolbar extends HorizontalBox {
            public UIDebuggerToolbar(){
                super(Pos.px(0), Pos.px(0), Dim.auto(), Dim.auto());

                setVerticalAlignment(Alignment.VerticalAlignment.TOP);

                setItemSpacing(10);

                addChild(new TextButton("D", Dim.px(50), Dim.px(50)));

                TextButton informationButton = new TextButton("i", Dim.px(50), Dim.px(50));
                addChild(informationButton);
                informationButton.onLeftClickEvent.subscribeManaged(() -> {
                    UIDebuggerElementList topScreen = getParentOfType(UIDebuggerElementList.class);
                    if(topScreen.elementInfo.isVisible()){
                        topScreen.elementInfo.hideAndDisableInstantly();
                    }else{
                        topScreen.elementInfo.showAndEnableInstantly();
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
    }

    //endregion
}
