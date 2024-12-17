package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.MethodBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.editors.StaticMethodBindingValueEditor;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class StaticMethodBinding extends MethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Static Method Binding";
    private static final String PROPERTY_EDITOR_SHORT_NAME = "stat";

    //region Variables

    private ArrayList<TProperty<?, ?>> declaredParams = new ArrayList<>();

    //endregion Variables

    //region Methods

    public void addDeclaredParam(TProperty<?, ?> param){
        declaredParams.add(param);
    }

    public ArrayList<TProperty<?, ?>> getDeclaredParams() {
        return declaredParams;
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new StaticMethodBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new StaticMethodBindingValueEditor((MethodBindingProperty) property);
    }

    //endregion Methods
}
