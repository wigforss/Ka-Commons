package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;

public interface ConstructorFilter {
    @SuppressWarnings("rawtypes")
    public boolean passFilter(Constructor constructor);
}
