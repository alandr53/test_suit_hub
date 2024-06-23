package com.mb.testsuithub;

public class SessionInfo {

    public enum SessionStatus {
        FREE(1000), INACCESSIBLE(1001), UNCONNECTED(1002), RESERVED(1003), OCCUPIED(1004), NOT_EXIST(1005), NOT_VALID(1006), NOT_HEALTHY(1007), ACCESS_DENIED(1008);

        private Integer code;

        private SessionStatus(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }

    private SessionStatus sessionStatus;
    private String sessionText;

    public SessionInfo(SessionStatus sessionStatus, String sessionText) {
        this.sessionStatus = sessionStatus;
        this.sessionText = sessionText;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public String getSessionText() {
        return sessionText;
    }
}

