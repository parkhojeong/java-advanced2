package annotation.validator;

public class User {
    @NotEmpty(message = "name is empty")
    private String name;

    @Range(min = 1, max = 100, message = "age is between 1 to 100")
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
