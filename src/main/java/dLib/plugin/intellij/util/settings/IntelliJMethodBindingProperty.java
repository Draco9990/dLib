package dLib.plugin.intellij.util.settings;

import dLib.plugin.intellij.PluginMessageSender;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.properties.objects.MethodBindingProperty;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class IntelliJMethodBindingProperty extends MethodBindingProperty implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private ScreenEditorBaseScreen screenEditor;

    //endregion

    //region Constructors
    //endregion

    //region Methods

    @Override
    public void onValueChanged(MethodBinding oldValue, MethodBinding newValue) {
        super.onValueChanged(oldValue, newValue);

        LinkedHashMap<String, String> parameters = getConvertedParameters();

        if(newValue instanceof DynamicMethodBinding){
            ((DynamicMethodBinding) newValue).addOnBoundMethodChangedConsumer((oldVal, newVal) -> {
                if(!newVal.isEmpty()){
                    if(oldVal.isEmpty() ){
                        PluginMessageSender.Send_AddMethodToClass(screenEditor.getEditingScreen(), getDNCReturnType().getName(), newVal, parameters, "{\n\t// TODO: Method implementation here\n}");
                    }
                    else{
                        PluginMessageSender.Send_RenameMethodInClass(screenEditor.getEditingScreen(), oldVal, newVal, parameters);
                    }
                }
            });
        }
        else if(oldValue instanceof DynamicMethodBinding && !((DynamicMethodBinding) oldValue).getBoundMethod().isEmpty()){
            PluginMessageSender.Send_RemoveMethodFromClass(screenEditor.getEditingScreen(), ((DynamicMethodBinding) oldValue).getBoundMethod(), parameters);
        }
    }

    private LinkedHashMap<String, String> getConvertedParameters(){
        LinkedHashMap<String, String> convertedParameters = new LinkedHashMap<>();
        for(Map.Entry<String, Class<?>> param : getDNCParameters().entrySet()){
            convertedParameters.put(param.getKey(), param.getValue().getName());
        }
        return convertedParameters;
    }

    //endregion
}
