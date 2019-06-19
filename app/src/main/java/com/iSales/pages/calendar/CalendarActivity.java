package com.iSales.pages.calendar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iSales.R;
import com.iSales.adapter.AgendaAdapter;
import com.iSales.adapter.AgendaEventAdapter;
import com.iSales.database.AppDatabase;
import com.iSales.database.DBHelper.DatabaseHelper;
import com.iSales.database.entry.AgendaEventEntry;
import com.iSales.database.entry.EventsEntry;
import com.iSales.database.entry.UserEntry;
import com.iSales.interfaces.ItemClickListenerAgenda;
import com.iSales.remote.ApiUtils;
import com.iSales.remote.ConnectionManager;
import com.iSales.remote.model.AgendaEvents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarActivity extends AppCompatActivity {
    private String TAG = CalendarActivity.class.getSimpleName();

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

    private AgendaAdapter mAgendaAdapter;
    private RecyclerView.LayoutManager mLayoutManager_rv;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Dialog mDialog;

    private ProgressDialog mProgressDialog;
    private AppDatabase mDB;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

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

    }
    /**
     * Shows the progress UI and hides.
     */
    private void showProgressDialog(boolean show, String title, String message) {

        if (show) {
            mProgressDialog = new ProgressDialog(this);
            if (title != null) mProgressDialog.setTitle(title);
            if (message != null) mProgressDialog.setMessage(message);

            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_view));
            mProgressDialog.show();
        } else {
            if (mProgressDialog != null) mProgressDialog.dismiss();
        }
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

        mAgendaAdapter = new AgendaAdapter(getApplicationContext(), dates, calendar, eventsList);
        recyclerView.setAdapter(mAgendaAdapter);

        mAgendaAdapter.setOnItemClickListener(new ItemClickListenerAgenda() {
            @Override
            public void OnItemClickAgendaEventAdd(int position) {
                mDialog = new Dialog(CalendarActivity.this);
                mDialog.setContentView(R.layout.dialog_add_new_event_layout);
                mDialog.setTitle("Créer un événement");
                InitAddAgendaDialog(mDialog, position);
                mDialog.show();
            }

            @Override
            public void OnItemLongClickAgendaEvent(int position) {
                String date = eventDateFormat.format(dates.get(position));
                Log.e(TAG, " OnItemLongClickAgendaEvent()::OnLongClickEvent || Date: "+date);

                mDialog = new Dialog(CalendarActivity.this);
                mDialog.setContentView(R.layout.custom_show_events_layout);
//                mDialog.setTitle("Créer un événement");

                RecyclerView recyclerViewEvents = mDialog.findViewById(R.id.custom_show_events_layout_recycle_view);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mDialog.getContext());
                recyclerViewEvents.setLayoutManager(layoutManager);
                recyclerViewEvents.setHasFixedSize(true);

                ArrayList tttlist = new ArrayList();
                AgendaEventAdapter mAgendaEventAdapter = new AgendaEventAdapter(mDialog.getContext(), collectEventsByDate(date)); //
                recyclerViewEvents.setAdapter(mAgendaEventAdapter);

                InitViewAgendaDialog(mDialog, position);
                mDialog.show();
            }
        });
    }

    private void saveEvent(String label, String location, String percentage, String fullDayEvent, String time, String date, String month, String year, Long startEvent, Long endEvent, String description) {
        Log.e(TAG, " SaveEvent() label: "+label+" location: "+location+" percentage: "+percentage+" fullDayEvent: "+fullDayEvent +
                " time: "+time+" date: "+date+" month: "+month+" year: "+year+" description: "+description);

        try{
            long i = mDB.eventsDao().insertNewEvent(new EventsEntry(label, location, percentage, fullDayEvent, time, date, month, year, startEvent, endEvent, description));
            List<EventsEntry> test = mDB.eventsDao().getEventsById((long) 1);

            String log = "List size: "+test.size()+"\n" +
                    "Id: "+i+"\n"+
                    "Event: "+test.get(0).getLABEL()+"\n" +
                    "Time: "+test.get(0).getTIME()+"\n" +
                    "Date: "+test.get(0).getDATE()+"\n" +
                    "Month: "+test.get(0).getMONTH()+"\n" +
                    "Year: "+test.get(0).getYEAR()+"\n\n";

            Log.e(TAG, " "+log);
            Toast.makeText(this, "Event Saved!!!", Toast.LENGTH_SHORT).show();

        } catch (SQLException e){
            e.getStackTrace();
            Toast.makeText(this, "Event Not Saved!!!", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.getStackTrace();
            Toast.makeText(this, "Event Not Saved!!!", Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<Events> collectEventsByDate(String Date){
        Log.e(TAG, " start collectEventsByDate()");
        List<EventsEntry> listEvents = mDB.eventsDao().getEventsByDate(Date);
        ArrayList<Events> arrayList = new ArrayList<>();

        for (int i=0; i<listEvents.size(); i++){
            Log.e(TAG," \nList size: "+i+"/"+listEvents.size()+"\n" +
                    "Event: "+listEvents.get(i).getLABEL()+"\n" +
                    "Time: "+listEvents.get(i).getTIME()+"\n" +
                    "Date: "+listEvents.get(i).getDATE()+"\n" +
                    "Month: "+listEvents.get(i).getMONTH()+"\n" +
                    "Year: "+listEvents.get(i).getYEAR()+"\n\n");

            String label = listEvents.get(i).getLABEL();
            String location = listEvents.get(i).getLIEU();
            String percentage = listEvents.get(i).getPERCENTAGE();
            String fullDayEvent = listEvents.get(i).getFULLDAYEVENT();
            String time = listEvents.get(i).getTIME();
            String date = listEvents.get(i).getDATE();
            String month = listEvents.get(i).getMONTH();
            String year = listEvents.get(i).getYEAR();
            Long startEvent = listEvents.get(i).getSTART_EVENT();
            Long endEvent = listEvents.get(i).getEND_EVENT();
            String description = listEvents.get(i).getDESCRIPTION();

            arrayList.add(new Events(label, location, percentage, fullDayEvent, time, date, month, year, startEvent, endEvent, description));
        }
        return arrayList;
    }


    private void collectEventsPerMonth(String Month, String Year){
        Log.e(TAG, " collectEventsPerMonth( "+Month+", "+Year+")");
        Log.e(TAG, " clearing event list status: "+eventsList.size());
        eventsList.clear();

        List<EventsEntry> listEvents = mDB.eventsDao().getEventsByMonth(Month, Year);
        Log.e(TAG, " Results size: "+listEvents.size());

        for (int i=0; i<listEvents.size(); i++){
            Log.e(TAG," \nList size: "+i+"/"+listEvents.size()+"\n" +
                    "Event: "+listEvents.get(i).getLABEL()+"\n" +
                    "Time: "+listEvents.get(i).getTIME()+"\n" +
                    "Date: "+listEvents.get(i).getDATE()+"\n" +
                    "Month: "+listEvents.get(i).getMONTH()+"\n" +
                    "Year: "+listEvents.get(i).getYEAR()+"\n\n");

            //Log.e(TAG, " Event List Month: "+listEvents.get(i).getMONTH()+" == "+Month+" && "+listEvents.get(i).getYEAR()+" == "+Year);
            String label = listEvents.get(i).getLABEL();
            String location = listEvents.get(i).getLIEU();
            String percentage = listEvents.get(i).getPERCENTAGE();
            String fullDayEvent = listEvents.get(i).getFULLDAYEVENT();
            String time = listEvents.get(i).getTIME();
            String date = listEvents.get(i).getDATE();
            String month = listEvents.get(i).getMONTH();
            String year = listEvents.get(i).getYEAR();
            Long startEvent = listEvents.get(i).getSTART_EVENT();
            Long endEvent = listEvents.get(i).getEND_EVENT();
            String description = listEvents.get(i).getDESCRIPTION();

            Events events = new Events(label, location, percentage, fullDayEvent, time, date, month, year, startEvent, endEvent, description);
            eventsList.add(events);
        }
        Log.e(TAG," eventList size: "+eventsList.size());
    }


    private void InitAddAgendaDialog(final Dialog dialog, int position){
        final EditText libelle_et = dialog.findViewById(R.id.dialog_add_new_event_libelle);
        final EditText lieu_et = dialog.findViewById(R.id.dialog_add_new_event_lieu);
        final Switch journe_st = dialog.findViewById(R.id.dialog_add_new_event_journee_st);
        final Switch disponible_st = dialog.findViewById(R.id.dialog_add_new_event_disponible_st);
        ImageButton setTimeStart = dialog.findViewById(R.id.dialog_add_new_event_display_startevent_ib);
        ImageButton setTimeEnd = dialog.findViewById(R.id.dialog_add_new_event_display_endevent_ib);

        final TextView dateStart = dialog.findViewById(R.id.dialog_add_new_event_display_starteventday_tv);
        final TextView timeStart = dialog.findViewById(R.id.dialog_add_new_event_display_starteventtime_tv);

        final TextView dateEnd = dialog.findViewById(R.id.dialog_add_new_event_display_endeventday_tv);
        final TextView timeEnd = dialog.findViewById(R.id.dialog_add_new_event_display_endeventtime_tv);

        EditText clockStatus_et = dialog.findViewById(R.id.dialog_add_new_event_clock_status_tv);
        //final TextView time_et = dialog.findViewById(R.id.dialog_add_new_event_time_tv);
        final EditText description_et = dialog.findViewById(R.id.dialog_add_new_event_description_et);
        final Button add_btn = dialog.findViewById(R.id.dialog_add_new_event_addevent_btn);
        Button cancel_btn = dialog.findViewById(R.id.dialog_add_new_event_cancelevent_btn);

        final DatePickerDialog.OnDateSetListener mDateSetListenerm;
        final TimePickerDialog.OnTimeSetListener mTimeSetListenerm;

        final Calendar combinedCalStart = Calendar.getInstance(Locale.FRENCH);
        final Calendar combinedCalEnd = Calendar.getInstance(Locale.FRENCH);
        Calendar calendar = Calendar.getInstance(Locale.FRENCH);
        final int yearStart = calendar.get(Calendar.YEAR);
        final int monthStart = calendar.get(calendar.MONTH);
        final int dayStart = calendar.get(calendar.DAY_OF_MONTH);
        final int hoursStart = calendar.get(calendar.HOUR_OF_DAY);
        final int minutesStart = calendar.get(calendar.MINUTE);

        final int yearEnd = calendar.get(Calendar.YEAR);
        final int monthEnd = calendar.get(calendar.MONTH);
        final int dayEnd = calendar.get(calendar.DAY_OF_MONTH);
        final int hoursEnd = calendar.get(calendar.HOUR_OF_DAY);
        final int minutesEnd = calendar.get(calendar.MINUTE);


        //set the date of the event
        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePickerDialog = new DatePickerDialog(dateStart.getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar c = Calendar.getInstance(Locale.FRENCH);
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, year);
                                c.set(Calendar.DAY_OF_MONTH, year);

                                SimpleDateFormat hformate = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
                                String event_date = hformate.format(c.getTime());

                                dateStart.setText(event_date);
                            }
                        },yearStart, monthStart, dayStart);
                mDatePickerDialog.show();
            }
        });

        //set the time of the event
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(add_btn.getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                Calendar c = Calendar.getInstance(Locale.FRENCH);
                                c.set(Calendar.HOUR_OF_DAY, hour);
                                c.set(Calendar.MINUTE, minutes);
                                c.setTimeZone(TimeZone.getDefault());

                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.FRENCH);
                                String event_Time = hformate.format(c.getTime());

                                timeStart.setText(event_Time);
                            }
                        }, hoursStart, minutesStart,false);
                timePickerDialog.show();
            }
        });


        //set the timeStamp from datePicker && timePicker
        combinedCalStart.set(yearStart, monthStart, dayStart, hoursStart, minutesStart);

        //set the end date and time of the event
        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePickerDialog = new DatePickerDialog(dateEnd.getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar c = Calendar.getInstance(Locale.FRENCH);
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, year);
                                c.set(Calendar.DAY_OF_MONTH, year);

                                SimpleDateFormat hformate = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
                                String event_date = hformate.format(c.getTime());

                                dateEnd.setText(event_date);
                            }
                        },yearEnd, monthEnd, dayEnd);
                mDatePickerDialog.show();
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                                timeEnd.setText(event_Time);
                            }
                        }, hoursEnd, minutesEnd,false);
                timePickerDialog.show();
            }
        });

        //set end event calendar
        combinedCalEnd.set(yearEnd, monthEnd, dayEnd, hoursEnd, minutesEnd);


        final String date = eventDateFormat.format(dates.get(position));
        final String month = monthFormat.format(dates.get(position));
        final String year = yearFormat.format(dates.get(position));


        //save the Event in database
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent(libelle_et.getText().toString(), lieu_et.getText().toString(), "-1", Boolean.toString(journe_st.isChecked()),
                        Boolean.toString(disponible_st.isChecked()), date, month, year, combinedCalStart.getTimeInMillis(), combinedCalEnd.getTimeInMillis(), description_et.getText().toString());
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

    private void InitViewAgendaDialog(final Dialog dialog, int position) {
        Button close_btn;

        close_btn = dialog.findViewById(R.id.custom_show_events_layout_close_btn);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
