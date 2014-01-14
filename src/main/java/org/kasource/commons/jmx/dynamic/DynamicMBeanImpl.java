package org.kasource.commons.jmx.dynamic;

import java.lang.reflect.Method;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * Dynamic MBean implementation, which delegates all access to a target object.
 * 
 * @author rikardwi
 **/
public class DynamicMBeanImpl implements DynamicMBean, MBeanRegistration {

    private Object target;
    private MBeanInfo mBeanInfo;
    private Map<String, Method> setters;
    private Map<String, Method> getters;
    private Map<String, Method> operations;
   
    /**
     * Constructor.
     * 
     * @param target Target object to delegate to.
     */
    public DynamicMBeanImpl(Object target) {
        this.target = target;
        
    }
    
    @Override
    public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        try {
            return getAttributeValue(attributeName);
        } catch (Exception e) {
            return null;
        }
       
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException,
                MBeanException, ReflectionException {
        setAttributeValue(attribute);
        
    }

    @Override
    public AttributeList getAttributes(String[] attributeNames) {
        AttributeList list = new AttributeList();
        for (String attributeName : attributeNames) {
            try {
                list.add(new Attribute(attributeName, getAttributeValue(attributeName)));
            } catch (Exception e) {
            }
        }
        return list;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributelist) {
        AttributeList list = new AttributeList();
        for (Attribute attribute : attributelist.asList()) {
           if (setAttributeValue(attribute)) {
               list.add(attribute);
           }
                       
        }
        return list;
    }

    @Override
    public Object invoke(String operation, Object[] parameters, String[] signature) throws MBeanException, ReflectionException {
        Method method = operations.get(operation);
        if (method != null) {
            try {
                return method.invoke(target, parameters);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return mBeanInfo;
    }

    private Object getAttributeValue(String attributeName) throws IllegalStateException, IllegalArgumentException{
        Method method = getters.get(attributeName);
        if (method != null) {
            try {
                return method.invoke(target);
            } catch (Exception e) {
                throw new IllegalStateException("Could not invokd getter for " + attributeName, e);
            }
        }
        throw new IllegalArgumentException("Could not find getter for " + attributeName);
    }
    
    private boolean setAttributeValue(Attribute attribute) {
        Method method = setters.get(attribute.getName());
        if (method != null) {
            try {
                 method.invoke(target, attribute.getValue());
                 return true;
            } catch (Exception e) {
            }
        }
        return false;
    }
    
    /**
     * @param mBeanInfo the mBeanInfo to set
     */
    public void setmBeanInfo(MBeanInfo mBeanInfo) {
        this.mBeanInfo = mBeanInfo;
    }

    /**
     * @param setters the setters to set
     */
    public void setSetters(Map<String, Method> setters) {
        this.setters = setters;
    }

    /**
     * @param getters the getters to set
     */
    public void setGetters(Map<String, Method> getters) {
        this.getters = getters;
    }

    /**
     * @param operations the operations to set
     */
    public void setOperations(Map<String, Method> operations) {
        this.operations = operations;
    }

    @Override
    public ObjectName preRegister(MBeanServer mbeanserver, ObjectName objectname) throws Exception {
       if (target instanceof MBeanRegistration) {
           return ((MBeanRegistration) target).preRegister(mbeanserver, objectname);
       }
        return objectname;
    }

    @Override
    public void postRegister(Boolean registrationDone) {
        if (target instanceof MBeanRegistration) {
             ((MBeanRegistration) target).postRegister(registrationDone);
        }
        
    }

    @Override
    public void preDeregister() throws Exception {
        if (target instanceof MBeanRegistration) {
            ((MBeanRegistration) target).preDeregister();
       }
        
    }

    @Override
    public void postDeregister() {
        if (target instanceof MBeanRegistration) {
            ((MBeanRegistration) target).postDeregister();
       }
        
    }

   

}
