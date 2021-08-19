package com.tmax.hyperauth.rest;

import org.keycloak.events.Event;
import org.keycloak.events.EventType;

import java.util.Map;

public class PictureModel {

    private String userName;

    private String base64EncodeImage;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBase64EncodeImage() {
        return base64EncodeImage;
    }

    public void setBase64EncodeImage(String base64EncodeImage) {
        this.base64EncodeImage = base64EncodeImage;
    }
}

