package org.kasource.commons.jmx.dynamic;

import javax.management.DynamicMBean;

public interface DynamicMBeanFactory {
    public DynamicMBean getMBeanFor(Object object);
}
