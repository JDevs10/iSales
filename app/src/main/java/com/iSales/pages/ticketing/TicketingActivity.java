package com.iSales.pages.ticketing;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iSales.R;
import com.iSales.database.AppDatabase;
import com.iSales.database.entry.DebugItemEntry;
import com.iSales.task.SendTicketMail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.iSales.utility.ISalesUtility.makeSureFileWasCreatedThenMakeAvailable;

public class TicketingActivity extends AppCompatActivity {
    private final String TAG = TicketingActivity.class.getSimpleName();
    private EditText ticketing_name, ticketing_email, ticketing_body;
    private Button ticketing_save, ticketing_cancel;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);

        //Prevent the keyboard from displaying on activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        db = AppDatabase.getInstance(this);

        ticketing_name = findViewById(R.id.ticketingActivity_ticket_name_et);
        ticketing_email = findViewById(R.id.ticketingActivity_ticket_email_et);
        ticketing_body = findViewById(R.id.ticketingActivity_ticket_body_et);
        ticketing_cancel = findViewById(R.id.ticketingActivity_ticket_cancel_btn);
        ticketing_save = findViewById(R.id.ticketingActivity_ticket_send_btn);

        ticketing_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketing_name.setText("");
                ticketing_email.setText("");
                ticketing_body.setText("");
                onBackPressed();
            }
        });

        ticketing_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTicket(ticketing_name.getText().toString(), ticketing_email.getText().toString(), ticketing_body.getText().toString());
            }
        });
    }

    private void prepareTicket(String ticketName, String priorityTicket, String ticketEmail, String ticketBody){
        boolean error = false;

        if (ticketName.isEmpty()){
            ticketing_name.setFocusable(true);
            ticketing_name.setError("Veuillez renseigner le nom du Ticket!");
            error = true;
        }
        if(priorityTicket.isEmpty()){
            //put the priority of the ticket depending of the user selected subject
            //the selected subject is a ist of Ticket errors which have the priority attributed (see the table chart i did)
            ticketing_priority.setFocusable(true);
            ticketing_priority.setError("Veuillez renseigner le nom du Ticket!");
            error = true;
        }
        if (ticketEmail.isEmpty()){
            ticketing_email.setError("Veuillez renseigner votre email!");
            ticketing_email.setFocusable(true);
            error = true;
        }
        if (ticketBody.isEmpty()){
            ticketing_body.setError("Expliquer en détail l'anomalie!");
            ticketing_body.setFocusable(true);
            error = true;
        }
        if (error){
            return;
        }

        //get ref Ticket from date
        String refTicket = "ST_" + DateFormat.format("dd-MM-yyyy_hh:mm:ss", new Date((System.currentTimeMillis()/1000)*1000)).toString();

        //get all logs
        String logDataText = "";
        List<DebugItemEntry> debugList = db.debugMessageDao().getAllDebugMessages();
        for(int i=0; i<debugList.size(); i++){
            logDataText += DateFormat.format("dd-MM-yyyy hh:mm:ss", new Date(debugList.get(i).getDatetimeLong()*1000)).toString() + " | " + debugList.get(i).getMask() + "\n" + debugList.get(i).getErrorMessage();
        }

        //create log file
        File dir = new File(Environment.getExternalStorageDirectory(), "iSales/iSales Logs");
        if (!dir.exists()) {
            if (dir.mkdirs()){
                Log.e(TAG, "Log folder created" );
            }
        }

        File file = new File(dir, String.format("%s.log", "iSales_logs_"+(System.currentTimeMillis()/1000)));

        if (file.exists ()) file.delete();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(logDataText.getBytes());

            fos.flush();
            fos.close();
            makeSureFileWasCreatedThenMakeAvailable(this, file);

            file.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "saveProduitImage:FileNotFoundException "+e.getMessage() );

            //send email with ticket of exception
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "saveProduitImage:IOException "+e.getMessage() );

            //send email with ticket of exception
        }

        //get isales log file to send in email attachment
        SendTicketMail mSendTicketMail = new SendTicketMail(this, "Manuel-Ticket", file, ticketEmail, ticketName, ticketBody, refTicket, priorityTicket);
        mSendTicketMail.execute();
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
