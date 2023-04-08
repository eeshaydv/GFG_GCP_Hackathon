package com.example.medhere.models;

public class FollowUpReminder {
    public FollowUpReminder(String doctorName, String dateOfVisit, String timeOfVisit) {
        this.doctorName = doctorName;
        this.dateOfVisit = dateOfVisit;
        this.timeOfVisit = timeOfVisit;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getTimeOfVisit() {
        return timeOfVisit;
    }

    public void setTimeOfVisit(String timeOfVisit) {
        this.timeOfVisit = timeOfVisit;
    }

    String doctorName, dateOfVisit, timeOfVisit;
}
