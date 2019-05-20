package com.iSales.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iSales.database.AppDatabase;
import com.iSales.database.entry.DebugItemEntry;

public class DebugMe extends AsyncTask<Void, Void, Void> {
    private String TAG = DebugMe.class.getSimpleName();

    private Context mContext;
    private String msg;
    private String part;

    private AppDatabase mDB;

    public DebugMe(Context context, String part, String message){
        this.mContext = context;
        this.part = part;
        this.msg = message;
        mDB = AppDatabase.getInstance(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.e(TAG, " doInBackground() => called.");
        if (part.equals("WL")){
            Log.e(TAG, " doInBackground() => ' WL ' called.");
            WindowLogs();
        }
        if (part.equals("LL")){
            Log.e(TAG, " doInBackground() => ' LL ' called.");
            LiveLogs();
        }
        if (part.equals("WL-LL")){
            Log.e(TAG, " doInBackground() => ' WL-LL ' called.");
            WindowLogs();
            LiveLogs();
        }
        return null;
    }

    private void WindowLogs(){
        mDB.debugMessageDao().insertDebugMessage(new DebugItemEntry(mContext, (System.currentTimeMillis()/1000), "DEB", msg));
    }

    private void LiveLogs(){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
