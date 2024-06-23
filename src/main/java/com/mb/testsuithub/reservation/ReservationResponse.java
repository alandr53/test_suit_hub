package com.mb.testsuithub.reservation;

import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationResponse {
    private String status;
    private String message;
    private Reservation reservation;
    private List<Reservation> existingReservations;
    private String timestamp;

    // Konstruktor
    public ReservationResponse(){};

    public ReservationResponse(String status, String message, Reservation reservation, List<Reservation> existingReservations) {
        this.status = status;
        this.message = message;
        this.reservation = reservation;
        this.existingReservations = existingReservations;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
    // Getter-Methoden
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public List<Reservation> getExistingReservations() {
        return existingReservations;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Methode zur Erstellung einer JSON-Antwort
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

