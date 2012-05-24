package org.kasource.commons.reflection.filter.methods;

import java.lang.reflect.Method;

/**
 * A filter which negates the result form another method filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder.
 * 
 * @author rikardwi
 **/
public class NegationMethodFilter implements MethodFilter {

    private MethodFilter filter;
    
    public NegationMethodFilter(MethodFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean passFilter(Method method) {
        return !filter.passFilter(method);
    }

}
