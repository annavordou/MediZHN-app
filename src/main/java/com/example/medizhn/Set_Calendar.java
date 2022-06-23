package com.example.medizhn;
/**
 * Αυτή η κλάση είναι υπεύθυνη για την προσθήκη νέων υπενθυμίσεων στο Ημερολόγιο ή αλλιώς στην κλάση CalendarSet.
 * Επίσης, κάνει την σύνδεση μεταξύ των αντικειμένων της CalEntry και της βάσης δεδομένων.
 */
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class Set_Calendar extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    EditText date_entry;//η μέρα που εισάγει ο χρήστης
    TextView time_entry;//η ώρα που εισάγει ο χρήστης
    EditText desc_entry;//η περιγραφή που εισάγει ο χρήστης

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_cal);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //η σύνδεση των μεταβλητών με το αρχείο xml
        date_entry = findViewById(R.id.edittext_date_reminder);
        time_entry = findViewById(R.id.text_time_reminder_show);
        desc_entry = findViewById(R.id.edittext_description_reminder);

        //Ακολουθεί η μετατροπή των αριθμών που θα δώσει ο χρήστης για την ημέρα, σε μετατροπή date format
        date_entry = (EditText) findViewById(R.id.edittext_date_reminder);
        date_entry.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //Έλεγχος ότι μπαίνουν σωστά νούμερα που αρμόζουν σε κάποια ημερομηνία
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12)
                            mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);

                        //Ελάχιστο έτος 2022 και μέγιστο 2100
                        year = (year < 2022) ? 2022 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date_entry.setText(current);
                    date_entry.setSelection(sel < current.length() ? sel : current.length());

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
            Intent i = new Intent(this, CalendarSet.class);
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
        Intent i = new Intent(this, CalendarSet.class);
        startActivity(i);
    }

    /**
     * Μέθοδος που εμφανίζει το Time Picker Fragment για την επιλογή ώρας
     * @param view
     */
    public void ButtonAlarmClock(View view){
        DialogFragment timePickerDialog =new TimePickerFragment();
        timePickerDialog.show(getSupportFragmentManager(),"time picker");
    }

    /**
     * Μέθοδος αλλαγής των στοιχείων του TextView σπου αφορά την ώρα
     * @param timePicker
     * @param intHourOfDay η ώρα της ημέρας
     * @param intMinute τα λεπτά
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int intHourOfDay, int intMinute){
        TextView textViewPicked=(TextView) findViewById(R.id.text_time_reminder_show);
        textViewPicked.setText((addZero(intHourOfDay) + ":" + addZero(intMinute)));
    }


    /**
     * Μέθοδος που γίνεται η δημιουργία της νέας υπενθύμισης και συνδέεται με τη βάση δεδομένων
     * @param view
     */
    public void newCalendarEntry (View view) {
        MyDBHandler_Cal dbHandler = new MyDBHandler_Cal(this, null, null, 1);
        String cal_date = date_entry.getText().toString();
        String cal_time = time_entry.getText().toString();
        String cal_desc = desc_entry.getText().toString();
        if (!cal_date.equals("") && !cal_time.equals("") && !cal_desc.equals("")){
                CalEntry cal = new CalEntry(cal_date, cal_time, cal_desc);
                dbHandler.addCalendar(cal);
                date_entry.setText("");
                time_entry.setText("");
                desc_entry.setText("");
                Context context = getApplicationContext();
                CharSequence success = context.getResources().getString(R.string.success_cal);
                int duration = Toast.LENGTH_SHORT;
                Toast toast_success = Toast.makeText(context, success, duration);
                toast_success.show();
                //Επιστροφή στην προηγούμενη δραστηριότητα και ανανέωσή της
                finish();
                Intent i = new Intent(this, CalendarSet.class);
                startActivity(i);
        }

    }

    /**
     * Αναζήτηση ύπαρξης υπενθύμισης
     * @param view
     */
    public void lookupCalendar (View view) {
        MyDBHandler_Cal dbHandler = new MyDBHandler_Cal(this, null, null, 1);
        CalEntry cal= dbHandler.findCalendar(date_entry.getText().toString());
        if (cal != null) {
            date_entry.setText(String.valueOf(cal.getCal_Date()));
        } else {
            date_entry.setText("");
        }

    }

    private String addZero(int i){
        String str=""+i;
        if(i<10)
            str="0"+i;
        return  str;
    }
}
