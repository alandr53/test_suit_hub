package com.mb.node;

import com.mb.testsuithub.reservation.Reservation;
import com.mb.testsuithub.reservation.ReservationManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class NodeManager {
    //ex TestBenchManager
    private static Map<String, Node> nodes = new HashMap<>(); // enthält alle registrierte Nodes
    private static Timer statusCheckTimer; // timer für zyklische abfragen von dem Status der angemeldeten Nodes
    protected Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public static void stopNodeStatusCheck() {
        statusCheckTimer.cancel();
    }
  /*  public static Node getNodeByIP(String ip) {
        return nodes.get(ip);
    }*/

    public static Node getNodeByUnicId(String unicId) {
        return nodes.get(unicId);
    }
    public static void removeNode(String unicId) {
        nodes.remove(unicId);
    }

    public static boolean registerNode(String ip, Integer port, String unicId, String status, String description) {
        if(!nodes.containsKey(unicId)){
            Node node = new Node(ip, port, unicId, status, description);
            nodes.put(unicId, node);
            return true;
        }
        // bei Neuanmeldung von node kann sein Status sich geändert haben
        if(!nodes.get(unicId).getStatus().equals(status)) {
            nodes.get(unicId).setStatus(status);
        }
        return false;
    }
    /*
    public void startNodeStatusCheck(int intervalMilliseconds) {
        statusCheckTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkNodeStatus();
            }
        }, 0, intervalMilliseconds);
    }

    public static void checkNodeStatus() {
        for (Node node : nodes.values()) {
            try {
                URL url = new URL("http://" + node.getIp() + "/check-status");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int status = con.getResponseCode();
                if (status == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Parse the response and update node status accordingly
                    String statusInfo = response.toString();
                    // Update node status based on statusInfo
                    node.setStatus(statusInfo);
                }
                con.disconnect();
            } catch (Exception e) {
                // Handle exception
                e.printStackTrace();
            }
        }
    }
*/
    public static String checkNodeStatus(String unicId) {
        Node node = getNodeByUnicId(unicId);
        return node != null ? node.getStatus() : null;
    }
    public static boolean setNodeStatus(String unicId, String status) {
        if (nodes.containsKey(unicId)) {
            getNodeByUnicId(unicId).setStatus(status);
            return true;
        }
        return false;
    }

    // Beispielmethode zur Überprüfung des Reservierungsstatus einer Node
    public static String checkNodeReservationStatus(String unicId) {
        Node node = getNodeByUnicId(unicId);
        if (node == null) {
            return "Node not found";
        }

        // Gewünschtes Zeitformat für die Zeitbereichsüberprüfung
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        //String formattedTime = now.format(formatter);
        String currentTime = now.toString();
        // Aktuelle Zeit inklusive des Offsets
       // OffsetDateTime now = OffsetDateTime.now();
//2024-06-01T08:00:00+02:00
        // Durchlaufe alle vorhandenen Reservierungen der Node
        for (Reservation existingReservation : node.getReservations()) {
            // Überprüfe, ob die aktuelle Zeit innerhalb des Zeitbereichs der Reservierung liegt
           if (existingReservation.isWithinTimeRange(currentTime)) {
                return "Reserved";
            }
        }

        // Falls keine Reservierung gefunden wurde, ist die Node frei
        return "Free";
    }

}




/*
    public static boolean registerNode(String username, String password, String ip, String hostname, int port, String status) {
        if(!nodes.containsKey(ip)){
            Node node = new Node(username, password, ip, hostname, port, status);
            nodes.put(ip, node);
            return true;
        }
        return false;
    }
    */


 /*   public static boolean authenticateNode(String username, String password, String ip) {
        Node node = nodes.get(ip);
        return node != null && node.getUsername().equals(username) && node.getPassword().equals(password);
    }
    */
