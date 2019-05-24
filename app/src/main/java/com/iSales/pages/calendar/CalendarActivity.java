package com.iSales.pages.calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.iSales.R;
import com.iSales.adapter.AngendaEventAdapter;
import com.iSales.database.AppDatabase;
import com.iSales.database.entry.AgendaEntry;
import com.iSales.interfaces.ItemClickListenerAgendaEvents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarActivity extends AppCompatActivity {
    private String TAG = CalendarActivity.class.getSimpleName();
    private Context mContext;

    private ImageButton previousBtn,nextBtn;
    private TextView currentDate;
    private RecyclerView recyclerView;
    private static final int MAX_CALENDAR_DAYS = 42;
    private Calendar calendar = Calendar.getInstance(Locale.FRENCH);

    private List<Date> dates = new ArrayList<>();
    private List<Events> eventsList = new ArrayList<>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.FRENCH);
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.FRENCH);
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.FRENCH);

    private AngendaEventAdapter mAngendaEventAdapter;
    private RecyclerView.LayoutManager mLayoutManager_rv;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        mDB = AppDatabase.getInstance(getApplicationContext());

        initLayout();
        initCalensar();

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                initCalensar();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, 1);
                initCalensar();
            }
        });

        mAngendaEventAdapter.setOnItemClickListener(new ItemClickListenerAgendaEvents() {
            @Override
            public void OnItemClickAgendaEventAdd(int position) {
                builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Update Ticket Info");
                builder.setCancelable(false);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_add_new_event_layout,null,false);
                InitAddAgendaDialog(view, position);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void OnItemClickAgendaEventView(int position, Events eventData) {

            }

            @Override
            public void OnItemClickAgendaEventDelete(int position, Events eventData) {

            }
        });

    }

    private void initLayout(){
        previousBtn = findViewById(R.id.calendar_previousMontBtn);
        nextBtn = findViewById(R.id.calendar_activity_nextMontBtn);
        currentDate = findViewById(R.id.calendar_activity_currentDate);

        recyclerView = findViewById(R.id.calendar_activity_recycler_view);
        mLayoutManager_rv = new GridLayoutManager(getApplicationContext(), 7);
        recyclerView.setLayoutManager(mLayoutManager_rv);


    }

    private void initCalensar(){
        String currentDate = dateFormat.format(calendar.getTime());
        this.currentDate.setText(currentDate);

        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int FirsDayOfTheMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirsDayOfTheMonth);

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        mAngendaEventAdapter = new AngendaEventAdapter(getApplicationContext(), dates, calendar, eventsList);
        recyclerView.setAdapter(mAngendaEventAdapter);
    }

    private ArrayList<Events> getAgendaEvents(){
        ArrayList<Events> list = new ArrayList<>();

        return list;
    }

    private void saveEvent(String event, String time, String date, String month, String year){
        Log.e(TAG, " SaveEvent() event: "+event+" time: "+time+" date: "+date+" month: "+month+" year: "+year);
        //mDB.agendaDao().insertCategorie(agendaEntry);
    }


    private void InitAddAgendaDialog(View view, int position){
        final EditText libelle_et = view.findViewById(R.id.dialog_add_new_event_libelle);
        EditText lieu_et = view.findViewById(R.id.dialog_add_new_event_lieu);
        Switch journe_st = view.findViewById(R.id.dialog_add_new_event_journee_st);
        Switch disponible_st = view.findViewById(R.id.dialog_add_new_event_disponible_st);
        ImageButton setTime = view.findViewById(R.id.dialog_add_new_event_clock_ib);
        EditText clockStatus_et = view.findViewById(R.id.dialog_add_new_event_clock_status_tv);
        final EditText time_et = view.findViewById(R.id.dialog_add_new_event_time_tv);
        EditText description_et = view.findViewById(R.id.dialog_add_new_event_description_et);
        final Button add_btn = view.findViewById(R.id.dialog_add_new_event_addevent_btn);
        Button cancel_btn = view.findViewById(R.id.dialog_add_new_event_cancelevent_btn);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(calendar.HOUR_OF_DAY);
                int minutes = calendar.get(calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(add_btn.getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, i);
                                c.set(Calendar.MINUTE, i1);
                                c.setTimeZone(TimeZone.getDefault());

                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.FRENCH);
                                String event_Time = hformate.format(c.getTime());

                                time_et.setText(event_Time);
                            }
                        }, hours, minutes,false);
                timePickerDialog.show();
            }
        });
        final String date = dateFormat.format(dates.get(position));
        final String month = dateFormat.format(dates.get(position));
        final String year = dateFormat.format(dates.get(position));


        //save the Event in database
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent(libelle_et.getText().toString(), time_et.getText().toString(), date, month, year);
                initCalensar();
                dialog.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCalensar();
                dialog.dismiss();
            }
        });

    }
}
