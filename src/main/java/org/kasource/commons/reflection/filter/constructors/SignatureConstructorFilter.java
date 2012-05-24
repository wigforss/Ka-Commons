package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * Filters constructors which parameters matches a specific signature.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
 * 
 * @author rikardwi
 **/
public class SignatureConstructorFilter implements ConstructorFilter {

    private Class<?>[] params;
    
    public SignatureConstructorFilter(Class<?>... params) {
        this.params = params;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {
        return (Arrays.equals(constructor.getParameterTypes(), params));
    }

}
