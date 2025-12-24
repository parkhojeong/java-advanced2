package webservice;

import io.member.Member;
import io.member.MemberRepository;
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

import java.util.List;

public class MemberController {
    final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
