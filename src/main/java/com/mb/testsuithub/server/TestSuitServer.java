package com.mb.testsuithub.server;

import com.mb.testsuithub.servlets.ServletManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServlet;
import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TestSuitServer extends Server {
    private Server server;
    private static final Logger Log = Logger.getLogger(TestSuitServer.class.getName());

    public TestSuitServer(ServerConfig property) {

        // alle verfügbaren Servlets aus dem hub.httpservlet package laden
        ServletManager servletManager = new ServletManager();
        servletManager.registerServlets("com.mb.testsuithub.servlets");

        //config server
        this.server = new Server();
        ServerConnector connector = new ServerConnector(this.server);
        connector.setPort(property.getPort());
        server.addConnector(connector);

        //add available servlets http & websockets
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

// Set up CORS filter
        FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,OPTIONS");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");




        //add all HTTP Servlets
        Map<String, HttpServlet> servletMapHttp = servletManager.getHttpServletMap();
        for (Map.Entry<String, HttpServlet> entry : servletMapHttp.entrySet()) {
            String urlPattern = entry.getKey();
            HttpServlet servlet = entry.getValue();
            context.addServlet(new ServletHolder(entry.getValue()),entry.getKey());
        }
        //add all WebsocketServlets
        Map<String, WebSocketServlet> servletMapWebSockets = servletManager.getWebsocketEndpointMap();
        for(Map.Entry<String, WebSocketServlet> entry: servletMapWebSockets.entrySet())
        {
            String urlPattern = entry.getKey();
            WebSocketServlet servlet = entry.getValue();
            context.addServlet(new ServletHolder(entry.getValue()),entry.getKey());
        }

    }

    public Server getServer() {
        return this.server;
    }
    /*
     * start server
     */
    public void startServer( ) {
        try {
            this.server.start();
            server.join();
            Log.log(Level.INFO, "TestSuit server is started... ");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.log(Level.SEVERE,"TestSuit start is failed! " + e.getMessage());
        }
    }

    public void stopServer() {
        try {
            this.server.stop();
            Log.log(Level.INFO, "TestSuit server is stopped");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.log(Level.SEVERE,"TestSuit stop is failed! " + e.getMessage());
        }
    }
}
