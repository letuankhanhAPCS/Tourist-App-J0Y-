package com.example.mobileproject;

import java.util.Date;

public class MySchedule implements java.io.Serializable {
    private String locationType;
    private int locationIndex;
    private Date scheduleDate;
    private boolean scheduleReminder;

    public MySchedule(String locationType, int locationIndex, Date scheduleDate, boolean scheduleReminder) {
        this.locationType = locationType;
        this.locationIndex = locationIndex;
        this.scheduleDate = scheduleDate;
        this.scheduleReminder = scheduleReminder;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public boolean isScheduleReminder() {
        return scheduleReminder;
    }

    public void setScheduleReminder(boolean scheduleReminder) {
        this.scheduleReminder = scheduleReminder;
    }
}
