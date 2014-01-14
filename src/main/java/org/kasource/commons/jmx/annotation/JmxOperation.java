package org.kasource.commons.jmx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kasource.commons.jmx.OperationImpact;

/**
 * Annotate methods with this annotation to expose them as JMX Operations.
 * 
 * @author rikardwi
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxOperation {
    
    /**
     * Returns the description of the operation.
     * 
     * @return the description of the operation.
     **/
    String description() default "";
    
    /**
     * Returns the system impact of invoking the operation.
     * 
     * @return system impact of invoking the operation.
     **/
    OperationImpact impact() default OperationImpact.UNKNOWN;
}
