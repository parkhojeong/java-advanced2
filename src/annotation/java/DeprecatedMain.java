package annotation.java;

public class DeprecatedMain {
    public static void main(String[] args) {
        System.out.println("DeprecatedMain.main");
        DeprecatedClass deprecatedClass = new DeprecatedClass();
        deprecatedClass.call1();
        deprecatedClass.call2();
        deprecatedClass.call3();
    }
}
