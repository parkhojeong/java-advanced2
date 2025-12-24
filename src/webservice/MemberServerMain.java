package webservice;

import io.member.impl.MemoryMemberRepository;
import was.httpserver.HttpServer;
import was.httpserver.HttpServlet;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.annotation.AnnotationServletV3;

import java.io.IOException;
import java.util.List;

public class MemberServerMain {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();
        List<Object> controllers = List.of(new MemberController(memoryMemberRepository));
        HttpServlet annotationServlet = new AnnotationServletV3(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(annotationServlet);
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
