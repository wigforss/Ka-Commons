package org.kasource.commons.reflection.filter.classes;

/**
 * Filters classes by negating the result of another class filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ClassFilterBuilder.
 *  
 * @author rikardwi
 **/
public class NegationClassFilter implements ClassFilter {

    private ClassFilter filter;
    
    public NegationClassFilter(ClassFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean passFilter(Class<?> clazz) {
        return !filter.passFilter(clazz);
    }

}
