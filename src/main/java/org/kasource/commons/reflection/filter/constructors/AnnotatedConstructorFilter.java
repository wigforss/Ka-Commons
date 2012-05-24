package org.kasource.commons.reflection.filter.constructors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * Filters constructors that is annotated with a specific annotation.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder.
 * 
 * @author rikardwi
 **/
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
