package com.iSales.pages.ticketing;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iSales.R;
import com.iSales.database.AppDatabase;
import com.iSales.database.entry.DebugItemEntry;
import com.iSales.interfaces.SendindMailListener;
import com.iSales.model.ISalesIncidentTable;
import com.iSales.task.SendTicketMail;
import com.iSales.utility.ISalesUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.iSales.utility.ISalesUtility.makeSureFileWasCreatedThenMakeAvailable;

public class TicketingActivity extends AppCompatActivity implements SendindMailListener {
    private final String TAG = TicketingActivity.class.getSimpleName();
    private EditText ticketing_name, ticketing_email, ticketing_body;
    private Spinner ticketing_subject_sp;
    private Button ticketing_save, ticketing_cancel;
    private AppDatabase db;
    private ProgressDialog mProgressDialog;

    private String ticketing_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);

        //Prevent the keyboard from displaying on activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        db = AppDatabase.getInstance(this);

        ticketing_name = findViewById(R.id.ticketingActivity_ticket_name_et);
        ticketing_subject_sp = findViewById(R.id.ticketingActivity_ticket_subjet_sp);
        ticketing_email = findViewById(R.id.ticketingActivity_ticket_email_et);
        ticketing_body = findViewById(R.id.ticketingActivity_ticket_body_et);
        ticketing_cancel = findViewById(R.id.ticketingActivity_ticket_cancel_btn);
        ticketing_save = findViewById(R.id.ticketingActivity_ticket_send_btn);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getIncidentNameList());
        ticketing_subject_sp.setAdapter(adapter);
        ticketing_subject_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("Veuillez selectionne un sujet du ticket...") || parent.getSelectedItem().toString().equals("")){
                    ticketing_subject = null;
                }else{
                    ticketing_subject = parent.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                showProgressDialog(true, null, "Préparer du ticket en cours....");
                prepareTicket(ticketing_name.getText().toString(), ticketing_subject, ticketing_email.getText().toString(), ticketing_body.getText().toString());
            }
        });
    }

    private ArrayList<String> getIncidentNameList(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Veuillez selectionne un sujet du ticket....");

        for (int i=0; i<ISalesUtility.getiSalesIncidentList().size(); i++){
            list.add(ISalesUtility.getiSalesIncidentList().get(i).getName());
        }
        return list;
    }

    private ISalesIncidentTable getISalesIncidentByName(String name){
        ISalesIncidentTable incident = null;

        if (name != null && !name.isEmpty()){
            incident = new ISalesIncidentTable();

            for (int i=0; i<ISalesUtility.getiSalesIncidentList().size(); i++){
                if (ISalesUtility.getiSalesIncidentList().get(i).getName().equals(name)){
                    incident = ISalesUtility.getiSalesIncidentList().get(i);
                    return incident;
                }
            }
        }else{
            incident = null;
        }
        return incident;
    }

    private void prepareTicket(String ticketName, String subjetTicket, String ticketEmail, String ticketBody){
        boolean error = false;

        if (ticketName.isEmpty()){
            ticketing_name.setFocusable(true);
            ticketing_name.setError("Veuillez renseigner le nom du Ticket!");
            error = true;
        }
        if(subjetTicket == null || subjetTicket.isEmpty()){
            TextView spinner_erreur = (TextView) ticketing_subject_sp.getSelectedView();
            spinner_erreur.setError("Veuillez selectionne un sujet du ticket !");
            spinner_erreur.setTextColor(Color.RED);
            spinner_erreur.setText("Veuillez selectionne un sujet du ticket !");
            ticketing_subject_sp.setFocusable(true);
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
        Date date = new Date((System.currentTimeMillis()/1000)*1000);
        String companyName = db.serverDao().getActiveServer(true).getRaison_sociale();
        String refTicket = "ST_"+companyName.replace(' ','_')+"_" + date.getTime();

        //get all logs
        String logDataText = "";
        String logDataClassName = "";
        boolean firstDebugMessage = true;
        List<DebugItemEntry> debugList = db.debugMessageDao().getAllDebugMessages();
        for (int i=0; i<debugList.size(); i++){
            if (logDataClassName.equals(debugList.get(i).getClassName())){
                logDataText += DateFormat.format("dd-MM-yyyy hh:mm:ss", new Date(debugList.get(i).getDatetimeLong()*1000)).toString() + " | Mask: " + debugList.get(i).getMask() + "\n" +
                        "Class: " + debugList.get(i).getClassName() + " => Method: " +debugList.get(i).getMethodName() + "\n" +
                        "Message: "+debugList.get(i).getMessage()+"\nStraceStack: "+debugList.get(i).getStackTrace()+"\n";
            }else{
                logDataClassName = debugList.get(i).getClassName();
                if(firstDebugMessage){
                    logDataText += DateFormat.format("dd-MM-yyyy hh:mm:ss", new Date(debugList.get(i).getDatetimeLong()*1000)).toString() + " | Mask: " + debugList.get(i).getMask() + "\n" +
                            "Class: " + debugList.get(i).getClassName() + " => Method: " +debugList.get(i).getMethodName() + "\n" +
                            "Message: "+debugList.get(i).getMessage()+"\nStraceStack: "+debugList.get(i).getStackTrace()+"\n";
                    firstDebugMessage = false;
                }else{
                    logDataText += "\n\n"+DateFormat.format("dd-MM-yyyy hh:mm:ss", new Date(debugList.get(i).getDatetimeLong()*1000)).toString() + " | Mask: " + debugList.get(i).getMask() + "\n" +
                            "Class: " + debugList.get(i).getClassName() + " => Method: " +debugList.get(i).getMethodName() + "\n" +
                            "Message: "+debugList.get(i).getMessage()+"\nStraceStack: "+debugList.get(i).getStackTrace()+"\n";
                }
            }
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

        ISalesIncidentTable iSalesIncident = getISalesIncidentByName(subjetTicket);

        //get isales log file to send in email attachment
        SendTicketMail mSendTicketMail = new SendTicketMail(this, TicketingActivity.this, "Manuel-Ticket", file, ticketEmail, ticketName, ticketBody, refTicket, iSalesIncident);
        mSendTicketMail.execute();
    }
    
    @Override
    public void onMailSend() {
        showProgressDialog(false, null, null);
        ticketing_name.setText("");
        ticketing_subject_sp.setSelection(0);
        ticketing_email.setText("");
        ticketing_body.setText("");
        Toast.makeText(this, "Le Ticket est envoyé avec success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
