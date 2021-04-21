package com.example.layaoutexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "Shift_Time1";
    private static final String COL0 = "id";
    private static final String COL1 = "date";
    private static final String COL2 = "hours";

    public DataBaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " + COL1 + " TEXT, " + COL2 + " INTEGER)";
        db.execSQL(createTable);


    }

    public void onCreate1(SQLiteDatabase db) {
        String createTable = "CREATE TABLE config  ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, companyName TEXT,rate INTEGER)";

        db.execSQL(createTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP  TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String date, int hours) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, date);
        contentValues.put(COL2, hours);

        Log.d(TAG, "AddData: Adding " + date + "to" + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }


    }


    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public void updateHours(int hours, int id, int oldhours) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET " + COL2 + "='" + hours + "' WHERE " + COL0 + "='" + id + "'";
        db.execSQL(sql);

    }


}
