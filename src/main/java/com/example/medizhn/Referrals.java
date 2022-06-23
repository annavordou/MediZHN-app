package com.example.medizhn;
/**
 * Αυτή η κλάση επιτυγχάνει την λειτουργικότητα των Παραπεμπτικών. Είναι υπεύθυνη για τη ρύθμιση του activity_medicines
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

public class Referrals extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int position=-1;
    ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.referrals_layout);

        //Set the layout of the items in the RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Cards' data
        MyDBHandler_Ref myDBHandler_ref = new MyDBHandler_Ref(this, null, null, 1);
        HashMap<Integer, CardsData> cardsData = myDBHandler_ref.getAllReferrals();

        //Set my Adapter for the RecyclerView
        ReferralsAdapter referralsAdapter = new ReferralsAdapter(cardsData, Referrals.this);
        recyclerView.setAdapter(referralsAdapter);
        delete= findViewById(R.id.delete_ref);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position= referralsAdapter.getPosition();
                if(myDBHandler_ref.deleteReferral(position)){
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
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence failure = context.getResources().getString(R.string.failure_del);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast_success = Toast.makeText(context, failure, duration);
                    toast_success.show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void goToSet_Referrals(View view) {
        finish();
        Intent intent = new Intent(this, Set_Referral.class);
        startActivity(intent);
    }

    /**
     * Μέθοδος υπεύθυνη για την πληροφόρηση του χρήστη σχετικά με την χρήση του activity.
     * Επεξηγεί τον τρόπο που επιτυγχάνεται η κάθε αλληλεπίδραση που μπορεί να διεξάγει ο χρήστης στο
     * παρόν activity.
     * @param view
     */
    public void openRef(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.ref_info_text);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
