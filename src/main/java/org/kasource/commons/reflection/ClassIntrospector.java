package org.kasource.commons.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.constructors.ConstructorFilter;
import org.kasource.commons.reflection.filter.fields.FieldFilter;
import org.kasource.commons.reflection.filter.methods.MethodFilter;

/**
 * Class Introspection.
 * 
 * This class can be used to extract information about class meta data
 * such as interfaces, methods and fields.
 * <p>
 * The difference between getFields and getDeclaredField is that getFields
 * returns fields declared by super classes as well as fields declared by
 * the target class. While getDeclaredField only returns fields declared by 
 * the target class.
 * <p>
 * The same pattern is applied for any getXXX and getDeclaredXXX method pairs.
 * <p>
 * ClassFilter, FieldFilter and MethodFilter is used to query (filter) for interfaces, fields 
 * and methods.
 * <p>
 * Example: Finding all getters:
 * {@code
 * ClassIntroSpector introspector = new ClassIntroSpector(MyClass.class);
 * Set<Method> getters = introspector.getMethods(new MethodFilterBuilder().name("get[A-Z].*").or().name("is[A-Z].*").or().name("has[A-Z]*").isPublic().not().returnType(Void.TYPE).numberOfParameters(0).build());
 * }
 * 
 * @author rikardwi
 **/
public class ClassIntrospector<T> {

    private Class<?> target;
    
    /**
     * Constructor.
     * 
     * @param target The target class to introspect.s
     **/
    public ClassIntrospector(Class<T> target) {
        this.target = target;
    }
    
    /**
     * Returns the named method from class <i>clazz</i>.
     * 
     * @param clazz
     *            The class to inspect
     * @param name
     *            The name of the method to get
     * @param params
     *            Parameter types for the method
     * 
     * @throws IllegalArgumentException
     *             if method does not exists or cannot be accessed.
     * 
     * @return Returns the named method from class <i>clazz</i>.
     */
    public Method getDeclaredMethod(String name, Class<?>... params) {
        try {
            return target.getDeclaredMethod(name, params);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not access method: " + name + " on " + target, e);
        }
    }

    /**
     * Returns the methods declared by clazz which matches the supplied
     * method filter.
     * 
     * @param clazz
     *            The class to inspect
     * @param methodFilter
     *            The method filter to apply.
     * 
     * @return methods that match the params argument in their method signature
     * 
     **/
    public  Set<Method> getDeclaredMethods(MethodFilter methodFilter) {
        return getDeclaredMethods(target, methodFilter);
    }
    
    /**
     * Returns the methods declared by clazz which matches the supplied
     * method filter.
     * 
     * @param clazz
     *            The class to inspect
     * @param methodFilter
     *            The method filter to apply.
     * 
     * @return methods that match the params argument in their method signature
     * 
     **/
    private Set<Method> getDeclaredMethods(Class<?> clazz, MethodFilter methodFilter) {
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> matches = new HashSet<Method>();
        for (Method method : methods) {       
            if (methodFilter.passFilter(method)) {
                matches.add(method);
            }
        }
        return matches;
    }

