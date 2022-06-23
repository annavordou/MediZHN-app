package com.example.medizhn;
/**
 * Αυτή η κλάση είναι υπεύθυνη για τη δημιουργία των υπολοιπόμενων χαπιών ως αντικείμενα μιας απλής κλάσης αρχικά, πρωτού εισαχθούν στην βάση δεδομένων.
 */
public class Remainder {
        private int _id;//το id των χαπιών
        private String medicine_title;//ο τίτλος των χαπιών
        private String numpills;//ο αριθμός των χαπιών που μένουν

        //Κονστράκτορες
        public Remainder() {
        }

        public Remainder(int _id, String medicine_title, String numpills) {
            this._id = _id;
            this.medicine_title = medicine_title;
            this.numpills = numpills;
        }

        public Remainder(String medicine_title, String numpills) {
            this.medicine_title = medicine_title;
            this.numpills = numpills;
        }

        //Setter & Getters για κάθε πεδίο της κλάσης
        public void setMed_Title(String title) {
            this.medicine_title = title;
        }

        public String getMed_Title() {
            return medicine_title;
        }

        public void setNumpills(String numpills) {
            this.numpills = numpills;
        }

        public String getNumpills() {
            return numpills;
        }

        public void setID(int id) {
            this._id = id;
        }

        public int getID() {
            return _id;
        }


}
