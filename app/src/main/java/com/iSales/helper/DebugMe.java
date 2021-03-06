package com.iSales.helper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iSales.database.AppDatabase;

public class DebugMe extends AsyncTask<Void, Void, Void> {
    private String TAG = DebugMe.class.getSimpleName();
    private Activity mActivity;
    private Context mContext;
    private String msg;
    private String part;

    private AppDatabase mDB;

    public DebugMe(Activity theActivity, Context context, String part, String message){
        this.mContext = context;
        this.part = part;
        this.msg = message;
        this.mActivity = theActivity;
        mDB = AppDatabase.getInstance(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.e(TAG, " doInBackground() => called.");
        if (part.equals("WL")){
            //Log.e(TAG, " doInBackground() => ' WL ' called.");
            WindowLogs();
        }
        if (part.equals("LL")){
            //Log.e(TAG, " doInBackground() => ' LL ' called.");
            LiveLogs();
        }
        if (part.equals("WL-LL")){
            //Log.e(TAG, " doInBackground() => ' WL-LL ' called.");
            WindowLogs();
            LiveLogs();
        }
        return null;
    }

    private void WindowLogs(){

    }

    private void LiveLogs(){
        //check if the live debugging settings from SuperAdmin are active
        if (mDB.debugSettingsDao().getAllDebugSettings().get(0).getCheckDebug() == 1) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //show debug message through the activity UI thread
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
