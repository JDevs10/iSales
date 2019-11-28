package com.iSales.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
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
    private String refTicket;
    private String priorityTicket;
    //private Files[] attachments;

    private AppDatabase mDB;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendTicketMail(Context context, String typeAction, File logFile, String email, String subject, String body, String refTicket, String priorityTicket){
        //Initializing variables
        this.context = context;
        this.typeAction = typeAction;
        this.logFile = logFile;
        this.ticketEmail = email;
        this.ticketSubject = subject;
        this.ticketBody = body;
        this.refTicket = refTicket;
        this.priorityTicket = priorityTicket;
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
        String deviceOS = System.getProperty("os.version");     // OS version
        String deviceSDK = android.os.Build.VERSION.SDK;        // API Level
        String device = android.os.Build.DEVICE;                // Device
        String deviceModel = android.os.Build.MODEL;            // Model
        String deviceProduct = android.os.Build.PRODUCT;        // Product
        String deviceBrand = android.os.Build.BRAND;            // Brand

        if (typeAction.equals("Automatic-Ticket")) {
            String clientName = mDB.userDao().getUser().get(0).name;
            String companyName = mDB.serverDao().getActiveServer(true).getRaison_sociale();
            return "Bonjour Team BDC,\n\n" +
                    "Le client "+clientName+" ("+companyName+") sur iSales a rencontré un problème, il nous envoie un ticket Ref: "+refTicket+"\n" +
                    "Voici ci-dessous son ticket : \n\n" +
                    "Ref: "+refTicket+"\n" +
                    "Priorité: "+priorityTicket+"\n" +
                    "Client "+clientName+" ("+companyName+")\n\n" +
                    "Information sur l'appareil utiliser : \n" +
                    "Appareil (Device) ===> "+device+"\n" +
                    "Marque (Brand) ===> "+deviceBrand+"\n" +
                    "Produit (Product) ===> "+deviceProduct+"\n" +
                    "Model (Model) ===> "+deviceModel+"\n" +
                    "SDK (SDK) ===> "+deviceSDK+"\n" +
                    "OS (OS) ===> "+deviceOS+"\n\n" +
                    "" + ticketBody;

        }else if(typeAction.equals("Manuel-Ticket")){
            String clientName = mDB.userDao().getUser().get(0).name;
            String companyName = mDB.serverDao().getActiveServer(true).getRaison_sociale();
            return "Bonjour Team BDC,\n\n" +
                    "Ceci est un Ticket Automatique qui vient de iSales du client "+clientName+" ("+companyName+") connecter.\n" +
                    "";
        }
        return "NULL";
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
        //Creating properties
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
                        return new PasswordAuthentication("jl@anexys.fr", "anexys1,");
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress("jl@anexys.fr", "iSales Support Ticket"));
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

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = logFile.getPath();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            mm.setContent(multipart);

            //Sending email
            Transport.send(mm);
            //multipart.removeBodyPart(1);

            //delete file after send
            if (logFile.exists()){
                Log.e(TAG, "Delete: " + logFile.getPath());
                logFile.delete();
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e(TAG, "onPostExecute()");
    }
}