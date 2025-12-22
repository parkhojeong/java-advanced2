package was.v3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpRequestHandlerV3 implements Runnable{
    private final Socket socket;

    public HttpRequestHandlerV3(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            log(e);
        }
    }

    private void process() {
        try(socket;
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), false, UTF_8);) {

            String requestString = requestToString(reader);
            if(requestString.contains("/favicon.ico")){
                log("favicon.ico");
                return;
            }

            log(requestString);

            if(requestString.startsWith("GET /site1")){
                site1(writer);
            }else if(requestString.startsWith("GET /site2")){
                site2(writer);
            }else if (requestString.startsWith("GET /search")){
                search(writer, requestString);
            }else if(requestString.startsWith("GET / ")){
                home(writer);
            }else {
                notFound(writer);
            }

            log("response sent");

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void home(PrintWriter writer) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>home</h1>");
        writer.println("<ul>");
        writer.println("<li><a href=\"/site1\">site1</a></li>");
        writer.println("<li><a href=\"/site2\">site2</a></li>");
        writer.println("<li><a href=\"/search?q=java\">search</a></li>");
        writer.println("</ul>");
        writer.flush();
    }

    private void site1(PrintWriter writer) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>site1</h1>");
        writer.flush();
    }

    private void site2(PrintWriter writer) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>site2</h1>");
        writer.flush();
    }

    private void notFound(PrintWriter writer) {
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>404 not found</h1>");
        writer.flush();
    }

    // "/search?q=java"
    private void search(PrintWriter writer, String requestString) {
        int startIndex = requestString.indexOf("q=");
        int endIndex = requestString.indexOf(" ", startIndex+2);
        String query = requestString.substring(startIndex + 2, endIndex);
        String decode = URLDecoder.decode(query, UTF_8);

        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println();
        writer.println("<h1>search</h1>");
        writer.println("<p>query: " + query + "</p>");
        writer.println("<p>decode: " + decode + "</p>");
        writer.flush();
    }

    private static void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String requestToString(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if( line.isEmpty()){
                break;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
