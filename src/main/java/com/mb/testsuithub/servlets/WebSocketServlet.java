package com.mb.testsuithub.servlets;

import com.mb.testsuithub.baseservices.BaseWebSocketServlet;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/stream")
public class WebSocketServlet extends BaseWebSocketServlet {
    @Override
    protected WebSocketAdapter getWebSocketAdapter() {
        return new MyWebSocketAdapter();
    }

    private static class MyWebSocketAdapter extends WebSocketAdapter {

        @Override
        public void onWebSocketConnect(Session session) {
            super.onWebSocketConnect(session);
            System.out.println("WebSocket Verbindung geöffnet: " + session.getRemoteAddress().getHostName());
        }

        @Override
        public void onWebSocketText(String message) {
            super.onWebSocketText(message);
            System.out.println("Nachricht empfangen: " + message);

            // Hier können Sie auf die empfangene Nachricht antworten
            try {
                getSession().getRemote().sendString("Nachricht erhalten: " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            super.onWebSocketClose(statusCode, reason);
            System.out.println("WebSocket Verbindung geschlossen: " + reason);
        }
    }
}
