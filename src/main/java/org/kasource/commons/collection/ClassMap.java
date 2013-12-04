package org.kasource.commons.collection;

import java.util.HashMap;
import java.util.Map;


/**
 * Wraps a map and resolves super classes and / or interfaces of the key.
 * 
 * @author rikardwi
 *
 * @param <T> Content type of the map
 **/
public class ClassMap<T> {

    private Map<Class<?>, T> map ;
    private boolean excludeInterfaces;
   
    public ClassMap() {    
    }
    
    public ClassMap(Map<Class<?>, T> map) {
        this.map = map;
    }
    
    /**
     * Returns the value of the map that matches the supplied class
     * or any of its super classes or any of its interfaces.
     * 
     * Values are resolved using the most specific classes and interfaces first      
     * and then more general (base classes). 
     * 
     * @param clazz Class to resolve value for by inspecting the class META data.
     * 
     * @return value found for the supplied class or null if no value could be resolved.
     **/
    public T get(Class<?> clazz) {
        if(map == null || map.isEmpty()) {
            return null;
        }
        Class<?> classToInspect = clazz;
        T object = map.get(classToInspect);
        if(object == null && !excludeInterfaces) {
            object = getByInterfaces(classToInspect);
        }
        while (object == null && classToInspect.getSuperclass() != null) {
            classToInspect = classToInspect.getSuperclass();
            object = map.get(classToInspect);
            if(object == null && !excludeInterfaces) {
                object = getByInterfaces(classToInspect);
            }
        }
        return object;
    }
    
  
    /**
     * Returns the vale associated with the any 
     * interface implemented by the supplied class.
     * 
     * This method will return the first match only, if
     * more than one value can be resolved the first is
     * returned.
     * 
     * @param clazz  Class to inspect interfaces of.
     * 
     * @return The resolved value if found, else null.
     **/
    public T getByInterfaces(Class<?> clazz) {
        T object = null;
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            object = map.get(interfaceClass);
            if(object != null) {
                break;
            }
        }
        return object;
    }
    
    
    
    
    public void setMap(Map<Class<?>, T> map) {
        this.map = map;
    }

    /**
     * @return the excludeInterfaces
     */
    public boolean isExcludeInterfaces() {
        return excludeInterfaces;
    }

    /**
     * @param excludeInterfaces the excludeInterfaces to set
     */
    public void setExcludeInterfaces(boolean excludeInterfaces) {
        this.excludeInterfaces = excludeInterfaces;
    }


    
}
