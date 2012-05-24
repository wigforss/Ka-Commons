package org.kasource.commons.reflection.filter.classes;


/**
 * Filters classes which are assignable from the supplied assignable class.
 * <p>
 * It is possible to cast all classes that passes this filter to the assignable class.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ClassFilterBuilder.
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
