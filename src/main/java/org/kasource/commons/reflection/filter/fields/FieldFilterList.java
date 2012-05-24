package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

/**
 * List of Field Filters.
 * 
 * Evaluates the filters in an AND manner.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 * 
 * @author rikardwi
 **/
public class FieldFilterList implements FieldFilter {

    private FieldFilter[] filters;
    
    public FieldFilterList(FieldFilter... filters) {
        this.filters = filters;
    }
    
    
    @Override
    public boolean passFilter(Field field) {
        for (FieldFilter filter : filters) {
            if (!filter.passFilter(field)) {
                return false;
            }
        }
        return true;
    }

}
