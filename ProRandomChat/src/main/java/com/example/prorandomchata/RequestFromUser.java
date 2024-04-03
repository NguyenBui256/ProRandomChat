package com.example.prorandomchata;

import java.io.Serializable;

public class RequestFromUser implements Serializable {
    protected User user, partner;
    protected String type, content, message;

    public RequestFromUser(User user, String type, String content) {
        this.user = user;
        this.type = type;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getPartner() {
        return partner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
