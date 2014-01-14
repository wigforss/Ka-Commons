package org.kasource.commons.jmx.standard;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.kasource.commons.jmx.OperationImpact;
import org.kasource.commons.jmx.annotation.JmxAttribute;
import org.kasource.commons.jmx.annotation.JmxBean;
import org.kasource.commons.jmx.annotation.JmxConstructor;
import org.kasource.commons.jmx.annotation.JmxOperation;
import org.kasource.commons.jmx.annotation.JmxParameter;
import org.kasource.commons.util.reflection.ClassUtils;

public class AnnotatedStandardMBean extends StandardMBean {

   
    protected AnnotatedStandardMBean(Class<?> mbeanInterface) throws NotCompliantMBeanException {
        super(mbeanInterface);
    }
    
    protected AnnotatedStandardMBean(Class<?> mbeanInterface, boolean isMXBean) throws NotCompliantMBeanException {
        super(mbeanInterface, isMXBean);
    }
    

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public AnnotatedStandardMBean(Object implementation,  Class mbeanInterface) throws NotCompliantMBeanException {
        super(implementation, mbeanInterface);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public AnnotatedStandardMBean(Object implementation, Class mbeanInterface, boolean isMXBean) throws NotCompliantMBeanException {
        super(implementation, mbeanInterface);
    }
    
    
    
    @Override
    protected String getDescription(@JmxParameter MBeanInfo mbeaninfo) {
        JmxBean jmxBean =  this.getImplementationClass().getAnnotation(JmxBean.class);
        if (jmxBean != null && !jmxBean.description().isEmpty()) {
           return jmxBean.description();
        } 
        if (mbeaninfo == null) {
            return null;
        } else {
            return mbeaninfo.getDescription();
        }
    }
    
    @Override
    protected String getDescription(MBeanAttributeInfo mBeanattributeInfo) {
        if (mBeanattributeInfo == null) {
            return null;
        } else {
            Method method = getGetter(mBeanattributeInfo);
            if (method != null) {
                JmxAttribute jmxAttribute =  method.getAnnotation(JmxAttribute.class);
                if (jmxAttribute != null && !jmxAttribute.description().isEmpty()) {
                    return jmxAttribute.description();
                }
            }
            return mBeanattributeInfo.getDescription();
        }
    }
    
    @Override
    protected String getDescription(MBeanOperationInfo operation) {
       if (operation == null) {
           return null;
       } else {
           Method method = getOperationsMethod(operation);
            if (method != null) {
                JmxOperation jmxOperation = method.getAnnotation(JmxOperation.class);
                if (jmxOperation != null) {
                    return jmxOperation.description();
                }
            }
            return operation.getDescription();
       }
    }
    
    private Method getOperationsMethod(MBeanOperationInfo operation) {
        try {
            MBeanParameterInfo[] parameterInfo = operation.getSignature();
            Class<?>[] params = new Class<?>[parameterInfo.length];
            for(int i = 0; i < params.length; i++) {
                params[i] = getClass(parameterInfo[i].getType());
            }
            return this.getImplementationClass().getDeclaredMethod(operation.getName(), params);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Class<?> getClass(String className) throws ClassNotFoundException {
        Class<?> clazz = ClassUtils.getPrimitiveClass(className);
        if (clazz == null) {
            clazz =  Class.forName(className);
        }
        return clazz;
    }
    
    private Method getGetter(MBeanAttributeInfo mBeanattributeInfo) {
        try {
            if (mBeanattributeInfo.isIs()) {
                return this.getImplementationClass().getDeclaredMethod("is"+mBeanattributeInfo.getName());
            } else {
                return this.getImplementationClass().getDeclaredMethod("get"+mBeanattributeInfo.getName());
            }
        } catch (Exception e) {
          return null;
        }
       
    }
    
   
    
    @Override
    protected String getParameterName(MBeanOperationInfo operation,
                                      MBeanParameterInfo paramInfo,
                                      int paramNo) {
        if (operation == null) {
            return null;
        } else {
            JmxParameter jmxParameter = getNthJmxParamter(operation, paramNo);
            if (jmxParameter != null && !jmxParameter.name().isEmpty()) {
                return jmxParameter.name();
            }
            return paramInfo.getName();
        }
    }
    
    private JmxParameter getNthJmxParamter(MBeanOperationInfo operation, int paramNo) {
        Method method = getOperationsMethod(operation);
        if (method != null) {
            Annotation[] annotations = method.getParameterAnnotations()[paramNo];
            for (Annotation annotation : annotations) {
                if (JmxParameter.class.isInstance(annotation)) {
                   return (JmxParameter) annotation; 
                }
            }
        }
        return null;
    }
    
    @Override
    protected String getDescription(MBeanOperationInfo operation,
                                    MBeanParameterInfo paramInfo, 
                                    int paramNo) {
        if (operation == null) {
            return null;
        } else {
            JmxParameter jmxParameter = getNthJmxParamter(operation, paramNo);
            if (jmxParameter != null && !jmxParameter.description().isEmpty()) {
                return jmxParameter.description();
            }
            
            return paramInfo.getDescription();
        }
    }

    @Override
    protected int getImpact(MBeanOperationInfo mBeanoperationInfo){
        if(mBeanoperationInfo == null) {
            return MBeanOperationInfo.UNKNOWN;
        } else {
            Method method = getOperationsMethod(mBeanoperationInfo);
            JmxOperation jmxOperation = method.getAnnotation(JmxOperation.class);
            if (jmxOperation != null && jmxOperation.impact() != OperationImpact.UNKNOWN){
                return jmxOperation.impact().getImpact();
            }
            return mBeanoperationInfo.getImpact();
        }
    }
    
    protected String getDescription(MBeanConstructorInfo mBeanConstructorInfo) {
        if(mBeanConstructorInfo == null) {
            return null;
        } else {
            Constructor<?> cons = getConstructor(mBeanConstructorInfo);
            if (cons != null) {
                JmxConstructor jmxConstructor = cons.getAnnotation(JmxConstructor.class);
                if (jmxConstructor != null && !jmxConstructor.description().isEmpty()) {
                    return jmxConstructor.description();
                }
            }
            return mBeanConstructorInfo.getDescription();
        }
    }

    private Constructor<?> getConstructor(MBeanConstructorInfo mBeanConstructorInfo) {
        try {
            MBeanParameterInfo[] parameterInfo = mBeanConstructorInfo.getSignature();
            Class<?>[] params = new Class<?>[parameterInfo.length];
            for(int i = 0; i < params.length; i++) {
                params[i] = getClass(parameterInfo[i].getType());
            }
        return this.getImplementationClass().getConstructor(params);
        } catch (Exception e) {
           return null;
        }
    }
    
    protected String getDescription(MBeanConstructorInfo mBeanConstructorInfo, MBeanParameterInfo mBeanParameterInfo, int index)
    {
        if(mBeanParameterInfo == null) {
            return null;
        } else {
            JmxParameter jmxParameter = getNthJmxParamter(mBeanConstructorInfo, index);
            if (jmxParameter != null && !jmxParameter.description().isEmpty()) {
                return jmxParameter.description();
            }
            
            return mBeanParameterInfo.getDescription();
        } 
    }

    protected String getParameterName(MBeanConstructorInfo mBeanConstructorInfo, MBeanParameterInfo mBeanParameterInfo, int index) {
        if(mBeanParameterInfo == null) {
            return null;
        } else {
            JmxParameter jmxParameter = getNthJmxParamter(mBeanConstructorInfo, index);
            if (jmxParameter != null && !jmxParameter.name().isEmpty()) {
                return jmxParameter.name();
            }
            
            return mBeanParameterInfo.getName();
        } 
    }
    
    private JmxParameter getNthJmxParamter(MBeanConstructorInfo mBeanConstructorInfo, int paramNo) {
        Constructor<?> cons = getConstructor(mBeanConstructorInfo);
        if (cons != null) {
            Annotation[] annotations = cons.getParameterAnnotations()[paramNo];
            for (Annotation annotation : annotations) {
                if (JmxParameter.class.isInstance(annotation)) {
                   return (JmxParameter) annotation; 
                }
            }
        }
        return null;
    }
}
