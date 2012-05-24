package org.kasource.commons.reflection.filter.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Filters methods that's annotated with a specific annotation.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.MethodFilterBuilder. 
 * 
 * @author rikardwi
 **/
public class AnnotatedMethodFilter implements MethodFilter {

    private Class<? extends Annotation> annotation;
    
    public AnnotatedMethodFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }
    
    public boolean passFilter(Method method) {
        return method.isAnnotationPresent(annotation);
    }
    
}
