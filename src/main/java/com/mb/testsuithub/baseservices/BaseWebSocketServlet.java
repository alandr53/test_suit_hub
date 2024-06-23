package com.mb.testsuithub.baseservices;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public abstract class BaseWebSocketServlet extends WebSocketServlet {

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(getWebSocketAdapter().getClass());
    }

    protected abstract WebSocketAdapter getWebSocketAdapter();
}
