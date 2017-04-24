package org.kap.louvainlinux.ludovic.smspooling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ludovic Zipsin on 26/11/16.
 * Mail: ludovic.j.r.zipsin@gmail.com
 * Github: https://github.com/LudoZipsin
 */
public class DBHelper extends SQLiteOpenHelper{

    protected static final String DATABASE_NAME = "SMSPooling";

    protected static final String TABLE_NAME_APPLICATION = "application";
    protected static final String COLUMN_APPLICATION_POOL = "pool";
    protected static final String COLUMN_APPLICATION_ID = "id";

    protected static final String TABLE_NAME_POOL ="pool";
    protected static final String COLUMN_POOL_NAME ="name";
    protected static final String COLUMN_POOL_DATE ="date";

    protected static final String TABLE_NAME_PARTICIPANT = "participant";
    protected static final String COLUMN_PARTICIPANT_ID = "id";
    protected static final String COLUMN_PARTICIPANT_NAME = "name";
    protected static final String COLUMN_PARTICIPANT_NUMBER = "number";
    protected static final String COLUMN_PARTICIPANT_POOL = "pool";
    protected static final String COLUMN_PARTICIPANT_VOTE = "vote";

    protected static final String TABLE_NAME_PHONE= "phone";
    protected static final String COLUMN_PHONE_NUMBER = "number";

