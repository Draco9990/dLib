package dLib.util;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;

import java.io.Serializable;
import java.lang.reflect.Field;

public class FieldInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables
    private String clazz = "";
    private String name = "";
    //endregion Variables

    //region Constructors
    public FieldInfo(Class<?> fieldClass, Field field){
        clazz = fieldClass.getName();
        name = field.getName();
    }

    //endregion Constructors

    //region Methods
    public Class<?> getFieldClass(){
        try{
            return Class.forName(clazz);
        }catch (Throwable ignored){
        }

        return null;
    }

    public Field getField(){
        return Reflection.getFieldByName(name, getFieldClass());
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
