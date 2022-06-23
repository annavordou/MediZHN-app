package com.example.medizhn;
/**
 * Αυτή η κλάση είναι υπεύθυνη για την προσθήκη νέων παραπεμπτικών στα Παραπεμπτικά ή αλλιώς στην κλάση Referrals.
 * Επίσης, κάνει την σύνδεση μεταξύ των αντικειμένων της CalEntry και της βάσης δεδομένων.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Set_Referral extends AppCompatActivity {

    EditText title_ref;//τίτλος παραπεμπτικού
    EditText from_ref;//αρχή παραπεμπτικού
    EditText to_ref;//λήξη παραπεμπτικού

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ref);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //η σύνδεση των μεταβλητών με το αρχείο xml
        title_ref = findViewById(R.id.title_of_new_referral);
        from_ref = findViewById(R.id.edittext_from_referral);
        to_ref = findViewById(R.id.edittext_to_referrals);

        //Ακολουθεί η μετατροπή των αριθμών που θα δώσει ο χρήστης για την ημέρα, σε μετατροπή date format
        from_ref = (EditText) findViewById(R.id.edittext_from_referral);
        to_ref = (EditText) findViewById(R.id.edittext_to_referrals);
        from_ref.addTextChangedListener(new TextWatcher() {
            private String current_from = "";
            private String ddmmyyyy_from = "DDMMYYYY";
            private Calendar cal_from = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current_from)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current_from.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy_from.substring(clean.length());
                    } else {
                        //Έλεγχος ότι μπαίνουν σωστά νούμερα που αρμόζουν σε κάποια ημερομηνία
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12)
                            mon = 12;
                        cal_from.set(Calendar.MONTH, mon - 1);
                        //Ελάχιστο έτος 2022 και μέγιστο 2100
                        year = (year < 2022) ? 2022 : (year > 2100) ? 2100 : year;
                        cal_from.set(Calendar.YEAR, year);


                        day = (day > cal_from.getActualMaximum(Calendar.DATE)) ? cal_from.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current_from = clean;
                    from_ref.setText(current_from);
                    from_ref.setSelection(sel < current_from.length() ? sel : current_from.length());


                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
            });

           to_ref.addTextChangedListener(new TextWatcher() {
            private String current_to = "";
            private String ddmmyyyy_to = "DDMMYYYY";
            private Calendar cal_to = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current_to)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current_to.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy_to.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal_to.set(Calendar.MONTH, mon - 1);

                        year = (year < 2022) ? 2022 : (year > 2100) ? 2100 : year;
                        cal_to.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal_to.getActualMaximum(Calendar.DATE)) ? cal_to.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current_to = clean;
                    to_ref.setText(current_to);
                    to_ref.setSelection(sel < current_to.length() ? sel : current_to.length());


                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
            Intent i = new Intent(this, Referrals.class);
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
        Intent i = new Intent(this, Referrals.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }

    /**
     * Μέθοδος που γίνεται η δημιουργία του νέου παραπεμπτικού και συνδέεται με τη βάση δεδομένων
     * @param view
     */
    public void newReferral (View view) {
        MyDBHandler_Ref dbHandler = new MyDBHandler_Ref(this, null, null, 1);
        String refName = title_ref.getText().toString();
        String refFrom = from_ref.getText().toString();
        String refTo = to_ref.getText().toString();
        if (!refName.equals("") && !refFrom.equals("") && !refTo.equals("")){
            Referral found = dbHandler.findReferral(refName);
            if (found == null){
                Referral ref = new Referral(refName, refFrom, refTo);
                dbHandler.addReferral(ref);
                title_ref.setText("");
                from_ref.setText("");
                to_ref.setText("");
                Context context = getApplicationContext();
                CharSequence success = context.getResources().getString(R.string.success_ref);
                int duration = Toast.LENGTH_SHORT;

                Toast toast_success = Toast.makeText(context, success, duration);
                toast_success.show();
                finish();
                //Create the Intent to start the Referrals Activity
                Intent i = new Intent(this, Referrals.class);
                //Ask Android to start the new Activity
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
    public void lookupReferral (View view) {
        MyDBHandler_Ref dbHandler = new MyDBHandler_Ref(this, null, null, 1);
        Referral referral = dbHandler.findReferral(title_ref.getText().toString());
        if (referral != null) {
            //idView.setText(String.valueOf(referral.getID()));
            title_ref.setText(String.valueOf(referral.getRef_Title()));
        } else {
            //idView.setText(getString(R.string.no_match_found));
            title_ref.setText("");
        }

    }
}
