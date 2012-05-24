package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;


import org.kasource.commons.reflection.filter.classes.ClassFilter;

/**
 * Filters constructors which parameters types passes a ClassFilter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
 * 
 * @author rikardwi
 **/
public class ParameterClassFilterConstructorFilter implements ConstructorFilter {

    private int parameterIndex;
    private ClassFilter filter;
    private ClassFilter[] filters;
    
    public ParameterClassFilterConstructorFilter(ClassFilter... filters) {
        this.filters = filters;
    }
    
    public ParameterClassFilterConstructorFilter(int parameterIndex, ClassFilter filter) {
        this.parameterIndex = parameterIndex;
        this.filter = filter;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {
        if(filter != null) {
            if((constructor.getParameterTypes().length - 1) < parameterIndex) {
                return false;
            }
            return filter.passFilter(constructor.getParameterTypes()[parameterIndex]);
        } else if(filters != null) {
            if(filters.length != constructor.getParameterTypes().length) {
                return false;
            }
            for(int i = 0; i <  filters.length; ++i) {
                Class<?> paramterType = constructor.getParameterTypes()[i];
                if(!filters[i].passFilter(paramterType)) {
                    return false;
                }
            }
        }
        return true;
    }
}
