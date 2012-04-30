package org.kasource.commons.reflection.filter.classes;


/**
 * Filters classes which are assignable from the supplied assignable class.
 * 
 * It is possible to cast all classes that passes this filter to the assignable class.
 * 
 * @author Rikard Wigforss
 **/
public class AssignableToClassFilter implements ClassFilter {

	/**
	 * Interface or super class to test candidates with
	 **/
	private Class<?> assignable;
	
	public AssignableToClassFilter(Class<?> assignable) {
		this.assignable = assignable;
	}
	
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isAssignableFrom(assignable);
	}

}
