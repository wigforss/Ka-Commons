package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;



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
