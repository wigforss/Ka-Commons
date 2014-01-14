package org.kasource.commons.jmx.dynamic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.DynamicMBean;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotificationEmitter;

import org.kasource.commons.jmx.annotation.JmxAttribute;
import org.kasource.commons.jmx.annotation.JmxBean;
import org.kasource.commons.jmx.annotation.JmxConstructor;
import org.kasource.commons.jmx.annotation.JmxOperation;
import org.kasource.commons.jmx.annotation.JmxParameter;
import org.kasource.commons.reflection.ClassIntrospector;
import org.kasource.commons.reflection.filter.ConstructorFilterBuilder;
import org.kasource.commons.reflection.filter.MethodFilterBuilder;
import org.kasource.commons.reflection.filter.methods.MethodFilter;

/**
 * Creates a DynamicMBean by reflection and annotation introspection for any object
 * with @JmxBean annotation.
 * 
 * - Creates attributes for getters annotated with @JmxAttribute
 * - Creates operations for methods annotated with @JmxOperation
 * - Creates constructors for constructors with @JmxConstructor
 * 
 * @author rikardwi
 **/
public class AnnotationDynamicMBeanFactory implements DynamicMBeanFactory{
    private static final MethodFilter GETTER_FILTER = new MethodFilterBuilder()
                                                            .isPublic()
                                                            .name("get.*").or().name("is.*")
                                                            .numberOfParameters(0)
                                                            .not().returnType(Void.TYPE)
                                                            .annotated(JmxAttribute.class)
                                                            .build();
    
    /**
     * Returns a DynamicMBean generated for the supplied object
     * 
     * @param object Object to create DynamicMBean for.
     * 
     * @return The DynamicMBean generated for the supplied object.
     * 
     * @throws IllegalStateException if error occurred.
     */
    @Override
    public DynamicMBean getMBeanFor(Object object) throws IllegalStateException {
        JmxBean jmxBean = object.getClass().getAnnotation(JmxBean.class);
        if (jmxBean == null) {
            throw new IllegalArgumentException("Could not find annotation " + JmxBean.class + " on " + object.getClass());
        }
        try {
            ClassIntrospector classIntrospector = new ClassIntrospector(object.getClass());
            DynamicMBeanImpl mbean = new DynamicMBeanImpl(object);
            mbean.setmBeanInfo(getInfo(object, classIntrospector));
            Map<String, Method> getters = new HashMap<String, Method>();
            Map<String, Method> setters = new HashMap<String, Method>();
            resolveGettersAndSetters(classIntrospector, getters, setters);
            mbean.setGetters(getters);
            mbean.setSetters(setters);
            mbean.setOperations(getOperationsMap(classIntrospector));
            return mbean;
        } catch (Exception e) {
           throw new IllegalStateException("Could not create DynamicMBean for " + object, e);
        }
    }
    
    /**
     * Create and return a MBeanInfo instance for the supplied object.
     * 
     * @param object             Supplied object to inspect.
     * @param classIntrospector  ClassIntrospector to use.
     * 
     * @return a MBeanInfo instance for the supplied object.
     * 
     * @throws IntrospectionException    If failed to create Info object
     * @throws IllegalArgumentException
     */
    private MBeanInfo getInfo(Object object, ClassIntrospector classIntrospector) throws IntrospectionException {
        JmxBean jmxBean = object.getClass().getAnnotation(JmxBean.class);
       
        MBeanInfo beanInfo = new MBeanInfo(object.getClass().getName(), 
                                           jmxBean.description(), 
                                           getAttributes(classIntrospector), 
                                           getConstructors(classIntrospector), 
                                           getOperations(classIntrospector), 
                                           getNotifications(object));
        return beanInfo;
    }
    
    /**
     * 
     * @param classIntrospector
     * @return
     * @throws IntrospectionException
     **/
    private MBeanAttributeInfo[] getAttributes(ClassIntrospector classIntrospector) throws IntrospectionException {
       
        Set<Method> methods = classIntrospector.getMethods(GETTER_FILTER);
        Iterator<Method> iter = methods.iterator();
        MBeanAttributeInfo[] attributeInfo = new MBeanAttributeInfo[methods.size()];
        for (int i = 0; i < attributeInfo.length; i++) {
            attributeInfo[i] = getAttribute(classIntrospector, iter.next());
        }
        return attributeInfo;
    }
    
    
    private MBeanAttributeInfo getAttribute(ClassIntrospector classIntrospector, Method getter) throws IntrospectionException {
        String name = getAttributeName(getter);
        JmxAttribute jmxAttribute = getter.getAnnotation(JmxAttribute.class);
        Method setter = null;
        Set<Method> setters = classIntrospector.getMethods(new MethodFilterBuilder()
                                                     .isPublic()
                                                     .name("set" + name)
                                                     .hasSignature(getter.getReturnType())
                                                     .returnType(Void.TYPE)
                                                     .annotated(JmxAttribute.class)
                                                     .build());
       if(!setters.isEmpty()) {
           setter = setters.iterator().next();
       }

       
       return  new MBeanAttributeInfo(name, jmxAttribute.description(), getter, setter);
        
    }
    
