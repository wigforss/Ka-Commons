package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

/**
 * Filters fields by negating the result of another field filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 *  
 * @author rikardwi
 **/
public class NegationFieldFilter implements FieldFilter {

    private FieldFilter filter;
    
    public NegationFieldFilter(FieldFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean passFilter(Field field) {
       return !filter.passFilter(field);
    }

}
