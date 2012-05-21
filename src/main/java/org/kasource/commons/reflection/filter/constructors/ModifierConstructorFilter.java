package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

public class ModifierConstructorFilter implements ConstructorFilter {

    private int modifier;
    
    public ModifierConstructorFilter(int modifier) {
        this.modifier = modifier;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {
       return (constructor.getModifiers() & modifier) > 0;
       
    }

}
