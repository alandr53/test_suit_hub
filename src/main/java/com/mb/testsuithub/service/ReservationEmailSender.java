package com.mb.testsuithub.service;
import java.util.logging.Logger;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Util class to send out emails from the Hub for created, updated or deleted reservations with an ICS calendar entry. Used by {@link ReservationConcierge}.
 */
public interface ReservationEmailSender {

    static Logger logger = Logger.getLogger(ReservationEmailSender.class.getName());
    static final String SMTP_HOST = "localhost";
    static final String MAIL_SUBJECT_PATTERN = "{0}Reservation for TestBench {1} with BookingId {2}";
    static final String VIN_DEEPLINK_PATTERN = "http://{0}/vehicle?hub={1}&vin={2}";
    static final String BODY_DESCRIPTION_PATTERN = "Dear TestBenchHub user,\\n\\nbelow you will find the reservation details for your desired TestBench:\\n\\nBooking Id: {0}\\n\\nDirect Link: {1}";

    default void sendCreatedEmail(String fromEmail, String toEmail, String bookingId, String created, String start, String end, String vin, String hubHost, int hubPort) throws /*GridInfrastructureException,*/ Exception {
        sendEmail("REQUEST", "0", "CONFIRMED", "", fromEmail, toEmail, bookingId, created, start, end, vin, hubHost, "" + hubPort);
    }

    default void sendUpdatedEmail(String fromEmail, String toEmail, String bookingId, String created, String start, String end, String vin, String hubHost, int hubPort) throws Exception /*GridInfrastructureException,*/ {
        sendEmail("REQUEST", "1", "CONFIRMED", "Updated ", fromEmail, toEmail, bookingId, created, start, end, vin, hubHost, "" + hubPort);
    }

    default void sendDeletedEmail(String fromEmail, String toEmail, String bookingId, String created, String start, String end, String vin, String hubHost, int hubPort) throws Exception /*GridInfrastructureException,*/ {
        sendEmail("CANCEL", "2", "CANCELLED", "Deleted ", fromEmail, toEmail, bookingId, created, start, end, vin, hubHost, "" + hubPort);
    }

    default void sendEmail(String method, String sequence, String status, String subjectPrefix, String fromEmail, String toEmail, String bookingId, String created, String start, String end, String vin, String hubHost,
                           String hubPort) throws Exception {

        try {

            if (!toEmail.contains("@")) {
                logger.info("Do not send reservation email to: " + toEmail);
                return;
            }

            // cut the pin from email if there is one
            if (toEmail.contains(":")) {
                toEmail = toEmail.split(":")[0];
            }

            // we allow mixed case for emails but all daimler emails are lower case
            toEmail = toEmail.toLowerCase();

            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", SMTP_HOST);
            Session session = Session.getDefaultInstance(properties);

            String subject = MessageFormat.format(MAIL_SUBJECT_PATTERN, subjectPrefix, vin, bookingId);

            String vinDeepLink = null;
            String bodyDescription = null;
            if (method.equals("CANCEL")) {
                bodyDescription = "Deleted";
            } else {
                vinDeepLink = MessageFormat.format(VIN_DEEPLINK_PATTERN, hubHost, hubPort, vin);
                bodyDescription = MessageFormat.format(BODY_DESCRIPTION_PATTERN, bookingId, vinDeepLink);
            }

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart("alternative");

            BodyPart messageBodyPart = buildCalendarPart(method, sequence, status, bookingId, created, start, end, vin, bodyDescription, hubPort);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport.send(message);
            logger.info(subjectPrefix + " reservation email sent to " + toEmail + " with subject: " + subject);

        } catch (Exception e) {

            logger.severe(e.toString());
            //throw new GridInfrastructureException(e.getMessage());
        }
    }

    default BodyPart buildCalendarPart(String method, String sequence, String status, String bookingId, String created, String start, String end, String vin, String bodyDescription, String hubPort) throws Exception {

        BodyPart calendarPart = new MimeBodyPart();

        // backwards compatibility for events before v0.13.1
        // created dat ewas added to DB with 0.13.1
        if (created == null || created.isEmpty()) {
            created = start;
        }
        String createdDate = parseEventDate(created);
        String startDate = parseEventDate(start);
        String endDate = parseEventDate(end);

        StringBuilder calendarContent = new StringBuilder();
        calendarContent.append("BEGIN:VCALENDAR\n");
        calendarContent.append("METHOD:" + method + "\n");
        calendarContent.append("PRODID:TBH\n");
        calendarContent.append("VERSION:2.0\n");
        calendarContent.append("BEGIN:VEVENT\n");
        calendarContent.append("DTSTAMP:" + createdDate + "\n");
        calendarContent.append("DTSTART:" + startDate + "\n");
        calendarContent.append("DTEND:" + endDate + "\n");
        calendarContent.append("UID:" + bookingId + "\n");
        calendarContent.append("LOCATION:TestBenchHub_" + hubPort + "\n");
        calendarContent.append("DESCRIPTION:" + bodyDescription + "\n");
        calendarContent.append("SEQUENCE:" + sequence + "\n");
        calendarContent.append("PRIORITY:5\n");
        calendarContent.append("CLASS:PUBLIC\n");
        calendarContent.append("STATUS:" + status + "\n");
        calendarContent.append("TRANSP:OPAQUE\n");
        calendarContent.append("ACTION:DISPLAY\n");
        calendarContent.append("END:VEVENT\n");
        calendarContent.append("END:VCALENDAR\n");
        logger.fine(calendarContent.toString());

        calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
        calendarPart.setContent(calendarContent.toString(), "text/calendar;method=" + method);

        return calendarPart;
    }

    // VClaendar date format is without : - and ending Z in UTC time zone
    default String parseEventDate(String date) {
        return date.replaceAll("-", "").replaceAll(":", "").substring(0, 15) + "Z";
    }
}