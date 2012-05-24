package org.kasource.commons.reflection.filter.classes;

/**
 * Filters classes which name matches a Regular Expression.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ClassFilterBuilder.
 * 
 * @author rikardwi
 **/
public class NameClassFilter implements ClassFilter {

    private String nameRegExp;
    
    public NameClassFilter(String nameRegExp) {
        this.nameRegExp = nameRegExp;
    }
    
    @Override
    public boolean passFilter(Class<?> clazz) {
        return clazz.getName().matches(nameRegExp);
    }

}
