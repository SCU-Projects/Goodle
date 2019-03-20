package com.techprimers.kafka.springbootkafkaconsumerexample.model;

public class User {

    private String meetingId;
    // private String[] host = meetingId.split(":");

    public String getMeetingId() {
        return meetingId;
    }

    public User() {
    }

    public User(String meetingId) {
        this.meetingId = meetingId;
    }

    @Override
    public String toString() {
        return meetingId;
    }
}
