package org.kasource.commons.util.reflection;

import java.util.HashMap;
import java.util.Map;


/**
 * Class loading Utilities.
 * 
 * @author rikardwi
 **/
public class ClassUtils {
    private static final Map<String, Class<?>> primitiveClasses = new HashMap<String, Class<?>>();
    
    static {
        primitiveClasses.put("byte", byte.class);
        primitiveClasses.put("short", short.class);
        primitiveClasses.put("char", char.class);
        primitiveClasses.put("int", int.class);
        primitiveClasses.put("long", long.class);
        primitiveClasses.put("float", float.class);
        primitiveClasses.put("double", double.class);
        primitiveClasses.put("boolean", boolean.class);
        primitiveClasses.put("[B", byte[].class);
        primitiveClasses.put("[Lbyte", byte[].class);
        primitiveClasses.put("[S", short[].class);
        primitiveClasses.put("[Lshort", short[].class);
        primitiveClasses.put("[C", char[].class);
        primitiveClasses.put("[Lchar", char[].class);
        primitiveClasses.put("[I", int[].class);
        primitiveClasses.put("[Lint", int[].class);
        primitiveClasses.put("[L", long[].class);
        primitiveClasses.put("[Llong", long[].class);
        primitiveClasses.put("[F", float[].class);
        primitiveClasses.put("[Lfloat", float[].class);
        primitiveClasses.put("[D", double[].class);
        primitiveClasses.put("[Ldouble", double[].class);
        primitiveClasses.put("[Z", boolean[].class);
        primitiveClasses.put("[Lboolean", boolean[].class);
        
    }
    
    public static Class<?> getPrimitiveClass(String primitiveClassName) {
        return primitiveClasses.get(primitiveClassName);
    }
    
    /**
     * Loads and returns the class named className of type superClass.
     * 
     * @param <T> Type of the class
     * @param className Name of the class to load
     * @param superClass Type of the class to load
     * @return The loaded class of type superClass.
     * 
     * @throws IllegalArgumentException if the class with className could not be loaded or
     * if the that class does not extend the class supplied in the superClass parameter.
     **/
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T>  loadClass(String className, Class<T> ofType) {
        try {
            Class<?> clazz = Class.forName(className);
         
            if(ofType == null || ! ofType.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Class " + className + " must extend or implement " + ofType + "!");
            }
            return (Class<? extends T>) clazz;
        }catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException("Class " + className + " could not be found!", cnfe);
        }
    }

}
