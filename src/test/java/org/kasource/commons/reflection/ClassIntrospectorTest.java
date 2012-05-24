package org.kasource.commons.reflection;

import java.lang.reflect.Method;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.unitils.inject.annotation.TestedObject;

public class ClassIntrospectorTest {

	@TestedObject
	private ClassIntrospector introspector = new ClassIntrospector<MyClass>(MyClass.class);
	
	@Test
	public void getDeclaredMethod() throws SecurityException, NoSuchMethodException {
		Method method = MyClass.class.getDeclaredMethod("oneParamter", String.class);
		assertEquals(method, introspector.getDeclaredMethod("oneParamter", String.class));
	}
	
	
	
	private static class MyClass {
		public void oneParamter(String name){}
		
		private void privateOneParamter(String name){}
	}
}
