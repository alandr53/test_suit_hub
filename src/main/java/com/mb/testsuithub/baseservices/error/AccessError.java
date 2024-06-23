package com.mb.testsuithub.baseservices.error;

import com.mb.testsuithub.SessionInfo.SessionStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * All access response messages of the matcher endpoint or if the hub is running over an idP.
 *
 * All error codes between 1000 - 1999
 *
 * The application code will be overridden by the below class:
 * {@link SessionStatus}
 *
 * NOT_EXIST(1005N)
 * NOT_VALID(1006N)
 * NOT_HEALTHY(1007N)
 * ACCESS_DENIED(1008)
 */
public enum AccessError implements ErrorEnumInterface<AccessError> {
    SLOT_IS_RESERVED(423, SessionStatus.RESERVED.getCode(), "Slot is reserved and only available with a valid BookingId.", LOCKED),
    SLOT_IS_OCCUPIED(423, SessionStatus.OCCUPIED.getCode(), "Slot is occupied.", LOCKED),

    CONFIG_NOT_EXIST(HttpServletResponse.SC_NOT_FOUND, SessionStatus.NOT_EXIST.getCode(), "Requested capability does not match existing device capability.", NOT_FOUND),
    USER_EMAIL_NOT_CONFIGURED(HttpServletResponse.SC_BAD_REQUEST, 10051, "User email is not configured.", BAD_REQUEST),
    NODE_PORT_NOT_EQUAL(HttpServletResponse.SC_CONFLICT, 10052, "Requested port is not equal to node port.", CONFLICT),
    SLOT_INACCESSIBLE(423, SessionStatus.INACCESSIBLE.getCode(), "Slot is currently unavailable.", LOCKED),
    DOCKER_CONFIGURATION_NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, 10054, "No valid Docker configuration.", NOT_FOUND),
    ACCESS_RIGHTS_NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, 10055, "Unable to find access rights for requested path: %s", NOT_FOUND),
    BENCH_NOT_NTG5_6(HttpServletResponse.SC_BAD_REQUEST,10056,"Reset not possible because telematics is not ntg5/6.", BAD_REQUEST),
    BENCH_RESET_NOT_SET(HttpServletResponse.SC_BAD_REQUEST, 10057,"Bench reset not set, please configure the TestBenchResetHook servlet and the ET-Framework correctly.",BAD_REQUEST),
    SERVICE_NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, 10058, "Requested service not found.", NOT_FOUND),
    CHANNELS_NOT_FOUND(HttpServletResponse.SC_BAD_REQUEST, 10059, "No channels specified.", BAD_REQUEST),
    NO_BOOKING_WITHIN_NEXT_15_MINS(HttpServletResponse.SC_NOT_FOUND, SessionStatus.NOT_VALID.getCode(), "There is no booking within the next 15mins, but the request was sent for a reservation.",
            NOT_FOUND),
    BOOKING_ID_DOES_NOT_MATCH_THE_NEXTBOOKING_ID(HttpServletResponse.SC_NOT_FOUND, 10061, "Booking id in request does not match the next booking id.", NOT_FOUND),
    CHANNELS_NOT_EXIST(HttpServletResponse.SC_NOT_FOUND, 10062, "Requested channels are not supported.", NOT_FOUND),

    DEVICE_STATUS_NOT_HEALTHY(HttpServletResponse.SC_BAD_REQUEST, SessionStatus.NOT_HEALTHY.getCode(), "Slot status is not healthy.", BAD_REQUEST),
    NOT_ENOUGH_ACCESS_RIGHTS(HttpServletResponse.SC_BAD_REQUEST, SessionStatus.ACCESS_DENIED.getCode(), "Not enough access rights to forward the request.", BAD_REQUEST);

    private int httpStatus;
    private int applicationCode;
    private String message;
    private String state;

    private AccessError(int httpStatus, int applicationCode, String message, String state) {
        this.httpStatus = httpStatus;
        this.applicationCode = applicationCode;
        this.message = message;
        this.state = state;
        // remove one digit
        int code = applicationCode / 10;
        if(SessionStatus.NOT_EXIST.getCode().equals(code)) {
            this.applicationCode = SessionStatus.NOT_EXIST.getCode();
        }
        if(SessionStatus.NOT_VALID.getCode().equals(code)) {
            this.applicationCode = SessionStatus.NOT_VALID.getCode();
        }
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getApplicationCode() {
        return applicationCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public AccessError getRaw() {
        return this;
    }

    @Override
    public String getState() {
        return state;
    }
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

}


