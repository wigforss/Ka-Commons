package org.kasource.commons.reflection.filter.fields;

import java.lang.reflect.Field;

/**
 * Filters fields by evaluating two other field filters and OR:ing their result.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder. 
 * 
 * @author rikardwi
 **/
public class OrFieldFilter implements FieldFilter {

    private FieldFilter left;
    private FieldFilter right;
    
    public OrFieldFilter(FieldFilter left, FieldFilter right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean passFilter(Field field) {
      return left.passFilter(field) || right.passFilter(field);
    }

}
