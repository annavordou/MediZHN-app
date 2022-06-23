package com.example.medizhn;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
/**
 * Κλάση που επιτρέπει την σύνδεση με τη βάση δεδομένων που αφορά τα παραπεμπτικά.
 */
public class MyDBHandler_Ref extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "referralsDB.db";
    public static final String TABLE_REFERRALS = "referrals";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REF_TITLE = "referral_title";
    public static final String COLUMN_FROM = "from_date";
    public static final String COLUMN_TO = "to_date";
    HashMap<Integer,Integer> keys=new HashMap<>();

    /**
     * Κατασκευαστής κλάσης
     **/
    public MyDBHandler_Ref(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Δημιουργία του σχήματος της ΒΔ (πίνακας products)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_REFERRALS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_REF_TITLE + " TEXT," +
                COLUMN_FROM + " TEXT," + COLUMN_TO + " TEXT" +")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
    //Αναβάθμιση ΒΔ: εδώ τη διαγραφώ και τη ξαναδημιουργώ ίδια
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REFERRALS);
        onCreate(db);
    }

    /**
     * Μέθοδος για την εισαγωγή νέας καταχώρησης στη βάση.
     * @param ref η νέα καταχώρηση
     */
    public void addReferral(Referral ref) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REF_TITLE, ref.getRef_Title());
        values.put(COLUMN_FROM, ref.getFrom());
        values.put(COLUMN_TO, ref.getTo());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_REFERRALS, null, values);
        db.close();
    }

    //Μέθοδος για εύρεση παραπεμπτικού βάσει ονομασίας του
    public Referral findReferral(String ref_name) {
        String query = "SELECT * FROM " + TABLE_REFERRALS + " WHERE " +
                COLUMN_REF_TITLE + " = '" + ref_name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Referral refs = new Referral();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            refs.setID(Integer.parseInt(cursor.getString(0)));
            refs.setRef_Title(cursor.getString(1));
            refs.setFrom(cursor.getString(2));
            refs.setTo(cursor.getString(3));
            cursor.close();
        } else {
            refs = null;
        }
        db.close();
        return refs;
    }

    /**
     * Εύρεση συγκεκριμένης εγγραφής πίνακα χρησιμοποιώντας το id
     * @param id συγκεκριμένο id εγγραφής
     * @return cal μία καταχώρηση του χρήστη αν βρεθεί, null αν δε βρεθεί
     */
    public Medicine findMedicinebyId(int id) {
        String query = "SELECT * FROM " + TABLE_REFERRALS + " WHERE " + COLUMN_ID + " = '" + id + "'";
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
    public boolean deleteReferral(int position) {
        boolean result = false;
        if(position>=0 && !keys.isEmpty()){
            Medicine med = findMedicinebyId(keys.get(position));
            if (med != null){
                SQLiteDatabase db = this.getWritableDatabase();
                db.delete(TABLE_REFERRALS, COLUMN_ID + " = " + keys.get(position),
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
    public HashMap<Integer, CardsData> getAllReferrals()
    {
        HashMap<Integer, CardsData> cardsDataHashMap = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_REFERRALS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int i = 0;
        while (cursor.moveToNext())
        {
            CardsData cardsData = new CardsData(cursor.getString(1), "Από: " + cursor.getString(2) + "    Μέχρι: " + cursor.getString(3));
            cardsDataHashMap.put(i, cardsData);
            keys.put(i,Integer.parseInt(cursor.getString(0)));
            i++;
        }
        cursor.close();
        db.close();
        return cardsDataHashMap;
    }

}
