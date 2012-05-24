package org.kasource.commons.reflection.filter.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Filters methods which is annotated with an annotation which is annotated with 
 * a specific meta annotation.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder. 
 * 
 * @author rikardwi
 **/
public class MetaAnnotatedMethodFilter implements MethodFilter {

    private Class<? extends Annotation> inheritedAnnotation;
    
    public MetaAnnotatedMethodFilter(Class<? extends Annotation> inheritedAnnotation) {
        this.inheritedAnnotation = inheritedAnnotation;
    }
    
    public boolean passFilter(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(inheritedAnnotation)) {
                return true;
            }
        }
        return false;
    }
    
}
