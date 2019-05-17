package com.iSales.pages.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iSales.R;
import com.iSales.database.AppDatabase;
import com.iSales.database.entry.ServerEntry;
import com.iSales.database.entry.UserEntry;
import com.iSales.pages.home.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private String TAG = ProfileActivity.class.getSimpleName();

    private LinearLayout ly_admin1, ly_admin2, ly_admin3;

    private TextView tv_mail, tv_societe, tv_server, tv_nom, tv_login, tv_lastConnexion;
    private Switch sw_msgDebogage;
    private Button btn_winDebogage;
    private RecyclerView rv_debogage;

    private boolean debugWin = false;

    //database instance
    private AppDatabase mDB;

    private UserEntry mUserEntry;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ly_admin3 = (LinearLayout) findViewById(R.id.activity_profile_layout_admin_3);

        tv_mail = (TextView) findViewById(R.id.activity_profile_text_view_email);
        tv_societe = (TextView) findViewById(R.id.activity_profile_text_view_email);
        tv_server = (TextView) findViewById(R.id.activity_profile_text_view_email);
        tv_nom = (TextView) findViewById(R.id.activity_profile_text_view_email);
        tv_login = (TextView) findViewById(R.id.activity_profile_text_view_email);
        tv_lastConnexion = (TextView) findViewById(R.id.activity_profile_text_view_email);
        sw_msgDebogage = (Switch) findViewById(R.id.activity_profile_switch_msg_debogage);
        btn_winDebogage = (Button) findViewById(R.id.activity_profile_btn_debogage);
        rv_debogage = (RecyclerView) findViewById(R.id.activity_profile_recycle_view_debogage);

        sw_msgDebogage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Toast.makeText(ProfileActivity.this, "Le mode débogage est activé !", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ProfileActivity.this, "Le mode débogage est désactivé !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_winDebogage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!debugWin) {
                    btn_winDebogage.setBackgroundColor(Color.RED);
                    btn_winDebogage.setText("Fermer");
                    ly_admin3.setVisibility(View.VISIBLE);
                    debugWin = true;
                    Toast.makeText(ProfileActivity.this, "La fenêtre des logs est ouverte!", Toast.LENGTH_SHORT).show();
                }else{
                    btn_winDebogage.setBackgroundColor(Color.GREEN);
                    btn_winDebogage.setText("Ouvert");
                    ly_admin3.setVisibility(View.GONE);
                    debugWin = false;
                    Toast.makeText(ProfileActivity.this, "La fenêtre des logs est fermé!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDB = AppDatabase.getInstance(getApplicationContext());

        //Get current user information
        loadUser();
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

                initUserValues();
            }
        });
    }

    public void initUserValues() {
        ServerEntry mServerEntry = mDB.serverDao().getActiveServer(true);
        String server = (mServerEntry.getHostname().replace("http://","")).replace("/api/index.php","");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd @ HHmmss");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyMMdd-HHmmss");

        tv_mail.setText(mUserEntry.getEmail());
        tv_societe.setText(mServerEntry.getRaison_sociale());
        tv_server.setText(server);
        tv_nom.setText(mUserEntry.getLastname());
        tv_login.setText(mUserEntry.getLogin());
        tv_lastConnexion.setText(mUserEntry.getDatelastlogin());

//        Log.e(TAG, " last login : "+mUserEntry.getDatelastlogin()+"\n" +
//                "in format : "+dateFormat1.format(mUserEntry.getDatelastlogin()));
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
