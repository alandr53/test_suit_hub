package com.mb.testsuithub.reservation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mb.node.Node;
import com.mb.node.NodeManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class ReservationManager {
    private List<Reservation> reservations; // hier sind alle verfügbare reservierungen für dieses Node enthalten als Objekt
    private static final Logger LOGGER = Logger.getLogger(ReservationManager.class.getName());

    public ReservationManager() {

        this.reservations = new ArrayList<>();
    }

    public ReservationResponse addReservation(Reservation reservation) {

        Node node = NodeManager.getNodeByUnicId(reservation.getNodeIdent());
        //String resStatus = NodeManager.checkNodeReservationStatus(reservation.getNodeIdent());
        List<Reservation> conflicts = new ArrayList<>();
              for(Reservation existingReservation : node.getReservations() ) {
                  if (existingReservation.isWithinTimeRange(reservation.getStartDate(), reservation.getEndDate())) {
                      LOGGER.info("The desired time slot overlaps with an existing reservation. The node is currently reserved from " + reservation.getStartDate() + " to " + reservation.getEndDate());
                      conflicts.add(existingReservation);
                  }
              }

        if (!conflicts.isEmpty()) {
            ReservationResponse response = new ReservationResponse("CONFLICT", "Time slot conflict with another reservation", reservation, conflicts);
            return response;
        }
        node.getReservations().add(reservation);
        LOGGER.info("Time slot from " + reservation.getStartDate() + " until " + reservation.getEndDate() + " reserved");
        ReservationResponse response = new ReservationResponse("CREATED", "Reservation created successfully", reservation, new ArrayList<>());
        return response;
    }

    public ReservationResponse updateReservation(String body) {
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(body);
        JsonObject jsonObject = jsonTree.getAsJsonObject();

        String eventId = jsonObject.get("eventId").getAsString();
        String nodeIdent = jsonObject.get("nodeIdent").getAsString();
        String startDate = jsonObject.get("startDate").getAsString();
        String endDate = jsonObject.get("endDate").getAsString();
        String description = jsonObject.get("description").getAsString();

        Reservation  existingReservation = findReservation(nodeIdent, eventId);
        if (existingReservation == null) {
            ReservationResponse response = new ReservationResponse("NOT_FOUND", "Reservation not found", null, new ArrayList<>());
            LOGGER.info("Reservation with eventId " + eventId + " not found.");
            return response;
        }

        boolean timeSlotChanged = !existingReservation.getStartDate().equals(startDate) ||
                !existingReservation.getEndDate().equals(endDate);

        if (!timeSlotChanged) {
            existingReservation.setDescription(description);
            ReservationResponse response = new ReservationResponse("UPDATED", "Reservation updated successfully", existingReservation, new ArrayList<>());
            LOGGER.info("Reservation was successfully updated.");
            return response;
        } else {
            List<Reservation> conflicts = new ArrayList<>();
            for (Reservation reservation : reservations) {
                if (!reservation.getEventId().equals(eventId) &&
                        reservation.isWithinTimeRange(startDate, endDate)) {
                    conflicts.add(reservation);
                }
            }
            if (!conflicts.isEmpty()) {
                ReservationResponse response = new ReservationResponse("CONFLICT", "Time slot conflict with another reservation", null, conflicts);
                LOGGER.info("Time slot conflict with another reservation.");
                return response;
            }

            existingReservation.setStartDate(startDate);
            existingReservation.setEndDate(endDate);
            existingReservation.setDescription(description);
            ReservationResponse response = new ReservationResponse("UPDATED", "Reservation updated successfully", existingReservation, new ArrayList<>());
            LOGGER.info("The duration of the reservation has been successfully updated .");
            return response;
        }
    }

    public ReservationResponse deleteReservation(String body) {
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(body);
        JsonObject jsonObject = jsonTree.getAsJsonObject();
        Node node = NodeManager.getNodeByUnicId(jsonObject.get("nodeIdent").getAsString());

        Iterator<Reservation> iterator = node.getReservations().iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getEventId().equals(jsonObject.get("eventId").getAsString())) {
                iterator.remove(); // Entfernt die Reservierung sicher während der Iteration
                LOGGER.info("Reservation with eventId " + jsonObject.get("eventId").getAsString() + " has been removed.");
                return new ReservationResponse("DELETED", "Reservation deleted successfully", reservation, new ArrayList<>());
            }
        }
        LOGGER.info("No reservation found with eventId " + jsonObject.get("eventId").getAsString());
        return new ReservationResponse("NOT_FOUND", "No reservation found with given eventId", null, new ArrayList<>());
    }

    public String findReservationIdByNodeAndTime(String nodeIdent, String startDate, String endDate) {
        Node node = NodeManager.getNodeByUnicId(nodeIdent);
        if (node == null) {
            LOGGER.info("No node found with nodeIdent " + nodeIdent);
            return null; // Kein Node mit der gegebenen ID gefunden
        }

        for (Reservation reservation : node.getReservations()) {
            if (reservation.getStartDate().equals(startDate) && reservation.getEndDate().equals(endDate)) {
                LOGGER.info("Reservation found with eventId " + reservation.getEventId());
                return reservation.getEventId(); // Gibt die eventId zurück, wenn die Zeitfenster übereinstimmen
            }
        }

        LOGGER.info("No reservation found for given time range on node " + nodeIdent);
        return null; // Keine passende Reservierung gefunden
    }


    public List<Reservation> getReservations() {
        return reservations;
    }
    public boolean isEmpty() {
        return reservations.isEmpty();
    }
    public List<Reservation> getReservationsForTestInstance(String eventId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getEventId().equals(eventId)) {
                result.add(reservation);
            }
        }
        return result;
    }

    public Reservation findReservation(String nodeIdent, String eventId) {
        Node node = NodeManager.getNodeByUnicId(nodeIdent);
        if (node == null) {
            return null;
        }
        for (Reservation reservation : node.getReservations()) {
            if (reservation.getEventId().equals(eventId)) {
                return reservation;
            }
        }
        return null;
    }

    // Hilfsmethode zum Finden einer Reservierung nach eventId
    public Reservation findReservationById(String eventId) {
        for (Reservation reservation : reservations) {
            if (reservation.getEventId().equals(eventId)) {
                return reservation;
            }
        }
        return null;
    }

    public Reservation findReservation(String eventId, LocalDateTime startTime) {
        for (Reservation reservation : reservations) {
            if (reservation.getEventId().equals(eventId) && reservation.getStartDate().equals(startTime)) {
                return reservation;
            }
        }
        return null;
    }
    public static String convertReservationsToJson(List<Reservation> reservations) {
        List<Event> events = new ArrayList<>();
        for (Reservation reservation : reservations) {
            events.add(new Event(reservation.getDescription(), reservation.getStartDate(), reservation.getEndDate()));
        }
        Gson gson = new Gson();
        return gson.toJson(events);
    }
}
