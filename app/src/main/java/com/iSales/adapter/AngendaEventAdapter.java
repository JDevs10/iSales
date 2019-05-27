package com.iSales.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iSales.R;
import com.iSales.interfaces.ItemClickListenerAgendaEvents;
import com.iSales.pages.calendar.Events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AngendaEventAdapter extends RecyclerView.Adapter<AngendaEventAdapter.AngendaEventViewHolder> {
    private final String TAG = AngendaEventAdapter.class.getSimpleName();
    private Context mContext;
    private List<Date> dates;
    private Calendar currentDate;
    private List<Events> events;
    private ArrayList<Events> dataEvents;

    private ItemClickListenerAgendaEvents itemClickListenerAgendaEvents = null;

    public AngendaEventAdapter(Context context, List<Date> dates, Calendar currentDate, List<Events> events){
        Log.e(TAG, " AngendaEventAdapter (context, dates size: "+dates.size()+", currentDate: "+currentDate.getTime()+", events size: "+events.size()+")");
        this.mContext = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
//        this.dataEvents = data;
    }

    public class AngendaEventViewHolder extends RecyclerView.ViewHolder {
        TextView Day_Number;
        TextView eventNumber;
        LinearLayout cellLayout;

        public AngendaEventViewHolder(View view) {
            super(view);

            cellLayout = view.findViewById(R.id.custom_calendar_cell_layout);
            Day_Number = view.findViewById(R.id.custom_calendar_day);
            eventNumber = view.findViewById(R.id.custom_calendar_events_id);

        }
    }

    @NonNull
    @Override
    public AngendaEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_single_cell_layout, parent, false);
        return new AngendaEventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AngendaEventViewHolder holder, final int i) {

        Date monthDate = dates.get(i);
        Calendar dateCalendar = Calendar.getInstance(Locale.FRENCH);
        dateCalendar.setTime(monthDate);
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);

        if (displayMonth == currentMonth && displayYear == currentYear){
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        holder.Day_Number.setText(String.valueOf(DayNo));
        Calendar eventCalendar = Calendar.getInstance();

        //populate the agenda with the events
        ArrayList<String> arrayList = new ArrayList<>();
        for (int y=0; y < events.size(); y++){
            eventCalendar.setTime(convertStringToDate(events.get(y).getDATE()));

            Log.e(TAG, "Day Number: "+DayNo+"\n" +
                    "eventCalendar Day of Month: "+eventCalendar.get(Calendar.DAY_OF_MONTH)+"\n" +
                    "displayMonth: "+displayMonth+"\n" +
                    "eventCalendar Month: "+eventCalendar.get(Calendar.MONTH)+1+"\n" +
                    "displayYear: "+displayYear+"\n" +
                    "eventCalendar Year: "+eventCalendar.get(Calendar.YEAR)+"\n" +
                    "Index: "+y+"/"+events.size());

            if (DayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH)+1 && displayYear == eventCalendar.get(Calendar.YEAR)){
                arrayList.add(events.get(y).getEVENT());
                holder.eventNumber.setText(arrayList.size()+" Events");
                holder.Day_Number.setBackground(ContextCompat.getDrawable(mContext, R.drawable.calendar_event_notification_circle));
            }
        }


        //select the days in the calendar
        holder.cellLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, " Item touched: "+i);
                itemClickListenerAgendaEvents.OnItemClickAgendaEventAdd(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }


    public void setOnItemClickListener(ItemClickListenerAgendaEvents itemClickListenerAgendaEvents){
        this.itemClickListenerAgendaEvents = itemClickListenerAgendaEvents;
    }

    private Date convertStringToDate(String eventDate){
        Log.e(TAG, " convertStringToDate( "+eventDate+" )");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
        Date date = null;
        try {
            date = sdf.parse(eventDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }
}
