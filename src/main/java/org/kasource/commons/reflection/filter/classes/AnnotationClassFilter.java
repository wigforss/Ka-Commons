package org.kasource.commons.reflection.filter.classes;

import java.lang.annotation.Annotation;


/**
 * Filters classes by presence of a certain annotation
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ClassFilterBuilder.
 *  
 * @author Rikard Wigforss
 **/
public class AnnotationClassFilter implements ClassFilter{

	private Class<? extends Annotation> annotation;
	
	
	public AnnotationClassFilter(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
		
	}
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isAnnotationPresent(annotation);
	}

}
