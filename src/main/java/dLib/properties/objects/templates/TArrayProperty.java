package dLib.properties.objects.templates;

import dLib.properties.ui.elements.ArrayPropertyEditor;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class TArrayProperty<ArrayItemClass, PropertyType extends TArrayProperty<ArrayItemClass, PropertyType>> extends TProperty<ArrayList<ArrayItemClass>, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public TArrayProperty(ArrayList<ArrayItemClass> defaultValue){
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

    //endregion

    //endregion
}
