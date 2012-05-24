package org.kasource.commons.reflection.filter.methods;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SignatureMethodFilterTest {

	@TestedObject
	private SignatureMethodFilter filter = new SignatureMethodFilter(String.class, int.class);
	
	@Test
	public void filterPass() throws SecurityException, NoSuchMethodException {
		Method method = MyClass.class.getMethod("twoParamters",String.class, int.class);
		assertTrue(filter.passFilter(method));
	}
	
	@Test
	public void filterNotPassTooFewParameters() throws SecurityException, NoSuchMethodException {
		Method method  = MyClass.class.getMethod("oneParamter",String.class);
		assertFalse(filter.passFilter(method));
	}
	
	@Test
	public void filterNotPassTooManyParameters() throws SecurityException, NoSuchMethodException {
		Method method  = MyClass.class.getMethod("threeParameters",String.class, int.class, int.class);
		assertFalse(filter.passFilter(method));
	}
	
	@Test
	public void filterNotPassWrongType() throws SecurityException, NoSuchMethodException {
		Method method  = MyClass.class.getMethod("twoParametersWrongType",String.class, String.class);
		assertFalse(filter.passFilter(method));
	}
	
	private static class MyClass {
		public void oneParamter(String name){}
		public void twoParamters(String name, int age){}
		public void twoParametersWrongType(String name, String ssn){}
		public void threeParameters(String name, int age, int length){}
	}
}
