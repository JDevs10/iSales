package com.iSales.pages.calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
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
import android.widget.Toast;

import com.iSales.R;
import com.iSales.adapter.AngendaEventAdapter;
import com.iSales.database.AppDatabase;
import com.iSales.database.DBHelper.DatabaseHelper;
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

    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<Events> eventsList = new ArrayList<>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.FRENCH);
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.FRENCH);
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.FRENCH);
    private SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);

    private AngendaEventAdapter mAngendaEventAdapter;
    private RecyclerView.LayoutManager mLayoutManager_rv;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private AppDatabase mDB;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        mContext = getBaseContext();
        mDB = AppDatabase.getInstance(getApplicationContext());
        db = new DatabaseHelper(this);

        initLayout();
        initCalendar();

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                initCalendar();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, 1);
                initCalendar();
            }
        });

        mAngendaEventAdapter.setOnItemClickListener(new ItemClickListenerAgendaEvents() {
            @Override
            public void OnItemClickAgendaEventAdd(int position) {

                builder = new AlertDialog.Builder(CalendarActivity.this, R.style.AppTheme); //, R.style.Theme_AppCompat
                builder.setTitle("Créer un événement");
                builder.setCancelable(false);
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_new_event_layout,null,false);
                InitAddAgendaDialog(view, position);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
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


    private void initCalendar(){
        String currentDate = dateFormat.format(calendar.getTime());
        this.currentDate.setText(currentDate);

        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int FirsDayOfTheMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirsDayOfTheMonth);

        collectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        mAngendaEventAdapter = new AngendaEventAdapter(getApplicationContext(), dates, calendar, eventsList);
        recyclerView.setAdapter(mAngendaEventAdapter);
    }

    private void collectEventsPerMonth(String Month, String Year){
        eventsList.clear();
        Log.e(TAG, " collectEventsPerMonth( "+Month+", "+Year+")");
        Log.e(TAG, " clearing event list status: "+eventsList.size());
        Cursor res = db.getEventsMonth(Month, Year);
        Log.e(TAG, " Results size: "+res.getCount());

        while(res.moveToNext()){
            String event = res.getString(res.getColumnIndex(db.EVENT));
            String time = res.getString(res.getColumnIndex(db.TIME));
            String date = res.getString(res.getColumnIndex(db.DATE));
            String month = res.getString(res.getColumnIndex(db.MONTH));
            String year = res.getString(res.getColumnIndex(db.YEAR));

            Events events = new Events(event, time, date, month, year);
            eventsList.add(events);
        }
        Log.e(TAG," eventList size: "+eventsList.size());
        res.close();
    }

    private ArrayList<Events> getStaticEvents(){
        ArrayList<Events> list = new ArrayList<>();

        list.add(new Events("Test Event 1", "3:34 AM", "2019-05-22", "mai", "2019"));
        list.add(new Events("Test Event 1", "4:34 AM", "2019-05-22", "mai", "2019"));
        list.add(new Events("Test Event 1", "10:34 PM", "2019-05-21", "mai", "2019"));
        list.add(new Events("Test Event 1", "9:34 PM", "2019-05-20", "mai", "2019"));
        list.add(new Events("Test Event 1", "6:00 AM", "2019-05-15", "mai", "2019"));

        return list;
    }

    private void saveEvent(String event, String time, String date, String month, String year){
        Log.e(TAG, " SaveEvent() event: "+event+" time: "+time+" date: "+date+" month: "+month+" year: "+year);
        db.saveEvents(event, time, date, month, year);
        Toast.makeText(mContext, "Event Saved!!!", Toast.LENGTH_SHORT).show();
        //mDB.agendaDao().insertCategorie(agendaEntry);
    }


    private void InitAddAgendaDialog(View view, int position){
        final EditText libelle_et = view.findViewById(R.id.dialog_add_new_event_libelle);
        EditText lieu_et = view.findViewById(R.id.dialog_add_new_event_lieu);
        Switch journe_st = view.findViewById(R.id.dialog_add_new_event_journee_st);
        Switch disponible_st = view.findViewById(R.id.dialog_add_new_event_disponible_st);
        ImageButton setTime = view.findViewById(R.id.dialog_add_new_event_clock_ib);
        EditText clockStatus_et = view.findViewById(R.id.dialog_add_new_event_clock_status_tv);
        final TextView time_et = view.findViewById(R.id.dialog_add_new_event_time_tv);
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
                initCalendar();
                dialog.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCalendar();
                dialog.dismiss();
            }
        });

    }
}
