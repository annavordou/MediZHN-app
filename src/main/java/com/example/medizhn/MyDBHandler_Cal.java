package com.example.medizhn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Κλάση που επιτρέπει την σύνδεση με τη βάση δεδομένων που αφορά το ημερολόγιο και τις υπενθυμίσεις.
 */

public class MyDBHandler_Cal extends SQLiteOpenHelper{

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "calendarDB.db";
        public static final String TABLE_CALENDAR = "calendars";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_DATE = "calendar_date";
        public static final String COLUMN_TIME= "calendar_time";
        public static final String COLUMN_DESC= "calendar_desc";
        private HashMap<Integer,Integer> keys= new HashMap<>();//πίνακας κατακρματισμού που συγκεντρώνει τη θέση της κάρτας και τα αντίστοιχα id

    /**
     * Κατασκευαστής κλάσης
     **/
        public MyDBHandler_Cal(Context context, String name,
                               SQLiteDatabase.CursorFactory factory, int version){
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        //Δημιουργία του σχήματος της ΒΔ (πίνακας products)
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                    TABLE_CALENDAR + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_TIME + " TEXT," + COLUMN_DESC + " TEXT" +")";
            db.execSQL(CREATE_PRODUCTS_TABLE);
        }
        //Αναβάθμιση ΒΔ: εδώ τη διαγραφώ και τη ξαναδημιουργώ ίδια
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);
            onCreate(db);
        }


    /**
     * Μέθοδος για την εισαγωγή νέας καταχώρησης στη βάση.
     * @param cal η νέα καταχώρηση
     */
    public void addCalendar(CalEntry cal) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE, cal.getCal_Date());
            values.put(COLUMN_TIME, cal.getTime());
            values.put(COLUMN_DESC, cal.getDesc());
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_CALENDAR, null, values);
            db.close();
        }

        //Μέθοδος για εύρεση προϊόντος βάσει ονομασίας του
        public CalEntry findCalendar(String cal_date) {
            String query = "SELECT * FROM " + TABLE_CALENDAR + " WHERE " + COLUMN_DATE + " = '" + cal_date + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            CalEntry cal = new CalEntry();
            if(cursor.moveToFirst()) {
                cursor.moveToFirst();
                cal.setID(Integer.parseInt(cursor.getString(0)));
                cal.setCal_Date(cursor.getString(1));
                cal.setTime(cursor.getString(2));
                cal.setDesc(cursor.getString(3));
                cursor.close();
            }
            else{
                cal = null;
            }
            db.close();
            return cal;
        }

    /**
     * Εύρεση συγκεκριμένης εγγραφής πίνακα χρησιμοποιώντας το id
     * @param id συγκεκριμένο id εγγραφής
     * @return cal μία καταχώρηση του χρήστη αν βρεθεί, null αν δε βρεθεί
     */
    public CalEntry findCalendarbyId(int id) {
        String query = "SELECT * FROM " + TABLE_CALENDAR + " WHERE " + COLUMN_ID + " = '" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        CalEntry cal = new CalEntry();
        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            cal.setID(Integer.parseInt(cursor.getString(0)));
            cal.setCal_Date(cursor.getString(1));
            cal.setTime(cursor.getString(2));
            cal.setDesc(cursor.getString(3));
            cursor.close();
        }
        else{
            cal = null;
        }
        db.close();
        return cal;
    }

    /**
     * Μέθοδος υπεύθυνη για διαγραφή γραμμής πίνακα με τη χρήση θέσης της κάρτας που περιέχει τα δεδομένα.
     * Χρησιμοποιεί τον πίνακα με τα κλειδιά που συνδέει θέση του στοιχείου στο HashMap και βρίσκει το
     * αντίστοιχο id. Ελέγχει αν η θέση είναι άδεια και αν ο πίνακας κατακερματισμού είναι άδειος και
     * αν βρεθεί η καταχώρηση με τα συγκεκριμένα στοιχεία εντοπίζει την αντίστοιχη γραμμή στη βάση και
     * την διαγράφει.
     * @param position η θέση του αντικειμένου στον πίνακα κατακερματισμού
     * @return bool true οταν η διαγραφή είναι επιτυχής και false λόταν δεν είναι
     */
    public boolean deleteCalendar(int position) {
            boolean result = false;
            if(position>=0 && !keys.isEmpty()){
                CalEntry cal = findCalendarbyId(keys.get(position));
                if (cal != null){
                    SQLiteDatabase db = this.getWritableDatabase();
                    db.delete(TABLE_CALENDAR, COLUMN_ID + " = " + keys.get(position) ,
                            null);
                    //new String[] { String.valueOf(cal.getID()) }
                    result = true;
                    db.close();
                }
            }
            return result;
        }

    /**
     * Μέθοδος που συλλέγει τα δεδομένα προς εμφάνιση ανάλογα με συγκεκριμένη ημερομηνία. Ταυτόχρονα ρυθμίζει
     * τον πίνακα κατακερματισμού keys ο οποίος είναι παράλληλος με τον cardsData και περιέχει τα αντίστοιχα id.
     * @param date η ημερομηνία για την οποία συγκεντρώνουμε τις καταχωρήσεις
     * @return HashMap cardsData ο πίνακας που περιέχει όλα τα στοιχεία που θα εμφανίζονται πάνω στην κάρτα.
     */
        public HashMap<Integer, CardsData> cardsData(String date){
            keys.clear();
            HashMap<Integer,CardsData> cd= new HashMap<>();
            int i=0;
            String query = "SELECT * FROM " + TABLE_CALENDAR + " WHERE " + COLUMN_DATE + " = '" + date + "'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor =db.rawQuery(query, null);
                while(cursor.moveToNext()){
                    CalEntry entry=new CalEntry(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                    CardsData c= new CardsData(entry.getDesc()+" "+entry.getTime(),date);
                    c.setId(Integer.parseInt(cursor.getString(0)));
                    cd.put(i,c);
                    keys.put(i,c.getId());
                    i++;
                }
                cursor.close();
                db.close();
                return cd;
        }




}


