package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractUISetting;
import dLib.ui.elements.settings.CustomUISetting;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class ArraySetting<ArrayListClass extends ArrayList<ArrayItemClass>, ArrayItemClass> extends CustomSetting<ArrayListClass> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */

    /** Constructors */
    public ArraySetting(ArrayListClass defaultValue){
        super(defaultValue);
    }

    /** Methods */
    public void add(ArrayItemClass arrayItem){
        currentValue.add(arrayItem);
    }

    public void clear(){
        currentValue.clear();
    }

    public int size(){
        return currentValue.size();
    }
}
