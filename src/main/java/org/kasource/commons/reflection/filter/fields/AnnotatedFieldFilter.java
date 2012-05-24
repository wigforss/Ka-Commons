package org.kasource.commons.reflection.filter.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Filter fields which are annotated with a specific annotation.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 * 
 * @author rikardwi
 **/
public class AnnotatedFieldFilter implements FieldFilter {

 private Class<? extends Annotation> annotation;
    
    public AnnotatedFieldFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }
    
    @Override
    public boolean passFilter(Field field) {
       return field.isAnnotationPresent(annotation);
    }

}
