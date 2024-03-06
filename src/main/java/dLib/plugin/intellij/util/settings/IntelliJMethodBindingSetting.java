package dLib.plugin.intellij.util.settings;

import dLib.plugin.intellij.PluginManager;
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
         super.setCurrentValue(currentValue);

         if(currentValue instanceof DynamicMethodBinding){
             ((DynamicMethodBinding) currentValue).setOnBoundMethodChangedConsumer(new BiConsumer<String, String>() {
                 @Override
                 public void accept(String oldVal, String newVal) {
                     if(oldVal.isEmpty()){
                         PluginManager.sendMessage("addMethod", ScreenEditorBaseScreen.instance.getEditingScreen(), "void", newVal, new LinkedHashMap<String, String>(), "{\n//Method implementation here\n}");
                     }
                 }
             });
         }

         return this;
    }
}
