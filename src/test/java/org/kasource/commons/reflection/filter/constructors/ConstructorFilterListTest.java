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
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ConstructorFilterListTest {
    @InjectIntoByType
    private ConstructorFilter[] filters;
    
    @Mock
    private ConstructorFilter methodFilter;
    
    @Mock
    private ConstructorFilter methodFilter2;
    
    @TestedObject
    private ConstructorFilterList filter = new ConstructorFilterList(filters);
    
    @Test(expected = NullPointerException.class)
    public void nullFilterTest() throws SecurityException, NoSuchMethodException {
    	Constructor<?>  cons = MyClass.class.getConstructor(null);
        filter.passFilter(cons);
    }
    
    @Test
    public void emptpyFilterTest() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new ConstructorFilter[]{}, filter, "filters");
        Constructor<?>  cons = MyClass.class.getConstructor(null);
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passTrue() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new ConstructorFilter[]{methodFilter, methodFilter2}, filter, "filters");
        Constructor<?>  cons = MyClass.class.getConstructor(null);
        EasyMock.expect(methodFilter.passFilter(cons)).andReturn(true);
        EasyMock.expect(methodFilter2.passFilter(cons)).andReturn(true);
        EasyMockUnitils.replay();
        
        assertTrue(filter.passFilter(cons));
    }
    
    @Test
    public void passFqlse() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new ConstructorFilter[]{methodFilter, methodFilter2}, filter, "filters");
        Constructor<?>  cons = MyClass.class.getConstructor(null);
        EasyMock.expect(methodFilter.passFilter(cons)).andReturn(false);
      
        EasyMockUnitils.replay();
        
        assertFalse(filter.passFilter(cons));
    }
    
    @Test
    public void passFalseSecondFilterFalse() throws SecurityException, NoSuchMethodException {
        InjectionUtils.injectInto(new ConstructorFilter[]{methodFilter, methodFilter2}, filter, "filters");
        Constructor<?>  cons = MyClass.class.getConstructor(null);
        EasyMock.expect(methodFilter.passFilter(cons)).andReturn(true);
        EasyMock.expect(methodFilter2.passFilter(cons)).andReturn(false);
        EasyMockUnitils.replay();
        
        assertFalse(filter.passFilter(cons));
    }
    
   
    
    private static class MyClass {       
        public MyClass(){}
    }
}
