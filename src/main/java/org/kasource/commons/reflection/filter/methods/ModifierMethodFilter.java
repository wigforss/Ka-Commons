package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

/**
 * Filter methods by modifiers,
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder.
 * 
 * @author rikardwi
 **/
public class ModifierMethodFilter implements MethodFilter {

    private int modifier;
    
    public ModifierMethodFilter(int modifier) {
        this.modifier = modifier;
    }
    
    @Override
    public boolean passFilter(Method method) {
       return (method.getModifiers() & modifier) > 0;
       
    }

}
