package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.BooleanProperty;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class HideAndDisableTopmostMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Hide and Disable Topmost";

    private BooleanProperty instant = new BooleanProperty(false)
            .setName("Instant");

    public HideAndDisableTopmostMethodBinding(){
        super();

        addDeclaredParam(instant);
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        if(invoker instanceof UIElement){
            if(instant.getValue()){
                ((UIElement) invoker).getTopParent().hideAndDisableInstantly();
            }
            else{
                ((UIElement) invoker).getTopParent().hideAndDisable();
            }
        }
        return null;
    }

    @Override
    public String getDisplayValue() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
