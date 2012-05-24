package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

/**
 * Filters Constructors.
 * <p>
 * Implement this interface to create a Constructor filter.
 * 
 * @author rikardwi
 **/
public interface ConstructorFilter {
    @SuppressWarnings("rawtypes")
    public boolean passFilter(Constructor constructor);
}
