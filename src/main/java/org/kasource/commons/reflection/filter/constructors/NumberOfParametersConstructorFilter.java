package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

public class NumberOfParametersConstructorFilter implements ConstructorFilter {

    private int numParameters;
    
    public NumberOfParametersConstructorFilter(int numParameters) {
        this.numParameters = numParameters;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {     
        return constructor.getParameterTypes().length == numParameters;
    }

}
