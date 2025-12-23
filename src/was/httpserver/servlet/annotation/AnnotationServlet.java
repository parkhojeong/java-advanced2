package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationServlet implements HttpServlet {

    private final List<Object> controllers;
    private final Map<String, Object[]> pathMap = new HashMap<>();

    public AnnotationServlet(List<Object> controllers) {
        this.controllers = controllers;

        initPathMap(controllers);
    }

    private void initPathMap(List<Object> controllers) {
        for (Object controller : controllers) {
            Class<?> aClass = controller.getClass();
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {

                Mapping mapping = method.getAnnotation(Mapping.class);
                if(mapping == null) {
                    continue;
                }
                if(pathMap.get(mapping.value()) != null) {
                    throw new IllegalArgumentException("duplicate mapping path=" + mapping.value());
                }
                pathMap.put(mapping.value(), new Object[]{controller, method});
            }
        }
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        Object[] result = pathMap.get(path);
        if(result == null) {
            throw new PageNotFoundException("request=" + path);
        }

        invoke(result[0], (Method) result[1], request, response);
    }

    private static void invoke(Object controller, Method method, HttpRequest request, HttpResponse response) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if(parameterTypes[i] == HttpRequest.class) {
                args[i] = request;
            } else if(parameterTypes[i] == HttpResponse.class) {
                args[i] = response;
            } else {
                throw new IllegalArgumentException("parameter type is not supported");
            }
        }


        try {
            method.invoke(controller, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
