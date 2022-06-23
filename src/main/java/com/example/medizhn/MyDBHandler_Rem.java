package com.example.medizhn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
/**
 * Κλάση που επιτρέπει την σύνδεση με τη βάση δεδομένων που αφορά τα υπολοιπόμενα δισκία ενός φαρμάκου.
 */
public class MyDBHandler_Rem extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "remainderDB.db";
        public static final String TABLE_REMAINDER = "remainder";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MED_TITLE = "medicine_title";
        public static final String COLUMN_NUMPILLS = "numpills";
        HashMap<Integer,Integer> keys=new HashMap<>();

    /**
     * Κατασκευαστής κλάσης
     **/
        public MyDBHandler_Rem(Context context, String name,
                               SQLiteDatabase.CursorFactory factory, int version){
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        }

        //Δημιουργία του σχήματος της ΒΔ (πίνακας products)
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                    TABLE_REMAINDER + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_MED_TITLE + " TEXT," +
                    COLUMN_NUMPILLS + " INTEGER" +")";
            db.execSQL(CREATE_PRODUCTS_TABLE);
        }
        //Αναβάθμιση ΒΔ: εδώ τη διαγραφώ και τη ξαναδημιουργώ ίδια
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMAINDER);
            onCreate(db);
        }

    /**
     * Μέθοδος για την εισαγωγή νέας καταχώρησης στη βάση.
     * @param rem η νέα καταχώρηση
     */
        public void addRemainder(Remainder rem) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MED_TITLE, rem.getMed_Title());
            values.put(COLUMN_NUMPILLS, rem.getNumpills());
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_REMAINDER, null, values);
            db.close();
        }

        //Μέθοδος για εύρεση προϊόντος βάσει ονομασίας του
        public Remainder findRemainder(String med_name) {
            String query = "SELECT * FROM " + TABLE_REMAINDER + " WHERE " +
                    COLUMN_MED_TITLE + " = '" + med_name + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Remainder rem = new Remainder();
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                rem.setID(Integer.parseInt(cursor.getString(0)));
                rem.setMed_Title(cursor.getString(1));
                rem.setNumpills(cursor.getString(2));
                cursor.close();
            } else {
                rem = null;
            }
            db.close();
            return rem;
        }

    /**
     * Εύρεση συγκεκριμένης εγγραφής πίνακα χρησιμοποιώντας το id
     * @param id συγκεκριμένο id εγγραφής
     * @return cal μία καταχώρηση του χρήστη αν βρεθεί, null αν δε βρεθεί
     */
    public Remainder findRemainderPillsbyId(int id) {
        String query = "SELECT * FROM " + TABLE_REMAINDER + " WHERE " + COLUMN_ID + " = '" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Remainder rem = new Remainder();
        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            rem.setID(Integer.parseInt(cursor.getString(0)));
            rem.setMed_Title(cursor.getString(1));
            rem.setNumpills(cursor.getString(2));
            cursor.close();
        }
        else{
            rem = null;
        }
        db.close();
        return rem;
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
    public boolean deleteRemainder(int position) {
            boolean result = false;
            if(position>=0 && !keys.isEmpty()){
                Remainder rem = findRemainderPillsbyId(keys.get(position));
                if (rem != null){
                    SQLiteDatabase db = this.getWritableDatabase();
                    db.delete(TABLE_REMAINDER, COLUMN_ID + " = " + keys.get(position),
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
    public HashMap<Integer, CardsData> getAllRemainders()
    {
        HashMap<Integer, CardsData> cardsDataHashMap = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_REMAINDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = 0;
        while (cursor.moveToNext())
        {
            CardsData cardsData = new CardsData(cursor.getString(1), "Υπολοιπόμενα δισκία: " + cursor.getString(2));
            cardsDataHashMap.put(i, cardsData);
            keys.put(i,Integer.parseInt(cursor.getString(0)));
            i++;
        }
        cursor.close();
        db.close();
        return cardsDataHashMap;
    }

    /**
     * Μέθοδος που αυξάνει το πλήθος των υπολοιπόμενων δισκίων κατά 1.
     * @param position θέση κάρτας
     * @return true αν επιτευχθεί η μετατροπή και false αν όχι
     */
    public boolean increasePills(int position) {
        boolean result = false;
        if(position>=0 && !keys.isEmpty()){
            Remainder rem = findRemainderPillsbyId(keys.get(position));
            if (rem != null){
                String num=Integer.toString(Integer.parseInt(rem.getNumpills())+1);
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("UPDATE remainder SET numpills='"+num+"' WHERE _id='"+keys.get(position)+"'");
                result=true;
            }
        }
        return result;
    }

    /**
     * Μέθοδος που μειώνει το πλήθος των υπολοιπόμενων δισκίων κατά 1.
     * @param position θέση κάρτας
     * @return true αν επιτευχθεί η μετατροπή και false αν όχι
     */
    public boolean reducePills(int position) {
        boolean result = false;
        if(position>=0 && !keys.isEmpty()){
            Remainder rem = findRemainderPillsbyId(keys.get(position));
            if(rem.getNumpills().equals("0")){
                return result;
            }
            if (rem != null){
                String num=Integer.toString(Integer.parseInt(rem.getNumpills())-1);
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("UPDATE remainder SET numpills='"+num+"' WHERE _id='"+keys.get(position)+"'");
                result=true;
            }
        }
        return result;
    }


}
