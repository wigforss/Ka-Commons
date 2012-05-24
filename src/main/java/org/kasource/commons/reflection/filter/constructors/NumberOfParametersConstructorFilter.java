package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

/**
 * Filters constructors which has a specific number of parameters.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
 * 
 * @author rikardwi
 **/
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
