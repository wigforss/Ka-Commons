package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

/**
 * Filters constructors which parameter types is assignable to a specific class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
 *  
 * @author rikardwi
 **/
public class AssignableToConstructorFilter implements ConstructorFilter {

    private int paramterIndex;
    private Class<?> assignbleToClass;
    private Class<?>[] assignbleTo;
    
    public AssignableToConstructorFilter(int paramterIndex, Class<?> assignbleToClass) {
        this.paramterIndex = paramterIndex;
        this.assignbleToClass = assignbleToClass;
    }
    
    public AssignableToConstructorFilter(Class<?>... assignbleFrom) {
      this.assignbleTo = assignbleFrom;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public boolean passFilter(Constructor constructor) {
        if(assignbleToClass != null) {
            if((constructor.getParameterTypes().length - 1) < paramterIndex) {
                return false;
            }
            return constructor.getParameterTypes()[paramterIndex].isAssignableFrom(assignbleToClass);
        } else if(assignbleTo.length == constructor.getParameterTypes().length) {
            for(int i = 0; i < assignbleTo.length; i++) {
               if(!constructor.getParameterTypes()[i].isAssignableFrom(assignbleTo[i])) {
                   return false;
               }
            }
            return true;
        }
        return false;
    }

}
