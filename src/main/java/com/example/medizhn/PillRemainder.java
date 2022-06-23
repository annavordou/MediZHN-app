package com.example.medizhn;
/**
 * Αυτή η κλάση επιτυγχάνει την λειτουργικότητα των Υπολοιπόμενων Χαπιών. Είναι υπεύθυνη για τη ρύθμιση του activity_medicines
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

public class PillRemainder extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int position = -1;
    ImageView delete;
    ImageView increase;
    ImageView reduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_remainder);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.pill_remainder_layout);

        //Set the layout of the items in the RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Cards' data
        MyDBHandler_Rem myDBHandler_rem = new MyDBHandler_Rem(this, null, null, 1);
        HashMap<Integer, CardsData> cardsData = myDBHandler_rem.getAllRemainders();

        //Set my Adapter for the RecyclerView
        PillRemainderAdapter pillRemainderAdapter = new PillRemainderAdapter(cardsData, PillRemainder.this);
        recyclerView.setAdapter(pillRemainderAdapter);
        delete = findViewById(R.id.delete_rem);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = pillRemainderAdapter.getPosition();
                if (myDBHandler_rem.deleteRemainder(position)) {
                    Context context = getApplicationContext();
                    CharSequence success = context.getResources().getString(R.string.success_del);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast_success = Toast.makeText(context, success, duration);
                    toast_success.show();
                    overridePendingTransition(0, 0);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } else {
                    Context context = getApplicationContext();
                    CharSequence failure = context.getResources().getString(R.string.failure_del);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast_success = Toast.makeText(context, failure, duration);
                    toast_success.show();
                }
            }
        });
        increase = findViewById(R.id.increase_pills);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = pillRemainderAdapter.getPosition();
                if (myDBHandler_rem.increasePills(position)) {
                    Context context = getApplicationContext();
                    CharSequence success = context.getResources().getString(R.string.success_change);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast_success = Toast.makeText(context, success, duration);
                    toast_success.show();
                    overridePendingTransition(0, 0);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } else {
                    Context context = getApplicationContext();
                    CharSequence failure = context.getResources().getString(R.string.failure_change);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast_success = Toast.makeText(context, failure, duration);
                    toast_success.show();
                }
            }
        });

        reduce = findViewById(R.id.reduce_pills);
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = pillRemainderAdapter.getPosition();
                if (myDBHandler_rem.reducePills(position)) {
                    Context context = getApplicationContext();
                    CharSequence success = context.getResources().getString(R.string.success_change);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast_success = Toast.makeText(context, success, duration);
                    toast_success.show();
                    overridePendingTransition(0, 0);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } else {
                    Context context = getApplicationContext();
                    CharSequence failure = context.getResources().getString(R.string.failure_change);
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
    public void goToSetRemainder(View view) {
        finish();
        Intent i = new Intent(this, Set_Remainder.class);
        startActivity(i);
    }

    /**
     * Μέθοδος υπεύθυνη για την πληροφόρηση του χρήστη σχετικά με την χρήση του activity.
     * Επεξηγεί τον τρόπο που επιτυγχάνεται η κάθε αλληλεπίδραση που μπορεί να διεξάγει ο χρήστης στο
     * παρόν activity.
     * @param view
     */
    public void openRem(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.pill_rem_info_text);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
