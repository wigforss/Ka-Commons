package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

/**
 * Filter constructors by modifiers,
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
 * 
 * @author rikardwi
 **/
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
