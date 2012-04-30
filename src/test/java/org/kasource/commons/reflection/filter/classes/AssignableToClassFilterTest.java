package org.kasource.commons.reflection.filter.classes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AssignableToClassFilterTest {

    
    
    @TestedObject
    private AssignableToClassFilter filter = new AssignableToClassFilter(ArrayList.class);
    
    @Test
    public void passTrue() {
        assertTrue(filter.passFilter(List.class));
    }
    
    @Test
    public void passFalse() {
        assertFalse(filter.passFilter(HashSet.class));
    }
}
