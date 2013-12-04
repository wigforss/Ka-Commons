package org.kasource.commons.collection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeList;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClassMapTest {

    @Test
    public void getNoMap() {    
        ClassMap<String> classMap = new ClassMap<String>();
        assertNull(classMap.get(Integer.class));       
    }
    
    @Test
    public void getEmptyMap() {    
       
        Map<Class<?>, String> map = new HashMap<Class<?>, String>();
        ClassMap<String> classMap = new ClassMap<String>(map);
        assertNull(classMap.get(Integer.class));       
    }
    
    @Test
    public void getInteger() {
        Map<Class<?>, String> map = new HashMap<Class<?>, String>();
        map.put(Number.class, "Number");
        map.put(Object.class, "Object");
        ClassMap<String> classMap = new ClassMap<String>(map);
        assertEquals("Number", classMap.get(Integer.class));       
    }
    
    @Test
    public void getAttributeList() {
        Map<Class<?>, String> map = new HashMap<Class<?>, String>();
        map.put(List.class, "List");
        map.put(Object.class, "Object");
        ClassMap<String> classMap = new ClassMap<String>(map);
        assertEquals("List", classMap.get(AttributeList.class));       
    }
}
