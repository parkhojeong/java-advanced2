package annotation.validator;

public class Team {
    @NotEmpty(message = "name is empty")
    private String name;

    @Range(min = 1, max = 999, message = "memberCount is between 1 to 999")
    private int memberCount;

    public Team(String name, int memberCount) {
        this.name = name;
        this.memberCount = memberCount;
    }

    public String getName() {
        return name;
    }

    public int getMemberCount() {
        return memberCount;
    }
}
