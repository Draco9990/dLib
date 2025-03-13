package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.BooleanProperty;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class ShowAndEnableTopmostMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Show and Enable Topmost";

    private BooleanProperty instant = new BooleanProperty(false)
            .setName("Instant");

    public ShowAndEnableTopmostMethodBinding(){
        super();

        addDeclaredParam(instant);
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        if(invoker instanceof UIElement){
            if(instant.getValue()){
                ((UIElement) invoker).getTopParent().showAndEnableInstantly();
            }
            else{
                ((UIElement) invoker).getTopParent().showAndEnable();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
