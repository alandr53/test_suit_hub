package com.mb.testsuithub.server;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ServerConfig {


    //port für die Serverkommunikation
    private int id = 0;
    private  int port = 3001;
    private  String ip = "localhost";

    private static final Logger log = Logger.getLogger(ServerConfig.class.getName());

    public int getServerID() {
        log.log( Level.INFO, "get server id: {0}", id );
        return id;
    }

    public void setServerID(int id) {
        log.log( Level.INFO, "set server id: {0}", id );
        this.id = id;
    }

    public int getPort() {
        log.log( Level.INFO, "get server port: {0}", this.port );
        return port;
    }

    public void setPort(int port) {
        log.log( Level.INFO, "set server port: {0}", port );
        this.port = port;
    }

    public void setIP(String ip) {
        log.log( Level.INFO, "set server ip: {0}", ip );
        this.ip = ip;
    }

    public String getIP() {
        log.log(Level.INFO, "get server ip: {0}", this.ip);
        return this.ip;
    }
}
