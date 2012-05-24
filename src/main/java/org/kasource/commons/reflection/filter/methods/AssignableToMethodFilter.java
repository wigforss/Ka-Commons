package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

/**
 * Filters methods which parameter types is assignable to a specific class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder.
 *  
 * @author rikardwi
 **/
public class AssignableToMethodFilter implements MethodFilter {

    private int paramterIndex;
    private Class<?> assignbleToClass;
    private Class<?>[] assignbleTo;
    
    public AssignableToMethodFilter(int paramterIndex, Class<?> assignbleToClass) {
        this.paramterIndex = paramterIndex;
        this.assignbleToClass = assignbleToClass;
    }
    
    public AssignableToMethodFilter(Class<?>... assignbleFrom) {
      this.assignbleTo = assignbleFrom;
    }
    
    @Override
    public boolean passFilter(Method method) {
        if(assignbleToClass != null) {
            if((method.getParameterTypes().length - 1) < paramterIndex) {
                return false;
            }
            return method.getParameterTypes()[paramterIndex].isAssignableFrom(assignbleToClass);
        } else if(assignbleTo.length == method.getParameterTypes().length) {
            for(int i = 0; i < assignbleTo.length; i++) {
               if(!method.getParameterTypes()[i].isAssignableFrom(assignbleTo[i])) {
                   return false;
               }
            }
            return true;
        }
        return false;
    }

}
