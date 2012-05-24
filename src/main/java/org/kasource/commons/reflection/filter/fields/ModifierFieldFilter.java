package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

/**
 * Filters fields by modifiers.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 *  
 * @author rikardwi
 **/
public class ModifierFieldFilter implements FieldFilter {
    
    private int modifier;
    
    public ModifierFieldFilter(int modifier) {
        this.modifier = modifier;
    }
    
    @Override
    public boolean passFilter(Field field) {
        return (field.getModifiers() & modifier) > 0;
    }

}
