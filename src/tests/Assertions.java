package tests;

public class Assertions {

    public static void assertCondition(boolean condition) {
        if (!condition) {
            throw new IllegalStateException("assertions error");
        }
    }
}
