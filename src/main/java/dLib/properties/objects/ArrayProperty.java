package dLib.properties.objects;

import dLib.properties.ui.elements.ArrayPropertyEditor;

import java.io.Serializable;
import java.util.ArrayList;

public class ArrayProperty<ArrayItemClass> extends Property<ArrayList<ArrayItemClass>> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public ArrayProperty(ArrayList<ArrayItemClass> defaultValue){
        super(defaultValue);

        propertyEditorClass = ArrayPropertyEditor.class;
    }

    public ArrayList<ArrayItemClass> getAllOptions() {
        return getValue();
    }

    //endregion

    //region Methods

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("ArrayProperty does not support setting value from string");
    }

    //region Array Management

    public void add(ArrayItemClass arrayItem){
        value.add(arrayItem);
    }

    public void clear(){
        value.clear();
    }

    public int size(){
        return value.size();
    }

    @Override
    public ArrayProperty<ArrayItemClass> setName(String newTitle) {
        return (ArrayProperty<ArrayItemClass>) super.setName(newTitle);
    }

    @Override
    public ArrayProperty<ArrayItemClass> setDescription(String description) {
        return (ArrayProperty<ArrayItemClass>) super.setDescription(description);
    }

    //endregion

    //endregion
}
