package org.kasource.commons.jmx.listener;


import java.lang.reflect.Method;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message Listener that invokes the registered method on the target object wuth reflection.
 * 
 * @author rikardwi
 **/
public class MethodNotificationListener implements NotificationListener {
    private static final Logger LOG = LoggerFactory.getLogger(MethodNotificationListener.class);
    private Method method;
    private Object target;
    
    /**
     * Constructor.
     * 
     * @param method  Method to invoke.
     * @param target  Object to invoke method on.
     */
    public MethodNotificationListener(Method method, Object target) {
        this.method = method;
        this.target = target;
    }
    
    /**
     * Invokes the registered method on the target object.
     */
    @Override
    public void handleNotification(Notification notification, Object handback) {
       try {
           method.invoke(target, notification, handback);
       } catch (Exception e) {
           LOG.error("Could not invoke MessageListener method " + method + " on " + target, e);
       }
        
    }

}
