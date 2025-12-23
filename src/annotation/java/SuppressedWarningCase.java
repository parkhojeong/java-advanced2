package annotation.java;

import java.util.List;

public class SuppressedWarningCase {
    @SuppressWarnings("unused")
    public void unusedWarning() {
        System.out.println("multipleWarning");

        int unusedVariable = 1;
    }

    @SuppressWarnings("deprecation")
    public void deprecatedWarning() {
        java.util.Date date = new java.util.Date();
        int date1 = date.getDate();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void uncheckedCast() {
        // raw type warning
        List list = new java.util.ArrayList();

        // unchecked cast warning
        List<String> list1 = (List<String>)list;
    }

    @SuppressWarnings("all")
    public void suppressAllWarning() {
        int i = 1;

        java.util.Date date = new java.util.Date();
        int date1 = date.getDate();

        // raw type warning
        List list = new java.util.ArrayList();

        // unchecked cast warning
        List<String> list1 = (List<String>)list;
    }
}
