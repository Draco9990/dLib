package dLib.properties.objects;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class ArrayProperty<ArrayListClass extends ArrayList<ArrayItemClass>, ArrayItemClass> extends CustomProperty<ArrayListClass> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables
    //endregion

    //region Constructors

    public ArrayProperty(ArrayListClass defaultValue){
        super(defaultValue);
    }

    //endregion

    //region Methods

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
