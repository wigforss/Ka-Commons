package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

/**
 * Filters fields which name matches a Regular Expression.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 * 
 * @author rikardwi
 **/
public class NameFieldFilter implements FieldFilter {

    private String nameRegExp;
    
    public NameFieldFilter(String nameRegExp) {
        this.nameRegExp = nameRegExp;
    }

    @Override
    public boolean passFilter(Field field) {
       return field.getName().matches(nameRegExp);
    }
    
    
}
