package com.iSales.database.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.awt.font.TextAttribute;

public class DatabaseHelper extends SQLiteOpenHelper {
    private String TAG = DatabaseHelper.class.getSimpleName();

    public static final String EVENT_TABLE_NAME = "eventdtable";
    public static final String EVENT = "event";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";

    private static final String CREATE_EVENTS_TABLE = "create table "+EVENT_TABLE_NAME+" (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EVENT+" TEXT, " +
            TIME+" TEXT, "+
            DATE+" TEXT, "+
            MONTH+" TEXT, "+
            YEAR+" TEXT) ";

    private static final String DROPE_EVENTS = "DROP TABLE IF EXISTs "+EVENT_TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, "EVENTS_DB", null, 1);
        Log.e("DB: ", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROPE_EVENTS);
        onCreate(db);
    }

    public void saveEvents(String event, String time, String date, String month, String year){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT, event);
        contentValues.put(TIME, time);
        contentValues.put(DATE, date);
        contentValues.put(MONTH, month);
        contentValues.put(YEAR, year);
        db.insert(EVENT_TABLE_NAME, null, contentValues);
    }

    public Cursor getEvents(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {EVENT, TIME, DATE, MONTH, YEAR};
        String selection = DATE+" = ?";
        String[] selectionArgs = {date};

        return db.query(EVENT_TABLE_NAME, projections, selection, selectionArgs, null, null, null);
    }

    public Cursor getEventsMonth(String month, String year){
        Log.e(TAG," getEventsMonth("+month+", "+year+") started.");

        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {EVENT, TIME, DATE, MONTH, YEAR};
        String selection = MONTH+" = ? and "+YEAR+" = ?";
        String[] selectionArgs = {month, year};

        return db.query(EVENT_TABLE_NAME, projections, selection, selectionArgs, null, null, null);
    }
}
