package org.kasource.commons.collection;

import java.util.Map;

/**
 * Wraps a map and resolves super packages of the key.
 * 
 * @author rikardwi
 *
 * @param <T> Content type of the map
 **/
public class PackageMap<T> {

    private Map<String, T> map;
    
    public PackageMap() {}
    
    public PackageMap(Map<String, T> map) {
        this.map = map;
    }
    
    
    public T get(Class<?> clazz) {
        return get(clazz.getName());
    }
    
    public T get(String packageOrClassName) {
        if(map == null || map.isEmpty()) {
            return null;
        }
        int index = packageOrClassName.lastIndexOf(".");
        String packageName = packageOrClassName;
        T object = map.get(packageName);
        
        while (object == null && index > 0) {
            packageName = packageName.substring(0, index);
            object = map.get(packageName);
            index = packageName.lastIndexOf(".");
        }
        
        return object;
    }
    
    
}
