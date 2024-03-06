package dLib.plugin.intellij.util.settings;

import dLib.plugin.intellij.PluginManager;
import dLib.plugin.intellij.PluginMessageSender;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.settings.Setting;
import dLib.util.settings.prefabs.MethodBindingSetting;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class IntelliJMethodBindingSetting extends MethodBindingSetting {
    @Override
    protected Setting<MethodBinding> setCurrentValue(MethodBinding currentValue) {
        MethodBinding previousValue = getCurrentValue();

        super.setCurrentValue(currentValue);

        if(currentValue instanceof DynamicMethodBinding){
            ((DynamicMethodBinding) currentValue).setOnBoundMethodChangedConsumer(new BiConsumer<String, String>() {
                @Override
                public void accept(String oldVal, String newVal) {
                    if(!newVal.isEmpty()){
                        if(oldVal.isEmpty() ){
                            PluginMessageSender.Send_AddMethodToClass(ScreenEditorBaseScreen.instance.getEditingScreen(), "void", newVal, new LinkedHashMap<>(), "{\n\t// TODO: Method implementation here\n}");
                        }
                        else{
                            PluginMessageSender.Send_RenameMethodInClass(ScreenEditorBaseScreen.instance.getEditingScreen(), oldVal, newVal, new LinkedHashMap<>());
                        }
                    }
                }
            });
        }
        else if(previousValue instanceof DynamicMethodBinding && !((DynamicMethodBinding) previousValue).getBoundMethod().isEmpty()){
            PluginMessageSender.Send_RemoveMethodInClass(ScreenEditorBaseScreen.instance.getEditingScreen(), ((DynamicMethodBinding) previousValue).getBoundMethod(), new LinkedHashMap<>());
        }

         return this;
    }
}