    /**
     * Returns the methods declared by clazz and any of its super classes, which matches the supplied
     * methodFilter.
     * 
     * @param clazz
     *            The class to inspect
     * @param methodFilter
     *            The method filter to apply.
     * 
     * @return methods that match the params argument in their method signature
     * 
     **/
    public  Set<Method> getMethods(MethodFilter methodFilter) {
        Class<?> clazz = target;
        Set<Method> matches = getDeclaredMethods(clazz, methodFilter);      
        while((clazz = clazz.getSuperclass()) != null) {     
            matches.addAll(getDeclaredMethods(clazz, methodFilter));
        }
        return matches;
    }
      
    
    /**
     * Returns a set of interfaces that the from clazz that passes the supplied filter.
     * This method also inspects any interfaces implemented by super classes.
     * 
     * @param clazz             The class to inspect
     * @param filter            The class filter to use.
     * 
     * @return all Interface classes from clazz that passes the filter.
     */
    public  Set<Class<?>> getInterfaces(ClassFilter filter) {
        Class<?> clazz = target;
        Set<Class<?>> interfacesFound = getDeclaredInterfaces(clazz, filter);
        
        while((clazz = clazz.getSuperclass()) != null) {
            interfacesFound.addAll(getDeclaredInterfaces(clazz, filter));
        }
        return interfacesFound;
       
    }

    
    /**
     * Returns a set of interfaces that that passes the supplied filter.
     * 
     * @param filter            The class filter to use.
     * 
     * @return all Interface classes from clazz that passes the filter.
     */
    public Set<Class<?>> getDeclaredInterfaces(ClassFilter filter) {
       return getDeclaredInterfaces(target, filter);
    }
    
    
    /**
     * Returns a set of interfaces that the from clazz that passes the supplied filter.
     * 
     * @param clazz             The class to inspect
     * @param filter            The class filter to use.
     * 
     * @return all Interface classes from clazz that passes the filter.
     */
    private Set<Class<?>> getDeclaredInterfaces(Class<?> clazz, ClassFilter filter) {
        Set<Class<?>> interfacesFound = new HashSet<Class<?>>();

        Class<?>[] interfaces = clazz.getInterfaces();
        for(Class<?> interfaceClass : interfaces) {
            if(filter.passFilter(interfaceClass)) {
                interfacesFound.add(interfaceClass);
            }
        }
        return interfacesFound;
    }
    
    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the target class.
     * 
     * @param filter    Filter to use.
     * 
     * @return All matching fields declared by the target class.
     **/
    public  Set<Field> getDeclaredFields(FieldFilter filter) {
        return getDeclaredFields(target, filter);
    }
    
    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the clazz class.
     * 
     * @param clazz     The class to inspect.
     * @param filter    Filter to use.
     * 
     * @return All matching fields declared by the clazz class.
     **/
    private  Set<Field> getDeclaredFields(Class<?> clazz, FieldFilter filter) {
        Set<Field> fields = new HashSet<Field>();
        Field[] allFields = clazz.getDeclaredFields();
        for(Field field : allFields) {
            if(filter.passFilter(field)) {
                fields.add(field);
            }
        }
        return fields;
    }
    
    /**
     * Returns a set of all fields matching the supplied filter
     * declared in the target class or any of its super classes.
     * 
     * @param filter    Filter to use.
     * 
     * @return All matching fields declared by the target class.
     **/
    public Set<Field> getFields(FieldFilter filter) {
        Class<?> clazz = target;
        Set<Field> fields = getDeclaredFields(clazz, filter);
        while((clazz = clazz.getSuperclass()) != null) {    
            fields.addAll(getDeclaredFields(clazz, filter));
        }
        return fields;
    }
    
    /**
     * Returns set of constructors that matches the filter parameter.
     * 
     * @param filter Filter to apply.
     * 
     * @return constructors that matches the filter parameter.
     **/
    @SuppressWarnings("unchecked")
    public Set<Constructor<T>> getConstructors(ConstructorFilter filter) {
        Set<Constructor<T>> cons = new HashSet<Constructor<T>>();
        Constructor<T>[] constructors = (Constructor<T>[]) target.getConstructors();
        for(Constructor<T> constructor : constructors) {
            if(filter.passFilter(constructor)) {
                cons.add(constructor);
            }
        }
        return cons;
    }

    /**
     * Returns the first constructor found that matches the filter parameter.
     * 
     * @param filter Filter to apply.
     * 
     * @return the first constructor found that matches the filter parameter.
     * @throws IllegalArgumentException if no constructor is found matching the filter.
     **/
    public Constructor<T> getConstructor(ConstructorFilter filter) {
        Set<Constructor<T>> cons = getConstructors(filter);
        if(cons.isEmpty()) {
            throw new IllegalArgumentException("No constructor found mathcing filter");
        }
        return cons.iterator().next();
    }
    
}
