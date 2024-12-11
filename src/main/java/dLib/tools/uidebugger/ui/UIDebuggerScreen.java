package dLib.tools.uidebugger.ui;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.ui.elements.prefabs.VerticalBox;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class UIDebuggerScreen extends UIElement {
    public UIDebuggerElementList elementList;

    public UIDebuggerScreen(){
        super();

        addChildNCS(elementList = new UIDebuggerElementList());
    }

    //region Subclasses

    public static class UIDebuggerElementList extends VerticalBox{
        public UIDebuggerToolbar toolbar;
        public UIDebuggerElementInfo elementInfo;

        public UIDebuggerElementList(){
            super(Pos.px(100), Pos.px(100), Dim.auto(), Dim.auto());

            setVerticalAlignment(Alignment.VerticalAlignment.TOP);
            setItemSpacing(30);

            addItem(toolbar = new UIDebuggerToolbar());
            addItem(elementInfo = new UIDebuggerElementInfo());
        }

        public static class UIDebuggerToolbar extends HorizontalBox {
            public UIDebuggerToolbar(){
                super(Pos.px(0), Pos.px(0), Dim.auto(), Dim.auto());

                setVerticalAlignment(Alignment.VerticalAlignment.TOP);

                setItemSpacing(10);

                addItem(new TextButton("D", Dim.px(50), Dim.px(50)));

                addItem(new TextButton("i", Dim.px(50), Dim.px(50)){
                    @Override
                    protected void onLeftClick() {
                        super.onLeftClick();

                        UIDebuggerElementList topScreen = getParentOfType(UIDebuggerElementList.class);
                        if(topScreen.elementInfo.isVisible()){
                            topScreen.elementInfo.hideAndDisableInstantly();
                        }else{
                            topScreen.elementInfo.showAndEnableInstantly();
                        }
                    }
                });
            }
        }

        public static class UIDebuggerElementInfo extends TextBox {
            public UIDebuggerElementInfo(){
                super("", Dim.px(600), Dim.px(400));

                setImage(UIThemeManager.getDefaultTheme().inputfield);
                hideAndDisableInstantly();

                setMarginPercY(0.1f);
            }
        }
    }

    //endregion
}
