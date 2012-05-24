package org.kasource.commons.reflection.filter.constructors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ModifierConstructorFilterTest {

    @TestedObject
    private ModifierConstructorFilter filter = new ModifierConstructorFilter(Modifier.PUBLIC);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
    	 Constructor<?>  cons = MyClass.class.getConstructor(null);
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
    	 Constructor<?>  cons = MyClass.class.getDeclaredConstructor(String.class);
        assertFalse(filter.passFilter(cons));
    }
    
    private static class MyClass {
       
        public MyClass(){}
        
      
        private MyClass(String name) {}
    }
    
}
