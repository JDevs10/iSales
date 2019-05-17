package com.iSales.pages.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.iSales.R;
import com.iSales.database.AppDatabase;
import com.iSales.database.entry.ServerEntry;
import com.iSales.database.entry.UserEntry;
import com.iSales.pages.home.viewmodel.UserViewModel;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private String TAG = ProfileActivity.class.getSimpleName();

    private TextView tv_test;

    //    database instance
    private AppDatabase mDB;

    private UserEntry mUserEntry;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv_test = (TextView) findViewById(R.id.profile_activity_test);

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

        String textClient = "Server : "+mServerEntry.getRaison_sociale()+"\n" +
                "Id : "+mUserEntry.getId()+"\n" +
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
                "Login : "+mUserEntry.getLogin()+"\n" +
                "Ville : "+mUserEntry.getTown()+"\n" +
                "Address : "+mUserEntry.getAddress();
        tv_test.setText(textClient);
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
