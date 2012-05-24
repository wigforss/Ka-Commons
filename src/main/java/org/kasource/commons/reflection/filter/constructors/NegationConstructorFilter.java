package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

/**
* A filter which negates the result form another constructor filter.
* <p>
* Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
* 
* @author rikardwi
**/
public class NegationConstructorFilter implements ConstructorFilter {

    private ConstructorFilter filter;
    
    public NegationConstructorFilter(ConstructorFilter filter) {
        this.filter = filter;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {
       return !filter.passFilter(constructor);
    }

}
