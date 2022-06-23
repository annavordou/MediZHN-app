package com.example.medizhn;
/**
 * Αυτή είναι η κλάση της αρχικής δραστηριότητας που εμφανίζεται στον χρήστη με το άνοιγμα
 * της εφαρμογής.
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void goToReferrals (View view) {
        //Create the Intent to start the Referrals Activity
        Intent i = new Intent(this, Referrals.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }

    public void goToMedicines (View view) {
        //Create the Intent to start the Medicines Activity
        Intent i = new Intent(this, Medicines.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }

    public void goToPillRemainder (View view) {
        //Create the Intent to start the Pill Remainder Activity
        Intent i = new Intent(this, PillRemainder.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }

    public void goToCalendar (View view) {
        //Create the Intent to start the Calendar Activity
        Intent i = new Intent(this, CalendarSet.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }
}