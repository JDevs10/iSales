package com.iSales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iSales.R;
import com.iSales.pages.calendar.Events;

import java.util.ArrayList;

public class AgendaEventAdapter extends RecyclerView.Adapter<AgendaEventAdapter.MyViewHolder> {
    private String TAG = AgendaEventAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList<Events> eventList;


    public AgendaEventAdapter (Context context, ArrayList<Events> eventsArrayList){
        this.mContext = context;
        this.eventList = eventsArrayList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView DateTxt, Event, Time;

        public MyViewHolder(View itemView) {
            super(itemView);

            DateTxt = itemView.findViewById(R.id.custome_event_eventdate);
            Event = itemView.findViewById(R.id.custome_event_eventname);
            Time = itemView.findViewById(R.id.custome_event_eventtime);
        }
    }

    @NonNull
    @Override
    public AgendaEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_events_row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaEventAdapter.MyViewHolder holder, final int position) {
        Events events = eventList.get(position);
        holder.DateTxt.setText(events.getDATE());
        holder.Event.setText(events.getEVENT());
        holder.Time.setText(events.getTIME());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
