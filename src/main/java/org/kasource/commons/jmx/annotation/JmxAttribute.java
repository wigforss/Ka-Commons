package org.kasource.commons.jmx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate getters and setters with this annotation to expose its
 * value as a JMX Attribute.
 * 
 * If only the getter is annotated the exposed JMX Attribute will be read-only.
 * 
 * @author rikardwi
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxAttribute {
    
    /**
     * Returns the description of the JMX Attribute
     *  
     * @return the description of the JMX Attribute
     **/
    String description() default "";
   
}
