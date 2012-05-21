package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;


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
