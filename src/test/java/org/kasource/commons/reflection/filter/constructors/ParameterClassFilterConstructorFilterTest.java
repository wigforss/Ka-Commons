package org.kasource.commons.reflection.filter.constructors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ParameterClassFilterConstructorFilterTest {

    @InjectIntoByType
    @Mock
    private ClassFilter classFilter;
    
    @Mock
    private ClassFilter classFilter2;
    
    @TestedObject
    private ParameterClassFilterConstructorFilter filter = new ParameterClassFilterConstructorFilter(0, classFilter);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
    	EasyMock.expect(classFilter.passFilter(String.class)).andReturn(true);
        EasyMockUnitils.replay();
        Constructor<?>  cons = MyClass.class.getConstructor(String.class);
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
    	EasyMock.expect(classFilter.passFilter(String.class)).andReturn(false);
        EasyMockUnitils.replay();
        Constructor<?>  cons = MyClass.class.getConstructor(String.class);
        assertFalse(filter.passFilter(cons));
    }
    
    @Test
    public void passFalseTooFewParameters() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(null);
        assertFalse(filter.passFilter(cons));
    }
    
    @Test
    public void passTrueTwoParameters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "filter");
        InjectionUtils.injectInto(new ClassFilter[]{classFilter, classFilter2}, filter, "filters");
        EasyMock.expect(classFilter.passFilter(String.class)).andReturn(true);
        EasyMock.expect(classFilter2.passFilter(int.class)).andReturn(true);
        EasyMockUnitils.replay();
        Constructor<?>  cons = MyClass.class.getConstructor(String.class, int.class);
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passFalseTooManyParameters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "filter");
        InjectionUtils.injectInto(new ClassFilter[]{classFilter, classFilter2}, filter, "filters");
        Constructor<?>  cons = MyClass.class.getConstructor(String.class, int.class, int.class);
        assertFalse(filter.passFilter(cons));
    }
    
    
    @Test
    public void passTrueEmptyFilters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "filter");
        InjectionUtils.injectInto(new ClassFilter[]{}, filter, "filters");
        Constructor<?>  cons = MyClass.class.getConstructor(null);
        assertTrue(filter.passFilter(cons));
    }
    
    private static class MyClass {
    	public MyClass(){};
    	
        public MyClass(String name){};
        
        public MyClass(String name, int age){};
        
        public MyClass(String name, int age, int length){};
        
    }
}
