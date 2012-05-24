package org.kasource.commons.reflection.filter.classes;

/**
 * Filters classes by modifiers.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ClassFilterBuilder.
 *  
 * @author rikardwi
 **/
public class ModifierClassFilter implements ClassFilter {

    private int modifier;
    
    public ModifierClassFilter(int modifier) {
        this.modifier = modifier;
    }
    
    @Override
    public boolean passFilter(Class<?> clazz) {
        return (clazz.getModifiers() & modifier) > 0;
    }

}
