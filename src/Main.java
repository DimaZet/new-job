import tests.Test1;
import tests.Test2;
import tests.Test3;

public class Main {

    public static void main(String[] args) {
        new Test1().test();
        new Test2().test();
        new Test3().test();

        System.out.println("DONE");
    }


}