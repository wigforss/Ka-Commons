package org.kasource.commons.jmx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with the same signature as
 * javax.management.NotificationListener.handleNotification() to enable
 * notification listening.
 * 
 * @author rikardwi
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnJmxNotification {
    /**
     * Returns the ObjectName pattern of sources to listen to notifications from.
     * 
     * Defaults to *:*, which means all MBeans in all domains.
     *  
     * @return the ObjectName pattern of sources to listen to notifications from
     **/
    String value() default "*:*";
}
