package org.kasource.commons.collection;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;


import org.junit.Assert;
import org.junit.Test;

public class PackageMapTest {

    
    @Test
    public void getMapIsNull() {
        
        PackageMap<String> packageMap = new PackageMap<String>();
        
        Assert.assertNull("bind", packageMap.get(XmlAdapter.class));
        
    }
    
    @Test
    public void get() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("javax", "javax");
        map.put("javax.xml", "xml");
        map.put("javax.xml.bind", "bind");
        PackageMap<String> packageMap = new PackageMap<String>(map);
        
        Assert.assertEquals("bind", packageMap.get(XmlAdapter.class));
        
    }
}
