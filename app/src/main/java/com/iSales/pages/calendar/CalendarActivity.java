package com.iSales.pages.calendar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.view.FocusFinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.iSales.database.entry.ClientEntry;
import com.iSales.database.entry.EventsEntry;
import com.iSales.database.entry.UserEntry;
import com.iSales.interfaces.FindAgendaEventsListener;
import com.iSales.interfaces.ItemClickListenerAgenda;
import com.iSales.remote.ApiUtils;
import com.iSales.remote.ConnectionManager;
import com.iSales.remote.model.AgendaEvents;
import com.iSales.remote.model.AgendaUserassigned;
import com.iSales.remote.rest.AgendaEventsREST;
import com.iSales.task.FindAgendaEventTask;
import com.iSales.task.FindThirdpartieTask;
import com.iSales.task.SendAgendaEventTask;
import com.iSales.utility.ISalesUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class CalendarActivity extends AppCompatActivity implements FindAgendaEventsListener {
    private String TAG = CalendarActivity.class.getSimpleName();


    private ImageButton previousBtn,nextBtn;
    private TextView currentDate;
    private ImageButton synchro_ib;
    private RecyclerView recyclerView;
    private static final int MAX_CALENDAR_DAYS = 42;
    private Calendar calendar = Calendar.getInstance(Locale.FRENCH);

    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<EventsEntry> eventsList = new ArrayList<>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.FRENCH);
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.FRENCH);
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.FRENCH);
    private SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);

    private AgendaAdapter mAgendaAdapter;
    private RecyclerView.LayoutManager mLayoutManager_rv;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Dialog mDialog;

    //    task de recuperation des clients
    private FindAgendaEventTask mFindAgendaEventTask = null;

    private ProgressDialog mProgressDialog;
    private AppDatabase mDB;

    private int mLimit = 0;
    private int mPageEvent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        mDB = AppDatabase.getInstance(getApplicationContext());

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

        synchro_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEventsFromServer();
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
        synchro_ib = (ImageButton) findViewById(R.id.calendar_activity_synchro);

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

                AgendaEventAdapter mAgendaEventAdapter = new AgendaEventAdapter(mDialog.getContext(), collectEventsByDate(date)); //
                recyclerViewEvents.setAdapter(mAgendaEventAdapter);

                InitViewAgendaDialog(mDialog, position);
                mDialog.show();
            }
        });
    }


    private void saveEvent(String label, String location, String percentage, String fullDayEvent, String disponibility,
                           String time, String date, String month, String year, Long startEvent, Long endEvent, String concernTier, String description) {

        /*
        final ProgressDialog progressDialog = new ProgressDialog(CalendarActivity.this);
        progressDialog.setMessage("Enregistrement de l'évenement en cours...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_view));
        progressDialog.show();
        */

        if (!ConnectionManager.isPhoneConnected(getApplication())){
            Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
            return;
        }

        Log.e(TAG, " SaveEvent() label: "+label+" location: "+location+" percentage: "+percentage+" fullDayEvent: "+fullDayEvent +
                " time: "+time+" date: "+date+" month: "+month+" year: "+year+" startEvent: "+startEvent+" endEvent: "+endEvent+" concernTier socId: "+concernTier+" description: "+description);

        EventsEntry newEvent = new EventsEntry(label, location, percentage, fullDayEvent, disponibility, time, date, month, year, startEvent, endEvent, concernTier, description);
        newEvent.setREF(String.format("PROV-%s", startEvent));
        mDB.eventsDao().insertNewEvent(newEvent);

        /**
         * Now to save to the server side now
         * save event on the server side....
         **/
        Calendar calendar = Calendar.getInstance(Locale.FRENCH);
        AgendaEvents mAgendaEvents = new AgendaEvents();

        mAgendaEvents.setTable_rowid("id");
        mAgendaEvents.setRef(String.format("EVE-%s", startEvent));
        mAgendaEvents.setType_code("AC_OTH_AUTO");
        mAgendaEvents.setType("Other (automatically inserted events)");
        mAgendaEvents.setLabel(newEvent.getLABEL());
        mAgendaEvents.setDatec(calendar.getTimeInMillis());
        mAgendaEvents.setDatem(calendar.getTimeInMillis());
        mAgendaEvents.setUsermodid("");

        Log.e(TAG, " getSTART_EVENT:: "+(Long) (newEvent.getSTART_EVENT()/1000));
        mAgendaEvents.setDatep( (newEvent.getSTART_EVENT()/1000) );

        Log.e(TAG, " getEND_EVENT:: "+(Long) (newEvent.getEND_EVENT()/1000));
        mAgendaEvents.setDatef( (newEvent.getEND_EVENT()/1000) );

        mAgendaEvents.setDurationp("-1");
        mAgendaEvents.setFulldayevent("1");
        mAgendaEvents.setPercentage(newEvent.getPERCENTAGE());
        mAgendaEvents.setLocation(newEvent.getLIEU());
        mAgendaEvents.setTransparency(newEvent.getTRANSPARENCY());
        mAgendaEvents.setPriority("0");

        mAgendaEvents.setUserassigned(new AgendaUserassigned(mDB.userDao().getUser().get(0).getId()+"", "0", "0","0"));

        mAgendaEvents.setUserownerid(mDB.userDao().getUser().get(0).getId()+"");
        mAgendaEvents.setSocid(newEvent.getTIER());
        mAgendaEvents.setNote(newEvent.getDESCRIPTION());

        SendAgendaEventTask sendAgendaEventTask = new SendAgendaEventTask(mAgendaEvents, CalendarActivity.this);
        sendAgendaEventTask.execute();

    }

    private List<EventsEntry> collectEventsByDate(String Date){
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

            Long id = listEvents.get(i).getId();
            String label = listEvents.get(i).getLABEL();
            String location = listEvents.get(i).getLIEU();
            String percentage = listEvents.get(i).getPERCENTAGE();
            String fullDayEvent = listEvents.get(i).getFULLDAYEVENT();
            String disponibility = listEvents.get(i).getTRANSPARENCY();
            String time = listEvents.get(i).getTIME();
            String date = listEvents.get(i).getDATE();
            String month = listEvents.get(i).getMONTH();
            String year = listEvents.get(i).getYEAR();
            Long startEvent = listEvents.get(i).getSTART_EVENT();
            Long endEvent = listEvents.get(i).getEND_EVENT();
            String description = listEvents.get(i).getDESCRIPTION();

            arrayList.add(new Events(id, label, location, percentage, fullDayEvent, disponibility, time, date, month, year, startEvent, endEvent, description));
        }
        return listEvents;
    }


    private void collectEventsPerMonth(String Month, String Year){
        Log.e(TAG, " collectEventsPerMonth( "+Month+", "+Year+")");
        Log.e(TAG, " clearing event list status: "+eventsList.size());
        eventsList.clear();

        List<EventsEntry> listEvents = mDB.eventsDao().getEventsByMonth(Month, Year);
        Log.e(TAG, " Results size: "+listEvents.size());

        /*
        for (int i=0; i<listEvents.size(); i++){
            Log.e(TAG," \nList size: "+i+"/"+listEvents.size()+"\n" +
                    "Event: "+listEvents.get(i).getLABEL()+"\n" +
                    "Time: "+listEvents.get(i).getTIME()+"\n" +
                    "Date: "+listEvents.get(i).getDATE()+"\n" +
                    "Month: "+listEvents.get(i).getMONTH()+"\n" +
                    "Year: "+listEvents.get(i).getYEAR()+"\n\n");

            Long id = listEvents.get(i).getId();
            String label = listEvents.get(i).getLABEL();
            String location = listEvents.get(i).getLIEU();
            String percentage = listEvents.get(i).getPERCENTAGE();
            String fullDayEvent = listEvents.get(i).getFULLDAYEVENT();
            String disponibility = listEvents.get(i).getTRANSPARENCY();
            String time = listEvents.get(i).getTIME();
            String date = listEvents.get(i).getDATE();
            String month = listEvents.get(i).getMONTH();
            String year = listEvents.get(i).getYEAR();
            Long startEvent = listEvents.get(i).getSTART_EVENT();
            Long endEvent = listEvents.get(i).getEND_EVENT();
            String description = listEvents.get(i).getDESCRIPTION();

            Events events = new Events(id, label, location, percentage, fullDayEvent, disponibility, time, date, month, year, startEvent, endEvent, description);
            eventsList.add(events);
        }
        */

        eventsList.addAll(listEvents);
        Log.e(TAG," eventList size: "+eventsList.size());
    }


    private void InitAddAgendaDialog(final Dialog dialog, final int position){
        final EditText libelle_et = dialog.findViewById(R.id.dialog_add_new_event_libelle);
        final EditText lieu_et = dialog.findViewById(R.id.dialog_add_new_event_lieu);
        final Switch journe_st = dialog.findViewById(R.id.dialog_add_new_event_journee_st);
        final Switch disponible_st = dialog.findViewById(R.id.dialog_add_new_event_disponible_st);

        final TextView dateStart = dialog.findViewById(R.id.dialog_add_new_event_display_starteventday_tv);
        final TextView timeStart = dialog.findViewById(R.id.dialog_add_new_event_display_starteventtime_tv);

        final TextView dateEnd = dialog.findViewById(R.id.dialog_add_new_event_display_endeventday_tv);
        final TextView timeEnd = dialog.findViewById(R.id.dialog_add_new_event_display_endeventtime_tv);

        final EditText description_et = dialog.findViewById(R.id.dialog_add_new_event_description_et);
        final Button add_btn = dialog.findViewById(R.id.dialog_add_new_event_addevent_btn);
        Button cancel_btn = dialog.findViewById(R.id.dialog_add_new_event_cancelevent_btn);

        Spinner clientSpinner = (Spinner) dialog.findViewById(R.id.dialog_add_new_event_concernclient_sp);
        ArrayAdapter<String> clientSpinnerAdapter = new ArrayAdapter<String>(CalendarActivity.this, android.R.layout.simple_spinner_item, getAllClients());
        clientSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientSpinner.setAdapter(clientSpinnerAdapter);

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

        final int[] startDate = new int[5];
        final int[] startEnd = new int[5];
        final String[] concernTier = new String[1];

        clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapterView.getSelectedItem().toString().equals("Veuillez selection Tiers concerné - Optionnelle")){
                    concernTier[0] = getSelectedClient(adapterView.getSelectedItem().toString()).getId().toString();
                    Toast.makeText(CalendarActivity.this, "Tiers concerné sélectionné: "+getSelectedClient(adapterView.getSelectedItem().toString()), Toast.LENGTH_SHORT).show();
                }else{
                    concernTier[0] = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //init date && time
        dateStart.setText(new SimpleDateFormat("EEEE, dd MMMM").format(dates.get(position)));
        timeStart.setText(new SimpleDateFormat("K:mm a").format(dates.get(position)));

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
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, day);
                                startDate[0] = year;
                                startDate[1] = month;
                                startDate[2] = day;

                                Log.e(TAG, " JL DatePickerDialog:: date=> "+ DateFormat.getDateInstance().format(c.getTime())+"\n" +
                                        "year: "+year+" || month: "+(month+1)+" || day: "+day);

                                SimpleDateFormat hformate = new SimpleDateFormat("EEEE, dd MMMM", Locale.FRENCH);
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
                                startDate[3] = hour;
                                startDate[4] = minutes;

                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.FRENCH);
                                String event_Time = hformate.format(c.getTime());

                                timeStart.setText(event_Time);
                            }
                        }, hoursStart, minutesStart,false);
                timePickerDialog.show();
            }
        });

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
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, day);
                                startEnd[0] = year;
                                startEnd[1] = month;
                                startEnd[2] = day;

                                SimpleDateFormat hformate = new SimpleDateFormat("EEEE, dd MMMM", Locale.FRENCH);
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
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hour);
                                c.set(Calendar.MINUTE, minutes);
                                c.setTimeZone(TimeZone.getDefault());
                                startEnd[3] = hour;
                                startEnd[4] = minutes;


                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.FRENCH);
                                String event_Time = hformate.format(c.getTime());

                                timeEnd.setText(event_Time);
                            }
                        }, hoursEnd, minutesEnd,false);
                timePickerDialog.show();
            }
        });

        final String date = eventDateFormat.format(dates.get(position));
        final String month = monthFormat.format(dates.get(position));
        final String year = yearFormat.format(dates.get(position));


        //save the Event in database
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;
                if (libelle_et.getText().toString().isEmpty()){
                    libelle_et.setError("Veuillez remplir le nom de l'événement!");
                    focusView = libelle_et;
                    cancel = true;
                }
                if ((startEnd[0] + startEnd[1] + startEnd[2]) == 0){
                    dateEnd.setError("Veuillez saisir une date de fin de l'événement!");
                    focusView = dateEnd;
                    cancel = true;
                }
                if ((startEnd[3] + startEnd[4]) == 0){
                    timeEnd.setError("Veuillez saisir une heure de fin de l'événement!");
                    focusView = timeEnd;
                    cancel = true;
                }

                //set event start and end calendars
                if ((startDate[0] + startDate[1] + startDate[2] + startDate[3] + startDate[4]) == 0){
                    combinedCalStart.setTime(dates.get(position));
                }else{
                    combinedCalStart.set(startDate[0], startDate[1], startDate[2], startDate[3], startDate[4]);
                }

                combinedCalEnd.set(startEnd[0], startEnd[1], startEnd[2], startEnd[3], startEnd[4]);


                //check if  startEventCalendar > endEventCalendar
                if (combinedCalStart.getTime().getTime() > combinedCalEnd.getTime().getTime()){
                    Log.e(TAG , " combinedCalStart= "+combinedCalStart.getTime().getTime()+" || combinedCalEnd= "+combinedCalEnd.getTime().getTime());
                    dateEnd.setError("Veuillez saisir une date de fin de l'événement!");
                    dateEnd.requestFocus();
                    timeEnd.setError("Veuillez saisir une heure de fin de l'événement!");
                    timeEnd.requestFocus();
                    cancel = true;
                    Toast.makeText(CalendarActivity.this, "Veuillez saisir correctement la durée de l'événement!", Toast.LENGTH_LONG).show();
                }

                if (cancel && (focusView != null)){
                    // form field with an error.
                    focusView.requestFocus();
                    return;
                }

                //save the event
                saveEvent(libelle_et.getText().toString(), lieu_et.getText().toString(), "-1", Boolean.toString(journe_st.isChecked()),
                        Boolean.toString(disponible_st.isChecked()), timeStart.getText().toString(), date, month, year,
                        combinedCalStart.getTimeInMillis(), combinedCalEnd.getTimeInMillis(), concernTier[0], description_et.getText().toString());
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

    private List<String> getAllClients(){
        List<String> theList = new ArrayList<>();
        List<ClientEntry> list = mDB.clientDao().getAllClient();

        theList.add("Veuillez selection Tiers concerné - Optionnelle");
        for (int i=0; i<list.size(); i++){
            theList.add(list.get(i).getName());
        }
        return theList;
    }

    private ClientEntry getSelectedClient(String clientName){
        ClientEntry clientEntry = null;
        List<ClientEntry> clientList = mDB.clientDao().getAllClient();
        for (int i=0; i<clientList.size(); i++){
            if (clientList.get(i).getName().equals(clientName)){
                clientEntry = clientList.get(i);
                break;
            }
        }
        return clientEntry;
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

    private void getEventsFromServer(){
        if (!ConnectionManager.isPhoneConnected(getApplication())){
            Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
            return;
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Objects.requireNonNull(CalendarActivity.this).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            Objects.requireNonNull(CalendarActivity.this).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        //affichage du loader dialog
        showProgressDialog(true, null, "Synchronisation des évènements en cours...");

        if (mFindAgendaEventTask == null) {
            mFindAgendaEventTask = new FindAgendaEventTask(this, CalendarActivity.this, "datec", "asc", mLimit, mPageEvent);
            mFindAgendaEventTask.execute();
        }
    }

    @Override
    public void onFindAgendaEventsTaskComplete(AgendaEventsREST mAgendaEventsREST) {
        mFindAgendaEventTask = null;

        //Si la recupération echoue, on renvoi un message d'erreur
        if (mAgendaEventsREST == null) {
            //Fermeture du loader
            showProgressDialog(false, null, null);
            Toast.makeText(this, getString(R.string.service_indisponible), Toast.LENGTH_LONG).show();
            return;
        }
        if (mAgendaEventsREST.getAgendaEvents() == null) {
            Objects.requireNonNull(this).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            //Fermeture du loader
            showProgressDialog(false, null, null);
            //reinitialisation du nombre de page
            mPageEvent = 0;

            Toast.makeText(this, "Evènements synchronizé !", Toast.LENGTH_LONG).show();

            collectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));
            return;
        }


        for (AgendaEvents eventItem : mAgendaEventsREST.getAgendaEvents()) {

            EventsEntry eventsEntry = new EventsEntry();
            eventsEntry.setId(eventItem.getId());
            eventsEntry.setREF(eventItem.getRef());
            eventsEntry.setLABEL(eventItem.getLabel());
            eventsEntry.setLIEU(eventItem.getLocation());
            eventsEntry.setPERCENTAGE(eventItem.getPercentage());
            eventsEntry.setFULLDAYEVENT(eventItem.getFulldayevent());
            eventsEntry.setTRANSPARENCY(eventItem.getTransparency());

            eventsEntry.setTIME(new SimpleDateFormat("K:mm a", Locale.FRENCH).format(new Date(eventItem.getDatec())));
            eventsEntry.setDATE(eventDateFormat.format(new Date(eventItem.getDatec())));
            eventsEntry.setMONTH(monthFormat.format(new Date(eventItem.getDatec())));
            eventsEntry.setYEAR(yearFormat.format(new Date(eventItem.getDatec())));

            eventsEntry.setSTART_EVENT(eventItem.getDatep());
            eventsEntry.setEND_EVENT(eventItem.getDatef());
            eventsEntry.setTIER(eventItem.getSocid());
            eventsEntry.setDESCRIPTION(eventItem.getNote());

            mDB.eventsDao().insertNewEvent(eventsEntry);
        }

        showProgressDialog(false, null, "Synchronisation des évènements en cours...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(item.getItemId() == R.id.action_fragclient_sync){
            if (!ConnectionManager.isPhoneConnected(this)) {
                Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
                return true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
