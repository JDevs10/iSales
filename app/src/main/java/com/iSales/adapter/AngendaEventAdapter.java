package com.iSales.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iSales.R;
import com.iSales.interfaces.ItemClickListenerAgendaEvents;
import com.iSales.pages.calendar.Events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AngendaEventAdapter extends RecyclerView.Adapter<AngendaEventAdapter.AngendaEventViewHolder> {
    private final String TAG = AngendaEventAdapter.class.getSimpleName();
    private Context mContext;
    private List<Date> dates;
    private Calendar currentDate;
    private List<Events> events;
    private ArrayList<Events> dataEvents;

    private ItemClickListenerAgendaEvents itemClickListenerAgendaEvents;

    public AngendaEventAdapter(Context context, List<Date> dates, Calendar currentDate, List<Events> events){
        this.mContext = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
//        this.dataEvents = data;
    }

    public class AngendaEventViewHolder extends RecyclerView.ViewHolder {
        TextView Day_Number;

        public AngendaEventViewHolder(View view) {
            super(view);

            Day_Number = view.findViewById(R.id.custom_calendar_day);

        }
    }

    @NonNull
    @Override
    public AngendaEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_single_cell_layout, parent, false);
        return new AngendaEventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AngendaEventViewHolder holder, final int i) {

        Date monthDate = dates.get(i);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH);
        int displayYear = dateCalendar.get(Calendar.YEAR)+1;
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);

        holder.Day_Number.setText(String.valueOf(DayNo));

        if (displayMonth == currentMonth && displayYear == currentYear){
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.click_selector));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        /*
        final int position = holder.getAdapterPosition();
        final Events dataEvent = events.get(i);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerAgendaEvents.OnItemClickAgendaEventView(position, dataEvent);
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }


    public void setOnItemClickListener(ItemClickListenerAgendaEvents itemClickListenerAgendaEvents){
        this.itemClickListenerAgendaEvents = itemClickListenerAgendaEvents;
    }
}
