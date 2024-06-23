package com.mb.node;

import com.mb.testsuithub.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String ip;
    private Integer port;
    private String unicID;
    private String status;
    private String description;
    private List<Reservation> reservations;

    public Node(String ip, Integer port, String unicID, String status, String description) {
        this.ip = ip;
        this.port = port;
        this.unicID = unicID;
        this.status = status;
        this.description = description;
        this.reservations = new ArrayList<>();
    }

    public boolean addReservation(Reservation reservation) {
        for (Reservation res : reservations) {
            if (res.isWithinTimeRange(reservation.getStartDate(), reservation.getEndDate())) {
                return false; // Zeitbereich ist bereits reserviert
            }
        }
        reservations.add(reservation);
        return true;
    }

    public boolean isTimeSlotAvailable(String startDate, String endDate) {
        for (Reservation res : reservations) {
            if (res.isWithinTimeRange(startDate, endDate)) {
                return false; // Zeitbereich ist bereits reserviert
            }
        }
        return true;
    }

    // Getter und Setter Methoden
    public List<Reservation> getReservations() {
        return this.reservations;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(String ip) {
        this.port = port;
    }
    public String getUnicID() {
        return unicID;
    }

    public void setUnicID(String unicID) {
        this.unicID = unicID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String message) {
        this.description = message;
    }

}