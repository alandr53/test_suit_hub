package com.mb.testsuithub.server;

import java.util.logging.Logger;

public class TestSuitLauncher {
    private static final Logger Log = Logger.getLogger(TestSuitLauncher.class.getName());
    public static final String FIXED_VERSION = "0.0.1";
    private static TestSuitServer server = buildLaunchers();

    //startpoint for the application
    public static void main(String[] args) {
        new TestSuitLauncher();
    }
    public TestSuitLauncher() {
    }

    private static TestSuitServer buildLaunchers() {

        ServerConfig property = new ServerConfig();// laden der Serverkonfiguration
        TestSuitServer server = new TestSuitServer(property); // konfiguration von dem Server
        server.startServer(); // start server
        return server;
    }




}
