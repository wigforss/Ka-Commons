package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

/**
 * Filters methods which has a specific number of parameters.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder.
 * 
 * @author rikardwi
 **/
public class NumberOfParametersMethodFilter implements MethodFilter {
    private int numberOfParameters;
    
    public NumberOfParametersMethodFilter(int numberOfParameters) {
        this.numberOfParameters = numberOfParameters;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return method.getParameterTypes().length == numberOfParameters;
    }

}
