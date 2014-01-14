package org.kasource.commons.jmx.registration;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.Set;

import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MXBean;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.kasource.commons.jmx.EmptyInterface;
import org.kasource.commons.jmx.annotation.JmxBean;
import org.kasource.commons.jmx.annotation.JmxInterface;
import org.kasource.commons.jmx.annotation.JmxNotificationFilter;
import org.kasource.commons.jmx.annotation.OnJmxNotification;
import org.kasource.commons.jmx.dynamic.AnnotationDynamicMBeanFactory;
import org.kasource.commons.jmx.dynamic.DynamicMBeanFactory;
import org.kasource.commons.jmx.listener.MethodNotificationListener;
import org.kasource.commons.jmx.standard.AnnotatedStandardMBean;
import org.kasource.commons.reflection.ClassIntrospector;
import org.kasource.commons.reflection.filter.ClassFilterBuilder;
import org.kasource.commons.reflection.filter.MethodFilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MBeanRegistratorImpl implements MBeanRegistrator {
    private static final Logger LOG = LoggerFactory.getLogger(MBeanRegistratorImpl.class); 
    private MBeanServerLookup serverLookup;
    private DynamicMBeanFactory dynamicBeanFactory = new AnnotationDynamicMBeanFactory();
    private MBeanServer mBeanServer;
    
    public void initialize() {
        if (serverLookup != null) {
            try {
                mBeanServer = serverLookup.getMBeanServer();
            } catch (Exception e) {
               LOG.warn("MBean Server Lookup with " + serverLookup + " failed, falling back to default", e);
            }
        }
        // Falling back to default
        if (mBeanServer == null) {
            mBeanServer =  ManagementFactory.getPlatformMBeanServer();
        }
        
    }
    
    @Override
    public void register(Object object) throws IllegalArgumentException {
        JmxBean jmxBean = object.getClass().getAnnotation(JmxBean.class);
        if (jmxBean == null) {
            throw new IllegalArgumentException(object.getClass() + " must be annotated with " + JmxBean.class.getName() + " to be registered.");
        }
        ObjectName name;
        try {
            name = new ObjectName(jmxBean.value());
        } catch (Exception e) {
           throw new IllegalArgumentException("Value of the annotation attribute value " + jmxBean.value() + " is not a valid JMX ObjectName for annotation " +  JmxBean.class.getName());
        } 
        try {
            Object mbean = toMBean(object);
            mBeanServer.registerMBean(mbean, name);
           registerListener(object);
        } catch (InstanceAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
                
    }
    
    private void registerListener(Object object) throws InstanceNotFoundException, IllegalStateException {
        NotificationListener listener = getListener(object);
        if (listener != null) {
            NotificationFilter filter = getFilter(object);
            ObjectName objectName = null;
            try {
                objectName = getFilterObjectName(object);
            } catch (MalformedObjectNameException e) {
                throw new IllegalStateException("Invalid ObjectName pattern set as value on " + OnJmxNotification.class + " for " + object);
            }
            mBeanServer.addNotificationListener(objectName, listener, filter, null);
        }
    }
    
    private NotificationListener getListener(Object object) {
        if (NotificationListener.class.isAssignableFrom(object.getClass())) {
            return (NotificationListener) object;
        }
        
        ClassIntrospector classIntrospector = new ClassIntrospector(object.getClass());
        Method listenerMethod = classIntrospector.getMethod(new MethodFilterBuilder()
                                                                    .isPublic()
                                                                    .annotated(OnJmxNotification.class)
                                                                    .hasSignature(Notification.class, Object.class)
                                                                    .build());
        if (listenerMethod != null) {
            return new MethodNotificationListener(listenerMethod, object);
        } else {
            return null;
        }
    }
    
    private ObjectName getFilterObjectName(Object object) throws MalformedObjectNameException {
        ObjectName filterObjectName = ObjectName.getInstance("*:*");
        ClassIntrospector classIntrospector = new ClassIntrospector(object.getClass());
        Method listenerMethod = classIntrospector.getMethod(new MethodFilterBuilder()
                                                                    .isPublic()
                                                                    .annotated(OnJmxNotification.class)
                                                                    .hasSignature(Notification.class, Object.class)
                                                                    .build());
        if (listenerMethod != null) {
            OnJmxNotification onJmxNotification = listenerMethod.getAnnotation(OnJmxNotification.class);
            filterObjectName = ObjectName.getInstance(onJmxNotification.value());
        }
        return filterObjectName;
    }
    
    private NotificationFilter getFilter(Object object) {
        ClassIntrospector classIntrospector = new ClassIntrospector(object.getClass());
        Method filterMethod = classIntrospector.getMethod(new MethodFilterBuilder()
                                                                    .annotated(JmxNotificationFilter.class)
                                                                    .numberOfParameters(0)
                                                                    .returnTypeExtends(NotificationFilter.class)
                                                                    .build());
        if (filterMethod != null) {
            filterMethod.setAccessible(true);
            try {
                return (NotificationFilter) filterMethod.invoke(object);
            } catch (Exception e) {
               LOG.error("Could not get NotificationFilter from " + filterMethod);
            } 
        }
        return null;
        
    }
    
  
    
    private boolean implementsInterface(Object object, Class<?> interfaceToImplement) {
        ClassIntrospector classIntrospector = new ClassIntrospector(object.getClass());
        Set<Class<?>> mBeanInterface = classIntrospector.getInterfaces(new ClassFilterBuilder().extendsType(interfaceToImplement).build());
        return !mBeanInterface.isEmpty();
    }
    
   
    
    /**
     * Returns either a AnnotatedStandardMBean or a DynamicMBean implementation.
     * 
     * @param object  Object to create MBean for.
     * 
     * @return either a AnnotatedStandardMBean or a DynamicMBean implementation.
     * 
     * @throws IllegalArgumentException
     * @throws NotCompliantMBeanException
     **/
    private Object toMBean(Object object) throws IllegalArgumentException, NotCompliantMBeanException {
        JmxBean jmxBean = object.getClass().getAnnotation(JmxBean.class);
        if (DynamicMBean.class.isAssignableFrom(object.getClass())) {
            return object;
        }
        ClassIntrospector classIntrospector = new ClassIntrospector(object.getClass());
        Class<?> mBeanInterface = classIntrospector.getInterface(new ClassFilterBuilder().name(object.getClass().getName().replace(".", "\\.") + "MBean").build());
        if (mBeanInterface != null) {
            return new AnnotatedStandardMBean(object, mBeanInterface);
        }
        Class<?> mxBeanInterface = classIntrospector.getInterface(new ClassFilterBuilder().name(".*MXBean").or().annotated(MXBean.class).build());
        if (mxBeanInterface!= null) {
            return new AnnotatedStandardMBean(object, mxBeanInterface, true);
        }
        // Interface set on JmxBean annotation
        if (!jmxBean.managedInterface().equals(EmptyInterface.class)) {
            if (implementsInterface(object, jmxBean.managedInterface())) {
                return new AnnotatedStandardMBean(object, jmxBean.managedInterface());
            } else {
                throw new IllegalArgumentException(JmxBean.class + " attribute managedInterface is set to " + jmxBean.managedInterface() + " but " + object.getClass() + " does not implement that interface"); 
            }
        }
        // Search for an implemented interface annotated with JmxInterface
        Class<?> annotatedInterface = classIntrospector.getInterface(new ClassFilterBuilder().annotated(JmxInterface.class).build());
        if (annotatedInterface != null) {
            return new AnnotatedStandardMBean(object, jmxBean.managedInterface());
        }
        // Create DynamicMBean by using reflection and inspecting annotations
        return dynamicBeanFactory.getMBeanFor(object);         
    }
   
}
