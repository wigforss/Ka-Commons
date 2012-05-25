package org.kasource.commons.reflection.filter;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.Retention;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.commons.reflection.filter.ConstructorFilterBuilder;
import org.kasource.commons.reflection.filter.classes.NameClassFilter;
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
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ConstructorFilterBuilderTest {

    @TestedObject
    private ConstructorFilterBuilder builder;

    @Test(expected = IllegalStateException.class)
    public void buildEmptyFilter() {
        builder.build();
    }

    
    @Test
    public void notTest() {
    	ConstructorFilter filter = builder.not().isPublic().build();
        assertTrue(filter instanceof NegationConstructorFilter);
    }
    
    @Test
    public void orTest() {
    	ConstructorFilter filter = builder.isPublic().or().isPrivate().or().isProtected().build();
        assertTrue(filter instanceof OrConstructorFilter);
    }
    
    @Test
    public void listTest() {
    	ConstructorFilter filter = builder.isPublic().isPrivate().build();
        assertTrue(filter instanceof ConstructorFilterList);
    }
    
    @Test
    public void isPublic() {
    	ConstructorFilter filter = builder.isPublic().build();
        assertTrue(filter instanceof ModifierConstructorFilter);
    }

    @Test
    public void isProtected() {
    	ConstructorFilter filter = builder.isProtected().build();
        assertTrue(filter instanceof ModifierConstructorFilter);
    }

    @Test
    public void isPrivate() {
    	ConstructorFilter filter = builder.isPrivate().build();
        assertTrue(filter instanceof ModifierConstructorFilter);
    }

   
    @Test
    public void isDefault() {
    	ConstructorFilter filter = builder.isDefault().build();
        assertTrue(filter instanceof NegationConstructorFilter);
    }

 

    @Test
    public void annotated() {
    	ConstructorFilter filter = builder.annotated(Retention.class).build();
        assertTrue(filter instanceof AnnotatedConstructorFilter);
    }

    @Test
    public void metaAnnotated() {
    	ConstructorFilter filter = builder.metaAnnotated(Retention.class).build();
        assertTrue(filter instanceof MetaAnnotatedConstructorFilter);
    }
    
    @Test
    public void hasSignature() {
    	ConstructorFilter filter = builder.hasSignature(int.class).build();
        assertTrue(filter instanceof SignatureConstructorFilter);
    }

  

    @Test
    public void numberOfParameters() {
    	ConstructorFilter filter = builder.numberOfParameters(4).build();
        assertTrue(filter instanceof NumberOfParametersConstructorFilter);
    }

    @Test
    public void parametersExtendsType() {
    	ConstructorFilter filter = builder.parametersExtendsType(List.class).build();
        assertTrue(filter instanceof AssignableFromConstructorFilter);
    }
    
    @Test
    public void parametersSuperType() {
    	ConstructorFilter filter = builder.parametersSuperType(List.class).build();
        assertTrue(filter instanceof AssignableToConstructorFilter);
    }

    @Test
    public void parameterExtendsType() {
    	ConstructorFilter filter = builder.parameterExtendsType(0, List.class).build();
        assertTrue(filter instanceof AssignableFromConstructorFilter);
    }

    @Test
    public void parameterSuperType() {
    	ConstructorFilter filter = builder.parameterSuperType(0, List.class).build();
        assertTrue(filter instanceof AssignableToConstructorFilter);
    }

    
    @Test
    public void parameterTypeFilter() {
    	ConstructorFilter filter = builder.parameterTypeFilter(0, new NameClassFilter("Abstract.*")).build();
        assertTrue(filter instanceof ParameterClassFilterConstructorFilter);
    }

    @Test
    public void parameterTypesFilter() {
    	ConstructorFilter filter = builder.parametersTypesFilter(new NameClassFilter("Abstract.*")).build();
        assertTrue(filter instanceof ParameterClassFilterConstructorFilter);
    }

}
