package org.kasource.commons.jmx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate parameters of a JmxOperation annotated method or
 * a JmxConstructor annotated constructor in order to provide
 * name and description of the parameter.
 * 
 * @author rikardwi
 **/
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxParameter {
    
    /**
     * Return the name of the parameter.
     * 
     * @return name of the parameter.
     */
    String name() default "";
    
    /**
     * Return the description of the parameter.
     * 
     * @return the description of the parameter.
     **/
    String description() default "";
}
