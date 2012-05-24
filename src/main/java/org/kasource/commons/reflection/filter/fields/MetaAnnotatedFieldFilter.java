package org.kasource.commons.reflection.filter.fields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Filters all fields which is annotated with an annotation which is annotated with a specific meta annotation.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.FieldFilterBuilder.
 * 
 * @author rikardwi
 **/
public class MetaAnnotatedFieldFilter implements FieldFilter {

    private Class<? extends Annotation> inheritedAnnotation;
    
    public MetaAnnotatedFieldFilter(Class<? extends Annotation> inheritedAnnotation) {
        this.inheritedAnnotation = inheritedAnnotation;
    }
    
    public boolean passFilter(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(inheritedAnnotation)) {
                return true;
            }
        }
        return false;
    }
    
}
