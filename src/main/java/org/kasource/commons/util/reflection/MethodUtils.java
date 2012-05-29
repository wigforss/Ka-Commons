package org.kasource.commons.util.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Utility class for Method based introspection.
 * 
 * @author rikardwi
 **/
public class MethodUtils {
    
    private MethodUtils() {}
    
    
    /**
     * Returns true if method has no return type
     * 
     * @param method
     *            The method to inspect
     * 
     * @return true if return type is void
     **/
    public static boolean hasMethodNoReturnType(Method method) {
        return method.getReturnType().equals(Void.TYPE);
    }

    /**
     * Verify that the supplied method's signature matches return type and
     * parameters types
     * 
     * @param method
     *            Method to inspect
     * @param returnType
     *            Return type to match
     * @param parameters
     *            Parameter types to match
     * 
     * @throws IllegalArgumentException
     *             if method fails to match return type or parameters types
     */   
    public static void verifyMethodSignature(Method method, Class<?> returnType, Class<?>... parameters) {
        if(method == null) {
                throw new IllegalArgumentException("Method is null");
        }
        if (!method.getReturnType().equals(returnType)) {
                throw new IllegalArgumentException("Method " + method + " return type " + method.getReturnType()
                        + " does not match: " + returnType);
        }
        Class<?>[] actualParameters = method.getParameterTypes();
        if (actualParameters.length != parameters.length) {
            throw new IllegalArgumentException("Method " + method + " number of parameters " + actualParameters.length
                    + " does not match: " + parameters.length);
        }
        if (!Arrays.equals(actualParameters,parameters)) {
            throw new IllegalArgumentException("Method " + method + " types of parameters "
                    + Arrays.toString(actualParameters) + " does not match: " + Arrays.toString(parameters));
        }
    }
}
