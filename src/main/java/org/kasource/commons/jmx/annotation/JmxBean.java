package org.kasource.commons.jmx.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kasource.commons.jmx.EmptyInterface;

/**
 * Annotate a class with this annotation to enable registration
 * of its instances with the MBean Server.
 * 
 * @author rikardwi
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxBean {
    
    /**
     * Returns the JMX ObjectName of the MBean.
     * 
     * @return JMX ObjectName of the MBean.
     **/
    String value();
    
    /**
     * Returns the description of the MBean.
     * 
     * @return the description of the MBean.
     **/
    String description() default "";
    
    /**
     * Returns the managed Interface to expose.
     * 
     * Defaults to EmptyInterface which indicates no
     * interface should be used. Managed interfaces may also be 
     * resolved by annotating the interface class itself with @JmxInterface.
     * 
     * @return Managed Interface to expose.
     **/
    Class<?> managedInterface() default EmptyInterface.class;
}
