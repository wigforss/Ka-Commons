package org.kasource.commons.reflection.filter.classes;


/**
 * Composite filter that runs the filter list supplied in the constructor.
 * <p>
 * Use this class to combine filters.
 * <p>
 * Note: It's not recommended to use this class directly, instead use the org.kasource.commons.reflection.ClassFilterBuilder.
 * 
 * @author rikard
 **/
public class ClassFilterList implements ClassFilter{

	private ClassFilter[] filters;
	
	public ClassFilterList(ClassFilter... filters) {
		this.filters = filters;
	}
	
	@Override
	public boolean passFilter(Class<?> clazz) {
		for(ClassFilter filter : filters) {
			if(!filter.passFilter(clazz)) {
				return false;
			}
		}
		return true;
	}

}
