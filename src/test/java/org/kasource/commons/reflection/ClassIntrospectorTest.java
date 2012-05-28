package org.kasource.commons.reflection;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.Set;

import static org.junit.Assert.*;

import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
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
		Method method = MyClass.class.getMethod("oneParamter", String.class);
		assertEquals(method, introspector.getDeclaredMethod("oneParamter", String.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getMissingMethod() throws SecurityException, NoSuchMethodException {
		
		 introspector.getMethod("noSuchMethod", String.class);
	}
	
	@Test
	public void getDeclaredMethods() throws SecurityException, NoSuchMethodException {
		Method method1 = MyClass.class.getMethod("oneParamter", String.class);
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
		Method method1 = MyClass.class.getMethod("oneParamter", String.class);
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
	
	private static class MyClass extends MyBase implements EventListener, Serializable {
		public void oneParamter(String name){}
		
		private void privateOneParamter(String name){}
	}
	
	private static class MyBase implements Runnable {
		public void superClassMethod(String name){}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
}
