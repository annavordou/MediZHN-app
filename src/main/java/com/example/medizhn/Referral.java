package com.example.medizhn;
/**
 * Αυτή η κλάση είναι υπεύθυνη για τη δημιουργία των παραπεμπτικών ως αντικείμενα μιας απλής κλάσης αρχικά, πρωτού εισαχθούν στην βάση δεδομένων.
 */

public class Referral {
    private int _id;//το id του παραπεμπτικού
    private String referral_title;//ο τίτλος του παραπεμπτικού
    private String from;//το πότε αρχίζει
    private String to;//το πότε λήγει

    //Κονστράκτορες
    public Referral() {
    }

    public Referral(int _id, String referral_title, String from, String to) {
        this._id = _id;
        this.referral_title = referral_title;
        this.from = from;
        this.to = to;
    }

    public Referral(String referral_title, String from, String to) {
        this.referral_title = referral_title;
        this.from = from;
        this.to = to;
    }

    //Setter & Getters για κάθε πεδίο της κλάσης
    public void setRef_Title(String title) {
        this.referral_title = title;
    }

    public String getRef_Title() {
        return referral_title;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return _id;
    }
}