package org.kasource.commons.reflection;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.MethodFilterBuilder;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.constructors.ConstructorFilter;
import org.kasource.commons.reflection.filter.fields.FieldFilter;
import org.kasource.commons.reflection.filter.methods.MethodFilter;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ClassIntrospectorTest {

	@Mock
	private MethodFilter methodFilter;
	
	@Mock
	private ClassFilter classFilter;
	
	@Mock
	private FieldFilter fieldFilter;
	
	@Mock
	private ConstructorFilter constructorFilter;
	
	@TestedObject
	private ClassIntrospector introspector = new ClassIntrospector(MyClass.class);
	
	@Test
	public void getDeclaredMethod() throws SecurityException, NoSuchMethodException {
		Method method = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
		assertEquals(method, introspector.getDeclaredMethod("privateOneParamter", String.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getMissingDeclaredMethod() throws SecurityException, NoSuchMethodException {
		
		 introspector.getDeclaredMethod("noSuchMethod", String.class);
	}
	
	@Test
	public void getMethod() throws SecurityException, NoSuchMethodException {
		Method method = MyClass.class.getMethod("oneParameter", String.class);
		assertEquals(method, introspector.getDeclaredMethod("oneParameter", String.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getMissingMethod() throws SecurityException, NoSuchMethodException {
		
		 introspector.getMethod("noSuchMethod", String.class);
	}
	
	@Test
	public void getDeclaredMethods() throws SecurityException, NoSuchMethodException {
		Method method1 = MyClass.class.getMethod("oneParameter", String.class);
		Method method2 = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
		expect(	methodFilter.passFilter(method1)).andReturn(true);
		expect(	methodFilter.passFilter(method2)).andReturn(false);
		expect(	methodFilter.passFilter(isA(Method.class))).andReturn(false).anyTimes();
		EasyMockUnitils.replay();
		Set<Method> methods = introspector.getDeclaredMethods(methodFilter);
		assertTrue(methods.contains(method1));
		assertFalse(methods.contains(method2));
		assertEquals(1, methods.size());
	}
	
	@Test
	public void getMethods() throws SecurityException, NoSuchMethodException {
		Method method1 = MyClass.class.getMethod("oneParameter", String.class);
		Method method2 = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
		Method method3 = MyClass.class.getMethod("superClassMethod", String.class);
		expect(methodFilter.passFilter(method1)).andReturn(false);
		expect(methodFilter.passFilter(method2)).andReturn(false);
		expect(methodFilter.passFilter(method3)).andReturn(true);
		expect(methodFilter.passFilter(isA(Method.class))).andReturn(false).anyTimes();
		EasyMockUnitils.replay();
		Set<Method> methods = introspector.getMethods(methodFilter);
		assertFalse(methods.contains(method1));
		assertFalse(methods.contains(method2));
		assertTrue(methods.contains(method3));
		assertEquals(1, methods.size());
	}
	
	@Test
	public void getInterfaces() {
		expect(classFilter.passFilter(EventListener.class)).andReturn(false);
		expect(classFilter.passFilter(Serializable.class)).andReturn(false);
		expect(classFilter.passFilter(Runnable.class)).andReturn(true);
		EasyMockUnitils.replay();
		Set<Class<?>> interfaces = introspector.getInterfaces(classFilter);
		assertFalse(interfaces.contains(EventListener.class));
		assertFalse(interfaces.contains(Serializable.class));
		assertTrue(interfaces.contains(Runnable.class));
		assertEquals(1, interfaces.size());
	}
	
	@Test
	public void getDeclaredInterfaces() {
		expect(classFilter.passFilter(EventListener.class)).andReturn(false);
		expect(classFilter.passFilter(Serializable.class)).andReturn(true);
		
		EasyMockUnitils.replay();
		Set<Class<?>> interfaces = introspector.getDeclaredInterfaces(classFilter);
		assertFalse(interfaces.contains(EventListener.class));
		assertTrue(interfaces.contains(Serializable.class));
	
		assertEquals(1, interfaces.size());
	}
	
	@Test
	public void getDeclaredFields() throws SecurityException, NoSuchFieldException {
	    Field name = MyClass.class.getDeclaredField("name");
	    Field age = MyClass.class.getDeclaredField("age");
	    
	    expect(fieldFilter.passFilter(name)).andReturn(false);
	    expect(fieldFilter.passFilter(age)).andReturn(true);
	    expect(fieldFilter.passFilter(isA(Field.class))).andReturn(false).anyTimes();
	    EasyMockUnitils.replay();
	    Set<Field> fields = introspector.getDeclaredFields(fieldFilter);
	    assertFalse(fields.contains(name));
        assertTrue(fields.contains(age));
    
        assertEquals(1, fields.size());
	}
	
	
	@Test
    public void getFields() throws SecurityException, NoSuchFieldException {
        Field name = MyClass.class.getDeclaredField("name");
        Field age = MyClass.class.getDeclaredField("age");
        Field superField = MyBase.class.getDeclaredField("superField");
        expect(fieldFilter.passFilter(name)).andReturn(false);
        expect(fieldFilter.passFilter(age)).andReturn(false);
        expect(fieldFilter.passFilter(superField)).andReturn(true);
        expect(fieldFilter.passFilter(isA(Field.class))).andReturn(false).anyTimes();
        EasyMockUnitils.replay();
        Set<Field> fields = introspector.getFields(fieldFilter);
        assertFalse(fields.contains(name));
        assertFalse(fields.contains(age));
        assertTrue(fields.contains(superField));
        assertEquals(1, fields.size());
    }
	
	@Test
	public void getConstructors() throws SecurityException, NoSuchMethodException {
	    Constructor<MyClass> defCons = MyClass.class.getDeclaredConstructor();
	    Constructor<MyClass> cons1 = MyClass.class.getDeclaredConstructor(String.class);
	    Constructor<MyClass> cons2 = MyClass.class.getDeclaredConstructor(String.class, int.class);
	    expect(constructorFilter.passFilter(defCons)).andReturn(true);
	    expect(constructorFilter.passFilter(cons1)).andReturn(false);
	    expect(constructorFilter.passFilter(cons2)).andReturn(true);
	    EasyMockUnitils.replay();
	    Set<Constructor<MyClass>> constructors = introspector.getConstructors(constructorFilter, MyClass.class);
	    assertTrue(constructors.contains(defCons));
        assertFalse(constructors.contains(cons1));
        assertTrue(constructors.contains(cons2));
        assertEquals(2, constructors.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getConstructorsWrongType() {
	    introspector.getConstructors(constructorFilter, MyBase.class);
	}
	
	@Test
	public void getConstructor() throws SecurityException, NoSuchMethodException {
	    Constructor<MyClass> defCons = MyClass.class.getDeclaredConstructor();
        Constructor<MyClass> cons1 = MyClass.class.getDeclaredConstructor(String.class);
        Constructor<MyClass> cons2 = MyClass.class.getDeclaredConstructor(String.class, int.class);
        expect(constructorFilter.passFilter(defCons)).andReturn(false);
        expect(constructorFilter.passFilter(cons1)).andReturn(false);
        expect(constructorFilter.passFilter(cons2)).andReturn(true);
        EasyMockUnitils.replay();
	    Constructor<MyClass> constructor = introspector.getConstructor(constructorFilter, MyClass.class);
	    assertEquals(cons2, constructor);
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void getConstructorNoMatch() throws SecurityException, NoSuchMethodException {
        Constructor<MyClass> defCons = MyClass.class.getDeclaredConstructor();
        Constructor<MyClass> cons1 = MyClass.class.getDeclaredConstructor(String.class);
        Constructor<MyClass> cons2 = MyClass.class.getDeclaredConstructor(String.class, int.class);
        expect(constructorFilter.passFilter(defCons)).andReturn(false);
        expect(constructorFilter.passFilter(cons1)).andReturn(false);
        expect(constructorFilter.passFilter(cons2)).andReturn(false);
        EasyMockUnitils.replay();
        Constructor<MyClass> constructor = introspector.getConstructor(constructorFilter, MyClass.class);
        assertEquals(cons2, constructor);
    }
	
	
	
	@Test(expected = IllegalArgumentException.class)
    public void getConstructorWrongType() {
        introspector.getConstructor(constructorFilter, MyBase.class);
    }
	
	@Test
    public void isAnnotationPresentTrue() {
        assertTrue(introspector.isAnnotationPresent(ClassAnnotation2.class));
    }
	
	@Test
    public void isAnnotationPresentFalse() {
        assertFalse(introspector.isAnnotationPresent(ClassAnnotation1.class));
    }
	
	@Test
    public void getAnnotation() {
        assertEquals("Value2", introspector.getAnnotation(ClassAnnotation2.class).value());
    }
	
	@Test
    public void getAnnotationNotFound() {
        assertEquals(null, introspector.getAnnotation(ClassAnnotation1.class));
    }
	
	@Test
	public void findAnnotatedMethods() throws SecurityException, NoSuchMethodException {
	    Method method1 = MyClass.class.getMethod("oneParameter", String.class);
        Method method2 = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
        Method method3 = MyClass.class.getMethod("run");
	    Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
	    annotations.add(MethodAnnotation1.class);
	    annotations.add(MethodAnnotation2.class);
	    Map<Class<? extends Annotation>, Set<Method>> map = introspector.findAnnotatedMethods(null, annotations);
	    Set<Method> anno1 = map.get(MethodAnnotation1.class);
	    Set<Method> anno2 = map.get(MethodAnnotation2.class);
	    assertEquals(2, anno1.size());
	    assertTrue(anno1.contains(method1));
	    assertTrue(anno1.contains(method2));
	    assertEquals(1, anno2.size());
	    assertTrue(anno2.contains(method3));
	}
	
	@Test
    public void findAnnotatedMethodsWithFilter() throws SecurityException, NoSuchMethodException {
        Method method1 = MyClass.class.getMethod("oneParameter", String.class);
        Method method2 = MyClass.class.getDeclaredMethod("privateOneParamter", String.class);
        Method method3 = MyClass.class.getMethod("run");
        Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
        annotations.add(MethodAnnotation1.class);
        annotations.add(MethodAnnotation2.class);
        Map<Class<? extends Annotation>, Set<Method>> map = introspector.findAnnotatedMethods(new MethodFilterBuilder().isPublic().build(), annotations);
        Set<Method> anno1 = map.get(MethodAnnotation1.class);
        Set<Method> anno2 = map.get(MethodAnnotation2.class);
        assertEquals(1, anno1.size());
        assertTrue(anno1.contains(method1));
        assertFalse(anno1.contains(method2));
        assertEquals(1, anno2.size());
        assertTrue(anno2.contains(method3));
    }
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface MethodAnnotation1{}
	
	@Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodAnnotation2{}
	
	@Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassAnnotation1{}
	
	@Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassAnnotation2{
	    String value();
	}
	
	
	private static class MyClass extends MyBase implements EventListener, Serializable {
	    private String name;
	    private int age;
	    
	    public MyClass() {}
	    
	    public MyClass(String name) {}
	    
	    
	    private MyClass(String name, int age) {}
	    
	    @MethodAnnotation1
		public void oneParameter(String name){}
		
	    @MethodAnnotation1
		private void privateOneParamter(String name){}
	}
	
	@ClassAnnotation2("Value2")
	private static class MyBase implements Runnable {
	    private String superField;
		public void superClassMethod(String name){}

		@MethodAnnotation2
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
}
