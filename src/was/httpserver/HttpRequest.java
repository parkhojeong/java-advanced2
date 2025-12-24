package was.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpRequest {
    private String method;
    private String path;
    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(BufferedReader reader) throws IOException {
        parseRequestLine(reader);
        parseHeaders(reader);

        parseBody(reader);
    }

    // GET /search?q=java HTTP/1.1
    // Host: localhost:12345
    private void parseRequestLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if(requestLine == null){
            throw new IOException("requestLine is null");
        }

        String[] requestLineArray = requestLine.split(" ");
        if(requestLineArray.length != 3){
            throw new IOException("invalid requestLine");
        }

        method = requestLineArray[0];
        String[] pathParts = requestLineArray[1].split("\\?");
        path = pathParts[0];
        if(pathParts.length == 2){
            parseQueryParameters(pathParts[1]);
        }
    }

    // key1=value1&key=2=value2
    // key1=
    private void parseQueryParameters(String pathPart) {
        String[] split = pathPart.split("&");
        for (String param : split) {
            String[] keyValue = param.split("=");

            String key = URLDecoder.decode(keyValue[0], UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], UTF_8) : "";
            queryParameters.put(key, value);
        }
    }

    //Host: localhost:12345
    //Connection: keep-alive
    //Sec-Fetch-Site: same-origin
    //
    private void parseHeaders(BufferedReader reader) throws IOException {
        String line;
        while(!(line = reader.readLine()).isEmpty()){
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
        }
    }

    private void parseBody(BufferedReader reader) throws IOException {
        if(!headers.containsKey("Content-Length")){
            return;
        }

        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        char[] bodyChars = new char[contentLength];
        int read = reader.read(bodyChars);
        if(read != contentLength){
            throw new IOException("invalid body. Expected: " + contentLength + ", actual: " + read);
        }
        String body = new String(bodyChars);

        if(headers.get("Content-Type").equals("application/x-www-form-urlencoded")){
            parseQueryParameters(body);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String name) {
        return queryParameters.get(name);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", queryParameters=" + queryParameters +
                ", headers=" + headers +
                '}';
    }
}
