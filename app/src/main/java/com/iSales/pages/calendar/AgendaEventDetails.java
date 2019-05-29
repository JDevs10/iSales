package com.iSales.pages.calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.iSales.R;

public class AgendaEventDetails extends AppCompatActivity {

    private Button annuler_btn, valider_btn, supprimer_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_event_details);

        //Prevent the keyboard from displaying on activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //init views
        annuler_btn = (Button) findViewById(R.id.activity_agenda_event_details_annuler_btn);
        valider_btn = (Button) findViewById(R.id.activity_agenda_event_details_valider_btn);
        supprimer_btn = (Button) findViewById(R.id.activity_agenda_event_details_supprimer_btn);


        annuler_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to agenda activity
                onBackPressed();
            }
        });

    }


}
