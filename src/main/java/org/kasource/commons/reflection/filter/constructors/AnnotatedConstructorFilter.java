package org.kasource.commons.reflection.filter.constructors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class AnnotatedConstructorFilter implements ConstructorFilter {

    private Class<? extends Annotation> annotation;
    
    public AnnotatedConstructorFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean passFilter(Constructor constructor) {      
        return constructor.isAnnotationPresent(annotation);
    }

}
