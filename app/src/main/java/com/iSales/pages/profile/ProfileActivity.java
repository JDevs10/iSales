package com.iSales.pages.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iSales.R;
import com.iSales.adapter.DebugAdapter;
import com.iSales.database.AppDatabase;
import com.iSales.database.entry.DebugItemEntry;
import com.iSales.database.entry.DebugSettingsEntry;
import com.iSales.database.entry.ServerEntry;
import com.iSales.database.entry.SettingsEntry;
import com.iSales.database.entry.UserEntry;
import com.iSales.helper.DebugMe;
import com.iSales.pages.home.viewmodel.UserViewModel;
import com.iSales.remote.ConnectionManager;
import com.iSales.remote.model.DebugItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private String TAG = ProfileActivity.class.getSimpleName();

    private LinearLayout ly_admin1, ly_admin2, ly_admin3,ll_email;

    private TextView tv_mail, tv_societe, tv_server, tv_nom, tv_login, tv_lastConnexion, tv_etatConn, tv_email, tv_email_pwd;
    private EditText et_email, et_email_pwd;
    private ImageView iv_email_edit;
    private Switch sw_msgDebogage;
    private Button btn_winDebogage;
    private RecyclerView rv_debogage;

    private DebugAdapter debugAdapter;
    private boolean debugWin = false;
    private boolean checkDebugAdapter = false;
    private Handler handler;

    //database instance
    private AppDatabase mDB;
    private int mTotalPdtQuery;

    private UserEntry mUserEntry;

    private boolean edit_email_visible = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDB = AppDatabase.getInstance(getApplicationContext());

        tv_mail = (TextView) findViewById(R.id.activity_profile_text_view_email);
        tv_societe = (TextView) findViewById(R.id.activity_profile_text_view_societe);
        tv_server = (TextView) findViewById(R.id.activity_profile_text_view_serveur);
        tv_nom = (TextView) findViewById(R.id.activity_profile_text_view_nom);
        tv_login = (TextView) findViewById(R.id.activity_profile_text_view_login);
        tv_lastConnexion = (TextView) findViewById(R.id.activity_profile_text_view_derniere_connexion);
        tv_etatConn = (TextView) findViewById(R.id.activity_profile_connexion_etat);

        ll_email = (LinearLayout) findViewById(R.id.activity_profile_view_edit_email_view);
        tv_email = (TextView) findViewById(R.id.activity_profile_text_view_email_1);
        tv_email_pwd = (TextView) findViewById(R.id.activity_profile_text_view_email_password);
        et_email = (EditText) findViewById(R.id.activity_profile_view_edit_email_view_email_et);
        et_email_pwd = (EditText) findViewById(R.id.activity_profile_view_edit_email_view_pwd_et);
        iv_email_edit = (ImageView) findViewById(R.id.activity_profile_image_vien_edit_email);

        //check if the device is connected to the internet every 10secs
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                connexionState();
                                /*
                                if(checkDebugAdapter) {
                                    debugAdapter = new DebugAdapter(getApplicationContext(), getDebugData());
                                    rv_debogage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    rv_debogage.setAdapter(debugAdapter);
                                }
                                */
                            }
                        });
                        Thread.sleep(10000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();


        //Get current user information
        loadUser();

        iv_email_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_email_visible){
                    if (!et_email.getText().toString().isEmpty() && !et_email_pwd.getText().toString().isEmpty()){
                        SettingsEntry settings = mDB.settingsDao().getAllSettings().get(0);
                        settings.setEmail(et_email.getText().toString().trim());
                        settings.setEmail_Pwd(et_email_pwd.getText().toString().trim());

                        mDB.settingsDao().updateSettings(settings);
                        tv_email.setText(et_email.getText().toString().trim());
                        tv_email_pwd.setText(et_email_pwd.getText().toString().trim());
                        Toast.makeText(ProfileActivity.this, "Configuration email enregistre!", Toast.LENGTH_SHORT).show();
                    }

                    ll_email.setVisibility(View.GONE);
                    edit_email_visible = false;
                }else{
                    ll_email.setVisibility(View.VISIBLE);
                    edit_email_visible = true;
                }
            }
        });
    }

    //    recuperation du user connecté dans la BD
    private void loadUser() {
        final UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.getUserEntry().observe(this, new Observer<List<UserEntry>>() {
            @Override
            public void onChanged(@Nullable List<UserEntry> userEntries) {

                //S'il n'y a aucun user alors on supprime la BD
                if (userEntries == null || userEntries.size() <= 0) {
                    finish();
                    return;
                }

                mUserEntry = userEntries.get(0);
                new DebugMe(ProfileActivity.this, ProfileActivity.this, "WL-LL",
                        TAG+" Server : Id : "+mUserEntry.getId()+"\n" +
                                "Status : "+mUserEntry.getStatut()+"\n " +
                                "Employee : "+mUserEntry.getEmployee()+"\n" +
                                "Gender : "+mUserEntry.getGender()+"\n" +
                                "Birth : "+mUserEntry.getBirth()+"\n" +
                                "Email : "+mUserEntry.getEmail()+"\n" +
                                "Firstname : "+mUserEntry.getFirstname()+"\n" +
                                "Lastname : "+mUserEntry.getLastname()+"\n" +
                                "Name : "+mUserEntry.getName()+"\n" +
                                "Country : "+mUserEntry.getCountry()+"\n" +
                                "Date employment : "+mUserEntry.getDateemployment()+"\n" +
                                "Photo : "+mUserEntry.getPhoto()+"\n" +
                                "Dernière connexion : "+mUserEntry.getDatelastlogin()+"\n" +
                                "Date c : "+mUserEntry.getDatec()+"\n" +
                                "Date m :"+mUserEntry.getDatem()+"\n" +
                                "Admin : "+mUserEntry.getAdmin()+"\n" +
                                "Login : "+mUserEntry.getLogin()+"\n" +
                                "Ville : "+mUserEntry.getTown()+"\n" +
                                "Address : "+mUserEntry.getAddress()).execute();


                //check if current user is SuperAdmin
                SuperAdmin(mUserEntry.getAdmin());

                //init
                initUserValues();
            }
        });
    }

    public void initUserValues() {
        ServerEntry mServerEntry = mDB.serverDao().getActiveServer(true);
        String server = (mServerEntry.getHostname().replace("http://","")).replace("/api/index.php","");

        Log.e(TAG, " mUserEntry.getDatelastlogin() => "+mUserEntry.getDatelastlogin());
        String dateString;
        if (mUserEntry.getDatelastlogin().matches("[0-9]+") && mUserEntry.getDatelastlogin().length() > 8){
            long millisecond = Long.parseLong(mUserEntry.getDatelastlogin());
            dateString = DateFormat.format("EEEE, dd MMMM yyy @ K:mm a", new Date(millisecond*1000)).toString();
        }else{
            dateString = "dd-MM-yyyy @ hh:mm:ss";
        }


        tv_mail.setText(mUserEntry.getEmail());
        tv_societe.setText(mServerEntry.getRaison_sociale());
        tv_server.setText(server);
        tv_nom.setText(mUserEntry.getLastname());
        tv_login.setText(mUserEntry.getLogin());
        tv_lastConnexion.setText(dateString);

        SettingsEntry settings = mDB.settingsDao().getAllSettings().get(0);
        tv_email.setText(settings.getEmail());
        tv_email_pwd.setText(settings.getEmail_Pwd());
        if (settings != null){
            if (settings.getEmail() != null && !settings.getEmail().isEmpty()){
                tv_email.setText(settings.getEmail());
            }else{
                tv_email.setText("");
            }
            if (settings.getEmail_Pwd() != null && !settings.getEmail_Pwd().isEmpty()){
                tv_email_pwd.setText(settings.getEmail_Pwd());
            }else{
                tv_email_pwd.setText("");
            }
        }else{
            tv_email.setText("");
            tv_email_pwd.setText("");
        }

        //Log.e(TAG, "Last Connexion : "+dateString);
    }

    private void connexionState(){
        //Si le téléphone n'est pas connecté
        if (!ConnectionManager.isPhoneConnected(this)) {
            tv_etatConn.setBackgroundResource(R.drawable.circle_red);
            new DebugMe(ProfileActivity.this, ProfileActivity.this, "LL",
                    TAG+" connexionState() => called.\nConnexion offline").execute();

            Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_LONG).show();
        }else{
            tv_etatConn.setBackgroundResource(R.drawable.circle_green);
            new DebugMe(ProfileActivity.this, ProfileActivity.this, "LL",
                    TAG+" connexionState() => called.\nConnexion online").execute();
        }
    }

    private void SuperAdmin(String isAdmin){
        if (isAdmin.equals("1")){
            //addDebugStaticData();

            //init superAdmin accesses
            ly_admin1 = (LinearLayout) findViewById(R.id.activity_profile_layout_admin_1);
            ly_admin2 = (LinearLayout) findViewById(R.id.activity_profile_layout_admin_2);
            ly_admin3 = (LinearLayout) findViewById(R.id.activity_profile_layout_admin_3);

            sw_msgDebogage = (Switch) findViewById(R.id.activity_profile_switch_msg_debogage);
            btn_winDebogage = (Button) findViewById(R.id.activity_profile_btn_debogage);
            rv_debogage = (RecyclerView) findViewById(R.id.activity_profile_recycle_view_debogage);

            ly_admin1.setVisibility(View.VISIBLE);
            ly_admin2.setVisibility(View.VISIBLE);

            // check if the debug check is checked
            if (mDB.debugSettingsDao().getAllDebugSettings().get(0).getCheckDebug() == 1){
                sw_msgDebogage.setChecked(true);
            }
            sw_msgDebogage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        mDB.debugSettingsDao().updateDebugSettings(new DebugSettingsEntry(1, 1));
                        Toast.makeText(ProfileActivity.this, "Le mode débogage est activé !\nDébogage état: "+mDB.debugSettingsDao().getAllDebugSettings().get(0).getCheckDebug(), Toast.LENGTH_SHORT).show();
                    }else{
                        mDB.debugSettingsDao().updateDebugSettings(new DebugSettingsEntry(1, 0));
                        Toast.makeText(ProfileActivity.this, "Le mode débogage est désactivé !\nDébogage état: "+mDB.debugSettingsDao().getAllDebugSettings().get(0).getCheckDebug(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btn_winDebogage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!debugWin) {
                        btn_winDebogage.setBackgroundColor(Color.GREEN);
                        btn_winDebogage.setText("Ouvert");
                        ly_admin3.setVisibility(View.VISIBLE);
                        debugWin = true;

                        debugAdapter = new DebugAdapter(getApplicationContext(), getDebugData());
                        rv_debogage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv_debogage.setAdapter(debugAdapter);
                        checkDebugAdapter = true;

                        Toast.makeText(ProfileActivity.this, "La fenêtre des logs est ouverte!", Toast.LENGTH_SHORT).show();
                    }else{
                        btn_winDebogage.setBackgroundColor(Color.RED);
                        btn_winDebogage.setText("Fermer");
                        ly_admin3.setVisibility(View.GONE);
                        debugWin = false;
                        checkDebugAdapter = false;
                        Toast.makeText(ProfileActivity.this, "La fenêtre des logs est fermé!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private List<DebugItemEntry> getDebugData(){
        List<DebugItemEntry> debugItemEntries = mDB.debugMessageDao().getAllDebugMessages();
        mTotalPdtQuery = debugItemEntries.size();

        Log.e(TAG, "getDebugData():: debugItemEntries=" + debugItemEntries.size() +
                " mTotalPdtQuery=" + mTotalPdtQuery);

        for (int i=0; i<debugItemEntries.size(); i++){
            DebugItemEntry debugItemEntry = debugItemEntries.get(i);
            Log.e(TAG, " getDebugData():: mCurrentPdtQuery = "+i+" debugMessageID = "+debugItemEntry.getRowId());


        }
    return debugItemEntries;
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
