package dLib.ui.elements.items.resourcepicker;

import dLib.properties.objects.templates.TProperty;

import java.util.ArrayList;

public class UIObjectPropertyResourcePicker extends AbstractUIListResourcePicker{
    private Class<?> objectClass;

    public UIObjectPropertyResourcePicker(Object object){
        super();
        this.objectClass = object.getClass();
    }

    @Override
    public ArrayList<Class<?>> getResourceTypes() {
        ArrayList<Class<?>> resourceTypes = new ArrayList<>();
        resourceTypes.add(TProperty.class);
        return resourceTypes;
    }

    @Override
    public ArrayList<Class<?>> getClassesToIndex() {
        ArrayList<Class<?>> classesToIndex = new ArrayList<>();
        classesToIndex.add(objectClass);
        return classesToIndex;
    }
}
