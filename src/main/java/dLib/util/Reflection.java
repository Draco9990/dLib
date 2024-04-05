package dLib.util;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import dLib.DLib;
import javassist.*;
import org.apache.commons.lang3.ClassUtils;
import org.clapper.util.classutil.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.util.*;

public class Reflection {

    /** Variables */
    private static final LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> fieldMap = new LinkedHashMap();

    //Returns value of a field from the object or its parent classes
    public static <T> T getFieldValue(String fieldName, Object source){
        if(fieldName == null){
            DLib.logError("getFieldValue called with null fieldName. Stacktrace:");
            Help.Dev.printStacktrace(5);
        }
        if(source == null){
            DLib.logError("getFieldValue called with null object source. Stacktrace:");
            Help.Dev.printStacktrace(5);
        }

        try{
            Field field = getFieldByName(fieldName, (source instanceof Class<?> ? (Class<?>) source : source.getClass()));
            field.setAccessible(true);
            return (T) field.get(source);
        } catch (Exception e) {
            DLib.logError("Could not get field " + fieldName + " due to " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static LinkedHashMap<String, Object> getFieldValues(Object source){
        ArrayList<Map<String, Field>> classFields = getAllFields(source instanceof Class<?> ? (Class<?>) source : source.getClass());

        LinkedHashMap<String, Object> fieldValues = new LinkedHashMap<>();

        for(Map<String, Field> fields : classFields){
            for(Field field : fields.values()){
                try{
                    fieldValues.put(field.getName(), field.get(source));
                }catch (Exception e){
                    DLibLogger.logError("Failed to get field value of field " + field.getName() + " due to " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return fieldValues;
    }
    public static <T> ArrayList<T> getFieldValuesByClass(Class<T> fieldClass, Object source){
        ArrayList<Field> classFields = getFieldsByClass(fieldClass, (source instanceof Class<?> ? (Class<?>) source : source.getClass()));

        ArrayList<T> fieldValues = new ArrayList<>();
        try{
            for(Field field : classFields){
                fieldValues.add((T) field.get(source));
            }
        }catch (IllegalAccessException e) {
            DLib.logError("Could not get field value due to " + e.getLocalizedMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }

        return fieldValues;
    }

    //Sets the value of a field in the object or its parent class
    public static void setFieldValue(String fieldName, Object source, Object value){
        if(fieldName == null){
            DLib.logError("setFieldValue called with null fieldName. Stacktrace:");
            Help.Dev.printStacktrace(5);
        }
        if(source == null){
            DLib.logError("setFieldValue called with null object source. Stacktrace:");
            Help.Dev.printStacktrace(5);
        }

        try{
            Field field = getFieldByName(fieldName, (source instanceof Class<?> ? ((Class<?>)source) : source.getClass()));
            field.setAccessible(true);

            if (!Modifier.isFinal(field.getModifiers())) {
                field.set(source, value);
            } else {
                // If it's final, we need to remove the final modifier
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }

        } catch (Exception e) {
            DLib.logError("Could not set field " + fieldName + " due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static Field getFieldByName(String fieldName, Class<?> objectClass){
        for(Map<String, Field> objectFields : getAllFields(objectClass)){
            if(objectFields != null){
                Field f = objectFields.get(fieldName);
                if(f != null) return f;
            }
        }

        return null;
    }
    public static ArrayList<Field> getFieldsByClass(Class<?> fieldClass, Class<?> objectClass){
        ArrayList<Field> fields = new ArrayList<>();

        ArrayList<Map<String, Field>> allFields = getAllFields(objectClass);
        for (int i = allFields.size() - 1; i >= 0; i--) {
            Map<String, Field> fieldsPerClass = allFields.get(i);
            for (Field field : fieldsPerClass.values()) {
                if (fieldClass.isAssignableFrom(field.getType())) {
                    fields.add(field);
                } else if (field.getGenericType() instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) field.getGenericType();
                    Class<?> rawType = (Class<?>) pType.getRawType();
                    if (fieldClass.isAssignableFrom(rawType)) {
                        fields.add(field);
                    }
                }
            }
        }
        return fields;
    }

    public static ArrayList<Map<String, Field>> getAllFields(Class<?> objClass){
        ArrayList<Map<String, Field>> objFields = new ArrayList<>();
        LinkedHashMap<String, Field> fields = fieldMap.get(objClass);

        if(fields != null){
            objFields.add(fields);
            objClass = objClass.getSuperclass();
            while(objClass != null && objClass != Object.class){
                objFields.add(fieldMap.get(objClass));
                objClass = objClass.getSuperclass();
            }
            return objFields;
        }

        while (objClass != null && objClass != Object.class) {
            fields = new LinkedHashMap<>();
            for(Field f : objClass.getDeclaredFields()){
                fields.put(f.getName(), f);
            }
            fieldMap.put(objClass, fields);
            objFields.add(fields);
            objClass = objClass.getSuperclass();
        }
        return objFields;
    }

    /** Methods */
    private static final LinkedHashMap<Class<?>, LinkedHashMap<String, Method>> methodMap = new LinkedHashMap();

    public static Object invokeMethod(String methodName, Object object, Object... params) {
        if(methodName == null){
            DLib.logError("invokeMethod called with null methodName. Stacktrace:");
            Help.Dev.printStacktrace(5);
        }
        if(methodName == null){
            DLib.logError("invokeMethod called with null object. Stacktrace:");
            Help.Dev.printStacktrace(5);
        }

        int paramCount = params.length;
        Method method;
        Object result = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            classArray[i] = params[i].getClass();
        }

        try {
            method = getMethodByNameAndParams(methodName, (object instanceof Class<?> ? (Class<?>) object : object.getClass()), classArray);
            method.setAccessible(true);
            result = method.invoke(object, params);
        } catch (Exception e) {
            DLib.logError("Could not invoke method of name " + methodName + " due to " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    private static Method getMethodByNameAndParams(String methodName, Class<?> objectClass, Class<?>[] params) throws NoSuchMethodException{
        for(Map<String, Method> objectMethods : getAllMethods(objectClass)){
            if(objectMethods != null){
                Method m = objectMethods.get(getMethodUID(methodName, params));
                if(m == null){
                    for(int i = 0; i < params.length; i++){
                        if(ClassUtils.isPrimitiveWrapper(params[i])) params[i] = ClassUtils.wrapperToPrimitive(params[i]);
                    }
                    m = objectMethods.get(getMethodUID(methodName, params));
                }
                if(m != null) return m;
            }
        }

        throw new NoSuchMethodException();
    }

    private static ArrayList<LinkedHashMap<String, Method>> getAllMethods(Object obj){
        return getAllMethods(obj.getClass());
    }
    private static ArrayList<LinkedHashMap<String, Method>> getAllMethods(Class<?> objClass){
        ArrayList<LinkedHashMap<String, Method>> objMethods = new ArrayList<>();
        LinkedHashMap<String, Method> methods = methodMap.get(objClass);

        if(methods != null){
            objMethods.add(methods);
            objClass = objClass.getSuperclass();
            while(objClass != null && objClass != Object.class){
                objMethods.add(methodMap.get(objClass));
                objClass = objClass.getSuperclass();
            }
            return objMethods;
        }

        while (objClass != null && objClass != Object.class) {
            methods = new LinkedHashMap<>();
            for(Method m : objClass.getDeclaredMethods()){
                methods.put(getMethodUID(m), m);
            }
            methodMap.put(objClass, methods);
            objMethods.add(methods);
            objClass = objClass.getSuperclass();
        }
        return objMethods;
    }

    private static String getMethodUID(Method m){
        return getMethodUID(m.getName(), m.getParameterTypes());
    }
    private static String getMethodUID(String mName, Class<?>[] params){
        StringBuilder methodName = new StringBuilder(mName);
        methodName.append(";");
        for(Class<?> c : params){
            methodName.append(c.getName()).append(";");
        }
        return methodName.toString();
    }

    /** Returns an implementation of the given method in all classes*/
    public static ArrayList<CtMethod> findMethodsFromClasses(CtBehavior ctBehavior, Class<?> parentClass, boolean includeParent, String methodName, Class<?>... methodParams){
        return findMethodsFromClasses(ctBehavior, findClassesOfType(parentClass, includeParent), methodName, methodParams);
    }
    public static ArrayList<CtMethod> findMethodsFromClasses(CtBehavior ctBehavior, ArrayList<ClassInfo> classesToSearch, String methodName, Class<?>... methodParams){
        ClassPool classPool = ctBehavior.getDeclaringClass().getClassPool();

        ArrayList<CtMethod> foundMethods = new ArrayList<>();

        try{
            outer:
            for(ClassInfo c : classesToSearch){
                CtClass ctClass = classPool.get(c.getClassName());

                try{
                    Collection<String> references = ctClass.getRefClasses();

                    for(String s : references){
                        if(classPool.getOrNull(s) == null){
                            continue outer;
                        }
                    }

                    boolean modified = ctClass.isModified();
                    CtMethod foundMethod = null;

                    for(CtMethod m : ctClass.getDeclaredMethods(methodName)){
                        CtClass[] params = m.getParameterTypes();

                        foundMethod = m;

                        if(methodParams == null && params.length == 0){
                            break;
                        }

                        if(methodParams == null || (params.length != methodParams.length)){
                            foundMethod = null;
                            continue;
                        }

                        for (int i = 0; i < methodParams.length; i++) {
                            if(!params[i].getName().equals(methodParams[i].getName())){
                                foundMethod = null;
                                break;
                            }
                        }
                    }

                    if(foundMethod != null){
                        foundMethods.add(foundMethod);
                    }
                    else{
                        if(!modified){
                            //* If we failed to find a method to patch, undo the modified signature if the class was not modified beforehand
                            setFieldValue("wasChanged", ctClass, false);
                        }
                    }
                }catch (Exception patchException){
                    DLibLogger.log("Could not find class method " + methodName + " due to: " + patchException.getMessage());
                    patchException.printStackTrace();
                }
            }
        }
        catch (NotFoundException notFoundException){
            DLibLogger.log("Could not find class method " + methodName + " due to: " + notFoundException.getMessage());
            notFoundException.printStackTrace();
        }

        return foundMethods;
    }

    /** Classes */
    public static ArrayList<ClassInfo> findClassesOfType(Class<?> parentClass, boolean returnParent){
        ClassFilter filter = new AndClassFilter(
            new NotClassFilter(new InterfaceOnlyClassFilter()),
            new ClassModifiersClassFilter(Modifier.PUBLIC),
            (
                returnParent
                ? new OrClassFilter(
                new SubclassClassFilter(parentClass),
                (classInfo, classFinder) -> classInfo.getClassName().equals(parentClass.getName()))
                : new SubclassClassFilter(parentClass)
            )
        );

        ArrayList<ClassInfo> foundClasses = new ArrayList<>();
        generateClassFinder().findClasses(foundClasses, filter);

        return foundClasses;
    }

    private static ClassFinder generateClassFinder(){
        ClassFinder finder = new ClassFinder();

        finder.add(new File(Loader.STS_JAR));
        for (ModInfo modInfo : Loader.MODINFOS) {
            if (modInfo.jarURL != null) {
                try {
                    finder.add(new File(modInfo.jarURL.toURI()));
                } catch (URISyntaxException ignored) {
                }
            }
        }

        return finder;
    }

    /** Misc */
    public static void clearCache(){
        fieldMap.clear();
        methodMap.clear();
    }
}
