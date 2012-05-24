package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

/**
 * Filters fields which type is assignable from a specific class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 * 
 * @author rikardwi
 **/
public class AssignableFromFieldFilter implements FieldFilter {

    /**
     * Interface or super class to test candidates with
     **/
    private Class<?> assignable;
    
    public AssignableFromFieldFilter(Class<?> assignable) {
        this.assignable = assignable;
    }
    
    @Override
    public boolean passFilter(Field field) {
        return assignable.isAssignableFrom(field.getType());
    }

}
