package org.kasource.commons.reflection.filter.constructors;

import java.lang.reflect.Constructor;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;

import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SignatureConstructorFilterTest {
	
	@TestedObject
	private SignatureConstructorFilter filter = new SignatureConstructorFilter(String.class, int.class);
	
	@Test
	public void filterPass() throws SecurityException, NoSuchMethodException {
		Constructor<?>  cons = MyClass.class.getConstructor(String.class, int.class);
		assertTrue(filter.passFilter(cons));
	}
	
	@Test
	public void filterNotPassTooFewParameters() throws SecurityException, NoSuchMethodException {
		Constructor<?>  cons = MyClass.class.getConstructor(String.class);
		assertFalse(filter.passFilter(cons));
	}
	
	@Test
	public void filterNotPassTooManyParameters() throws SecurityException, NoSuchMethodException {
		Constructor<?>  cons = MyClass.class.getConstructor(String.class, int.class, int.class);
		assertFalse(filter.passFilter(cons));
	}
	
	@Test
	public void filterNotPassWrongType() throws SecurityException, NoSuchMethodException {
		Constructor<?>  cons = MyClass.class.getConstructor(String.class, String.class);
		assertFalse(filter.passFilter(cons));
	}
	
	private static class MyClass {
		public MyClass(String name){}
		public MyClass(String name, int age){}
		public MyClass(String name, String ssn){}
		public MyClass(String name, int age, int length){}
	}
	
}
