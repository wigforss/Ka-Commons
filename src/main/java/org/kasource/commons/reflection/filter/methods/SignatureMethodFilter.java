package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Filters methods which parameters matches a specific signature.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder.
 * 
 * @author rikardwi
 **/
public class SignatureMethodFilter implements MethodFilter {

    private Class<?>[] params;
    
    public SignatureMethodFilter(Class<?>... params) {
        this.params = params;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return (Arrays.equals(method.getParameterTypes(), params));
    }

}
