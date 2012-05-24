package org.kasource.commons.reflection.filter.constructors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class NumberOfParametersConstructorFilterTest {
    
    @TestedObject
    private NumberOfParametersConstructorFilter filter = new NumberOfParametersConstructorFilter(1);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(String.class);
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(String.class, int.class);
        assertFalse(filter.passFilter(cons));
    }
    
    private static class MyClass {
        @SuppressWarnings("unused")
		private String name;
        
        @SuppressWarnings("unused")
		public MyClass(String name) {
        	this.name = name;
        }
        
        @SuppressWarnings("unused")
		public MyClass(String name, int age) {
        	this.name = name;
        }
    }
}
