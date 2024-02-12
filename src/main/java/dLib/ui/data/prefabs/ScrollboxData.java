package dLib.ui.data.prefabs;

import dLib.ui.data.implementations.DraggableData;
import dLib.ui.data.implementations.RenderableData;
import dLib.ui.elements.prefabs.Scrollbox;
import dLib.util.Reflection;

import java.io.Serializable;

public class ScrollboxData extends RenderableData  implements Serializable {
    private static final long serialVersionUID = 1L;

    public DraggableData slider;

    @Override
    public Scrollbox makeLiveInstance(Object... params) {
        Object pageCountInvoker = params[0];
        String pageCountMethodName = (String) params[1];
        return new Scrollbox(this) {
            @Override
            public int getPageCount() {
                return (int) Reflection.invokeMethod(pageCountMethodName, pageCountInvoker);
            }
        };
    }
}
