package com.mb.testsuithub.servlets;

import com.mb.util.ClassPathScanner;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.websocket.server.ServerEndpoint;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ServletManager {
    private final Map<String, HttpServlet> httpServletMap;
    private final Map<String, WebSocketServlet> websocketEndpointMap;

    public ServletManager() {
        this.httpServletMap = new HashMap<>();
        this.websocketEndpointMap = new HashMap<>();
    }

    public void registerServlets(String packageName) {
        try {
            ClassPathScanner classPathScanner = new ClassPathScanner();

            classPathScanner.findClassesImplementing(packageName, Object.class).forEach(clazz -> {
                try {
                    Annotation httpAnnotation = clazz.getAnnotation(WebServlet.class);
                    Annotation websocketAnnotation = clazz.getAnnotation(ServerEndpoint.class);

                    if (httpAnnotation != null) {
                        HttpServlet servlet = (HttpServlet) clazz.getDeclaredConstructor().newInstance();
                        for (String path : ((WebServlet) httpAnnotation).value()) {
                            httpServletMap.put(path, servlet);
                        }
                    }

                    if (websocketAnnotation != null) {
                        WebSocketServlet servlet = (WebSocketServlet) clazz.getDeclaredConstructor().newInstance();
                        String path = ((ServerEndpoint)websocketAnnotation).value();
                        websocketEndpointMap.put(path, servlet);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, HttpServlet> getHttpServletMap() {
        return httpServletMap;
    }

    public Map<String, WebSocketServlet> getWebsocketEndpointMap() {
        return websocketEndpointMap;
    }
}