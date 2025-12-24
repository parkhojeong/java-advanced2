package webservice;

import io.member.Member;
import io.member.MemberRepository;
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

import java.util.List;

import static util.MyLogger.log;

public class MemberController {
    final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Mapping("/")
    public void home(HttpResponse response) {
        String str = "<html>"
                + "<body>" +
                "<h1>Member manager</h1>" +
                "<ul>" +
                "<li><a href='/add-member'>join</a></li>" +
                "<li><a href='/users'>user list</a></li>" +
                "</ul>" +
                "</body>"
                + "</html>";
        response.writeBody(str);
    }

    @Mapping("/add-member")
    public void addMember(HttpResponse response) {
        String body = "<html>"
                + "<body>" +
                "<form action='/join' method='post'>" +
                "<input type='text' name='id'>" +
                "<input type='text' name='name'>" +
                "<input type='text' name='age'>" +
                "<input type='submit' value='join'>" +
                "</form>" +
                "</body>"
                + "</html>";
        response.writeBody(body);
    }

    @Mapping("/join")
    public void saveUser(HttpRequest request, HttpResponse response) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        if(id.isEmpty()){
            throw new IllegalArgumentException("id is empty");
        }
        if(name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        if(age.isEmpty()){
            throw new IllegalArgumentException("age is empty");
        }
        if(!age.matches("[0-9]+")){
            throw new IllegalArgumentException("age is invalid");
        }
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            if(member.getId().equals(id)){
                throw new IllegalArgumentException("id is duplicated");
            }
        }

        memberRepository.add(new Member(id, name, Integer.parseInt(age)));
        response.writeBody("join success");
    }

    @Mapping("/users")
    public void getUsers(HttpResponse response) {
        List<Member> members = memberRepository.findAll();
        response.writeBody(members.toString());
    }
}
