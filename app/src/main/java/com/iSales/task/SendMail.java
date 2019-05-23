package com.iSales.task;

import javax.mail.Session;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

import com.iSales.model.ClientParcelable;
import com.iSales.remote.model.Order;
import com.iSales.remote.model.OrderLine;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends AsyncTask<Void,Void,Void> {
    //Declaring Variables
    private Context context;
    private Session session;

    //Email configuration
    private final String configEmail = "jl@anexys.fr";
    private final String configPassword = "JLAnexys20";

    //Information to send email
    private String email;
    private String subject;
    private ClientParcelable userSelected;
    private Order data;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String email, String subject, ClientParcelable userSelected, Order data){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.userSelected = userSelected;
        this.data = data;
    }

    private String getMessage(Order data){
        List<OrderLine> listOrderLine = data.getLines();
        String orderLineMessage = "";

        for (int i=0; i<listOrderLine.size(); i++){
            orderLineMessage += "   • Ref: "+listOrderLine.get(i).getRef()+
                                ", Label: "+listOrderLine.get(i).getLabel()+
                                ", Quantité: "+listOrderLine.get(i).getQty()+
                                ", Prix: "+priceFormat(listOrderLine.get(i).getPrice())+" €\n";
        }
        orderLineMessage += "\nEquivalent d'un prix total de "+priceFormat(data.getTotal_ttc())+" € par "+data.getMode_reglement();

        return  "Bonjour Madame, Monsieur "+userSelected.getName()+"\n\n" +
                "Veuillez trouver, ci-joint la commande référence: "+data.getRef()+" effectué le "+data.getDate_commande()+" avec vos produits selectionné :\n"+
                orderLineMessage + "\n\n\nCordialement.";
    }

    private String priceFormat(String priceST){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(Double.valueOf(priceST));
    }



    private String dateFormat(String dateInLong){
        Log.e("SendMail", "deteFormat: date before => "+dateInLong);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long dateLong = Long.valueOf(dateInLong);
        Log.e("SendMail", "deteFormat: date before => "+dateLong);
        return sdf.format(dateLong);
    }

    @Override
    protected Void doInBackground(Void... params) {
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
            mm.setFrom(new InternetAddress(configEmail));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(getMessage(data));

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
