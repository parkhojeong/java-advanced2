package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class AnnotationServlet implements HttpServlet {

    private final List<Object> controllers;

    public AnnotationServlet(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        for (Object controller : controllers) {
            Class<?> aClass = controller.getClass();


            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                Mapping mapping = method.getAnnotation(Mapping.class);
                if (mapping == null) {
                    continue;
                }

                if (path.equals(mapping.value())) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if(parameterTypes.length == 2 && parameterTypes[0] == HttpRequest.class && parameterTypes[1] == HttpResponse.class) {
                        invoke(controller, method, request, response);
                        return;
                    } else if (parameterTypes.length == 1 && parameterTypes[0] == HttpResponse.class) {
                        invoke(controller, method, response);
                        return;
                    }
                }
            }
        }

        throw new PageNotFoundException("request=" + path);
    }

    private void invoke(Object controller, Method method, HttpResponse response) {
        try {
            method.invoke(controller, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static void invoke(Object controller, Method method, HttpRequest request, HttpResponse response) {
        try {
            method.invoke(controller, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
