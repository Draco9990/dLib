package dLib.util;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;

import java.io.Serializable;
import java.lang.reflect.Field;

public class FieldInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    private String clazz = "";
    private String name = "";

    private transient Class<?> resolvedClass = null;
    private transient Field resolvedField = null;

    //endregion Variables

    //region Constructors

    public FieldInfo(Class<?> fieldClass, Field field){
        clazz = fieldClass.getName();
        name = field.getName();

        resolvedClass = fieldClass;
        resolvedField = field;
    }

    //endregion Constructors

    //region Methods

    public Class<?> getFieldClass(){
        if(resolvedClass == null){
            try{
                resolvedClass = Class.forName(clazz);
            }catch (Throwable ignored){
            }
        }

        return resolvedClass;
    }

    public Field getField(){
        if(resolvedField == null){
            resolvedField = Reflection.getFieldByName(name, getFieldClass());
        }

        return resolvedField;
    }

    public boolean isSpireField(){
        Field field = getField();
        return field != null && SpireField.class.isAssignableFrom(field.getType());
    }

    @Override
    public String toString() {
        return clazz + "." + name;
    }

    //endregion Methods
}
