package annotation.validator;

import static util.MyLogger.log;

public class ValidatorV1Main {
    public static void main(String[] args) {
        User user = new User("user1", 0);
        Team team = new Team("", 0);

        try{
            log("== validate user ==");
            validateUser(user);
        } catch (Exception e) {
            log(e);
        }

        try {
            log("== validate team ==");
            validateTeam(team);
        } catch (Exception e) {
            log(e);
        }
    }

    private static void validateUser(User user) {
        if(user.getName() == null || user.getName().isEmpty()) {
            throw new RuntimeException("name is null or empty");
        }
        if (user.getAge() < 1 || user.getAge() > 100) {
            throw new RuntimeException("age is invalid");
        }
    }

    private static void validateTeam(Team team) {
        if(team.getName() == null || team.getName().isEmpty()) {
            throw new RuntimeException("name is null or empty");
        }
        if (team.getMemberCount() < 1 || team.getMemberCount() > 999) {
            throw new RuntimeException("memberCount is invalid");
        }
    }
}
