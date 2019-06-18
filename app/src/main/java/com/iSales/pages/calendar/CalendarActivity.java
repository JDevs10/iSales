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

    private String[] monthString = {"Janvier", "Février", "Mars", "Avril", "May", "Juin", "Juilet", "Août", "September", "Octobre", "Novembre", "Décembre"};
    private Integer[] monthInt = {2678400,2419200,2678400,2592000,2678400,2592000,2678400,2678400,2592000,2678400,2592000};
    private Integer[] yearInt = {1483228800,1514764800,1546300800};

    private ImageButton previousBtn,nextBtn;
    private TextView currentDate;
    private RecyclerView recyclerView;
    private static final int MAX_CALENDAR_DAYS = 42;
    private Calendar calendar = Calendar.getInstance(Locale.FRENCH);

    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<AgendaEventEntry> eventsList = new ArrayList<>();

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

        Log.e(TAG, " Get Actual month calendar: "+calendar.getTime().getMonth()+" || table: "+monthString[calendar.getTime().getMonth()]);
    }

    private void initCalendar(){
        String currentDate = dateFormat.format(calendar.getTime());
        this.currentDate.setText(currentDate);

        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int FirsDayOfTheMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirsDayOfTheMonth);

        getAllEvents();
        //collectEventsPerMonth(monthInt[calendar.getTime().getMonth()], yearFormat.format(calendar.getTime()));

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
                AgendaEventAdapter mAgendaEventAdapter = new AgendaEventAdapter(mDialog.getContext(), collectEventsByDate(dates.get(position).getTime())); //
                recyclerViewEvents.setAdapter(mAgendaEventAdapter);

                InitViewAgendaDialog(mDialog, position);
                mDialog.show();
            }
        });
    }

    private void saveEvent(String eventName, String lieu, boolean myAvaibility, Long startEventTimeStamp, Long endEventTimeStamp, String noteEvent) {
        Log.e(TAG, " SaveEvent() event: " + eventName + " lieu: " + lieu + " myAvaibility: " + myAvaibility + " dateStart: " + startEventTimeStamp + " dateEnd: " + endEventTimeStamp + " description: "+noteEvent);

        showProgressDialog(true, "Enregistrement", "Enregistrement d'un évènement...");

        UserEntry mUserEntry = mDB.userDao().getUser().get(0);
        Calendar cal = Calendar.getInstance(Locale.FRENCH);

        AgendaEvents agendaEvents = new AgendaEvents();
        agendaEvents.setLabel(eventName);
        agendaEvents.setDatec(cal.getTimeInMillis());
        agendaEvents.setDatem(cal.getTimeInMillis());
        agendaEvents.setLocation(lieu);
        agendaEvents.setTransparency(String.valueOf(myAvaibility));
        agendaEvents.setDatep(startEventTimeStamp);
        agendaEvents.setDatef(endEventTimeStamp);
        agendaEvents.setDurationp("-1");    //disable
        agendaEvents.setPercentage("-1");   //disable
        agendaEvents.setPriority("0");
        agendaEvents.setUserownerid(String.valueOf(mUserEntry.getId()));
        agendaEvents.setNote(noteEvent);

        //save on server
        if (!ConnectionManager.isPhoneConnected(this)){
            Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
            showProgressDialog(false, null, null);
            return;
        }

        Call<AgendaEvents> callInsertEvent = ApiUtils.getISalesService(this).createEvent(agendaEvents);
        callInsertEvent.enqueue(new Callback<AgendaEvents>() {
            @Override
            public void onResponse(Call<AgendaEvents> call, Response<AgendaEvents> response) {
                if (response.isSuccessful()){
                    AgendaEvents agendaEventRespose = response.body();
                    Log.e(TAG, "Agenda Event create response => "+agendaEventRespose.getRef());
                }
            }

            @Override
            public void onFailure(Call<AgendaEvents> call, Throwable t) {
                Log.e(TAG, "Agenda Event create onFailure => "+t.getMessage());
            }
        });

        //save in offline mode
        AgendaEventEntry agendaEventEntry = new AgendaEventEntry();
        agendaEventEntry.setLabel(eventName);
        agendaEventEntry.setDatec(cal.getTimeInMillis());
        agendaEventEntry.setDatem(cal.getTimeInMillis());
        agendaEventEntry.setLocation(lieu);
        agendaEventEntry.setTransparency(String.valueOf(myAvaibility));
        agendaEventEntry.setDatep(startEventTimeStamp);
        agendaEventEntry.setDatef(endEventTimeStamp);
        agendaEventEntry.setDurationp("-1");    //disable
        agendaEventEntry.setPercentage("-1");   //disable
        agendaEventEntry.setPriority("0");
        agendaEventEntry.setUserownerid(String.valueOf(mUserEntry.getId()));
        agendaEventEntry.setNote(noteEvent);

        try{
            long i = mDB.agendaEventsDao().insertNewEvent(agendaEventEntry);
            List<AgendaEventEntry> test = mDB.agendaEventsDao().getEventsById((long) 1);

            String log = "List size: "+test.size()+"\n" +
                    "Id: "+i+"\n"+
                    "Event id: "+test.get(0).getId()+"\n" +
                    "Event label: "+test.get(0).getLabel()+"\n" +
                    "Event Datec: "+test.get(0).getDatec()+"\n\n";

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

    private void getAllEvents(){
        /*
        Log.e(TAG, " getAllEvents()::Get local events ");

        List<AgendaEventEntry> eventEntryList = mDB.agendaEventsDao().getAllEvents();
        Log.e(TAG, " getAllEvents() => Results size: "+eventEntryList.size());

        for (int i=0; i<eventEntryList.size(); i++){
            Log.e(TAG," \nList size: "+i+"/"+eventEntryList.size()+"\n" +
                    "Event: "+eventEntryList.get(i).getId()+"\n" +
                    "Time: "+eventEntryList.get(i).getLabel()+"\n\n");
        }

*/
        Log.e(TAG, " getAllEvents()::Get Server events ");

        UserEntry userEntry = AppDatabase.getInstance(this).userDao().getUser().get(0);
        String sqlfilters = "fk_user_author="+userEntry.getId();

        Call<ArrayList<AgendaEvents>> agendaEventsCall = ApiUtils.getISalesService(this).getAllEvents(sqlfilters, "", "DESC",0,0);
        agendaEventsCall.enqueue(new Callback<ArrayList<AgendaEvents>>() {
            @Override
            public void onResponse(Call<ArrayList<AgendaEvents>> call, Response<ArrayList<AgendaEvents>> response) {
                if (response.isSuccessful()){
                    Log.e(TAG, " onResponse()::Response size: "+response.body().size());

                    ArrayList<AgendaEvents> serverAgendaEvents = response.body();
                    for (int i=0; i<serverAgendaEvents.size(); i++){
                        Log.e(TAG, "Event Label Name => "+serverAgendaEvents.get(i).getLabel()+"\n " +
                                "Event Label Datec => "+serverAgendaEvents.get(i).getDatec());

                        AgendaEventEntry agendaEventEntry = new AgendaEventEntry(serverAgendaEvents.get(i));
                        mDB.agendaEventsDao().insertNewEvent(agendaEventEntry);
                    }

                    eventsList.clear();
                    List<AgendaEventEntry> listEvents = mDB.agendaEventsDao().getAllEvents();
                    Log.e(TAG, " Results size: "+listEvents.size());
                    eventsList.addAll(listEvents);
                }else{
                    Log.e(TAG, " onResponse()::Noting: "+response.body().size());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AgendaEvents>> call, Throwable t) {
                Log.e(TAG, " onFailure():: "+t);
            }
        });
    }


    private ArrayList<Events> collectEventsByDate(Long Date){
        Log.e(TAG, " start collectEventsByDate() || DateLong: "+Date);
        List<AgendaEventEntry> listEvents = mDB.agendaEventsDao().getEventsByDate(Date);
        ArrayList<Events> arrayList = new ArrayList<>();

        for (int i=0; i<listEvents.size(); i++){
            Log.e(TAG," \nList size: "+i+"/"+listEvents.size()+"\n" +
                    "Event Id: "+listEvents.get(i).getId()+"\n" +
                    "Event Label: "+listEvents.get(i).getLabel()+"\n\n");

            /*
                String event = listEvents.get(i).getEVENT();
                String time = listEvents.get(i).getTIME();
                String date = listEvents.get(i).getDATE();
                String month = listEvents.get(i).getMONTH();
                String year = listEvents.get(i).getYEAR();

                arrayList.add(new Events(event, time, date, month, year));
                */
        }
        return arrayList;
    }

    /*
    private void collectEventsPerMonth(Long Month, Long Year){
        Log.e(TAG, " collectEventsPerMonth( "+Month+", "+Year+")");
        Log.e(TAG, " clearing event list status: "+eventsList.size());
        eventsList.clear();

        List<AgendaEventEntry> listEvents = mDB.agendaEventsDao().getEventsByMonth(Month, Year);
        Log.e(TAG, " Results size: "+listEvents.size());

        for (int i=0; i<listEvents.size(); i++){
            Log.e(TAG," \nList size: "+i+"/"+listEvents.size()+"\n" +
                    "Event: "+listEvents.get(i).getId()+"\n" +
                    "Time: "+listEvents.get(i).getId()+"\n\n");


            //Log.e(TAG, " Event List Month: "+listEvents.get(i).getMONTH()+" == "+Month+" && "+listEvents.get(i).getYEAR()+" == "+Year);
            String event = listEvents.get(i).getEVENT();
            String time = listEvents.get(i).getTIME();
            String date = listEvents.get(i).getDATE();
            String month = listEvents.get(i).getMONTH();
            String year = listEvents.get(i).getYEAR();

            Events events = new Events(event, time, date, month, year);
            eventsList.add(events);

        }
        Log.e(TAG," eventList size: "+eventsList.size());
    }
*/

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
                saveEvent(libelle_et.getText().toString(), lieu_et.getText().toString(), disponible_st.isChecked(), combinedCalStart.getTimeInMillis(), combinedCalEnd.getTimeInMillis(), description_et.getText().toString());
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
