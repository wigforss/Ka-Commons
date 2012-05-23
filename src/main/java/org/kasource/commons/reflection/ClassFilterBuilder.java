package org.kasource.commons.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kasource.commons.reflection.filter.classes.AnnotationClassFilter;
import org.kasource.commons.reflection.filter.classes.AssignableFromClassFilter;
import org.kasource.commons.reflection.filter.classes.AssignableToClassFilter;
import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.classes.ClassFilterList;
import org.kasource.commons.reflection.filter.classes.IsAnnotationClassFilter;
import org.kasource.commons.reflection.filter.classes.IsAnonymousClassFilter;
import org.kasource.commons.reflection.filter.classes.IsArrayClassFilter;
import org.kasource.commons.reflection.filter.classes.IsEnumClassFilter;
import org.kasource.commons.reflection.filter.classes.IsInterfaceClassFilter;
import org.kasource.commons.reflection.filter.classes.IsLocalClassFilter;
import org.kasource.commons.reflection.filter.classes.IsMemberClassFilter;
import org.kasource.commons.reflection.filter.classes.IsPrimitiveClassFilter;
import org.kasource.commons.reflection.filter.classes.IsSyntheticClassFilter;
import org.kasource.commons.reflection.filter.classes.MetaAnnotatedClassFilter;
import org.kasource.commons.reflection.filter.classes.ModifierClassFilter;
import org.kasource.commons.reflection.filter.classes.NameClassFilter;
import org.kasource.commons.reflection.filter.classes.NegationClassFilter;
import org.kasource.commons.reflection.filter.classes.OrClassFilter;

/**
 * Class Filter Builder.
 * 
 * Builds a ClassFilter by configuring a filter and the invoke the build() method.
 * <p>
 * Supports AND (default) OR and NOT operators.
 * <p>
 * Example:
 * {@code
 * ClassFilter testClasses = new ClassFilterBuilder().isPublic().name("[A-Z]\\w*Test").build();
 * }
 * 
 * @author rikardwi
 **/
public class ClassFilterBuilder {

    private enum Operator {NONE, NOT, OR};
    
    private List<ClassFilter> filters = new ArrayList<ClassFilter>();
    private Operator operator = Operator.NONE;
    
    /**
     * Adds a new filter and applies current operator.
     * 
     * @param filter filter to add.
     **/
    private void add(ClassFilter filter) {
        switch(operator) {
        case NOT:
            filters.add(new NegationClassFilter(filter));
            operator = Operator.NONE;
            break;
        case OR:
            filters.set(filters.size() - 1, 
                        new OrClassFilter(filters.get(filters.size() - 1), filter));
            operator = Operator.NONE;
            break;
        default:
            filters.add(filter);
            break;
        }
    }
    
    /**
     * Sets current operator to NOT.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }
    
    /**
     * Sets current operator to OR.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }
    
    /**
     * Adds filter for interface classes classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isInterface() {
        add(new IsInterfaceClassFilter());
        return this;
    }
    
    /**
     * Adds filter for annotations classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isAnnotation() {
        add(new IsAnnotationClassFilter());
        return this;
    }
    
    /**
     * Adds filter for anonymous classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isAnonymous() {
        add(new IsAnonymousClassFilter());
        return this;
    }
    
    /**
     * Adds filter for array classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isArray() {
        add(new IsArrayClassFilter());
        return this;
    }
    
    /**
     * Adds filter for enum classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isEnum() {
        add(new IsEnumClassFilter());
        return this;
    }
    
    /**
     * Adds filter for local classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isLocal() {
        add(new IsLocalClassFilter());
        return this;
    }
    
    /**
     * Adds filter for member classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isMember() {
        add(new IsMemberClassFilter());
        return this;
    }
    
    /**
     * Adds filter for primitive classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isPrimitive() {
        add(new IsPrimitiveClassFilter());
        return this;
    }
    
    /**
     * Adds filter for synthetic classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isSynthetic() {
        add(new IsSyntheticClassFilter());
        return this;
    }
    
    /**
     * Adds filter for classes that is assignable from a superType.
     * 
     * extendsType(java.lang.Number) will be true for java.lang.Integer, since java.lang.Integer is extends java.lang.Number.
     * 
     * @param superType the class any matching classes should extend or implement.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder extendsType(Class<?> superType) {
        add(new AssignableFromClassFilter(superType));
        return this;
    }
    
    /**
     * Adds filter for classes is assignable to an extendedType.
     * 
     * superType(java.lang.Integer) will be true for java.lang.Number, since java.lang.Number is a superType of java.lang.Integer. 
     * 
     * @param extendedType The class that any matching should be a super class of.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder superType(Class<?> extendedType) {
        add(new AssignableToClassFilter(extendedType));
        return this;
    }
    
    /**
     * Adds filter for classes annotated with the supplied annotation.
     * 
     * @param annotation The annotation to be present on any matching class.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotationClassFilter(annotation));
        return this;
    }
    
    /**
     * Adds filter for classes annotated with an annotation which is annotated with the supplied annotation.
     * 
     * Think springs stereotype annotations: @Service and @Repository is annotated with @Component. Thus 
     * I can find any classes @Component classes even though they are actually annotated with Service or 
     * Repository with a metaAnnotated(org.springframework.stereotype.Component) filter.
     * 
     * @param annotation The meta annotation to find.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder metaAnnotated(Class<? extends Annotation> annotation) {
        add(new MetaAnnotatedClassFilter(annotation));
        return this;
    }
    
    /**
     * Adds filter for classes which fully qualified names matches the supplied regularExpression.
     * 
     * @param nameRegExp The regular expression to match fully qualified name with.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder name(String nameRegExp) {
        add(new NameClassFilter(nameRegExp));
        return this;
    }
    
    /**
     * Adds filter for public classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isPublic() {
        add(new ModifierClassFilter(Modifier.PUBLIC));
        return this;
    }
    
    /**
     * Adds filter for protected classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isProtected() {
        add(new ModifierClassFilter(Modifier.PROTECTED));
        return this;
    }
    
    /**
     * Adds filter for private classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isPrivate() {
        add(new ModifierClassFilter(Modifier.PRIVATE));
        return this;
    }
    
    /**
     * Adds filter for default access classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isDefault() {
        add(new NegationClassFilter(new ModifierClassFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }
    
    /**
     * Adds filter for classes by modifiers.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder byModifiers(int modifiers) {
        add(new ModifierClassFilter(modifiers));
        return this;
    }
    
    /**
     * Adds filter for final classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isFinal() {
        add(new ModifierClassFilter(Modifier.FINAL));
        return this;
    }
    
    /**
     * Adds filter for abstract classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isAbstract() {
        add(new ModifierClassFilter(Modifier.ABSTRACT));
        return this;
    }
    
    /**
     * Adds filter for static classes.
     * 
     * @return This builder for method chaining.
     **/
    public ClassFilterBuilder isStatic() {
        add(new ModifierClassFilter(Modifier.STATIC));
        return this;
    }
    
    /**
     * Builds the filter and returns the result.
     * 
     * @return the ClassFilter built.
     * 
     * @throws IllegalStateException if no filter has been added.
     **/
    public ClassFilter build() throws IllegalStateException {
        if(filters.isEmpty()) {
            throw new IllegalStateException("No filters set.");
        }
        if(filters.size() == 1) {
            return filters.get(0);
        }
        ClassFilter[] classFilters = new ClassFilter[filters.size()];
        filters.toArray(classFilters);
        return new ClassFilterList(classFilters);
    }
}
