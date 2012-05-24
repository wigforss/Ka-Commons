package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

/**
 * Filters constructors which parameter types is assignable from a specific class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
 *  
 * @author rikardwi
 **/
public class AssignableFromConstructorFilter implements ConstructorFilter {

    private int paramterIndex;
    private Class<?> assignbleFromClass;
    private Class<?>[] assignbleFrom;
    
    public AssignableFromConstructorFilter(int paramterIndex, Class<?> assignbleFromClass) {
        this.paramterIndex = paramterIndex;
        this.assignbleFromClass = assignbleFromClass;
    }
    
    public AssignableFromConstructorFilter(Class<?>... assignbleFrom) {
      this.assignbleFrom = assignbleFrom;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {
        if(assignbleFromClass != null) {
            if((constructor.getParameterTypes().length - 1) < paramterIndex) {
                return false;
            }
            return assignbleFromClass.isAssignableFrom(constructor.getParameterTypes()[paramterIndex]);
        } else if(assignbleFrom.length == constructor.getParameterTypes().length) {
            for(int i = 0; i < assignbleFrom.length; i++) {
               if(!assignbleFrom[i].isAssignableFrom(constructor.getParameterTypes()[i])) {
                   return false;
               }
            }
            return true;
        }
        return false;
    }

}
