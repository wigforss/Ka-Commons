package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

/**
 * Method Filter.
 * 
 * Implement this interface to create a MethodFilter.
 * 
 * @author rikardwi
 **/
public interface MethodFilter {

    public boolean passFilter(Method method);
}
