package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

/**
 * Filters constructors by evaluating two other constructor filters by OR:ing their result.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
 * 
 * @author rikardwi
 **/
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
