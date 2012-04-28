package org.kasource.commons.reflection.filter.fields;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class InheritlyAnnotatedFieledFilterTest {
    @TestedObject
    private MetaAnnotatedFieldFilter filter = new MetaAnnotatedFieldFilter(SuperAnnotation.class);
    
    @Test
    public void passTrue() throws SecurityException, NoSuchFieldException  {
        Field field = MyClass.class.getField("number");
        assertTrue(filter.passFilter(field));
    }
    
    @Test
    public void passFalse() throws SecurityException, NoSuchFieldException {
        Field field  = MyClass.class.getField("number2");
        assertFalse(filter.passFilter(field));
    }
    
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface SuperAnnotation{}
    
    @SuperAnnotation
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {
        
    }
    
    private static class MyClass {
        @MyAnnotation
        @SuppressWarnings("unused")
        public int number;
        
        
        @SuppressWarnings("unused")
        public int number2;
    }
}
