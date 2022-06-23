package com.example.medizhn;
/**
 * Αυτή η κλάση είναι υπεύθυνη για τη δημιουργία των φαρμάκων ως αντικείμενα μιας απλής κλάσης αρχικά, πρωτού εισαχθούν στην βάση δεδομένων.
 */
public class Medicine {
    private int _id;//το id του φαρμάκου
    private String medicine_title;//ο τίτλος του φαρμάκου
    private String reason;// ο λόγος λήψης του


    //Κονστράκτορες
    public Medicine() {
    }

    public Medicine(int _id, String medicine_title, String reason) {
        this._id = _id;
        this.medicine_title = medicine_title;
        this.reason = reason;
    }

    public Medicine(String medicine_title, String reason) {
        this.medicine_title = medicine_title;
        this.reason = reason;
    }

    //Setter & Getters για κάθε πεδίο της κλάσης
    public void setMed_Title(String title) {
        this.medicine_title = title;
    }

    public String getMed_Title() {
        return medicine_title;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return _id;
    }
}
