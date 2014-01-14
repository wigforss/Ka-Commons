package org.kasource.commons.jmx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate MBean Interfaces with this annotation to mark them as available for JMX registration.
 * 
 * Interfaces annotated with this annotation will be considered when 
 * registering a bean with no interface information.
 * 
 * @author rikardwi
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxInterface {

}
