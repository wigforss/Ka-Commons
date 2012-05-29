package org.kasource.commons.util.reflection;

import java.util.EventListener;
import java.util.List;



import org.junit.Test;

public class ClassUtilsTest {

    
    @Test
    public void loadClassTest() {
       Class<? extends List> list =  ClassUtils.loadClass("java.util.ArrayList", List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void loadClassNotAssingableTest() {
       Class<? extends List> list =  ClassUtils.loadClass("java.lang.Integer", List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void loadClassNotFoundTest() {
       Class<? extends List> list =  ClassUtils.loadClass("org.myorg.MyClass", List.class);
    }
    
    @Test
    public void loadInterfaceClassTest() {
       
       Class<? extends EventListener> listener =  ClassUtils.loadClass("javax.swing.event.ChangeListener", EventListener.class);
    }
    
 
}
