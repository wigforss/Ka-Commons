package org.kasource.commons.reflection;

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
    
    public FieldFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotatedFieldFilter(annotation));
        return this;
    }
    
    
    public FieldFilterBuilder metaAnnotated(Class<? extends Annotation> annotation) {
        add(new MetaAnnotatedFieldFilter(annotation));
        return this;
    }
    
    public FieldFilterBuilder extendsType(Class<?> superType) {
        add(new AssignableFromFieldFilter(superType));
        return this;
    }
    
    
    public FieldFilterBuilder superType(Class<?> baseType) {
        add(new AssignableToFieldFilter(baseType));
        return this;
    }
    
    public FieldFilterBuilder typeFilter(ClassFilter filter) {
        add(new FieldClassFieldFilter(filter));
        return this;
    }
    
    public FieldFilterBuilder isEnumConstant() {
        add(new IsEnumConstantFieldFilter());
        return this;
    }
    
    public FieldFilterBuilder isPublic() {
        add(new ModifierFieldFilter(Modifier.PUBLIC));
        return this;
    }
    
    public FieldFilterBuilder isProtected() {
        add(new ModifierFieldFilter(Modifier.PROTECTED));
        return this;
    }
    
    public FieldFilterBuilder isPrivate() {
        add(new ModifierFieldFilter(Modifier.PRIVATE));
        return this;
    }
    
    public FieldFilterBuilder isStatic() {
        add(new ModifierFieldFilter(Modifier.STATIC));
        return this;
    }
    
    public FieldFilterBuilder isTransient() {
        add(new ModifierFieldFilter(Modifier.TRANSIENT));
        return this;
    }
    
    public FieldFilterBuilder isFinal() {
        add(new ModifierFieldFilter(Modifier.FINAL));
        return this;
    }
    
    public FieldFilterBuilder isVolatile() {
        add(new ModifierFieldFilter(Modifier.VOLATILE));
        return this;
    }
    
    public FieldFilterBuilder isDefault() {
        add(new NegationFieldFilter(new ModifierFieldFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }
    
    public FieldFilterBuilder byModifiers(int modifiers) {
        add(new ModifierFieldFilter(modifiers));
        return this;
    }
    
    
    public FieldFilter build() {
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
