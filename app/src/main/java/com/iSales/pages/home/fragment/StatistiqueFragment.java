package com.iSales.pages.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.iSales.R;
import com.iSales.adapter.StatistiqueBasicAdapter;

import java.util.ArrayList;

public class StatistiqueFragment extends Fragment {
    private String TAG = StatistiqueFragment.class.getSimpleName();
    private Context mContext;

    private RecyclerView basic_rv;
    private StatistiqueBasicAdapter mStatistiqueBasicAdapter;

    private BarChart nbCmdClient_bc, mtCmdClient_bc;

    private String[] statisName = {"Client","Produits","Facture Client","Commande Client"};
    private int[] statisIcon = {R.drawable.ic_person_black_24dp, R.drawable.ic_shopping_black_24dp, R.drawable.ic_event_note_black, R.drawable.ic_event_note_black};
    private int[] statisData = {499, 599, 504, 593};

    public StatistiqueFragment(){
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static StatistiqueFragment newInstance() {

        Bundle args = new Bundle();
        StatistiqueFragment fragment = new StatistiqueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_statistique, container, false);

        basic_rv = (RecyclerView) rootView.findViewById(R.id.fragment_statistique_basic);
        nbCmdClient_bc = (BarChart) rootView.findViewById(R.id.fragment_statistique_nBrCmdClient);
        mtCmdClient_bc = (BarChart) rootView.findViewById(R.id.fragment_statistique_montantCmdClient);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showBasicStatis();
        showCmdClientParMois();

    }

    private void showBasicStatis(){
        mStatistiqueBasicAdapter = new StatistiqueBasicAdapter(mContext, statisName, statisIcon, statisData);
        basic_rv.setAdapter(mStatistiqueBasicAdapter);
    }

    private ArrayList<BarEntry> setBarEntries(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 44));
        barEntries.add(new BarEntry(1, 34));
        barEntries.add(new BarEntry(2, 92));
        barEntries.add(new BarEntry(3, 14));
        barEntries.add(new BarEntry(4, 20));
        barEntries.add(new BarEntry(5, 4));
        barEntries.add(new BarEntry(6, 7));
        barEntries.add(new BarEntry(7, 9));
        barEntries.add(new BarEntry(8, 62));
        barEntries.add(new BarEntry(9, 43));
        barEntries.add(new BarEntry(10, 70));
        barEntries.add(new BarEntry(11, 37));
        return barEntries;
    }

    private ArrayList<String> setDates(){
        ArrayList<String> dates = new ArrayList<>();
        dates.add("Jan.");
        dates.add("Fév.");
        dates.add("Mars");
        dates.add("Avril");
        dates.add("May");
        dates.add("Jun");
        dates.add("Juil.");
        dates.add("Août");
        dates.add("Sept.");
        dates.add("Oct.");
        dates.add("Nov.");
        dates.add("Déc.");
        return dates;
    }

    private void showCmdClientParMois(){
        nbCmdClient_bc.setTouchEnabled(true);
        nbCmdClient_bc.setDragEnabled(true);
        nbCmdClient_bc.setScaleEnabled(true);

        nbCmdClient_bc.setDrawBarShadow(false);
        nbCmdClient_bc.setDrawValueAboveBar(true);
        nbCmdClient_bc.setPinchZoom(false);
        nbCmdClient_bc.setDrawGridBackground(true);

        BarDataSet barDataSet = new BarDataSet(setBarEntries(), "Date Set 1");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        nbCmdClient_bc.setData(barData);

        Description descrip = new Description();
        descrip.setText("");

        nbCmdClient_bc.setDescription(descrip);
        nbCmdClient_bc.getAxisRight().setEnabled(false);

        XAxis xAxis = nbCmdClient_bc.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(setDates()));
    }

    public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter{
        ArrayList<String> mValue;

        public MyXAxisValueFormatter(ArrayList<String> value){
            this.mValue = value;
        }

        @Override
        public String getFormattedValue(float value) {
            return mValue.get((int)value);
        }
    }

}
