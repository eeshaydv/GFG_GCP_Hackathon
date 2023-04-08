package com.example.medhere.models;

public class PastRecordItem {
    String doctorName, dateOfVisit;

    public String getDoctorName() {
        return doctorName;
    }

    public PastRecordItem(String doctorName, String dateOfVisit) {
        this.doctorName = doctorName;
        this.dateOfVisit = dateOfVisit;
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
}
