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
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class NegationConstructorFilterTest {

    @InjectIntoByType
    @Mock
    private ConstructorFilter methodFilter;
    
    @TestedObject
    private NegationConstructorFilter filter = new NegationConstructorFilter(methodFilter);
    
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        Constructor<?>  cons = MyClass.class.getConstructor(String.class);
        EasyMock.expect(methodFilter.passFilter(cons)).andReturn(false);
        EasyMockUnitils.replay();
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(String.class);
        EasyMock.expect(methodFilter.passFilter(cons)).andReturn(true);
        EasyMockUnitils.replay();
       
        assertFalse(filter.passFilter(cons));
    }
    
    private static class MyClass {       
    	@SuppressWarnings("unused")
		public MyClass(String msg){}
    }
}
