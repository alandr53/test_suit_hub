package com.mb.testsuithub.baseservices;

import org.apache.http.client.methods.HttpPost;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.core.MediaType;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

//package com.daimler.testbenchgrid.service.servlet; -> HIER NOCH VERFOLLSTÄNDIGEN!!!
public class BaseHttpServlet extends HttpServlet {

    protected Logger LOGGER = Logger.getLogger(this.getClass().getName());

    protected String initDefaultIncomingRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setAccessControlHeaders(response);
        setDefaultEncodingAndContentType(response);
        return logIncomingRequest(false, request);
    }

    protected void setAccessControlHeaders(HttpServletResponse resp) {
        // TODO: allow Ip proxy usermanagement.
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT,OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "X-Access-Groups, X-EventId, X-BookingId, X-DrivingLicense, X-ClientId, Access-Control-Allow-Headers, "
                + "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
    }
    protected void setDefaultEncodingAndContentType(HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        //response.setContentType(MediaType.APPLICATION_JSON);
    }

    // should be called from all endpoint methods of the servlets
    protected String logIncomingRequest(boolean quiet, HttpServletRequest request) throws IOException {

        String incMsg = "Incoming " + this.getClass().getSimpleName() + " " + request.toString() + " from " + request.getRemoteHost();
        incMsg += "\n" + getRequestHeadersAsString(request);
        String requestBody = IOUtils.toString(request.getReader());
        if (request.getMethod().equals(HttpPost.METHOD_NAME)) {
            // Forwarding requests send the body as a attribute and not as a header.
            if (requestBody.isEmpty() && request.getAttribute("body") != null) {
                requestBody = (String) request.getAttribute("body");
                incMsg += "\nRequest Body from getAttribute: " + requestBody;
            } else {
                incMsg += "\nRequest Body: " + requestBody;
            }
        }

        if (quiet) {
            LOGGER.fine(incMsg);
        } else {
            LOGGER.info(incMsg);
        }
        return requestBody;
    }

    protected String getRequestHeadersAsString(HttpServletRequest request) {
        StringBuilder printableHeaders = new StringBuilder();
        printableHeaders.append("Request Header: ");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerKey = headers.nextElement();
            printableHeaders.append(headerKey);
            printableHeaders.append("=");
            printableHeaders.append(request.getHeader(headerKey));
            printableHeaders.append(" ");
        }
        return printableHeaders.toString();
    }
}
