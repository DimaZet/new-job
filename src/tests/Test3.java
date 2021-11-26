package tests;

import copy.CopyUtils;

import java.util.Objects;

import static tests.Assertions.assertCondition;

public class Test3 implements BaseTest {

    @Override
    public void test() {
        String className = this.getClass().getName();
        System.out.println("Start test " + className);

        CircularDependency another = new CircularDependency(null, "another");
        CircularDependency source = new CircularDependency(another, "self");
        another.another = source;

        CircularDependency copy = CopyUtils.deepCopy(source);
        assertCondition(source != copy);
        assertCondition(source.equals(copy));

        assertCondition(source.another != copy.another);
        assertCondition(source.self != copy.self);
        assertCondition(source.another.another != copy.another.another);

        assertCondition(copy.self == copy);
        assertCondition(copy.another != copy);
        assertCondition(copy.another.another == copy);

        System.out.println("Finish test " + className);
    }

    public static class CircularDependency {
        public CircularDependency self = this;
        public CircularDependency another;
        public final String forEqualsCompare;

        public CircularDependency(CircularDependency another, String forEqualsCompare) {
            this.another = another;
            this.forEqualsCompare = forEqualsCompare;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CircularDependency that = (CircularDependency) o;
            return Objects.equals(forEqualsCompare, that.forEqualsCompare);
        }

        @Override
        public int hashCode() {
            return Objects.hash(forEqualsCompare);
        }
    }
}
