package org.kasource.commons.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;


/**
 * Class for scanning classpath after annotated classes.
 * 
 * @author rikardwi
 **/
public class AnnotationScanner {
    
    
    /**
     * Scan classpath for packages matching scanPath for classes annotated with the supplied annotationClass that
     * extends or implements the supplied ofType.
     * 
     * @param <T>               Type of class to find
     * @param scanPath          Comma separated list of packages to scan for classes.
     * @param annotationClass   The annotation to find.
     * @param ofType            The type the found classes should extend or implement, set to Object if no validation is needed.
     * 
     * @return All classes found by scanning the classapth that extends or implements the ofType type.
     * 
     * @throws IOException      If exception occurred while accessing class path.
     * @throws IllegalStateException if class found does not extends of implement the ofType parameter or the class found could not be loaded.
     **/
    public <T> Set<Class<? extends T>> findAnotatedClasses(String scanPath, Class<? extends Annotation> annotationClass, Class<T> ofType) throws IOException {
        Set<Class<? extends T>> classes = new HashSet<Class<? extends T>>(); 
        if (scanPath.contains(".")) {
            scanPath = scanPath.replace('.', '/');
        }     
        String[] scanPaths = scanPath.split(",");
        
        String includeRegExp = buildIncludeRegExp(scanPaths);
        Set<URL> urls = resolverUrls(scanPaths);
         
        Map<String, Set<String>> annotationIndex = scanForAnnotatedClasses(urls);
        Set<String> classNames = annotationIndex.get(annotationClass.getName());
        if (classNames != null) {
            for (String className : classNames) {
                addClass(classes, includeRegExp, className, ofType, annotationClass);
            }
        }

        return classes;

    }

    
    /**
     * Created and adds the event to the eventsFound set, if its package matches the includeRegExp.
     * 
     * @param eventBuilderFactory      Event Factory used to create the EventConfig instance with.
     * @param eventsFound       Set of events found, to add the newly created EventConfig to.
     * @param includeRegExp     Regular expression to test eventClassName with.
     * @param eventClassName    Name of the class.
     **/
    @SuppressWarnings("unchecked")
    private <T> void addClass(Set<Class<? extends T>> classes, 
                         String includeRegExp,
                         String className,
                         Class<T> ofType,
                         Class<? extends Annotation> annotationClass) {
        if (className.matches(includeRegExp)) {
        try {
            Class<?> matchingClass = Class.forName(className);
            matchingClass.asSubclass(ofType);
           
            classes.add((Class<T>) matchingClass);
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalStateException("Scannotation found a class that does not exist " + className + " !", cnfe);
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Class " + className
                    + " is annoted with @"+annotationClass+" but does not extend or implement "+ofType);
        }
        }
    }
    
    
    /**
     * Returns the URLs which has classes from scanPaths.
     * 
     * @param scanPaths Comma separated list of package names.
     * 
     * @return URLs matching classes in scanPaths.
     **/
    private Set<URL> resolverUrls(String[] scanPaths) {
       
        Set<URL> urls = new HashSet<URL>();
        for (String path : scanPaths) {
            URL[] urlsFromPath = ClasspathUrlFinder.findResourceBases(path.trim());
            urls.addAll(Arrays.asList(urlsFromPath));
            
        }
        
        return urls;
    }
    
    /**
     * Returns a regular expression of acceptable package names. 
     * 
     * @param scanPaths  Comma separated list of package names.
     * @return a regular expression of acceptable package names. 
     **/
    private String buildIncludeRegExp(String[] scanPaths) {
        String includeRegExp = "";
        for (String path : scanPaths) {
            includeRegExp += path.trim().replace("/", "\\.") + ".*|";
        }
        if (scanPaths.length > 0) {
            // Remove last |
            includeRegExp = includeRegExp.substring(0, includeRegExp.length() - 1);
        }
        return includeRegExp;
    }
    
    /**
     * Scans for classes in URLs.
     * 
     * @param urls URLs to scan for annotated classes.
     * 
     * @return Map of annotated classes found.
     * @throws IOException If exception occurs.
     **/
    private Map<String, Set<String>> scanForAnnotatedClasses(Set<URL> urls) throws IOException {
        AnnotationDB db = new AnnotationDB();
        db.setScanClassAnnotations(true);
        db.setScanFieldAnnotations(false);
        db.setScanMethodAnnotations(false);
        db.setScanParameterAnnotations(false);
      
        db.scanArchives(urls.toArray(new URL[urls.size()]));
        return db.getAnnotationIndex();
        
    }
    
    
}
