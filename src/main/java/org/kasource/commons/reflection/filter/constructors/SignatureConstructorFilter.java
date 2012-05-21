package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;
import java.util.Arrays;

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
