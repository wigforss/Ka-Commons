package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

/**
 * Filter methods which name matches a Regular Expresson.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder.
 * 
 * @author rikardwi
 **/
public class NameMethodFilter implements MethodFilter {

    private String nameRegExp;
    
    public NameMethodFilter(String nameRegExp) {
        this.nameRegExp = nameRegExp;
    }
    
    
    @Override
    public boolean passFilter(Method method) {
        return method.getName().matches(nameRegExp);
    }

}
