package org.kasource.commons.reflection.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.fields.AnnotatedFieldFilter;
import org.kasource.commons.reflection.filter.fields.AssignableFromFieldFilter;
import org.kasource.commons.reflection.filter.fields.AssignableToFieldFilter;
import org.kasource.commons.reflection.filter.fields.FieldClassFieldFilter;
import org.kasource.commons.reflection.filter.fields.FieldFilter;
import org.kasource.commons.reflection.filter.fields.FieldFilterList;
import org.kasource.commons.reflection.filter.fields.IsEnumConstantFieldFilter;
import org.kasource.commons.reflection.filter.fields.MetaAnnotatedFieldFilter;
import org.kasource.commons.reflection.filter.fields.ModifierFieldFilter;
import org.kasource.commons.reflection.filter.fields.NameFieldFilter;
import org.kasource.commons.reflection.filter.fields.NegationFieldFilter;
import org.kasource.commons.reflection.filter.fields.OrFieldFilter;

/**
 * Builder for FieldFilter.
 * <p>
 * This class offers functionality for building more complex
 * filter compositions in an expressive manner. The filters added will be
 * evaluated with AND operator. This builder also allows both NOT and OR as
 * additional operators.
 * <p>
 * Examples:
 * {@code
 *  FieldFilter publicResourceInjectionTargets = new FieldFilterBuilder().isPublic().annotated(javax.annotation.Resource.class).build();
 *  
 * }
 * 
 * @author rikardwi
 **/
public class FieldFilterBuilder {
    private enum Operator {NONE, NOT, OR};
    private List<FieldFilter> filters = new ArrayList<FieldFilter>();
    private Operator operator = Operator.NONE;
    
    /**
     * Adds a filter and applies the current operator.
     * 
     * @param filter Filter to add.
     **/
    private void add(FieldFilter filter) {
        switch(operator) {
        case NOT:
            filters.add(new NegationFieldFilter(filter));
            operator = Operator.NONE;
            break;
        case OR:
            filters.set(filters.size() - 1, 
                        new OrFieldFilter(filters.get(filters.size() - 1), filter));
            operator = Operator.NONE;
            break;
        default:
            filters.add(filter);
            break;
        }
    }
    
    /**
     * Sets the current operator to NOT.
     * 
     * @return  This builder to support method chaining.
     **/
    public FieldFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }
    
    /**
     * Sets the current operator to OR.
     * 
     * @return  This builder to support method chaining.
     **/
    public FieldFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }
    
    /**
     * Filter for fields which name matches to supplied regular expression.
     * 
     * @param nameRegExp Regular expression to match field name with.
     * 
     * @return  This builder to support method chaining.
     **/
    public FieldFilterBuilder name(String nameRegExp) {
        add(new NameFieldFilter(nameRegExp));
        return this;
    }
    
    /**
     * Filter fields that is annotated with the supplied annotation.
     * 
     * @param annotation The annotation to be present on filtered fields.
     * 
     * @return This builder to support method chaining.
     **/
    public FieldFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotatedFieldFilter(annotation));
        return this;
    }
    
    /**
     * Adds filter for fields annotated with an annotation which is annotated with the supplied annotation.
     * 
     * Think springs stereotype annotations: @Service and @Repository is annotated with @Component. Thus 
     * I can find any @Component classes (by looking for the meta annotation Component) even though they 
     * are actually annotated with Service or Repository with a metaAnnotated(org.springframework.stereotype.Component) filter.
     * 
     * @param annotation The meta annotation to find.
     * 
     * @return This builder to support method chaining.
     **/
    public FieldFilterBuilder metaAnnotated(Class<? extends Annotation> annotation) {
        add(new MetaAnnotatedFieldFilter(annotation));
        return this;
    }
    
    /**
     * Adds filter for fields which type is assignable from a superType.
     * 
     * extendsType(java.lang.Number) will be true for fields of type java.lang.Integer, since java.lang.Integer  extends java.lang.Number.
     * 
     * @param superType the class any matching classes should extend or implement.
     * 
     * @return This builder to support method chaining.
     **/
    public FieldFilterBuilder extendsType(Class<?> superType) {
        add(new AssignableFromFieldFilter(superType));
        return this;
    }
    
    /**
     * Adds filter for fields which type is assignable to an extendedType.
     * 
     * superType(java.lang.Integer) will be true for a fields of type java.lang.Number, since java.lang.Number is a superType of java.lang.Integer. 
     * 
     * @param extendedType The class that any matching should be a super class of.
     * 
     * @return This builder to support method chaining.
     **/
    public FieldFilterBuilder superType(Class<?> extendedType) {
        add(new AssignableToFieldFilter(extendedType));
        return this;
    }
    
    /**
     * Filter fields which type passes the supplied ClassFilter.
     * 
     * @param filter The class filter to apply of candidate fields type.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder typeFilter(ClassFilter filter) {
        add(new FieldClassFieldFilter(filter));
        return this;
    }
    
    /**
     * Filter fields which are enum constants.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isEnumConstant() {
        add(new IsEnumConstantFieldFilter());
        return this;
    }
    
    /**
     * Filter public fields.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isPublic() {
        add(new ModifierFieldFilter(Modifier.PUBLIC));
        return this;
    }
    
    /**
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isProtected() {
        add(new ModifierFieldFilter(Modifier.PROTECTED));
        return this;
    }
    
    /**
     * Filter private fields.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isPrivate() {
        add(new ModifierFieldFilter(Modifier.PRIVATE));
        return this;
    }
    
    /**
     * Filter static fields.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isStatic() {
        add(new ModifierFieldFilter(Modifier.STATIC));
        return this;
    }
    
    /**
     * Filter transient fields.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isTransient() {
        add(new ModifierFieldFilter(Modifier.TRANSIENT));
        return this;
    }
    
    /**
     * Filter final fields.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isFinal() {
        add(new ModifierFieldFilter(Modifier.FINAL));
        return this;
    }
    
    /**
     * Filter volatile fields.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isVolatile() {
        add(new ModifierFieldFilter(Modifier.VOLATILE));
        return this;
    }
    
    /**
     * Filter fields with default access.
     * 
     * @return This builder to support method chaining.
     */
    public FieldFilterBuilder isDefault() {
        add(new NegationFieldFilter(new ModifierFieldFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }
    
    /**
     * Filter fields that has the supplied modifiers.
     * 
     * @param modifiers Modifiers to match on candidate field.
     * 
     * @return This builder to support method chaining.
     **/
    public FieldFilterBuilder byModifiers(int modifiers) {
        add(new ModifierFieldFilter(modifiers));
        return this;
    }
    
    /**
     * Build and returns a FieldFilter.
     * 
     * @return FieldFilter built.
     * 
     * @throws IllegalStateException if no filter was added before build() was invoked.
     **/
    public FieldFilter build() throws IllegalStateException {
        if (filters.isEmpty()) {
            throw new IllegalStateException("No field filters configured!");
        }
        if (filters.size() == 1) {
           return filters.get(0); 
        }
        FieldFilter[] fieldFilters = new FieldFilter[filters.size()];
        filters.toArray(fieldFilters);
        return new FieldFilterList(fieldFilters);
    }
}
