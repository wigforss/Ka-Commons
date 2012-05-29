package org.kasource.commons.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.kasource.commons.reflection.filter.MethodFilterBuilder;
import org.kasource.commons.reflection.filter.methods.AnnotatedMethodFilter;
import org.kasource.commons.reflection.filter.methods.SignatureMethodFilter;
import org.kasource.commons.util.reflection.MethodUtils;


public class MethodUtilsTest {
   
    
   @Test
   public void hasMethodNoReturnTypeTrue() throws SecurityException, NoSuchMethodException {
       Method method = MyClass.class.getMethod("setName", String.class);
       Assert.assertTrue(MethodUtils.hasMethodNoReturnType(method));     
   }
   
   @Test
   public void hasMethodNoReturnTypeFalse() throws SecurityException, NoSuchMethodException {
       Method method = MyClass.class.getMethod("getName");
       Assert.assertFalse(MethodUtils.hasMethodNoReturnType(method));
       
   }
   
   @Test
   public void verifyMethodSignature() throws SecurityException, NoSuchMethodException {
       Method method = MyClass.class.getMethod("setName", String.class);
       MethodUtils.verifyMethodSignature(method, Void.TYPE, String.class);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void verifyMethodSignatureNullMethod() throws SecurityException, NoSuchMethodException {
       
       MethodUtils.verifyMethodSignature(null, Void.TYPE, int.class);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void verifyMethodSignatureWrongParamType() throws SecurityException, NoSuchMethodException {
       Method method = MyClass.class.getMethod("setName", String.class);
       MethodUtils.verifyMethodSignature(method, Void.TYPE, int.class);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void verifyMethodSignatureTooManyParams() throws SecurityException, NoSuchMethodException {
       Method method = MyClass.class.getMethod("setName", String.class);
       MethodUtils.verifyMethodSignature(method, Void.TYPE);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void verifyMethodSignatureTooFewParams() throws SecurityException, NoSuchMethodException {
       Method method = MyClass.class.getMethod("setName", String.class);
       MethodUtils.verifyMethodSignature(method, Void.TYPE, String.class, int.class);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void verifyMethodSignatureWrongReturnType() throws SecurityException, NoSuchMethodException {
       Method method = MyClass.class.getMethod("setName", String.class);
       MethodUtils.verifyMethodSignature(method, String.class, String.class);
   }
   
   private static class MyClass {
       public void setName(String name) {}
       
       public String getName() {
           return "name";
       }
   }
    
   
}
