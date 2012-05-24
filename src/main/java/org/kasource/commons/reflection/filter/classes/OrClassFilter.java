package org.kasource.commons.reflection.filter.classes;

/**
 * Class filter that evaluates two other class filters (left and right) and 
 * returns the result of OR:ing the result of the left and right filter.
 * 
 * @author rikardwi
 **/
public class OrClassFilter implements ClassFilter {

    private ClassFilter left;
    private ClassFilter right;
    
    public OrClassFilter(ClassFilter left, ClassFilter right) {
        this.left = left;
        this.right = right;
    }
    
    
    @Override
    public boolean passFilter(Class<?> clazz) {
      return left.passFilter(clazz) || right.passFilter(clazz);
    }

}
