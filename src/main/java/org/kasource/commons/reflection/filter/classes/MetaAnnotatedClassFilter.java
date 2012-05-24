package org.kasource.commons.reflection.filter.classes;

import java.lang.annotation.Annotation;

/**
 * Filters all classes which is annotated with an annotation which is annotated with a specific meta annotation.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ClassFilterBuilder.
 * 
 * @author rikardwi
 **/
public class MetaAnnotatedClassFilter implements ClassFilter {

    private Class<? extends Annotation> inheritedAnnotation;
    
    public MetaAnnotatedClassFilter(Class<? extends Annotation> inheritedAnnotation) {
        this.inheritedAnnotation = inheritedAnnotation;
    }
    
    public boolean passFilter(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(inheritedAnnotation)) {
                return true;
            }
        }
        return false;
    }
    
}
