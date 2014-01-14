package org.kasource.commons.jmx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate Constructors with this annotation to expose it to JMX.
 * 
 * @author rikardwi
 **/
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxConstructor {
    
    /**
     * Returns the description of the constructor.
     * 
     * @return the description of the constructor.
     **/
    String description() default "";
}
