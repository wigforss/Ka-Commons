package org.kasource.commons.reflection.filter.constructors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class MetaAnnotatedConstructorFilter implements ConstructorFilter {

    private Class<? extends Annotation> inheritedAnnotation;
    
    public MetaAnnotatedConstructorFilter(Class<? extends Annotation> inheritedAnnotation) {
        this.inheritedAnnotation = inheritedAnnotation;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {
        Annotation[] annotations = constructor.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(inheritedAnnotation)) {
                return true;
            }
        }
        return false;
    }

}
