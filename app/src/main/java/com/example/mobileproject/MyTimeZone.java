package com.example.mobileproject;

import java.io.Serializable;

public class MyTimeZone implements Serializable {
    private String openTime;
    private String closeTime;

    public MyTimeZone(String openTime, String closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
