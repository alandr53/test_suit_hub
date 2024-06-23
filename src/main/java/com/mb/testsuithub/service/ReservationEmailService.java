package com.mb.testsuithub.service;

public class ReservationEmailService implements ReservationEmailSender{

    private String email;

    public ReservationEmailService(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