    private String getAttributeName(Method getter) {
        String name = getter.getName();
        int index = 2;
        if (name.startsWith("get")) {
            index = 3;
        }
        return name.substring(index);
    }
    
    
    private  MBeanConstructorInfo[] getConstructors(ClassIntrospector classIntrospector) {
        Set<Constructor<?>> constructors = classIntrospector.getConstructors(new ConstructorFilterBuilder()
                                                .isPublic()
                                                .annotated(JmxConstructor.class)
                                                .build());
        Iterator<Constructor<?>> iter = constructors.iterator();
        MBeanConstructorInfo[] constructorsInfo = new MBeanConstructorInfo[constructors.size()];
        for (int i = 0; i < constructorsInfo.length; i++) {
            constructorsInfo[i] = getConstructor(iter.next());
        }
        return constructorsInfo;
    }
    
    private MBeanConstructorInfo getConstructor(Constructor<?> constructor) {
        JmxConstructor jmxConstructor = constructor.getAnnotation(JmxConstructor.class);
        MBeanParameterInfo[] parameterInfo = getParamaterInfo(constructor.getParameterTypes(), constructor.getParameterAnnotations());
        MBeanConstructorInfo constructorInfo 
                            = new MBeanConstructorInfo(constructor.getName(), 
                                        jmxConstructor.description(),
                                        parameterInfo);
        return constructorInfo;
    }
    
   
    
    private MBeanParameterInfo[] getParamaterInfo(Class<?>[] parameters, Annotation[][] parameterAnnotations) {
        MBeanParameterInfo[] parameterInfo = new MBeanParameterInfo[parameterAnnotations.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterInfo[i] = getParamaterInfo(parameters[i], parameterAnnotations[i], i);
        }
        return parameterInfo;
    }
    
    MBeanParameterInfo getParamaterInfo(Class<?> parameter, Annotation[] annotations, int index) {
        String name = "p" + index;
        String description = "";
       
        JmxParameter jmxParameter = getJmxParameter(annotations);
       
        if (jmxParameter != null) {
            if (!jmxParameter.name().isEmpty()) {
              name =jmxParameter.name();
            }
            description = jmxParameter.description();
        } 
        return new MBeanParameterInfo(name, parameter.getName(), description);
    }
    
    private JmxParameter getJmxParameter(Annotation[] annotations) {
        
        for (Annotation annotation : annotations) {
            if (JmxParameter.class.isInstance(annotation)) {
                return (JmxParameter) annotation;
                
            }
        }
        return null;
    }
    
    private MBeanOperationInfo[] getOperations(ClassIntrospector classIntrospector) {
        Set<Method> methods = classIntrospector.getMethods(new MethodFilterBuilder()
                                                                .isPublic()
                                                                .annotated(JmxOperation.class)
                                                                .build());
        Iterator<Method> iter = methods.iterator();
        MBeanOperationInfo[] operationInfo = new MBeanOperationInfo[methods.size()];
        for (int i = 0; i < operationInfo.length; i++) {
            operationInfo[i] = getOperation(iter.next());
        }
        return operationInfo;
    }
    
    private MBeanOperationInfo getOperation(Method method) {
        JmxOperation jmxOperation = method.getAnnotation(JmxOperation.class);
        MBeanOperationInfo operation = new MBeanOperationInfo(method.getName(), 
                                                              jmxOperation.description(), 
                                                              getParamaterInfo(method.getParameterTypes(), method.getParameterAnnotations()), 
                                                              method.getReturnType().getName(), 
                                                              jmxOperation.impact().getImpact());
        return operation;
    }
    
   
    
    private Map<String, Method> getOperationsMap(ClassIntrospector classIntrospector) {
        Map<String, Method> operations = new HashMap<String, Method>();
        Set<Method> methods = classIntrospector.getMethods(new MethodFilterBuilder()
                                            .isPublic()
                                            .annotated(JmxOperation.class)
                                            .build());
        for (Method method : methods) {
            operations.put(method.getName(), method);
        }
        return operations;
    }
    
    private void resolveGettersAndSetters(ClassIntrospector classIntrospector, Map<String, Method> getters, Map<String, Method> setters) {
        Set<Method> methods = classIntrospector.getMethods(GETTER_FILTER);
        for (Method getter : methods) {
            String attributeName = getAttributeName(getter);
            getters.put(attributeName, getter);
            Set<Method> setterCandidates = classIntrospector.getMethods(new MethodFilterBuilder()
                                                                            .isPublic()
                                                                            .name("set" + attributeName)
                                                                            .hasSignature(getter.getReturnType())
                                                                            .returnType(Void.TYPE)
                                                                            .annotated(JmxAttribute.class)
                                                                            .build());
            if (!setterCandidates.isEmpty()) {
                Method setter = setterCandidates.iterator().next();
                setters.put(attributeName, setter);
            }
        }
    }
    
    private MBeanNotificationInfo[] getNotifications(Object target) {
       
        if (NotificationEmitter.class.isAssignableFrom(target.getClass())) {
            return ((NotificationEmitter) target).getNotificationInfo(); 
        } else {
            return new MBeanNotificationInfo[]{};
        }
        
    }

}
