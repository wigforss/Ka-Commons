package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

/**
 * Field Filter.
 * 
 * Implement this interface to create a field filter.
 * 
 * @author rikardwi
 **/
public interface FieldFilter {
    public boolean passFilter(Field field);
}
