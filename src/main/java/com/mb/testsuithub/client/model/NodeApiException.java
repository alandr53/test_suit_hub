package com.mb.testsuithub.client.model;

public class NodeApiException extends RuntimeException {

    public NodeApiException(String message) {
        super(message);
    }
    public NodeApiException(Throwable t) {
        super(t);
    }

    public NodeApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
