package com.example.medhere.models;

public class MedicineReminder {
    String name, quantity, time;

    public MedicineReminder(String name, String quantity, String time) {
        this.name = name;
        this.quantity = quantity;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTime() {
        return time;
    }
}
