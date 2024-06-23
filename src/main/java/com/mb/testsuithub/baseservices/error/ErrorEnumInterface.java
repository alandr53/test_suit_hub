package com.mb.testsuithub.baseservices.error;

public interface ErrorEnumInterface<T extends Enum<T>> {
    public static final String NOT_FOUND="NOT_FOUND";
    public static final String BAD_REQUEST="BAD_REQUEST";
    public static final String SC_FORBIDDEN = "FORBIDDEN";
    public static final String NOT_AVAILABLE = "NOT_AVAILABLE";
    public static final String CONFLICT = "CONFLICT";
    public static final String UNAUTHORIZED="NOT_AUTHORIZED";
    public static final String METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED";
    public static final String LOCKED = "LOCKED";
    public static final String UNPROCESSABLE_ENTITY="UNPROCESSABLE_ENTITY";

    int getHttpStatus();
    int getApplicationCode();
    String getMessage();
    void setMessage(String message);
    String getState();
    T getRaw();

    default ErrorEnumInterface<?> prepare(Object... params) {
        setMessage(String.format(this.getMessage(), params));
        return this;
    }
}
