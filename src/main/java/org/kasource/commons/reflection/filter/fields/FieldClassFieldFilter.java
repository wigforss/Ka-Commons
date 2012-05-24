package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

import org.kasource.commons.reflection.filter.classes.ClassFilter;

/**
 * FIlter fields which type passes a class filter.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 *  
 * @author rikardwi
 **/
public class FieldClassFieldFilter implements FieldFilter {

    private ClassFilter filter;
    
    public FieldClassFieldFilter(ClassFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean passFilter(Field field) {
       return filter.passFilter(field.getType());
    }

}
