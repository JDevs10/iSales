package com.iSales.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iSales.database.AppDatabase;
import com.iSales.database.entry.SettingsEntry;
import com.iSales.model.ClientParcelable;
import com.iSales.remote.model.Order;
import com.iSales.remote.model.OrderLine;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class SendTicketMail extends AsyncTask<Void,Void,Void> {
    //Declaring Variables
    private final String TAG = SendTicketMail.class.getSimpleName();
    private Context context;
    private Session session;
    private String typeAction;
    private File logFile;

    //Email configuration
    private String configEmail;//commercial@anexys.fr
    private String configPassword;

    //Information to send email
    private String ticketEmail;
    private String ticketSubject;
    private String ticketBody;
    //private Files[] attachments;

    private AppDatabase mDB;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendTicketMail(Context context, String typeAction, File logFile, String email, String subject, String body){
        //Initializing variables
        this.context = context;
        this.typeAction = typeAction;
        this.logFile = logFile;
        this.ticketEmail = email;
        this.ticketSubject = subject;
        this.ticketBody = body;
        mDB = AppDatabase.getInstance(this.context);
    }

    /*
    public SendTicketMail(Context context, File logFile, String email, String subject, String body, Files[] attachments){
        //Initializing variables
        this.context = context;
        this.logFile = logFile;
        this.ticketEmail = email;
        this.ticketSubject = subject;
        this.ticketBody = body;
        this.attachments = attachments;
        mDB = AppDatabase.getInstance(this.context);
    }
    */

    private String setMessage(){
        if (typeAction.equals("Manuel-Ticket")) {
            return "Bonjour Team BDC,\n\n" +
                    "Le client .... sur iSales a rencontré un problème, il nous envoie un ticket Ref: ....\n" +
                    "Voici ci-dessous son ticket : \n\n" +
                    "";
        }else{
            return "";
        }
    }

    public boolean sendTicket(){
        //Creating properties
        boolean result = false;
        Properties props = new Properties();
        String newBody = setMessage();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(configEmail, configPassword);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(configEmail, "iSales Support Ticket"));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress("jl@anexys.fr"));
            mm.addRecipient(Message.RecipientType.CC, new InternetAddress(ticketEmail));
            mm.addRecipient(Message.RecipientType.CC, new InternetAddress("commercial@anexys.fr"));
            mm.addRecipient(Message.RecipientType.CC, new InternetAddress("fahd@anexys.fr"));
            //Adding subject
            mm.setSubject(ticketSubject);

            // Create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            // Now set the actual message
            messageBodyPart.setText(newBody);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = logFile.getAbsolutePath();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            mm.setContent(multipart);

            //Sending email
            Transport.send(mm);
            result = true;

        } catch (MessagingException e) {
            e.printStackTrace();
            result = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    private String dateFormat(String dateInString){
        Log.e(TAG, "deteFormat: date before => "+dateInString);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM");
        long dateLong = Long.valueOf(dateInString);
        Log.e(TAG, "deteFormat: date before => "+sdf.format(new Date(dateLong*1000)));
        return sdf.format(new Date(dateLong*1000));
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Get Email config
        SettingsEntry settings = mDB.settingsDao().getAllSettings().get(0);
        configEmail = settings.getEmail();
        configPassword = settings.getEmail_Pwd();

        if (settings.getEmail() == null || settings.getEmail().isEmpty() ||
            settings.getEmail_Pwd() == null || settings.getEmail_Pwd().isEmpty()){

            Toast.makeText(context, "Veuillez configurer votre adresse mail dans votre profile!", Toast.LENGTH_LONG).show();
            return null;
        }

        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(configEmail, configPassword);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(configEmail, "iSales Support Ticket"));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress("jl@anexys.fr"));
            mm.addRecipient(Message.RecipientType.CC, new InternetAddress(ticketEmail));
            mm.addRecipient(Message.RecipientType.CC, new InternetAddress("commercial@anexys.fr"));
            mm.addRecipient(Message.RecipientType.CC, new InternetAddress("fahd@anexys.fr"));
            //Adding subject
            mm.setSubject(ticketSubject);
            //Adding message
            mm.setText("body");

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
