package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email")
public class Email {
    @Id
    private String id;
    private String subject;
    private String from;
    private String body;
    private String status;
    private boolean urgent;

    public Email() {}

    public Email(String subject, String from, String body, String status, boolean urgent) {
        this.subject = subject;
        this.from = from;
        this.body = body;
        this.status = status;
        this.urgent = urgent;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public boolean isUrgent() {
        return urgent;
    }
    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}