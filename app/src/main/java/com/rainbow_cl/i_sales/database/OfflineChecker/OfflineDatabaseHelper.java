package com.rainbow_cl.i_sales.database.OfflineChecker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
* This class will create a sqlite db to store 3 different values true/false
* so when the user load client, categories and commander fragments for the first time
* iSales will download data from the server and CHECK it as true so if/when the current user return
* to those 3 fragments the app will load the data stored in the local db
* */

public class OfflineDatabaseHelper extends SQLiteOpenHelper {
    private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window

    private static final String DATABASE_NAME ="OfflineChecker.db";
    private static final String TABLE_NAME = "OfflineDataChecker";
    private static final String COL_1 ="ID";
    private static final String COL_2 ="clientChecker";
    private static final String COL_3 ="productsChecker";
    private static final String COL_4 ="ordersChecker";

    public OfflineDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_2+" INTEGER, "+COL_3+" INTEGER, "+COL_4+" INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    // Inserting all check data
    public boolean insertDataCheckStatus(int clientCheck, int productsChecker, int ordersChecker){
        SQLiteDatabase db = this.getWritableDatabase();

        boolean result = false;
        if (clientCheck == 0 && productsChecker == 0 && ordersChecker == 0){
            db.execSQL("insert into OfflineDataChecker values(1, "+clientCheck+", "+productsChecker+", "+ordersChecker+")");
            result = true;
            Log.e("DB: ", "insert is done");
        }else {
            result = false;
            Log.e("DB: ", "insert failed");
        }

        if (!result){
            db.close();
            return false;
        }else {
            db.close();
            return true;
        }
    }

    //Get the first and only data
    public Cursor getDataChecker(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where ID == 1", null);
        return res;
    }

    // Update the table / reset it
    public void resetOfflineCheck(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_2+" INTEGER, "+COL_3+" INTEGER, "+COL_4+" INTEGER)");
    }

    // Updating the client check
    public boolean updateOfflineClientCheck(int clientStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;

        // clientChecker is false
        if (clientStatus == 0){
            db.execSQL("update OfflineDataChecker set "+COL_2+" = 0 where ID == 1");
            result = false;
        }
        // clientChecker is true
        if (clientStatus == 1){
            db.execSQL("update OfflineDataChecker set "+COL_2+" = 1 where ID == 1");
            result = true;
        }

        db.close();
        return result;
    }

    // Updating the categories && products check
    public boolean updateOfflineProductCheck(int productStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;

        // productChecker is false
        if (productStatus == 0){
            db.execSQL("update OfflineDataChecker set "+COL_3+" = 0 where ID == 1");
            result = false;
        }
        // productChecker is true
        if (productStatus == 1){
            db.execSQL("update OfflineDataChecker set "+COL_3+" = 1 where ID == 1");
            result = true;
        }

        db.close();
        return result;
    }

    // Updating the order check
    public boolean updateOfflineOrderCheck(int orderStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;

        // orderChecker is false
        if (orderStatus == 0){
            db.execSQL("update OfflineDataChecker set "+COL_4+" = 0 where ID == 1");
            result = false;
        }
        // orderChecker is true
        if (orderStatus == 1){
            db.execSQL("update OfflineDataChecker set "+COL_4+" = 1 where ID == 1");
            result = true;
        }

        db.close();
        return result;
    }

    // Delete the first and only row
    public Integer deleteDataCheckStatus(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_1+" = ?", new String[] {String.valueOf(id)});
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

}
