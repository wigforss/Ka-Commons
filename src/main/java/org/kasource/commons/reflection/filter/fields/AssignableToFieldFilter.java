package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

public class AssignableToFieldFilter implements FieldFilter {

    /**
     * Interface or super class to test candidates with
     **/
    private Class<?> assignable;
    
    public AssignableToFieldFilter(Class<?> assignable) {
        this.assignable = assignable;
    }
    
    @Override
    public boolean passFilter(Field field) {
        return field.getType().isAssignableFrom(assignable);
    }

}
