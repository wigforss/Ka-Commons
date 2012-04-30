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

public class ClassFilterBuilder {

    private enum Operator {NONE, NOT, OR};
    
    private List<ClassFilter> filters = new ArrayList<ClassFilter>();
    private Operator operator = Operator.NONE;
    
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
    
    public ClassFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }
    
    public ClassFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }
    
    public ClassFilterBuilder isInterface() {
        add(new IsInterfaceClassFilter());
        return this;
    }
    
    public ClassFilterBuilder isAnnotation() {
        add(new IsAnnotationClassFilter());
        return this;
    }
    
    public ClassFilterBuilder isAnonymous() {
        add(new IsAnonymousClassFilter());
        return this;
    }
    
    public ClassFilterBuilder isArray() {
        add(new IsArrayClassFilter());
        return this;
    }
    
    public ClassFilterBuilder isEnum() {
        add(new IsEnumClassFilter());
        return this;
    }
    
    public ClassFilterBuilder isLocal() {
        add(new IsLocalClassFilter());
        return this;
    }
    
    public ClassFilterBuilder isMember() {
        add(new IsMemberClassFilter());
        return this;
    }
    
    public ClassFilterBuilder isPrimitive() {
        add(new IsPrimitiveClassFilter());
        return this;
    }
    
    public ClassFilterBuilder isSynthetic() {
        add(new IsSyntheticClassFilter());
        return this;
    }
    
    public ClassFilterBuilder extendsType(Class<?> superType) {
        add(new AssignableFromClassFilter(superType));
        return this;
    }
    
    public ClassFilterBuilder superType(Class<?> baseType) {
        add(new AssignableToClassFilter(baseType));
        return this;
    }
    
    public ClassFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotationClassFilter(annotation));
        return this;
    }
    
    public ClassFilterBuilder metaAnnotated(Class<? extends Annotation> annotation) {
        add(new MetaAnnotatedClassFilter(annotation));
        return this;
    }
    
    public ClassFilterBuilder name(String nameRegExp) {
        add(new NameClassFilter(nameRegExp));
        return this;
    }
    
    public ClassFilterBuilder isPublic() {
        add(new ModifierClassFilter(Modifier.PUBLIC));
        return this;
    }
    
    public ClassFilterBuilder isProtected() {
        add(new ModifierClassFilter(Modifier.PROTECTED));
        return this;
    }
    
    public ClassFilterBuilder isPrivate() {
        add(new ModifierClassFilter(Modifier.PRIVATE));
        return this;
    }
    
    public ClassFilterBuilder isDefault() {
        add(new NegationClassFilter(new ModifierClassFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }
    
    public ClassFilterBuilder byModifiers(int modifiers) {
        add(new ModifierClassFilter(modifiers));
        return this;
    }
    
    public ClassFilterBuilder isFinal() {
        add(new ModifierClassFilter(Modifier.FINAL));
        return this;
    }
    
    public ClassFilterBuilder isAbstract() {
        add(new ModifierClassFilter(Modifier.ABSTRACT));
        return this;
    }
    
    public ClassFilterBuilder isStatic() {
        add(new ModifierClassFilter(Modifier.STATIC));
        return this;
    }
    
    
    public ClassFilter build() {
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
