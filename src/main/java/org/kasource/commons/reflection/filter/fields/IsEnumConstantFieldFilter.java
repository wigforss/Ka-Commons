package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

/**
 * Filters fields which are Enum constants.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 * 
 * @author rikardwi
 **/
public class IsEnumConstantFieldFilter implements FieldFilter {

    @Override
    public boolean passFilter(Field field) {
       return field.isEnumConstant();
    }

}
