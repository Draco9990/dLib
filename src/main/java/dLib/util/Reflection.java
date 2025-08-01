package dLib.util;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import dLib.util.helpers.DebugHelpers;
import javassist.*;
import org.apache.commons.lang3.ClassUtils;
import org.clapper.util.classutil.*;
import org.scannotation.AnnotationDB;

import java.io.File;
import java.lang.reflect.Modifier;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Reflection {
    //region Fields

    private static final LinkedHashMap<Class<?>, LinkedHashMap<String, Field>> fieldMap = new LinkedHashMap();

    private static final LinkedHashMap<Class<?>, ArrayList<ClassInfo>> classHierarchyMap = new LinkedHashMap();

    //Returns value of a field from the object or its parent classes
    public static <T> T getFieldValue(String fieldName, Object source){
        if(fieldName == null){
            DLibLogger.logError("getFieldValue called with null fieldName. Stacktrace:");
            DebugHelpers.printStacktrace(5);
        }
        if(source == null){
            DLibLogger.logError("getFieldValue called with null object source. Stacktrace:");
            DebugHelpers.printStacktrace(5);
        }

        try{
            Field field = getFieldByName(fieldName, (source instanceof Class<?> ? (Class<?>) source : source.getClass()));
            return getFieldValue(field, source);
        } catch (Exception e) {
            String message = "Could not get field " + fieldName + " due to " + e.getLocalizedMessage();
            DLibLogger.logError(message);
            e.printStackTrace();
            return null;
        }
    }
    public static <T> T getFieldValue(Field field, Object source){
        if(field == null){
            DLibLogger.logError("getFieldValue called with null field. Stacktrace:");
            DebugHelpers.printStacktrace(5);
        }

        try{
            field.setAccessible(true);
            return (T) field.get(source);
        } catch (Exception e) {
            String message = "Could not get field " + field.getName() + " due to " + e.getLocalizedMessage();
            DLibLogger.logError(message);
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
        for(Field field : classFields){
            if(source instanceof Class<?> && !Modifier.isStatic(field.getModifiers())){
                continue;
            }
            fieldValues.add(getFieldValue(field, source));
        }

        return fieldValues;
    }

    //Sets the value of a field in the object or its parent class
    public static void setFieldValue(String fieldName, Object source, Object value){
        if(fieldName == null){
            DLibLogger.logError("setFieldValue called with null fieldName. Stacktrace:");
            DebugHelpers.printStacktrace(5);
        }
        if(source == null){
            DLibLogger.logError("setFieldValue called with null object source. Stacktrace:");
            DebugHelpers.printStacktrace(5);
        }

        try{
            Field field = getFieldByName(fieldName, (source instanceof Class<?> ? ((Class<?>)source) : source.getClass()));
            setFieldValue(field, source, value);

        } catch (Exception e) {
            String message = "Could not set field " + fieldName + " due to " + e.getLocalizedMessage();
            DLibLogger.logError(message);
            e.printStackTrace();
        }
    }
    public static void setFieldValue(Field field, Object source, Object value){
        if(field == null){
            DLibLogger.logError("setFieldValue called with null field. Stacktrace:");
            DebugHelpers.printStacktrace(5);
            return;
        }

        try{
            //If our field type is not a spire field or if it is and the value we're setting is also spirefield
            field.setAccessible(true);

            if (Modifier.isFinal(field.getModifiers())) {
                // If it's final, we need to remove the final modifier
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }

            field.set((source instanceof Class<?> ? null : source), value);

        } catch (Exception e) {
            String message = "Could not set field " + field.getName() + " due to " + e.getLocalizedMessage();
            DLibLogger.logError(message);
            e.printStackTrace();
        }
    }

    public static void removeFinalModifier(String fieldName, Object source){
        if(fieldName == null){
            DLibLogger.logError("setFieldValue called with null fieldName. Stacktrace:");
            DebugHelpers.printStacktrace(5);
        }
        if(source == null){
            DLibLogger.logError("setFieldValue called with null object source. Stacktrace:");
            DebugHelpers.printStacktrace(5);
        }

        try{
            Field field = getFieldByName(fieldName, (source instanceof Class<?> ? ((Class<?>)source) : source.getClass()));
            removeFinalModifier(field, source);
        } catch (Exception e) {
            String message = "Could not remove final modified from field " + fieldName + " due to " + e.getLocalizedMessage();
            DLibLogger.logError(message);
            e.printStackTrace();
        }
    }
    public static void removeFinalModifier(Field field, Object source){
        if(field == null){
            DLibLogger.logError("setFieldValue called with null field. Stacktrace:");
            DebugHelpers.printStacktrace(5);
            return;
        }

        try{
            //If our field type is not a spire field or if it is and the value we're setting is also spirefield
            field.setAccessible(true);

            if (Modifier.isFinal(field.getModifiers())) {
                // If it's final, we need to remove the final modifier
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }
        } catch (Exception e) {
            String message = "Could not remove final modifier from field " + field.getName() + " due to " + e.getLocalizedMessage();
            DLibLogger.logError(message);
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
    public static ArrayList<Field> getAllFieldsSimple(Class<?> objClass){
        ArrayList<Field> objFields = new ArrayList<>();

        for(Map<String, Field> classFields : getAllFields(objClass)){
            objFields.addAll(classFields.values());
        }

        return objFields;
    }

    public static Type[] getFieldTypeArguments(Field field){
        Type type = field.getGenericType();

        if(type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments();
        }

        return null;
    }

    //endregion Fields

    //region Methods

    private static final LinkedHashMap<Class<?>, LinkedHashMap<String, Method>> methodMap = new LinkedHashMap();

    public static <T> T invokeMethod(String methodName, Object object, Object... params) {
        if(methodName == null){
            DLibLogger.logError("invokeMethod called with null methodName. Stacktrace:");
            DebugHelpers.printStacktrace(5);
        }
        if(methodName == null){
            DLibLogger.logError("invokeMethod called with null object. Stacktrace:");
            DebugHelpers.printStacktrace(5);
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
            if(method == null){
                return null;
            }

            method.setAccessible(true);
            result = method.invoke(object, params);
        } catch (Exception e) {
            String message = "Could not invoke method of name " + methodName + " due to " + e.getMessage();
            DLibLogger.logError(message);
            e.printStackTrace();
        }

        return (T) result;
    }

    public static Method getMethodByNameAndParams(String methodName, Class<?> objectClass, Class<?>[] params){
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

        return null;
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
        return findMethodsFromClasses(ctBehavior, findClassInfosOfType(parentClass, includeParent), methodName, methodParams);
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

    public static ArrayList<CtConstructor> findConstructorsFromClasses(CtBehavior ctBehavior, Class<?> parentClass, boolean includeParent){
        return findConstructorsFromClasses(ctBehavior, findClassInfosOfType(parentClass, includeParent));
    }
    public static ArrayList<CtConstructor> findConstructorsFromClasses(CtBehavior ctBehavior, ArrayList<ClassInfo> classesToSearch){
        ClassPool classPool = ctBehavior.getDeclaringClass().getClassPool();

        ArrayList<CtConstructor> foundMethods = new ArrayList<>();

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

                    CtConstructor[] constructors = ctClass.getDeclaredConstructors();
                    if(constructors.length > 0){
                        foundMethods.addAll(Arrays.asList(constructors));
                    }
                    else {
                        CtConstructor constructor = CtNewConstructor.defaultConstructor(ctClass);
                        foundMethods.add(constructor);
                    }
                }catch (Exception patchException){
                    DLibLogger.log("Could not find class constructors due to: " + patchException.getMessage());
                    patchException.printStackTrace();
                }
            }
        }
        catch (NotFoundException notFoundException){
            DLibLogger.log("Could not find class constructors due to: " + notFoundException.getMessage());
            notFoundException.printStackTrace();
        }

        return foundMethods;
    }

    //endregion Methods

    //region Classes

    public static <T> ArrayList<Class<? extends T>> findClassesOfType(Class<? extends T> parentClass, boolean returnParent){
        ArrayList<ClassInfo> classes = findClassInfosOfType(parentClass, returnParent);

        ArrayList<Class<? extends T>> toReturn = new ArrayList<>();
        for(ClassInfo c : classes){
            try{
                toReturn.add((Class<? extends T>) Class.forName(c.getClassName()));
            }catch (Exception ignored){
            }
        }
        return toReturn;
    }

    public static ArrayList<ClassInfo> findClassInfosOfType(Class<?> parentClass, boolean returnParent){
        if(classHierarchyMap.containsKey(parentClass)){
            ArrayList<ClassInfo> toReturn = new ArrayList<>(classHierarchyMap.get(parentClass));
            if(returnParent){
                toReturn.removeIf(classInfo -> classInfo.getClassName().equals(parentClass.getName()));
            }
            return toReturn;
        }

        ClassFilter filter = new AndClassFilter(
                new NotClassFilter(new InterfaceOnlyClassFilter()),
                new OrClassFilter(
                        new SubclassClassFilter(parentClass),
                        (classInfo, classFinder) -> classInfo.getClassName().equals(parentClass.getName()))
        );

        ArrayList<ClassInfo> foundClasses = new ArrayList<>();
        generateClassFinder().findClasses(foundClasses, filter);

        classHierarchyMap.put(parentClass, foundClasses);
        ArrayList<ClassInfo> toReturn = new ArrayList<>(foundClasses);
        if(returnParent){
            toReturn.removeIf(classInfo -> classInfo.getClassName().equals(parentClass.getName()));
        }
        return toReturn;
    }

    public static ArrayList<Class<?>> getAllClassesFromFile(URL path) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        try {
            AnnotationDB db = new AnnotationDB();
            db.scanArchives(path);

            Set<String> classNames = db.getClassIndex().keySet();
            for (String className : classNames) {
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return classes;
    }

    public static <T> HashMap<Class<?>, T> getAllClassesWithAnnotation(Class<T> annotation){
        HashMap<Class<?>, T> classes = new HashMap<>();

        try {
            URL[] urlPaths = new URL[Loader.MODINFOS.length];
            ModInfo[] modinfos = Loader.MODINFOS;
            for (int i = 0; i < modinfos.length; i++) {
                ModInfo info = modinfos[i];
                urlPaths[i] = info.jarURL;
            }

            Set<String> classNames = new HashSet<>();
            for(Map.Entry<URL, AnnotationDB> annotationDBEntry : Patcher.annotationDBMap.entrySet()){
                try{
                    classNames.addAll(annotationDBEntry.getValue().getAnnotationIndex().get(annotation.getName()));
                }catch (Exception | Error ignored){
                }
            }

            for (String className : classNames) {
                CtClass clazz = ((ClassPool) getFieldValue("POOL", Loader.class)).get(className);
                if(clazz != null){
                    T annObject = (T) clazz.getAnnotation(annotation);
                    if(annObject != null){
                        try{
                            Class<?> annClass = Class.forName(className);
                            classes.put(annClass, annObject);
                        }catch (Exception | Error ignored){
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return classes;
    }

    //endregion Classes

    //region Misc

    public static ClassFinder generateClassFinder(){
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

    //endregion Misc

    /** Classes */


    /** Misc */
    public static void clearCache(){
        fieldMap.clear();
        methodMap.clear();
    }
}
