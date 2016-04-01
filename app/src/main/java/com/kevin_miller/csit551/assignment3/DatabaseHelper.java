package com.kevin_miller.csit551.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kevin_miller.csit551.assignment3.DBContract.FeedEntry;

/**
 * Created by Kevin on 3/30/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, FeedEntry.DB_NAME, null, FeedEntry.DB_VERSION);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_String = "CREATE TABLE "+FeedEntry.TABLE_NAME+" (" +
                FeedEntry.COL_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FeedEntry.COL_USERNAME + " TEXT,"+
                FeedEntry.COL_FULLNAME + " TEXT,"+
                FeedEntry.COL_DOB      + " TEXT,"+
                FeedEntry.COL_MAJOR    + " TEXT,"+
                FeedEntry.COL_EMAIL    + " TEXT,"+
                FeedEntry.COL_PASS     + " TEXT"+")";
        db.execSQL(SQL_String);
    }

    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+FeedEntry.TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String id, String username, String major, String email, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COL_USERNAME,username);
        contentValues.put(FeedEntry.COL_MAJOR,major);
        contentValues.put(FeedEntry.COL_EMAIL,email);
        contentValues.put(FeedEntry.COL_PASS,password);
        long result = db.update(FeedEntry.TABLE_NAME, contentValues, "ID=?", new String[]{id});
        return result != -1;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(FeedEntry.TABLE_NAME, "ID=?", new String[]{id}); //return 0 or less if nothing deleted
    }

    public void deleteDatabase(String dbName){
        this.deleteDatabase(dbName);
    }

    public Cursor getRecForUsername(String username){
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            String sql = "SELECT * FROM "+ FeedEntry.TABLE_NAME +" WHERE "+FeedEntry.COL_USERNAME+" LIKE '"+username+"';";
            Cursor res = db.rawQuery(sql,null);
            return res;
        }catch (Exception e){
            Log.e("Hmm", "Let's fix it");
            e.printStackTrace();
            return null;
        }

    }

    public boolean insertData(String username, String fullname, String dob, String major, String email, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COL_USERNAME,username.toLowerCase());
        contentValues.put(FeedEntry.COL_FULLNAME,fullname);
        contentValues.put(FeedEntry.COL_DOB, dob);
        contentValues.put(FeedEntry.COL_MAJOR,major);
        contentValues.put(FeedEntry.COL_EMAIL,email.toLowerCase());
        contentValues.put(FeedEntry.COL_PASS,password);
        long result = db.insert(FeedEntry.TABLE_NAME,null, contentValues);
        return result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME);
        onCreate(db);
    }

    public Cursor login(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + FeedEntry.TABLE_NAME + " WHERE " + FeedEntry.COL_USERNAME + " = '" + username + "' AND " + FeedEntry.COL_PASS + " = '" + password + "';";
        try {
            Cursor cursor = db.rawQuery(sql, null);
            return cursor;
        } catch (Exception e) {
            Log.d("KSM", "SQL Login exception");
            e.printStackTrace();
            return null;
        }
    }

}
