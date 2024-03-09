package dLib.plugin.intellij.util.settings;

import dLib.plugin.intellij.PluginMessageSender;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.settings.Property;
import dLib.util.settings.prefabs.MethodBindingProperty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class IntelliJMethodBindingProperty extends MethodBindingProperty {
    private ScreenEditorBaseScreen screenEditor;

    public IntelliJMethodBindingProperty(ScreenEditorBaseScreen screenEditor){
        this.screenEditor = screenEditor;
    }

    @Override
    protected Property<MethodBinding> setValue_internal(MethodBinding value) {
        MethodBinding previousValue = getValue();

        super.setValue_internal(value);

        LinkedHashMap<String, String> parameters = getConvertedParameters();

        if(value instanceof DynamicMethodBinding){
            ((DynamicMethodBinding) value).addOnBoundMethodChangedConsumer(new BiConsumer<String, String>() {
                @Override
                public void accept(String oldVal, String newVal) {
                    if(!newVal.isEmpty()){
                        if(oldVal.isEmpty() ){
                            PluginMessageSender.Send_AddMethodToClass(screenEditor.getEditingScreen(), getReturnType().getName(), newVal, parameters, "{\n\t// TODO: Method implementation here\n}");
                        }
                        else{
                            PluginMessageSender.Send_RenameMethodInClass(screenEditor.getEditingScreen(), oldVal, newVal, parameters);
                        }
                    }
                }
            });
        }
        else if(previousValue instanceof DynamicMethodBinding && !((DynamicMethodBinding) previousValue).getBoundMethod().isEmpty()){
            PluginMessageSender.Send_RemoveMethodInClass(screenEditor.getEditingScreen(), ((DynamicMethodBinding) previousValue).getBoundMethod(), parameters);
        }

         return this;
    }

    private LinkedHashMap<String, String> getConvertedParameters(){
        LinkedHashMap<String, String> convertedParameters = new LinkedHashMap<>();
        for(Map.Entry<String, Class<?>> param : getParameters().entrySet()){
            convertedParameters.put(param.getKey(), param.getValue().getName());
        }
        return convertedParameters;
    }
}
