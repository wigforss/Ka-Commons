package org.kasource.commons.reflection.filter.constructors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class OrConstructorFilterTest {

    @InjectInto(property = "left")
    @Mock
    private ConstructorFilter left;
    
    @InjectInto(property = "right")
    @Mock
    private ConstructorFilter right;
    
   
    
    @TestedObject
    private OrConstructorFilter filter = new OrConstructorFilter(left, right);
    
    @Test
    public void passTrueRight() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(String.class);
        EasyMock.expect(left.passFilter(cons)).andReturn(false);
        EasyMock.expect(right.passFilter(cons)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passTrueLeft() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(String.class);
        EasyMock.expect(left.passFilter(cons)).andReturn(true);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(cons));
    }
    
    
   
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(String.class);
        EasyMock.expect(left.passFilter(cons)).andReturn(false);
        EasyMock.expect(right.passFilter(cons)).andReturn(false);
        EasyMockUnitils.replay();
       
        assertFalse(filter.passFilter(cons));
    }
    
    private static class MyClass {       
        
        public MyClass(String name) {}
    }
}
