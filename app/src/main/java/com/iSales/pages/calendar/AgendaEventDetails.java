package com.iSales.pages.calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iSales.R;
import com.iSales.database.AppDatabase;
import com.iSales.database.entry.EventsEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AgendaEventDetails extends AppCompatActivity {
    private String TAG = AgendaEventDetails.class.getSimpleName();

    private ImageView eventImage_iv;
    private TextView eventRef_tv, eventLabel_tv;
    private EditText eventDateStart, eventDateEnd, eventLocation, eventAssignTo, eventDescription;
    private CheckBox eventFullDay_cb;
    private Spinner eventConcernTier;

    private Button annuler_btn, modifierValider_btn, supprimer_btn;
    private Events event;

    private boolean isModifiable = false;

    final Calendar combinedCalStart = Calendar.getInstance(Locale.FRENCH);
    final Calendar combinedCalEnd = Calendar.getInstance(Locale.FRENCH);

    private AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_event_details);

        //Prevent the keyboard from displaying on activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mDB = AppDatabase.getInstance(getApplicationContext());

        //init views
        eventImage_iv = (ImageView) findViewById(R.id.activity_agenda_event_details_event_img_iv);
        eventRef_tv = (TextView) findViewById(R.id.activity_agenda_event_details_ref_tv);
        eventLabel_tv = (TextView) findViewById(R.id.activity_agenda_event_details_label_tv);
        eventDateStart = (EditText) findViewById(R.id.activity_agenda_event_details_startevent_et);
        eventDateEnd = (EditText) findViewById(R.id.activity_agenda_event_details_endevent_et);
        eventLocation = (EditText) findViewById(R.id.activity_agenda_event_details_locationevent_et);
        eventAssignTo = (EditText) findViewById(R.id.activity_agenda_event_details_eventassigneto_et);
        eventDescription = (EditText) findViewById(R.id.activity_agenda_event_details_eventdescription_et);
        eventFullDay_cb = (CheckBox) findViewById(R.id.activity_agenda_event_details_fulldayevent_cb);
        eventConcernTier = (Spinner) findViewById(R.id.activity_agenda_event_details_concerntier_sp);
        viewModifyChange(false);

        annuler_btn = (Button) findViewById(R.id.activity_agenda_event_details_annuler_btn);
        modifierValider_btn = (Button) findViewById(R.id.activity_agenda_event_details_modifier_valider_btn);
        supprimer_btn = (Button) findViewById(R.id.activity_agenda_event_details_supprimer_btn);


        //get event passed from AgendarEventAdapter
        event = (Events) getIntent().getSerializableExtra("eventObj");
        displayCurrentEvent(event);

        eventDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isModifiable){
                    dateOnClick("Start");
                }
            }
        });

        eventDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isModifiable){
                    dateOnClick("End");
                }
            }
        });

        modifierValider_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isModifiable){
                    isModifiable = true;
                    viewModifyChange(isModifiable);
                    modifierValider_btn.setText("Enregister");
                    //set all field to be modify ...
                }else{
                    //set all field not to be modify ...
                    isModifiable = false;
                    viewModifyChange(isModifiable);
                    modifierValider_btn.setText("Modifier");
                    //save the modification
                    saveModifications(event);
                }
            }
        });
        supprimer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButton(event);
            }
        });
        annuler_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to agenda activity
                onBackPressed();
            }
        });
    }

    private void saveModifications(Events currentEvent){
        /*
        currentEvent.setFULLDAYEVENT(Boolean.toString(eventFullDay_cb.isChecked()));
        currentEvent.setSTART_EVENT(convertTimeStringToLong(eventDateStart.getText().toString().trim()));
        currentEvent.setEND_EVENT(convertTimeStringToLong(eventDateEnd.getText().toString().trim()));
        currentEvent.setLIEU(eventLocation.getText().toString().trim());
        currentEvent.setDESCRIPTION(eventDescription.getText().toString().trim());
        */

        EventsEntry currentEventEntry = mDB.eventsDao().getEventsById(currentEvent.getID()).get(0);
        currentEventEntry.setFULLDAYEVENT(Boolean.toString(eventFullDay_cb.isChecked()));
        currentEventEntry.setSTART_EVENT(convertTimeStringToLong(eventDateStart.getText().toString().trim()));
        currentEventEntry.setEND_EVENT(convertTimeStringToLong(eventDateEnd.getText().toString().trim()));
        currentEventEntry.setLIEU(eventLocation.getText().toString().trim());
        currentEventEntry.setDESCRIPTION(eventDescription.getText().toString().trim());

        mDB.eventsDao().updateEvent(currentEventEntry);
        //back to aganda activity
        startActivity(new Intent(AgendaEventDetails.this, CalendarActivity.class));

        Toast.makeText(this, "Evènement "+currentEventEntry.getId()+" Enregisté!", Toast.LENGTH_SHORT).show();
    }

    private void deleteButton(Events event) {
        //delete local
        mDB.eventsDao().deleteEvent(event.getID());
        //but need to delete on the server
        mDB.agendaEventsDao().deleteEvent(event.getID());
        Toast.makeText(this, "Evènement supprimé!", Toast.LENGTH_SHORT).show();
        //back to aganda activity
        startActivity(new Intent(AgendaEventDetails.this, CalendarActivity.class));
    }

    private void viewModifyChange(Boolean isChecked){
        Log.e(TAG, " viewModifyChange( "+isChecked+" )");
        eventFullDay_cb.setClickable(isChecked);
        //////////////////////////////////////////////
        eventDateStart.setClickable(true);
        eventDateStart.setCursorVisible(false);
        eventDateStart.setFocusable(false);
        eventDateStart.setFocusableInTouchMode(false);
        //////////////////////////////////////////////
        eventDateEnd.setClickable(true);
        eventDateEnd.setCursorVisible(false);
        eventDateEnd.setFocusable(false);
        eventDateEnd.setFocusableInTouchMode(false);
        //////////////////////////////////////////////
        eventLocation.setClickable(isChecked);
        eventLocation.setCursorVisible(isChecked);
        eventLocation.setFocusable(isChecked);
        eventLocation.setFocusableInTouchMode(isChecked);
        //////////////////////////////////////////////
        eventAssignTo.setClickable(isChecked);
        eventAssignTo.setCursorVisible(isChecked);
        eventAssignTo.setFocusable(isChecked);
        eventAssignTo.setFocusableInTouchMode(isChecked);
        ////////////////////////////////////////////
        eventConcernTier.setClickable(isChecked);
        eventConcernTier.setFocusable(isChecked);
        eventConcernTier.setFocusableInTouchMode(isChecked);
        /////////////////////////////////////////////
        eventDescription.setClickable(isChecked);
        eventDescription.setCursorVisible(isChecked);
        eventDescription.setFocusable(isChecked);
        eventDescription.setFocusableInTouchMode(isChecked);
    }

    private void displayCurrentEvent(Events event){
        //eventImage_iv;
        eventRef_tv.setText("Ref: nothing...");
        eventLabel_tv.setText(event.getLABEL());
        eventDateStart.setText(convertTimeStamp(event.getSTART_EVENT()));
        eventDateEnd.setText(convertTimeStamp(event.getEND_EVENT()));
        eventLocation.setText(event.getLIEU());
        eventDescription.setText(event.getDESCRIPTION());
        eventFullDay_cb.setChecked(Boolean.valueOf(event.getFULLDAYEVENT()));
        //eventConcernTier;
    }

    private String convertTimeStamp(Long dateInLong){
        Log.e(TAG, " convertTimeStamp( "+dateInLong+" )");
        if (dateInLong == null){
            return "Aucunne date";
        }
        Calendar cal = Calendar.getInstance(Locale.FRENCH);
        cal.setTime(new Date(dateInLong));
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyy 'à' K:mm a", Locale.FRENCH);
        return sdf.format(cal.getTime());
    }

    private Long convertTimeStringToLong(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyy 'à' K:mm a", Locale.FRENCH);
        Long res = null;

        try {
            Date date = sdf.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            res = cal.getTime().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e(TAG, " convertTimeStringToLong( "+dateInString+" ) ==> result: "+res);
        return res;
    }

    private void dateOnClick(String XXXX){
        switch(XXXX){
            case "Start":
                eventDateStart.setText("");
                Calendar calendarStart = Calendar.getInstance(Locale.FRENCH);
                final int yearStart = calendarStart.get(Calendar.YEAR);
                final int monthStart = calendarStart.get(Calendar.MONTH);
                final int dayStart = calendarStart.get(Calendar.DAY_OF_MONTH);
                final int hoursStart = calendarStart.get(Calendar.HOUR_OF_DAY);
                final int minutesStart = calendarStart.get(Calendar.MINUTE);

                final TimePickerDialog timePickerDialog1 = new TimePickerDialog(eventDateStart.getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                Calendar c = Calendar.getInstance(Locale.FRENCH);
                                c.set(Calendar.HOUR_OF_DAY, hour);
                                c.set(Calendar.MINUTE, minutes);
                                c.setTimeZone(TimeZone.getDefault());

                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.FRENCH);
                                String time = hformate.format(c.getTime());

                                String date = eventDateStart.getText().toString();
                                eventDateStart.setText(date + " à " + time);
                                //set the timeStamp from datePicker && timePicker
                                combinedCalStart.set(yearStart, monthStart, dayStart, hoursStart, minutes);
                            }
                        }, hoursStart, minutesStart, false);
                timePickerDialog1.show();

                DatePickerDialog mDatePickerDialog1 = new DatePickerDialog(eventDateStart.getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar c = Calendar.getInstance(Locale.FRENCH);
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, day);

                                Log.e(TAG, " JL DatePickerDialog:: date=> "+ DateFormat.getDateInstance().format(c.getTime())+"\n" +
                                        "year: "+year+" || month: "+(month+1)+" || day: "+day);

                                SimpleDateFormat hformate = new SimpleDateFormat("EEEE, dd MMMM yyy", Locale.FRENCH);
                                String event_day = hformate.format(c.getTime());
                                Log.e(TAG, " JL onDateSet:: "+event_day);
                                eventDateStart.setText(event_day);
                            }
                        },yearStart, monthStart, dayStart);
                mDatePickerDialog1.show();

                mDatePickerDialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timePickerDialog1.dismiss();
                    }
                });
                break;

            case "End":
                eventDateEnd.setText("");
                Calendar calendarEnd = Calendar.getInstance(Locale.FRENCH);
                final int yearEnd = calendarEnd.get(Calendar.YEAR);
                final int monthEnd = calendarEnd.get(Calendar.MONTH);
                final int dayEnd = calendarEnd.get(Calendar.DAY_OF_MONTH);
                final int hoursEnd = calendarEnd.get(Calendar.HOUR_OF_DAY);
                final int minutesEnd = calendarEnd.get(Calendar.MINUTE);

                final TimePickerDialog timePickerDialog2 = new TimePickerDialog(eventDateEnd.getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                Calendar c = Calendar.getInstance(Locale.FRENCH);
                                c.set(Calendar.HOUR_OF_DAY, hour);
                                c.set(Calendar.MINUTE, minutes);
                                c.setTimeZone(TimeZone.getDefault());

                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.FRENCH);
                                String time = hformate.format(c.getTime());

                                String date = eventDateEnd.getText().toString();
                                eventDateEnd.setText(date + " à " + time);
                                //set the timeStamp from datePicker && timePicker
                                combinedCalEnd.set(yearEnd, monthEnd, dayEnd, hoursEnd, minutes);
                            }
                        }, hoursEnd, minutesEnd, false);
                timePickerDialog2.show();

                DatePickerDialog mDatePickerDialog2 = new DatePickerDialog(eventDateEnd.getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar c = Calendar.getInstance(Locale.FRENCH);
                                c.set(Calendar.YEAR, datePicker.getYear());
                                c.set(Calendar.MONTH, datePicker.getMonth());
                                c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                                Log.e(TAG, " JL DatePickerDialog:: date=> "+ DateFormat.getDateInstance().format(c.getTime())+"\n" +
                                        "year: "+year+" || month: "+(month+1)+" || day: "+day);

                                SimpleDateFormat hformate = new SimpleDateFormat("EEEE, dd MMMM yyy", Locale.FRENCH);
                                String event_day = hformate.format(c.getTime());
                                Log.e(TAG, " JL onDateSet:: "+event_day);
                                eventDateEnd.setText(event_day);
                            }
                        },yearEnd, monthEnd, dayEnd);
                mDatePickerDialog2.show();

                mDatePickerDialog2.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timePickerDialog2.dismiss();
                    }
                });
                break;
        }
    }
}
