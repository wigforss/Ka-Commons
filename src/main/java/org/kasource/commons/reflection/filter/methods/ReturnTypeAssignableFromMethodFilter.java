package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

/**
 * Filter methods which return type extends/implements the assignableFromClass.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder.
 * 
 * @author rikardwi
 **/
public class ReturnTypeAssignableFromMethodFilter implements MethodFilter {

    private Class<?> assignableFromClass;
    
    public ReturnTypeAssignableFromMethodFilter(Class<?> assignableFromClass) {
        this.assignableFromClass = assignableFromClass;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return assignableFromClass.isAssignableFrom(method.getReturnType());
    }

   

}
