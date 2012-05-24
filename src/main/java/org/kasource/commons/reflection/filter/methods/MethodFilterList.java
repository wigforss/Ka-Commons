package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;


/**
 * List of Method Filters.
 * 
 * Evaluates the filters in an AND manner.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder. 
 *  
 * @author rikardwi
 **/
public class MethodFilterList implements MethodFilter {
private MethodFilter[] filters;
    
    public MethodFilterList(MethodFilter... filters) {
        this.filters = filters;
    }
    
    @Override
    public boolean passFilter(Method method) {
        for(MethodFilter filter : filters) {
            if(!filter.passFilter(method)) {
                return false;
            }
        }
        return true;
    }
}
