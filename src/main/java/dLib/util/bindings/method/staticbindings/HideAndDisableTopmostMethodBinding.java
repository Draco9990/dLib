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
    public Object resolve(Object invoker, Object... args) {
        if(invoker instanceof UIElement){
            if(instant.getValue()){
                ((UIElement) invoker).getTopParent().setVisibilityAndEnabledInstantly(false, false);
            }
            else{
                ((UIElement) invoker).getTopParent().setVisibilityAndEnabled(false, false);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
