package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

public class OrConstructorFilter implements ConstructorFilter {

    private ConstructorFilter left;
    private ConstructorFilter right;
    
    public OrConstructorFilter(ConstructorFilter left, ConstructorFilter right) {
        this.left = left;
        this.right = right;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {
        return left.passFilter(constructor) || right.passFilter(constructor);
    }

}
