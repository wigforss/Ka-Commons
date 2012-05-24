package org.kasource.commons.reflection.filter.constructors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AssignableFromConstructorFilterTest {

    @TestedObject
    private AssignableFromConstructorFilter filter = new AssignableFromConstructorFilter(0, List.class);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(ArrayList.class);
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passFalseNoSuchParameterIndex() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(null);
        assertFalse(filter.passFilter(cons));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(Integer.class);
        assertFalse(filter.passFilter(cons));
    }
    
    @Test
    public void passTrueTwoParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignbleFromClass");
        InjectionUtils.injectInto(new Class[]{List.class, Integer.class}, filter, "assignbleFrom");
        Constructor<?>  cons = MyClass.class.getConstructor(ArrayList.class, Integer.class);
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passFalseTooFewParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignbleFromClass");
        InjectionUtils.injectInto(new Class[]{List.class, Integer.class}, filter, "assignbleFrom");
        Constructor<?>  cons = MyClass.class.getConstructor(ArrayList.class, Integer.class, List.class);
        assertFalse(filter.passFilter(cons));
    }
    
    @Test
    public void passFalseTooManyParamters() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(null, filter, "assignbleFromClass");
        InjectionUtils.injectInto(new Class[]{List.class, Integer.class}, filter, "assignbleFrom");
        Constructor<?>  cons = MyClass.class.getConstructor(ArrayList.class);
        assertFalse(filter.passFilter(cons));
    }
    
    
    
    private static class MyClass {
    	 public MyClass(ArrayList<String> list) {}
         
         public MyClass(Integer age) {}
              
         public MyClass(ArrayList<String> list, Integer age) {}
         
         public MyClass(ArrayList<String> list, Integer number, List<String> list2) {}
         
         public MyClass() {}
    }
}
