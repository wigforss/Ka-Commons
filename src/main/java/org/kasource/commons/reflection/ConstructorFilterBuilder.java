package org.kasource.commons.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kasource.commons.reflection.filter.classes.ClassFilter;
import org.kasource.commons.reflection.filter.constructors.AnnotatedConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.AssignableFromConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.AssignableToConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.ConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.ConstructorFilterList;
import org.kasource.commons.reflection.filter.constructors.MetaAnnotatedConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.ModifierConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.NegationConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.NumberOfParametersConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.OrConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.ParameterClassFilterConstructorFilter;
import org.kasource.commons.reflection.filter.constructors.SignatureConstructorFilter;


/**
 * Builder for ConstructorFilters.
 * <p>
 * This class offers functionality for building more complex
 * filter compositions in an expressive manner. The filters added will be
 * evaluated with AND operator. This builder allows both NOT and OR as
 * additional operators.
 * <p>
 * Examples:
 * {@code
 *  ConstructorFilter publicConstructors = new ConstructorFilterBuilder().isPublic().build();
 *  ConstructorFilter nonPublicfilter = new ConstructorFilterBuilder().not().isPublic().build();
 *  ConstructorFilter privateOrProtected = new ConstructorFilterBuilder().isPrivate().or().isProtected().build();
 * }
 * 
 * @author rikardwi
 **/
public class ConstructorFilterBuilder {
   
    
    private enum Operator {NONE, NOT, OR};
    private List<ConstructorFilter> filters = new ArrayList<ConstructorFilter>();
    private Operator operator = Operator.NONE;
    
    /**
     * Adds filter to filter list and apply operator.
     * 
     * @param filter filter to add.
     **/
    private void add(ConstructorFilter filter) {
        switch(operator) {
        case NOT:
            filters.add(new NegationConstructorFilter(filter));
            operator = Operator.NONE;
            break;
        case OR:
            filters.set(filters.size() - 1, 
                        new OrConstructorFilter(filters.get(filters.size() - 1), filter));
            operator = Operator.NONE;
            break;
        default:
            filters.add(filter);
            break;
        }
    }
    
