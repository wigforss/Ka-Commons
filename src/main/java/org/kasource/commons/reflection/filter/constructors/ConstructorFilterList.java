package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;


/**
 * List of constructor Filters.
 * 
 * Evaluates the filters in an AND manner.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder. 
 *  
 * @author rikardwi
 **/
public class ConstructorFilterList implements ConstructorFilter {
    private ConstructorFilter[] filters;
    
    public ConstructorFilterList(ConstructorFilter... filters) {
        this.filters = filters;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {
        for(ConstructorFilter filter : filters) {
            if(!filter.passFilter(constructor)) {
                return false;
            }
        }
        return true;
    }
}
