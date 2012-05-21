package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

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