    /**
     * Set current operator to NOT, which will be applied
     * on the next filter added.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder not() {
        operator = Operator.NOT;
        return this;
    }
    
    /**
     * Set current operator to OR, which will be applied
     * on the next filter added, by applying OR to the last filter
     * added and the next.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder or() {
        operator = Operator.OR;
        return this;
    }
    
    /**
     * Adds a filter for public constructors only.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder isPublic() {
        add(new ModifierConstructorFilter(Modifier.PUBLIC));
        return this;
    }
    
    /**
     * Adds a filter for protected constructors only.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder isProtected() {
        add(new ModifierConstructorFilter(Modifier.PROTECTED));
        return this;
    }
    
    /**
     * Adds a filter for private constructors only.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder isPrivate() {
        add(new ModifierConstructorFilter(Modifier.PRIVATE));
        return this;
    }
    
    
    
    
    /**
     * Adds a filter for default access constructors only.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder isDefault() {
        add(new NegationConstructorFilter(new ModifierConstructorFilter(Modifier.PUBLIC & Modifier.PROTECTED & Modifier.PRIVATE)));
        return this;
    }
    
   
    
    
    
    /**
     * Adds a filter for constructors which is annotated
     * with the supplied annotation.
     * 
     * @param annotation The annotation to match.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder annotated(Class<? extends Annotation> annotation) {
        add(new AnnotatedConstructorFilter(annotation));
        return this;
    }
    
    /**
     * Adds a filter for constructors which is annotated
     * with an annotation which is annotated with 
     * the supplied annotation.
     * 
     * @param annotation The meta annotation to match constructor annotations with.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder metaAnnotated(Class<? extends Annotation> inheritedAnnotation) {
        add(new MetaAnnotatedConstructorFilter(inheritedAnnotation));
        return this;
    }
    
    /**
     * Adds a filter for constructors which has the exact signature (types) as
     * the supplied parameter types.
     * 
     * @param params    Varargs type of the parameters, may be empty.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder hasSignature(Class<?>... params) {
        add(new SignatureConstructorFilter(params));
        return this;
    }
    
    
    
   
    
    /**
     * Adds a filter for constructors with matching number of
     * parameters as the supplied parameter: numberOfParameters.
     * 
     * @param numberOfParameters The number of parameters to match.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder numberOfParameters(int numberOfParameters) {
        add(new NumberOfParametersConstructorFilter(numberOfParameters));
        return this;
    }
    
    /**
     * Adds a filter that allows only constructors which parameter types extends the supplied
     * types in the superType parameter. 
     * 
     * Only constructors with same number of parameters as the extendsClass parameter will
     * be allowed.   
     * 
     * @param extendsClass Varargs classes that the parameters types should extend, may be empty.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder parametersExtendsType(Class<?>... superType) {
        add(new AssignableFromConstructorFilter(superType));
        return this;
    }
    
    /**
     * Adds a filter that allows only constructors which parameter types which is a base class of the supplied
     * types in the baseType parameter. 
     * 
     * Only constructors with same number of parameters as the extendsClass parameter will
     * be allowed.   
     * 
     * @param extendsClass Varargs classes that the parameters types should extend, may be empty.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder parametersSuperType(Class<?>... baseType) {
        add(new AssignableToConstructorFilter(baseType));
        return this;
    }
    
    /**
     * Adds a filter for constructors that has a parameter which extends the supplied type in the
     * extendsType parameter.
     * 
     * If a constructor does not have a parameter at parameterIndex (too few parameters) its not allowed. 
     * 
     * @param parameterIndex    The index of the parameter to inspect.
     * @param superType         The class the parameter should extend.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder parameterExtendsType(int parameterIndex, Class<?> superType) {
        add(new AssignableFromConstructorFilter(parameterIndex, superType));
        return this;
    }
    
    /**
     * Adds a filter for constructors that has a parameter which is a base class to the supplied type in the
     * baseType parameter.
     * 
     * If a constructor does not have a parameter at parameterIndex (too few parameters) its not allowed. 
     * 
     * @param parameterIndex    The index of the parameter to inspect.
     * @param baseType          The class the parameter should have a base class.
     * 
     * @return The builder to support method chaining.
     **/
    public ConstructorFilterBuilder parameterSuperType(int parameterIndex, Class<?> baseType) {
        add(new AssignableToConstructorFilter(parameterIndex, baseType));
        return this;
    }
    
   
    /**
     * Adds a filter for constructors that has a parameter which type passes the supplied class filter.
     * 
     * If a constructor does not have a parameter at parameterIndex (too few parameters) its not allowed. 
     * <p>
     * Example:
     * {@code
     *  ConstructorFilter filter = new ConstructorFilterBuilder().parameterTypeFilter(0, new ClassFilterBuilder().annotated(MyAnnotation.class).build()).build();
     * }
     * Created a filter for constructors which first parameter type should be annotated with @MyAnnotation.
     * 
     * @param parameterIndex    The index of the parameter to inspect.
     * @param filter            The filter which the parameter type should pass.
     * 
     * @return The builder to support constructor chaining.
     **/
    public ConstructorFilterBuilder parameterTypeFilter(int parameterIndex, ClassFilter filter) {
        add(new ParameterClassFilterConstructorFilter(parameterIndex, filter));
        return this;
    }
    
    /**
     * Adds a filter that allows only constructors which parameter types passes the class filter supplied
     * in the parameter filter. 
     * 
     * Only constructors with same number of parameters as the filter parameter will
     * be allowed.   
     * 
     * @param filter Varargs class filters that the parameters should pass, may be empty.
     * 
     * @return The builder to support constructor chaining.
     **/
    public ConstructorFilterBuilder parametersTypesFilter(ClassFilter... filter) {    
        add(new ParameterClassFilterConstructorFilter(filter));
        return this;
    }
    
    /**
     * Returns the ConstructorFilter built.
     * 
     * @return the ConstructorFilter built.
     * @throws IllegalStateException if no filter was specified before calling build()
     **/
    public ConstructorFilter build() {
        if(filters.isEmpty()) {
            throw new IllegalStateException("No filter specified.");
        }
        if(filters.size() == 1) {
            return filters.get(0);
        }
        ConstructorFilter[] methodFilters = new ConstructorFilter[filters.size()];
        filters.toArray(methodFilters);
        return new ConstructorFilterList(methodFilters);
    }
    
}
