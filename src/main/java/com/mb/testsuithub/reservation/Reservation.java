package com.mb.testsuithub.reservation;

import com.google.gson.Gson;

import java.util.Locale;

public class Reservation {
    private String eventId;
    private String nodeIdent;
    private String name;
    private String email;
    private String startDate;
    private String endDate;
    private String description;

    // Konstruktor, der JSON-String als Parameter nimmt
    public Reservation(String jsonBody) {
        Gson gson = new Gson();
        Reservation parsedObject = gson.fromJson(jsonBody, Reservation.class);
        this.eventId = parsedObject.eventId;
        this.nodeIdent = parsedObject.nodeIdent;
        this.email = parsedObject.email;
        this.name = parsedObject.email;

        // Extrahiere den Namen aus der E-Mail-Adresse
        if (this.name.contains(":")) {
            this.name = this.email.split(":")[0];
        }
        if (this.name.contains("@")) {
            this.name = this.name.split("@")[0];
        }
        this.name = capitalize(this.name.replace(".", " ").replace("_", " "));

        this.startDate = parsedObject.startDate;
        this.endDate = parsedObject.endDate;
        this.description = parsedObject.description != null ? parsedObject.description : "";
    }

    // Getter und Setter für alle Felder
    public boolean isValid() {
        return eventId != null && !eventId.isEmpty()
                && email != null && !email.isEmpty()
                && name != null && !name.isEmpty()
                && startDate != null && !startDate.isEmpty()
                && endDate != null && !endDate.isEmpty();
    }

    // Prüft, ob diese Reservierung in einem bestimmten Zeitbereich liegt
    public boolean isWithinTimeRange(String startTime, String endTime) {
        return startDate.compareTo(endTime) < 0 && endDate.compareTo(startTime) > 0;
    }

    public boolean isWithinTimeRange(String currentTime) {
        return startDate.compareTo(currentTime) <= 0 && endDate.compareTo(currentTime) >= 0;
    }


    // Aktualisiert diese Reservierung mit neuen Daten
    public void update(String jsonBody) {
        Reservation updatedReservation = new Reservation(jsonBody);
        if (eventId.equals(updatedReservation.getEventId())) {
            this.email = updatedReservation.getEmail();
            this.name = updatedReservation.getName();
            this.startDate = updatedReservation.getStartDate();
            this.endDate = updatedReservation.getEndDate();
            this.description = updatedReservation.getDescription();
        } else {
            throw new IllegalArgumentException("Cannot update reservation with different eventId");
        }
    }

    public static String capitalize(String text) {
        String c = (text != null) ? text.trim() : "";
        String[] words = c.split(" ");
        StringBuilder result = new StringBuilder();
        for (String w : words) {
            result.append((w.length() > 1
                    ? w.substring(0, 1).toUpperCase(Locale.US) + w.substring(1).toLowerCase(Locale.US)
                    : w)).append(" ");
        }
        return result.toString().trim();
    }

    // Getter-Methoden
    public String getEventId() {
        return eventId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public String getDescription() {
        return description;
    }
    public String getNodeIdent() { return nodeIdent; }

    // Setter-Methoden
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setNodeIdent() { this.nodeIdent =  nodeIdent; }
}

