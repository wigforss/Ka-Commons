package org.kasource.commons.reflection.filter.classes;


/**
 * Filter that keeps only synthetic classes.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ClassFilterBuilder.
 * 
 * @author Rikard Wigforss
 **/
public class IsSyntheticClassFilter implements ClassFilter{
	@Override
	public boolean passFilter(Class<?> clazz) {
		return clazz.isSynthetic();
	}
}
