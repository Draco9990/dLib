package dLib.tools.uidebugger.ui;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextButton;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class UIDebuggerScreen extends UIElement {
    public UIDebuggerScreen(){
        super();

        UIDebuggerToolbar toolbar = new UIDebuggerToolbar();
        addChildNCS(toolbar);
    }

    //region Subclasses

    private static class UIDebuggerToolbar extends HorizontalBox {
        public UIDebuggerToolbar(){
            super(Pos.px(100), Pos.px(100), Dim.auto(), Dim.auto());

            setVerticalAlignment(Alignment.VerticalAlignment.TOP);

            addChildNCS(new TextButton("D", Dim.px(50), Dim.px(50)));
        }
    }

    //endregion
}
