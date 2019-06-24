package com.iSales.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.iSales.database.AppDatabase;
import com.iSales.database.entry.UserEntry;
import com.iSales.interfaces.FindAgendaEventsListener;
import com.iSales.interfaces.FindOrdersListener;
import com.iSales.remote.ApiUtils;
import com.iSales.remote.model.AgendaEvents;
import com.iSales.remote.rest.AgendaEventsREST;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class FindAgendaEventTask extends AsyncTask<Void, Void, AgendaEventsREST> {
    private static final String TAG = FindAgendaEventTask.class.getSimpleName();
    private Context context;

    private FindAgendaEventsListener task;
    private String sortfield;
    private String sortorder;
    private long limit;
    private long page;
    private long thirdparty_ids;

    private UserEntry userEntry;

    public FindAgendaEventTask(Context context, FindAgendaEventsListener taskComplete, String sortfield, String sortorder, long limit, long page) {
        this.task = taskComplete;
        this.sortfield = sortfield;
        this.sortorder = sortorder;
        this.limit = limit;
        this.page = page;
        this.context = context;
        AppDatabase mDb = AppDatabase.getInstance(context.getApplicationContext());
        userEntry = mDb.userDao().getUser().get(0);
        Log.e(TAG, "FindAgendaEventTask: ");
    }

    @Override
    protected AgendaEventsREST doInBackground(Void... voids) {
        Log.e(TAG, "doInBackground: ");

        String sqlfilters = "fk_user_author="+userEntry.getId();
//        Requete de connexion de l'internaute sur le serveur
        Call<ArrayList<AgendaEvents>> call = ApiUtils.getISalesService(context).getAllEvents(sqlfilters, sortfield, sortorder, limit, page);
        try {
            Response<ArrayList<AgendaEvents>> response = call.execute();
            Log.e(TAG, " Response: "+response);
            if (response.isSuccessful()) {
                ArrayList<AgendaEvents> agendaEventsArrayList = response.body();
                Log.e(TAG, "doInBackground: eventArrayList=" + agendaEventsArrayList.size());

                return new AgendaEventsREST(agendaEventsArrayList);
            } else {
                Log.e(TAG, "doInBackground: !isSuccessful");
                String error = null;
                AgendaEventsREST mAgendaEventsREST = new AgendaEventsREST();
                mAgendaEventsREST.sendAgendaEvents(null);
                mAgendaEventsREST.setErrorCode(response.code());
                try {
                    error = response.errorBody().string();
//                    JSONObject jsonObjectError = new JSONObject(error);
//                    String errorCode = jsonObjectError.getString("errorCode");
//                    String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "doInBackground: onResponse err: " + error + " code=" + response.code());
                    mAgendaEventsREST.setErrorBody(error);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return mAgendaEventsREST;
            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IOException");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(AgendaEventsREST mAgendaEventsREST) {
        Log.e(TAG, "onPostExecute: ");
//        super.onPostExecute(findProductsREST);
        task.onFindAgendaEventsTaskComplete(mAgendaEventsREST);
    }
}
