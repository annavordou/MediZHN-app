package com.example.medizhn;
/**
 * Αυτή η κλάση είναι υπεύθυνη για τη δημιουργία των υπενθυμίσεων ως αντικείμενα μιας απλής κλάσης αρχικά, πρωτού εισαχθούν στην βάση δεδομένων.
 */

public class CalEntry {
    private int _id;//το id της υπενθύμισης
    private String date;//η ημερομηνία
    private String time;//η ώρα
    private String desc;//η περιγραφή

    //Κονστράκτορες
    public CalEntry() {
    }

    public CalEntry(int _id, String date, String time, String desc) {
        this._id = _id;
        this.date = date;
        this.time = time;
        this.desc = desc;
    }

    public CalEntry(String date, String time, String desc) {
        this.date = date;
        this.time = time;
        this.desc = desc;
    }

    //Setter & Getters για κάθε πεδίο της κλάσης
    public void setCal_Date(String date) {
        this.date = date;
    }

    public String getCal_Date() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return _id;
    }
}
