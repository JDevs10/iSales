package com.iSales.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iSales.remote.ApiUtils;
import com.iSales.remote.model.AgendaEvents;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendAgendaEventTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = SendAgendaEventTask.class.getSimpleName();
    private AgendaEvents agendaEvents;
    private Context mContext;



    public SendAgendaEventTask(AgendaEvents agendaEvents, Context context){
        this.agendaEvents = agendaEvents;
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //Save the event to the server
        Call<ArrayList<AgendaEvents>> call = ApiUtils.getISalesService(mContext).createEvent(agendaEvents);
        call.enqueue(new Callback<ArrayList<AgendaEvents>>() {
            @Override
            public void onResponse(Call<ArrayList<AgendaEvents>> call, Response<ArrayList<AgendaEvents>> response) {
                if(response.isSuccessful()){
                    final ArrayList<AgendaEvents> responseAgenda = response.body();
                    Log.e(TAG, "onResponse: saveAgendaEvent eventId=" + responseAgenda.get(0).getId());

                    String log = "Id: "+responseAgenda.get(0).getId()+"\n"+
                            "Event: "+responseAgenda.get(0).getLabel()+"\n" +
                            "Start Date: "+responseAgenda.get(0).getDatep()+"\n" +
                            "End Date: "+responseAgenda.get(0).getDatem()+"\n\n";

                    Log.e(TAG, " "+log);
                    Toast.makeText(mContext, "Event Saved!!!", Toast.LENGTH_SHORT).show();
                }else {

                    try {
                        Log.e(TAG, "doInBackground: onResponse err: message=" + response.message() + " | code=" + response.code() + " | code=" + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "doInBackgroundonResponse: message=" + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AgendaEvents>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
                return;
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e(TAG, "onPostExecute: ");
    }
}
