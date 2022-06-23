package com.example.medizhn;
/**
 * Αυτή η κλάση επιτυγχάνει την λειτουργικότητα των Φαρμάκων. Είναι υπεύθυνη για τη ρύθμιση του activity_medicines
 * και την παρουσίαση τους με δυναμικό τρόπο στην εφαρμογή.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

public class Medicines extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int position=-1;
    ImageView delete;
    /**
     * Η μέθοδος onCreate υλοποιεί τα δυναμικά στοιχεία της διεπαφής. Χρησιμοποιεί listeners για να αντιδρά σε επιλογές του χρήστη
     * επιτρέποντας τη διαγραφή καρτών, την εμφάνιση πληροφοριών σχετικά με τον τρόπο λειτουργίας του activity και περιέχει την μέθοδο
     * που ανακατευθύνει τον χρήστη ώστε να φτιάξει νέα καταχώρηση.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);

        // κλήση του action bar
        ActionBar actionBar = getSupportActionBar();
        // εμφάνιση κουμπιού επιστροφής action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.medicines_layout);

        //Set the layout of the items in the RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Τα δεδομένα των καρτών
        MyDBHandler_Med myDBHandler_med = new MyDBHandler_Med(this, null, null, 1);
        HashMap<Integer, CardsData> cardsData = myDBHandler_med.getAllMedicines();

        //Set my Adapter for the RecyclerView
        MedicinesAdapter medicinesAdapter = new MedicinesAdapter(cardsData, Medicines.this);
        recyclerView.setAdapter(medicinesAdapter);

        delete= findViewById(R.id.delete_med);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //εντοπισμός θέσης κάρτας
                position= medicinesAdapter.getPosition();
                //προσπάθεια έυρεσης δεδομένων προς διαγραφή
                if(myDBHandler_med.deleteMedicine(position)){
                    //επιτυχής διαγραφή, ενημέρωση χρήστη με toast και ανανέωση σελίδας
                    Context context = getApplicationContext();
                    CharSequence success = context.getResources().getString(R.string.success_del);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast_success = Toast.makeText(context, success, duration);
                    toast_success.show();
                    overridePendingTransition(0, 0);
                    //ανανέωση της activity για να εμφανιστούν τα σωστά στοιχεία
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                else{
                    //αποτυχία στη διαγραφή, ενημέρωση χρήστη με toast και ανανέωση σελίδας
                    Context context = getApplicationContext();
                    CharSequence failure = context.getResources().getString(R.string.failure_del);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast_success = Toast.makeText(context, failure, duration);
                    toast_success.show();
                }
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Μέθοδος υπεύθυνη για την ανακατεύθυνση του χρήστη στο activity δημιουργίας νέας υπενθύμισης
     * @param view
     */
    public void goToSetMedicine (View view) {
        finish();
        //Create the Intent to start the Referrals Activity
        Intent i = new Intent(this, Set_Medicine.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }


    /**
     * Μέθοδος υπεύθυνη για την πληροφόρηση του χρήστη σχετικά με την χρήση του activity.
     * Επεξηγεί τον τρόπο που επιτυγχάνεται η κάθε αλληλεπίδραση που μπορεί να διεξάγει ο χρήστης στο
     * παρόν activity.
     * @param view
     */
    public void openMed(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.med_info_text);
                alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}