    protected static final String TABLE_NAME_M2M_PHONE_POOL = "m2m_phone_pool";
    protected static final String COLUMN_M2M_ID = "id";
    protected static final String COLUMN_M2M_PHONE_NUMBER = "number";
    protected static final String COLUMN_M2M_POOL_NAME = "name";
    protected static final String COLUMN_M2M_VOTE = "vote";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Initialise DB if not already existing
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // creation of application data
        sqLiteDatabase.execSQL(
                "create table " + TABLE_NAME_APPLICATION + "(" +
                        COLUMN_APPLICATION_ID + " INTEGER PRIMARY KEY, " + // DEFAULT TO 1
                        COLUMN_APPLICATION_POOL + " TEXT" + // DEFAULT TO ""
                        ")"
        );
        // creation of pool table
        sqLiteDatabase.execSQL(
                "create table " + TABLE_NAME_POOL + "(" +
                        COLUMN_POOL_NAME + " TEXT PRIMARY KEY, " +
                        COLUMN_POOL_DATE + " TEXT" +
                        ")"
        );
        // creation of participant table
        sqLiteDatabase.execSQL(
                "create table " + TABLE_NAME_PARTICIPANT + "(" +
                        COLUMN_PARTICIPANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_PARTICIPANT_NAME + " TEXT NOT NULL, " +
                        COLUMN_PARTICIPANT_NUMBER + " INTEGER, " +
                        COLUMN_PARTICIPANT_POOL + " TEXT, " +
                        COLUMN_PARTICIPANT_VOTE + " INTEGER NOT NULL, " +
                        " FOREIGN KEY (" + COLUMN_PARTICIPANT_POOL + ") REFERENCES " + TABLE_NAME_POOL + "(" + COLUMN_POOL_NAME + ")" +
                        ")"
        );
        // creation of phoneNumber table
        sqLiteDatabase.execSQL(
                "create table " + TABLE_NAME_PHONE + "(" +
                        COLUMN_PHONE_NUMBER + " TEXT PRIMARY KEY" +
                        ")"
        );
        // creation of table many to many for phone number and pool
        sqLiteDatabase.execSQL(
                "create table " + TABLE_NAME_M2M_PHONE_POOL + "(" +
                        COLUMN_M2M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_M2M_PHONE_NUMBER + " TEXT, " +
                        COLUMN_M2M_POOL_NAME + " TEXT, " +
                        COLUMN_M2M_VOTE + " INTEGER, " +
                        " FOREIGN KEY (" + COLUMN_M2M_PHONE_NUMBER + ") REFERENCES " + TABLE_NAME_PHONE + "(" + COLUMN_PHONE_NUMBER + "), " +
                        " FOREIGN KEY (" + COLUMN_M2M_POOL_NAME + ") REFERENCES " + TABLE_NAME_POOL + "(" + COLUMN_POOL_NAME + ")" +
                        ")"
        );
//        sqLiteDatabase.close();
    }

    /**
     * Migration code if needing hotfix update to the db
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // NOTHING TO DO HERE
    }

    /**
     * Initiate the application basic data
     */
    public void initAPPData(){
        if (numberOfEntries(TABLE_NAME_APPLICATION) == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_APPLICATION_POOL, "");
            contentValues.put(COLUMN_APPLICATION_ID, 1);
            long test = db.insert(TABLE_NAME_APPLICATION, null, contentValues);
            db.close();
        }
    }

    /**
     * Set the current pool. If the string is void (""), it means there is no pool currently running
     * @param pool
     */
    public void setAppPool(String pool){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_APPLICATION_POOL, pool);
        int test = db.update(TABLE_NAME_APPLICATION, contentValues, COLUMN_APPLICATION_ID+"="+1 +"", null);
        db.close();
    }

    /**
     * Deactivate the pooling. It means setting the pool to void ("")
     */
    public void deactivateAppPool(){
        setAppPool("");
    }

    /**
     * return the current pool (if running). If not, return null
     * @return
     */
    public String getAppPool(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_APPLICATION + " WHERE " + COLUMN_APPLICATION_ID + "=" + 1 + "", null);
        cursor.moveToFirst();
        String pool = cursor.getString(cursor.getColumnIndex(COLUMN_APPLICATION_POOL));
        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        return pool.equals("") ? null : pool;
    }

    /**
     * Insert a new Pool inside
     * @param name
     * @param date
     */
    public boolean insertPool(String name, String date){
        if (getPool(name) != null){
            Log.e("DBHelper", "Pool " + name + " already exist. Can't insert it...");
            return false;
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_POOL_NAME, name);
            contentValues.put(COLUMN_POOL_DATE, date);
            db.insert(TABLE_NAME_POOL, null, contentValues);
            db.close();
            return true;
        }
    }

    /**
     * Delete a Pool
     * @param name
     */
    public void deletePool(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_PARTICIPANT + " WHERE " + COLUMN_PARTICIPANT_POOL + "='" + name + "'");
        db.execSQL("DELETE FROM " + TABLE_NAME_POOL + " where " + COLUMN_POOL_NAME + "='"+ name +"'");
        db.execSQL("DELETE FROM " + TABLE_NAME_M2M_PHONE_POOL + " where " + COLUMN_M2M_POOL_NAME + "='"+ name +"'");
        db.close();
    }

    /**
     * return the pool if exist. If not, return null
     * @param name
     * @return
     */
    public Pool getPool(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_POOL + " WHERE " + COLUMN_POOL_NAME + "='"+name+"'", null);
        Pool pool = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            pool = new Pool(
                    cursor.getString(cursor.getColumnIndex(COLUMN_POOL_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_POOL_DATE))
            );
            cursor.close();
        }
        db.close();
        return pool;
    }

    /**
     * return the number of entries inside the specified table
     * @param tableName
     * @return
     */
    private int numberOfEntries(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        int returned =  (int) DatabaseUtils.queryNumEntries(db, tableName);
        db.close();
        return returned;
    }

    /**
     * Get the number of Pool inside db
     * @return
     */
    public int numberOfPool(){
        return numberOfEntries(TABLE_NAME_POOL);
    }

    /**
     * Get the number of Participant inside db (mostly for test purpose)
     * @return
     */
    public int numberOfParticipant(){
        return numberOfEntries(TABLE_NAME_PARTICIPANT);
    }

    /**
     * Get the number of Participant inside the db for a specified Pool
     * @param poolName
     * @return
     */
    public int numberOfParticipant(String poolName){
        SQLiteDatabase db = this.getReadableDatabase();
        int nbrParticipant = (int) DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + TABLE_NAME_PARTICIPANT + " where " + COLUMN_PARTICIPANT_POOL + "='" + poolName + "'", null);
        db.close();
        return nbrParticipant;
    }

    /**
     * get all the pools
     * @return
     */
    public ArrayList<Pool> getPools(){
        ArrayList<Pool> pools = new ArrayList<Pool>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_POOL, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            pools.add(new Pool(
                    cursor.getString(cursor.getColumnIndex(COLUMN_POOL_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_POOL_DATE))));
            cursor.moveToNext();
        }
        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        return pools;
    }

    public boolean insertParticipant(String participantName, String poolName){
        if (getParticipant(participantName, poolName) != null){
            Log.e("DBHelper", "Participant " + participantName + " inside pool " + poolName + " already exist. Can't insert it...");
            return false;
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_PARTICIPANT_NAME, participantName);
            contentValues.put(COLUMN_PARTICIPANT_NUMBER, 0);
            contentValues.put(COLUMN_PARTICIPANT_POOL, poolName);
            contentValues.put(COLUMN_PARTICIPANT_VOTE, 0);
            db.insert(TABLE_NAME_PARTICIPANT, null, contentValues);
            db.close();
            return true;
        }
    }

    public void deleteParticipant(String participantName, String poolName){
        SQLiteDatabase db = this.getWritableDatabase();
        Participant participant = getParticipant(participantName, poolName);
        db.execSQL("DELETE FROM " + TABLE_NAME_PARTICIPANT + " WHERE " + COLUMN_PARTICIPANT_NAME + "='" + participantName + "' AND " + COLUMN_PARTICIPANT_POOL + "='" + poolName + "'");
        db.execSQL("DELETE FROM " + TABLE_NAME_M2M_PHONE_POOL + " WHERE " + COLUMN_M2M_VOTE + "='" + participant.getNumber() + "' AND " + COLUMN_M2M_POOL_NAME + "='" + poolName + "'");
        db.close();
    }

    /**
     * Get the id of a participant. If it doesn't exist, then return null
     * @param participantName
     * @param poolName
     * @return
     */
    public Participant getParticipant(String participantName, String poolName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_PARTICIPANT +
                " WHERE " + COLUMN_PARTICIPANT_NAME + "='" + participantName + "'" +
                " AND " + COLUMN_PARTICIPANT_POOL + "='" + poolName + "'", null);
        Participant participant = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            participant = new Participant(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_VOTE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_POOL)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID))
            );
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        db.close();
        return participant;
    }

    public Participant getParticipant(int participantNumber, String poolName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_PARTICIPANT +
                " WHERE " + COLUMN_PARTICIPANT_NUMBER + "=" + participantNumber + "" +
                " AND " + COLUMN_PARTICIPANT_POOL + "='" + poolName + "'", null);
        Participant participant = null;
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            participant = new Participant(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_VOTE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_POOL)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID))
            );
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        else {
            Log.e("DBHelper", "Too many participant with the same number...");
        }
        db.close();
        return participant;
    }


    /**
     * return a list of all participant inside a given pool
     * @param poolName
     * @return
     */
    public ArrayList<Participant> getParticipants(String poolName){
        ArrayList<Participant> participants = new ArrayList<Participant>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_PARTICIPANT + " WHERE " + COLUMN_PARTICIPANT_POOL + "='" + poolName + "'", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            participants.add(new Participant(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_VOTE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_POOL)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID))
            ));
            cursor.moveToNext();
        }
        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        return participants;
    }

    public void setParticipantNumber(int number, String participantName, String poolName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PARTICIPANT_NUMBER, number);
        db.update(TABLE_NAME_PARTICIPANT, contentValues,
                COLUMN_PARTICIPANT_NAME + "='" + participantName + "'" +
                " AND " + COLUMN_PARTICIPANT_POOL + "='" + poolName + "'",
                null);
        db.close();
    }

    public void setParticipantVote(int vote, String participantName, String poolName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PARTICIPANT_VOTE, vote);
        db.update(TABLE_NAME_PARTICIPANT, contentValues,
                COLUMN_PARTICIPANT_NAME + "='" + participantName + "'" +
                        " AND " + COLUMN_PARTICIPANT_POOL + "='" + poolName + "'",
                null);
        db.close();
    }

    public void increaseParticipantVote(String participantName, String poolName){
        Log.e("current DEC", "1");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("current DEC", "2");
        Participant participant = getParticipant(participantName, poolName);
        // We assume participant always exists
        Log.e("current DEC", "3");
        Log.e("current DEC", participant.getName());
        Log.e("current DEC", participant.getName() + " " + participant.getVote());
        ContentValues contentValues = new ContentValues();
        Log.e("current DEC", "4");
        int newCount = participant.getVote() + 1;
        setParticipantVote(newCount, participantName, poolName);
        Log.e("current DEC", "6");
        db.close();
        Log.e("current DEC", "7");
    }

    public void decreaseParticipantVote(String participantName, String poolName){
        Log.e("current DEC", "1");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("current DEC", "2");
        Participant participant = getParticipant(participantName, poolName);
        // We assume participant always exists
        Log.e("current DEC", "3");
        Log.e("current DEC", participant.getName());
        Log.e("current DEC", participant.getName() + " " + participant.getVote());
        ContentValues contentValues = new ContentValues();
        Log.e("current DEC", "4");
        int newCount = participant.getVote() - 1;
        setParticipantVote(newCount, participantName, poolName);
        Log.e("current DEC", "6");
        db.close();
        Log.e("current DEC", "7");
    }

    public void makeVote(String phoneNumber, int vote, String poolName){
        Log.e("current", "1");
        insertPhoneNumber(phoneNumber);
        Log.e("current", "2");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("current", "3");
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_M2M_PHONE_POOL +
                " WHERE " + COLUMN_M2M_POOL_NAME + "='" + poolName + "'" +
                " AND " + COLUMN_M2M_PHONE_NUMBER + "='" + phoneNumber + "'", null);
        Log.e("current", "4");
        if (cursor.getCount() > 0) { // This phone number already votes...
            Log.e("current", "5");
            cursor.moveToFirst();
            Log.e("current", "6");
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_M2M_ID));
            Log.e("current", "7");
            int oldVote = cursor.getInt(cursor.getColumnIndex(COLUMN_M2M_VOTE));
            Log.e("current", "8");
            Participant participantOld = getParticipant(oldVote, poolName);

            Log.e("current", "9");
            Log.e("current", "9" + participantOld);
            Participant participantNew = getParticipant(vote, poolName);
            Log.e("current", "10");
            Log.e("current", "10 " + participantNew);
            if ((participantNew != null) && (participantOld != null)){
                Log.e("current", "11");
                Log.e("current", participantOld.getName());
                Log.e("current", participantNew.getName());
                decreaseParticipantVote(participantOld.getName(), participantOld.getPool());
                Log.e("current", "12");
                increaseParticipantVote(participantNew.getName(), participantNew.getPool());
                Log.e("current", "13");
                updateM2M(phoneNumber, poolName, vote);
                Log.e("current", "14");
            }
        }
        else {
            Log.e("current", "15");
            Participant participantNew = getParticipant(vote, poolName);
            Log.e("current", "16");
            if (participantNew != null){
                Log.e("current", "17");
                insertM2M(phoneNumber, vote, poolName);
                Log.e("current", "18");
                increaseParticipantVote(participantNew.getName(), participantNew.getPool());
                Log.e("current", "19");
            }
        }
    }

    public void updateM2M(String phoneNumber, String pool, int vote){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_M2M_VOTE, vote);
        db.update(TABLE_NAME_M2M_PHONE_POOL, contentValues,
                COLUMN_M2M_PHONE_NUMBER + "='" + phoneNumber + "'" +
                " AND " + COLUMN_M2M_POOL_NAME + "='" + pool + "'",
                null);
        db.close();
    }

    public void insertM2M(String phoneNumber, int vote, String pool){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_M2M_PHONE_NUMBER, phoneNumber);
        contentValues.put(COLUMN_M2M_POOL_NAME, pool);
        contentValues.put(COLUMN_M2M_VOTE, vote);
        db.insert(TABLE_NAME_M2M_PHONE_POOL, null, contentValues);
        db.close();
    }

    public boolean insertPhoneNumber(String phoneNumber){
        if (phoneNumberPresent(phoneNumber)){
            Log.e("DBHelper", "Phone number " + phoneNumber + " already exist. Can't insert it...");
            return false;
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
            db.insert(TABLE_NAME_PHONE, null, contentValues);
            db.close();
            return true;
        }
    }

    public boolean phoneNumberPresent(String phone){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_PHONE + " WHERE " + COLUMN_PHONE_NUMBER + "='" + phone + "'", null);
        boolean isThere = (cursor.getCount() > 0) ? true : false;

        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return isThere;
    }

}
