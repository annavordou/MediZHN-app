package com.example.medizhn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
/**
 * Κλάση που επιτρέπει την σύνδεση με τη βάση δεδομένων που αφορά τα Φάρμακα.
 */

public class MyDBHandler_Med extends SQLiteOpenHelper{
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "medicineDB.db";
        public static final String TABLE_MEDICINES = "medicines";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MED_TITLE = "medicine_title";
        public static final String COLUMN_REASON = "medicine_reason";
        HashMap<Integer,Integer> keys=new HashMap<>();

    /**
     * Κατασκευαστής κλάσης
     **/
        public MyDBHandler_Med(Context context, String name,
                               SQLiteDatabase.CursorFactory factory, int version){
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        //Δημιουργία του σχήματος της ΒΔ (πίνακας products)
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                    TABLE_MEDICINES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_MED_TITLE + " TEXT," +
                    COLUMN_REASON + " TEXT" +")";
            db.execSQL(CREATE_PRODUCTS_TABLE);
        }
        //Αναβάθμιση ΒΔ: εδώ τη διαγραφώ και τη ξαναδημιουργώ ίδια
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINES);
            onCreate(db);
        }

    /**
     * Μέθοδος για την εισαγωγή νέας καταχώρησης στη βάση.
     * @param med η νέα καταχώρηση
     */
        public void addMedicine(Medicine med) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MED_TITLE, med.getMed_Title());
            values.put(COLUMN_REASON, med.getReason());
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_MEDICINES, null, values);
            db.close();
        }

        //Μέθοδος για εύρεση προϊόντος βάσει ονομασίας του
        public Medicine findMedicine(String med_name) {
            String query = "SELECT * FROM " + TABLE_MEDICINES + " WHERE " +
                    COLUMN_MED_TITLE + " = '" + med_name + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Medicine med = new Medicine();
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                med.setID(Integer.parseInt(cursor.getString(0)));
                med.setMed_Title(cursor.getString(1));
                med.setReason(cursor.getString(2));
                cursor.close();
            } else {
                med = null;
            }
            db.close();
            return med;
        }

    /**
     * Εύρεση συγκεκριμένης εγγραφής πίνακα χρησιμοποιώντας το id
     * @param id συγκεκριμένο id εγγραφής
     * @return cal μία καταχώρηση του χρήστη αν βρεθεί, null αν δε βρεθεί
     */
    public Medicine findMedicinebyId(int id) {
        String query = "SELECT * FROM " + TABLE_MEDICINES + " WHERE " + COLUMN_ID + " = '" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Medicine med = new Medicine();
        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            med.setID(Integer.parseInt(cursor.getString(0)));
            med.setMed_Title(cursor.getString(1));
            med.setReason(cursor.getString(2));
            cursor.close();
        }
        else{
            med = null;
        }
        db.close();
        return med;
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
        public boolean deleteMedicine(int position) {
            boolean result = false;
            if(position>=0 && !keys.isEmpty()){
            Medicine med = findMedicinebyId(keys.get(position));
            if (med != null){
                SQLiteDatabase db = this.getWritableDatabase();
                db.delete(TABLE_MEDICINES, COLUMN_ID + " = " + keys.get(position),
                        null);
                result = true;
                db.close();
            }
            }
            return result;
        }

    /**
     * Μέθοδος που συλλέγει τα δεδομένα προς εμφάνιση ανάλογα με συγκεκριμένη ημερομηνία. Ταυτόχρονα ρυθμίζει
     * τον πίνακα κατακερματισμού keys ο οποίος είναι παράλληλος με τον cardsData και περιέχει τα αντίστοιχα id.
     * @return HashMap cardsDataHashMap ο πίνακας που περιέχει όλα τα στοιχεία που θα εμφανίζονται πάνω στην κάρτα.
     */
    public HashMap<Integer, CardsData> getAllMedicines()
    {
        HashMap<Integer, CardsData> cardsDataHashMap = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_MEDICINES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = 0;
        while (cursor.moveToNext())
        {
            CardsData cardsData = new CardsData(cursor.getString(1), "Λόγος λήψης: " + cursor.getString(2));
            cardsDataHashMap.put(i, cardsData);
            keys.put(i,Integer.parseInt(cursor.getString(0)));
            i++;
        }
        cursor.close();
        db.close();
        return cardsDataHashMap;
    }


}
