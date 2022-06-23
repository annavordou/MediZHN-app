package com.example.medizhn;
/**
 * Αυτή η κλάση είναι υπεύθυνη για την προσθήκη νέων χαπιών και τον αριθμό αυτών στο Υπολοιπόμενο Χαπιών ή αλλιώς στην κλάση PillRemainder.
 * Επίσης, κάνει την σύνδεση μεταξύ των αντικειμένων της CalEntry και της βάσης δεδομένων.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Set_Remainder extends AppCompatActivity {

    EditText med_name;//όνομα χαπιών
    EditText number_pills;//αριθμός που μένει

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rem);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //η σύνδεση των μεταβλητών με το αρχείο xml
        med_name = findViewById(R.id.eddittext_name_of_pill);
        number_pills=findViewById(R.id.number_of_remaining_pills);
    }
    /**
     * Μέθοδος που υλοποιεί την λειτουργία του κουμπιού πίσω "<-" στο πάνω αριστερά μέρος της οθόνης
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            //Create the Intent to start the Medicines Activity
            Intent i = new Intent(this, PillRemainder.class);
            //Ask Android to start the new Activity
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Μέθοδος που υλοποιεί την λειτουργία του κομπιού του κινητού, όταν ο χρήστης
     * πατάει το κουμπί πίσω σε αυτό
     */
    @Override
    public void onBackPressed() {
        this.finish();
        //Create the Intent to start the Medicines Activity
        Intent i = new Intent(this, PillRemainder.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }

    /**
     * Μέθοδος που γίνεται η δημιουργία νέου χαπιού και συνδέεται με τη βάση δεδομένων
     * @param view
     */
    public void newRemainder (View view) {
        MyDBHandler_Rem dbHandler = new MyDBHandler_Rem(this, null, null, 1);
        String name_med = med_name.getText().toString();
        String numpills = number_pills.getText().toString();
        if (!name_med.equals("") && !numpills.equals("")){
            Remainder found = dbHandler.findRemainder(name_med);
            if (found == null){
                Remainder rem = new Remainder(name_med, numpills);
                dbHandler.addRemainder(rem);
                med_name.setText("");
                number_pills.setText("");
                Context context = getApplicationContext();
                CharSequence success = context.getResources().getString(R.string.success_remainder);
                int duration = Toast.LENGTH_SHORT;

                Toast toast_success = Toast.makeText(context, success, duration);
                toast_success.show();
                //Επιστροφή στην προηγούμενη δραστηριότητα και ανανέωσή της
                finish();
                Intent i = new Intent(this, PillRemainder.class);
                startActivity(i);
            }else{
                Context context = getApplicationContext();
                CharSequence failure = context.getResources().getString(R.string.failure_ref);
                int duration = Toast.LENGTH_SHORT;

                Toast toast_failure = Toast.makeText(context, failure, duration);
                toast_failure.show();
            }
        }

    }
    /**
     * Αναζήτηση ύπαρξης υπενθύμισης
     * @param view
     */
    public void lookupRemainder (View view) {
        MyDBHandler_Rem dbHandler = new MyDBHandler_Rem(this, null, null, 1);
        Remainder rem = dbHandler.findRemainder(med_name.getText().toString());
        if (rem != null) {
            med_name.setText(String.valueOf(rem.getMed_Title()));
        } else {
            med_name.setText("");
        }

    }
}
