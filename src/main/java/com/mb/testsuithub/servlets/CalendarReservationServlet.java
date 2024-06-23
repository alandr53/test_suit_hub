package com.mb.testsuithub.servlets;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mb.node.Node;
import com.mb.node.NodeManager;
import com.mb.testsuithub.baseservices.BaseHttpServlet;
import com.mb.testsuithub.reservation.Reservation;
import com.mb.testsuithub.reservation.ReservationManager;
import com.mb.testsuithub.reservation.ReservationResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.mb.util.GmailSender.sendEmail;

@WebServlet({"/hub/reservations",
             "/hub/reservations/add",
             "/hub/reservations/update",
             "/hub/reservations/delete",
             "/hub/reservations/getreservation",
             "/hub/reservations/getreservationid",})
public class CalendarReservationServlet extends BaseHttpServlet {

    ReservationManager nodeReservationManager = new ReservationManager();
    private static final String X_BOOKING_ID = "X-BookingId";
    private static final String RESERVATION_EMAIL_ADDRESS = "reservationEmailAddress";
    private static final Logger LOGGER = Logger.getLogger(CalendarReservationServlet.class.getSimpleName());
    private static String[] DEBUG_DRIVING_LICENSES = new String[] { "tbhuitestdriver@gmail.com", "UnitTestDriver", "APITestDriver", "SecretTestDriver:1335" };
    private static String ACCEPTED_DATE_TIME_FORMAT = "yyyy-MM-ddTHH:mm:ss.000Z";

    /**
     * Fetch a list with all booked events for a certain slot.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        initDefaultIncomingRequest(request, response);
        //ReservationResponse reservationResponse = new ReservationResponse();
        Node node = NodeManager.getNodeByUnicId(request.getParameter("unicid"));
        ;
        Gson gson = new Gson();
        String jsonResponse = "";

        //findReservationIdByNodeAndTime(String nodeIdent, String startDate, String endDate)
        String str = request.getRequestURI().split("/")[3];

        switch (str) {
                case "getreservation":
                    if (node != null) {
                        jsonResponse = nodeReservationManager.convertReservationsToJson(node.getReservations());
                    } else {
                        jsonResponse = gson.toJson(new ResponseObject("No nodes with unicId " + request.getParameter("unicid") + " provided"));
                    }
                    break;
                case "getreservationid":
                    String starttime = request.getParameter("start");
                    String endtime = request.getParameter("end");
                    String resp = nodeReservationManager.findReservationIdByNodeAndTime(node.getUnicID(), starttime, endtime);
                    jsonResponse = gson.toJson(new ResponseObject(resp));
                    break;

                default:
                    jsonResponse = "Unknown request";
                    LOGGER.info(jsonResponse);
                    break;
            }

            response.getWriter().println(jsonResponse);
        }
    /**
     * Create a new reservation based on the provided body data and send a confirmation with the booking id to the user email.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        String body = initDefaultIncomingRequest(request, response);
        ReservationResponse reservationResponse = new ReservationResponse();

        String str = request.getRequestURI().split("/")[3];
        switch(str) {
            case "add":
                LOGGER.info("Request for a new reservation");
                Reservation reservation = new Reservation(body);
                reservationResponse = nodeReservationManager.addReservation(reservation);
                /*      String recipient = "fombuffer2@gmail.com"; // E-Mail des Empfängers
                String subject = "Test Email from Java";
                String text = "Hello, this is a test email sent from Java using Gmail!";
                sendEmail(recipient, subject, text);*/
                break;

            case "update":
                reservationResponse = nodeReservationManager.updateReservation(body);
                LOGGER.info("An existing reservation is to be updated");
                break;

            case "delete":
                reservationResponse = nodeReservationManager.deleteReservation(body);
                LOGGER.info("Request for cancellation of reservation");
                //data = restClient.sendRequest("/node/reservations/delete", headers, reqBody,"POST");
                break;

            default:
                LOGGER.info("Unknown request");
                break;
        }

        response.getWriter().println(reservationResponse.toJson() );
    }
    /**
     * Update an existing event based on the provided body data and send an update to the user email.
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("PUT request received");
    }
    /**
     * Delete an existing event and send an information to the user email.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        String body = initDefaultIncomingRequest(request, response);

        response.getWriter().println("DELETE request received");
    }


    static class ResponseObject {
        private String message;

        public ResponseObject(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}




//String jsonBody = jsonBodyBuilder.toString();
    /*    String jsonBody = "{" +
                "\"eventId\": \"12345\"," +
                "\"name\": \"John Doe\"," +
                "\"email\": \"johndoe@example.com\"," +
                "\"startDate\": \"2024-06-01T10:00:00\","+
                "\"endDate\": \"2024-06-01T12:00:00\","+
                "\"description\": \"Meeting to discuss project updates\""+
                "}";




             Map<String, String> headers = new HashMap<>();
        headers.put("X-Username", "NodeProperties.NODE_USER");
        headers.put("X-Password", "NodeProperties.NODE_PASSWORD");
        headers.put("X-Node-IP", "NodeProperties.NODE_IP");




    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody reqBody = RequestBody.create(mediaType, body);
        NodeReservation reservation = new  NodeReservation(body);
        //NodeReservation nodeReservation = reservation.fromJson(jsonBody);
        RestClient restClient = new RestClient("http://localhost:3002");
        ResponseData data = restClient.sendRequest("/node/reservations", headers, reqBody,"POST"); //

*/
