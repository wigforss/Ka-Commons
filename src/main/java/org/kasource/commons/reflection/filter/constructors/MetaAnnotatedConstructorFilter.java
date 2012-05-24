package org.kasource.commons.reflection.filter.constructors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
* Filters constructors which is annotated with an annotation which is annotated with 
* a specific meta annotation.
* <p>
* Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ConstructorFilterBuilder. 
* 
* @author rikardwi
**/
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
