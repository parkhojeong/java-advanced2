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
    private final Map<String, ControllerMethod> pathMap = new HashMap<>();

    public AnnotationServlet(List<Object> controllers) {
        this.controllers = controllers;

        initPathMap(controllers);
    }

    private static class ControllerMethod {
        private final Object controller;
        private final Method method;

        private ControllerMethod(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        public void invoke(HttpRequest request, HttpResponse response) {
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

    private void initPathMap(List<Object> controllers) {
        for (Object controller : controllers) {
            Class<?> aClass = controller.getClass();
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(Mapping.class)) {
                    String path = method.getAnnotation(Mapping.class).value();
                    if (pathMap.get(path) != null) {
                        ControllerMethod controllerMethod = pathMap.get(path);
                        throw new IllegalStateException("duplicate mapping path=" + path + ", controller=" + controllerMethod.controller.getClass() + ", method=" + controllerMethod.method);
                    }
                    pathMap.put(path, new ControllerMethod(controller, method));
                }
            }
        }
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        ControllerMethod controllerMethod = pathMap.get(path);
        if(controllerMethod == null) {
            throw new PageNotFoundException("request=" + path);
        }

        controllerMethod.invoke(request, response);
    }
}
