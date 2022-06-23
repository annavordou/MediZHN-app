package com.example.medizhn;
/**
 * Αυτή η κλάση είναι υπεύθυνη για την προσθήκη νέων φαρμάκων στα Φάρμακα ή αλλιώς στην κλάση Medicines.
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

public class Set_Medicine extends AppCompatActivity {

    EditText title_med;//ονομασία φαρμάκου
    EditText reason_med;//λόγος λήψης

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_med);

        //η σύνδεση των μεταβλητών με το αρχείο xml
        title_med = findViewById(R.id.button_choose_medicine);
        reason_med = findViewById(R.id.edittext_reason_medicine);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            Intent i = new Intent(this, Medicines.class);
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
        Intent i = new Intent(this, Medicines.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }

    /**
     * Μέθοδος που γίνεται η δημιουργία του νέου φαρμάκου και συνδέεται με τη βάση δεδομένων
     * @param view
     */
    public void newMedicine (View view) {
        MyDBHandler_Med dbHandler = new MyDBHandler_Med(this, null, null, 1);
        String medTitle = title_med.getText().toString();
        String medReason = reason_med.getText().toString();
        if (!medTitle.equals("") && !medReason.equals("")){
            Medicine found = dbHandler.findMedicine(medTitle);
            if (found == null){
                Medicine med = new Medicine(medTitle, medReason);
                dbHandler.addMedicine(med);
                title_med.setText("");
                reason_med.setText("");
                Context context = getApplicationContext();
                CharSequence success = context.getResources().getString(R.string.success_med);
                int duration = Toast.LENGTH_SHORT;

                Toast toast_success = Toast.makeText(context, success, duration);
                toast_success.show();
                //Επιστροφή στην προηγούμενη δραστηριότητα και ανανέωσή της
                finish();
                Intent i = new Intent(this, Medicines.class);
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
     * Αναζήτηση ύπαρξης φαρμάκου
     * @param view
     */
    public void lookupMedicine (View view) {
        MyDBHandler_Med dbHandler = new MyDBHandler_Med(this, null, null, 1);
        Medicine med = dbHandler.findMedicine(title_med.getText().toString());
        if (med != null) {
            //idView.setText(String.valueOf(referral.getID()));
            title_med.setText(String.valueOf(med.getMed_Title()));
        } else {
            //idView.setText(getString(R.string.no_match_found));
            title_med.setText("");
        }
    }
}
