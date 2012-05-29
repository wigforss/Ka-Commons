package org.kasource.commons.util.reflection;


/**
 * Class loading Utilities.
 * 
 * @author rikardwi
 **/
public class ClassUtils {
     
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
