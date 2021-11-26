package tests;

import copy.CopyUtils;

import java.util.List;
import java.util.Objects;

import static tests.Assertions.assertCondition;

public class Test2 implements BaseTest {

    static String staticString = FinalStatic.staticString;

    @Override
    public void test() {
        String className = this.getClass().getName();
        System.out.println("Start test " + className);

        FinalStatic source = new FinalStatic(1, new FinalStatic.Inner("str"), FinalStatic.Enum.ENUM2);
        FinalStatic copy = CopyUtils.deepCopy(source);
        assertCondition(source != copy);
        assertCondition(source.equals(copy));

        assertCondition(source.integer == copy.integer); // didn't deep clone boxed and unboxed primitives
        assertCondition(source.inner != copy.inner);
        assertCondition(source.anEnum == copy.anEnum); // didn't deep clone enums
        assertCondition(FinalStatic.staticString == staticString); // didn't clone and change static fields
        assertCondition(source.equals(copy));

        System.out.println("Finish test " + className);
    }

    public static class FinalStatic {
        private final Integer integer;
        private final Inner inner;
        private final Enum anEnum;
        private final static String staticString = "staticString";

        public FinalStatic(Integer integer, Inner inner, Enum anEnum) {
            this.integer = integer;
            this.inner = inner;
            this.anEnum = anEnum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FinalStatic that = (FinalStatic) o;
            return Objects.equals(integer, that.integer) && Objects.equals(inner, that.inner) && anEnum == that.anEnum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(integer, inner, anEnum);
        }

        public static class Inner {
            private final String string;

            public Inner(String string) {
                this.string = string;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Inner inner = (Inner) o;
                return Objects.equals(string, inner.string);
            }

            @Override
            public int hashCode() {
                return Objects.hash(string);
            }
        }

        public enum Enum {
            ENUM1, ENUM2
        }
    }
}