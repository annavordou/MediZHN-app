package com.example.medizhn;
/**
 * Αυτή η κλάση επιτυγχάνει την λειτουργικότητα του ημερολογίου. Είναι υπεύθυνη για τη ρύθμιση του activity_calendar
 * και την παρουσίαση τους με δυναμικό τρόπο στην εφαρμογή.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class CalendarSet extends AppCompatActivity {

    private FloatingActionButton fab;//floating action button var
    private Button cal;//date picker var
    private DatePickerDialog datePickerDialog;
    DatePicker datePicker; //μεταβλητή τύπου Datepicker
    String date;//ημερομηνία που λαμβάνεται απο το Datepicker
    RecyclerView reminderLayout;//διάταξη του RecyclerView
    RecyclerView.LayoutManager layoutManager;//διαχειριστής διάταξης του Recycler View
    EditText cal_1;
    TextView time_1;
    ImageView delete;//κουμπί διαγραφής κάρτας
    int position=-1;//θέση κάρτας
    HashMap<Integer,CardsData> cd;//HashMap με το περιεχόμενο των καρτών
    MyDBHandler_Cal dbHandler_cal = new MyDBHandler_Cal(this, null, null, 1);//χειριστής βάσης δεδομένων

    @RequiresApi(api = Build.VERSION_CODES.O)


/**
 * Η μέθοδος onCreate υλοποιεί τα δυναμικά στοιχεία της διεπαφής. Χρησιμοποιεί listeners για να αντιδρά σε επιλογές του χρήστη
 * επιτρέποντας τη διαγραφή καρτών, την εμφάνιση πληροφοριών σχετικά με τον τρόπο λειτουργίας του activity και περιέχει την μέθοδο
 * που ανακατευθύνει τον χρήστη ώστε να φτιάξει νέα καταχώρηση.
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // κλήση του action bar
        ActionBar actionBar = getSupportActionBar();
        // εμφάνιση κουμπιού επιστροφής action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //Αρχικοποίηση στοιχείων datepicker,date,reminderLayout
        datePicker = (DatePicker) findViewById(R.id.RemindersCalendar);
        date= addZero(datePicker.getDayOfMonth())+"/"+addZero(datePicker.getMonth() + 1)+"/"+ datePicker.getYear();
        reminderLayout = findViewById(R.id.RemindersLayout);

        //Καθορισμός της διάταξης των στοιχείων του Recycler View
        layoutManager = new LinearLayoutManager(this);
        reminderLayout.setLayoutManager(layoutManager);
        //Καθορισμός των δεδομένων του Recycler View μέσω της βάσης δεδομένων
        cd= dbHandler_cal.cardsData(date);

        //Αρχικοποίηση του προσαρμογέα του Recycler View
        CalendarSetAdapter csa= new CalendarSetAdapter(cd, CalendarSet.this);
        reminderLayout.setAdapter(csa);
        //Ενημέρωση θέσης της κάρτας που έχει κάνει κλικ ο χρήστης
        position= csa.getPosition();

        /**
         * Ακροατής γεγονότων υπεύθυνος για την αντίδραση στην περίπτωση αλλαγής ημερομηνίας που είναι
         * επιλεγμένη από το ημερολόγιο
         */
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
                //Αλλαγή ημερομηνίας
                date= addZero(datePicker.getDayOfMonth())+"/"+addZero(datePicker.getMonth() + 1)+"/"+ datePicker.getYear();
                // Εμφάνιση νέων τιμών μέσω του recycler_view
                reminderLayout = findViewById(R.id.RemindersLayout);
                cd= dbHandler_cal.cardsData(date);
                csa.CalendarSetAdapterSetter(cd, CalendarSet.this);
                reminderLayout.setAdapter(csa);
                position= csa.getPosition();

            }
        });
        //Αρχικοποίηση κουμπιού διαγραφής κάρτας
        delete= findViewById(R.id.delete_cal);
        /**
         * Ακροατής γεγονότων υπεύθυνος για την αντίδραση στην περίπτωση που ο χρήστης κάνει απόπειρα
         * διαγραφής κάρτας,
         **/
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //εντοπισμός θέσης κάρτας
                position= csa.getPosition();
                //προσπάθεια έυρεσης δεδομένων προς διαγραφή
               if(dbHandler_cal.deleteCalendar(position)){
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
    public void goToSetCalendar (View view) {
        finish();
        Intent i = new Intent(this, Set_Calendar.class);
        startActivity(i);
    }

    /**
     * Μέθοδος υπεύθυνη για την επεξεργασία δεδομένων ημερομηνίας και ώρας. Αν ένας αριθμός είναι <10
     * προστίθεται ένα 0 μπροστά του και επιστρέφεται ως String.
     * @param i ο αριθμός προς επεξεργασία
     * @return str η συμβολοσειρά που επιστρέφεται μετά την επεργασίσ
     */
    private String addZero(int i){
        String str=""+i;
        if(i<10)
            str="0"+i;
        return  str;
    }

    /**
     * Μέθοδος υπεύθυνη για την πληροφόρηση του χρήστη σχετικά με την χρήση του activity.
     * Επεξηγεί τον τρόπο που επιτυγχάνεται η κάθε αλληλεπίδραση που μπορεί να διεξάγει ο χρήστης στο
     * παρόν activity.
     * @param view
     */
    public void openCal(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.cal_info_text );
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}




