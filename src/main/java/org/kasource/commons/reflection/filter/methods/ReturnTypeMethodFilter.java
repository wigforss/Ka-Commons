package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

/**
 * Filters methods which return type is of a specific class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder.
 * 
 * @author rikardwi
 **/
public class ReturnTypeMethodFilter implements MethodFilter {

    private Class<?> returnType;
    
    public ReturnTypeMethodFilter(Class<?> returnType) {
        this.returnType = returnType;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return method.getReturnType().equals(returnType);
    }

}
