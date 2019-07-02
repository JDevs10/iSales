package com.iSales.pages.home.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.iSales.R;
import com.iSales.adapter.StatistiqueBasicAdapter;
import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.RequiresApi;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class StatistiqueFragment extends Fragment {
    private String TAG = StatistiqueFragment.class.getSimpleName();
    private Context mContext;

    private RecyclerView basic_rv;
    private StatistiqueBasicAdapter mStatistiqueBasicAdapter;

    //private BarChart barChart;
    private CombinedChart nbCmdClient_bc, mtCmdClient_bc, nbFactClient_bc, mtFactClient_bc;

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
        //barChart = (BarChart) rootView.findViewById(R.id.fragment_statistique_nbCategory);
        nbCmdClient_bc = (CombinedChart) rootView.findViewById(R.id.fragment_statistique_nBrCmdClient);
        mtCmdClient_bc = (CombinedChart) rootView.findViewById(R.id.fragment_statistique_montantCmdClient);
        nbFactClient_bc = (CombinedChart) rootView.findViewById(R.id.fragment_statistique_nBrfactureClient);
        mtFactClient_bc = (CombinedChart) rootView.findViewById(R.id.fragment_statistique_montantFactureClient);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showBasicStatis();
        //categoryProduit();
        showNbCmdClientParMois();
        showMttCmdClientParMois();
        showNbFactureClientParMois();
        showMttFactureClientParMois();
    }

    private void showBasicStatis(){
        mStatistiqueBasicAdapter = new StatistiqueBasicAdapter(mContext, statisName, statisIcon, statisData);
        basic_rv.setAdapter(mStatistiqueBasicAdapter);
    }

    //2019 data
    private ArrayList<BarEntry> setEntries1(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 44000));
        barEntries.add(new BarEntry(1, 34000));
        barEntries.add(new BarEntry(2, 42000));
        barEntries.add(new BarEntry(3, 14000));
        barEntries.add(new BarEntry(4, 2000));
        barEntries.add(new BarEntry(5, 4000));
        barEntries.add(new BarEntry(6, 7000));
        barEntries.add(new BarEntry(7, 1256));
        barEntries.add(new BarEntry(8, 0));
        barEntries.add(new BarEntry(9, 0));
        barEntries.add(new BarEntry(10, 0));
        barEntries.add(new BarEntry(11, 0));
        return barEntries;
    }

    //2018 data
    private ArrayList<Entry> setEntries2(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 40000));
        entries.add(new Entry(1, 30000));
        entries.add(new Entry(2, 50000));
        entries.add(new Entry(3, 10000));
        entries.add(new Entry(4, 1000));
        entries.add(new Entry(5, 2000));
        entries.add(new Entry(6, 4000));
        entries.add(new Entry(7, 3000));
        entries.add(new Entry(8, 31000));
        entries.add(new Entry(9, 21000));
        entries.add(new Entry(10, 40000));
        entries.add(new Entry(11, 15000));
        return entries;
    }

    private ArrayList<String> setXAxisDates(){
        ArrayList<String> dates = new ArrayList<>();
        dates.add("Jan.");
        dates.add("Fév.");
        dates.add("Mars");
        dates.add("Avr.");
        dates.add("Mai");
        dates.add("Juin");
        dates.add("Juil.");
        dates.add("Août");
        dates.add("Sep.");
        dates.add("Oct.");
        dates.add("Nov.");
        dates.add("Déc.");
        return dates;
    }

    private void showNbCmdClientParMois(){
        nbCmdClient_bc.setTouchEnabled(true);
        nbCmdClient_bc.setDragEnabled(true);
        nbCmdClient_bc.setScaleEnabled(true);

        nbCmdClient_bc.getDescription().setEnabled(false);
        nbCmdClient_bc.setBackgroundColor(Color.WHITE);
        nbCmdClient_bc.setDrawGridBackground(false);
        nbCmdClient_bc.setDrawBarShadow(false);
        nbCmdClient_bc.setHighlightFullBarEnabled(false);

        nbCmdClient_bc.setDrawBarShadow(false);
        nbCmdClient_bc.setDrawValueAboveBar(true);
        nbCmdClient_bc.setPinchZoom(false);
        nbCmdClient_bc.setDrawGridBackground(true);

        BarDataSet barDataSet = new BarDataSet(setEntries1(), "2019");
        barDataSet.setColor(Color.rgb(0, 166, 255));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        // set line data
        LineData liData = new LineData();
        LineDataSet set = new LineDataSet(setEntries2(), "2018");
        set.setColor(Color.GREEN);
        set.setLineWidth(2f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(4f);
        set.setFillColor(Color.GREEN);
        set.setDrawValues(true);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        liData.addDataSet(set);


        //Combine data
        CombinedData data = new CombinedData();
        data.setData(liData);
        data.setData(barData);
        data.setDrawValues(false);

        nbCmdClient_bc.setData(data);
        nbCmdClient_bc.invalidate();

        Description descrip = new Description();
        descrip.setText("");

        nbCmdClient_bc.setDescription(descrip);
        nbCmdClient_bc.getAxisRight().setEnabled(false);

        XAxis xAxis = nbCmdClient_bc.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(setXAxisDates()));
    }

    private void showMttCmdClientParMois(){
        nbCmdClient_bc.setTouchEnabled(true);
        nbCmdClient_bc.setDragEnabled(true);
        nbCmdClient_bc.setScaleEnabled(true);

        nbCmdClient_bc.getDescription().setEnabled(false);
        nbCmdClient_bc.setBackgroundColor(Color.WHITE);
        nbCmdClient_bc.setDrawGridBackground(false);
        nbCmdClient_bc.setDrawBarShadow(false);
        nbCmdClient_bc.setHighlightFullBarEnabled(false);

        nbCmdClient_bc.setDrawBarShadow(false);
        nbCmdClient_bc.setDrawValueAboveBar(true);
        nbCmdClient_bc.setPinchZoom(false);
        nbCmdClient_bc.setDrawGridBackground(true);

        BarDataSet barDataSet = new BarDataSet(setEntries1(), "2019");
        barDataSet.setColor(Color.rgb(0, 166, 255));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        // set line data
        LineData liData = new LineData();
        LineDataSet set = new LineDataSet(setEntries2(), "2018");
        set.setColor(Color.GREEN);
        set.setLineWidth(2f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(4f);
        set.setFillColor(Color.GREEN);
        set.setDrawValues(true);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        liData.addDataSet(set);


        //Combine data
        CombinedData data = new CombinedData();
        data.setData(liData);
        data.setData(barData);
        data.setDrawValues(false);

        mtCmdClient_bc.setData(data);
        mtCmdClient_bc.invalidate();

        Description descrip = new Description();
        descrip.setText("");

        mtCmdClient_bc.setDescription(descrip);
        mtCmdClient_bc.getAxisRight().setEnabled(false);

        XAxis xAxis = mtCmdClient_bc.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(setXAxisDates()));
    }

    private void showNbFactureClientParMois(){
        nbFactClient_bc.setTouchEnabled(true);
        nbFactClient_bc.setDragEnabled(true);
        nbFactClient_bc.setScaleEnabled(true);

        nbFactClient_bc.getDescription().setEnabled(false);
        nbFactClient_bc.setBackgroundColor(Color.WHITE);
        nbFactClient_bc.setDrawGridBackground(false);
        nbFactClient_bc.setDrawBarShadow(false);
        nbFactClient_bc.setHighlightFullBarEnabled(false);

        nbFactClient_bc.setDrawBarShadow(false);
        nbFactClient_bc.setDrawValueAboveBar(true);
        nbFactClient_bc.setPinchZoom(false);
        nbFactClient_bc.setDrawGridBackground(true);

        BarDataSet barDataSet = new BarDataSet(setEntries1(), "2019");
        barDataSet.setColor(Color.rgb(0, 166, 255));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        // set line data
        LineData liData = new LineData();
        LineDataSet set = new LineDataSet(setEntries2(), "2018");
        set.setColor(Color.GREEN);
        set.setLineWidth(2f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(4f);
        set.setFillColor(Color.GREEN);
        set.setDrawValues(true);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        liData.addDataSet(set);


        //Combine data
        CombinedData data = new CombinedData();
        data.setData(liData);
        data.setData(barData);
        data.setDrawValues(false);

        nbFactClient_bc.setData(data);
        nbFactClient_bc.invalidate();

        Description descrip = new Description();
        descrip.setText("");

        nbFactClient_bc.setDescription(descrip);
        nbFactClient_bc.getAxisRight().setEnabled(false);

        XAxis xAxis = nbFactClient_bc.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(setXAxisDates()));
    }

    private void showMttFactureClientParMois(){
        mtFactClient_bc.setTouchEnabled(true);
        mtFactClient_bc.setDragEnabled(true);
        mtFactClient_bc.setScaleEnabled(true);

        mtFactClient_bc.getDescription().setEnabled(false);
        mtFactClient_bc.setBackgroundColor(Color.WHITE);
        mtFactClient_bc.setDrawGridBackground(false);
        mtFactClient_bc.setDrawBarShadow(false);
        mtFactClient_bc.setHighlightFullBarEnabled(false);

        mtFactClient_bc.setDrawBarShadow(false);
        mtFactClient_bc.setDrawValueAboveBar(true);
        mtFactClient_bc.setPinchZoom(false);
        mtFactClient_bc.setDrawGridBackground(true);

        BarDataSet barDataSet = new BarDataSet(setEntries1(), "2019");
        barDataSet.setColor(Color.rgb(0, 166, 255));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        // set line data
        LineData liData = new LineData();
        LineDataSet set = new LineDataSet(setEntries2(), "2018");
        set.setColor(Color.GREEN);
        set.setLineWidth(2f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(4f);
        set.setFillColor(Color.GREEN);
        set.setDrawValues(true);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        liData.addDataSet(set);


        //Combine data
        CombinedData data = new CombinedData();
        data.setData(liData);
        data.setData(barData);
        data.setDrawValues(false);

        mtFactClient_bc.setData(data);
        mtFactClient_bc.invalidate();

        Description descrip = new Description();
        descrip.setText("");

        mtFactClient_bc.setDescription(descrip);
        mtFactClient_bc.getAxisRight().setEnabled(false);

        XAxis xAxis = mtFactClient_bc.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(setXAxisDates()));
    }

    private int[] generateColorTemplate(int range){
        int[] result = new int[range];
        ArrayList<Integer> saveColors = new ArrayList<>();

        while (true){
            // generate random color
            final Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            int randomColor = 0;

            //sdk version
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                randomColor = Color.rgb(r,g,b);
            }

            //check if the color generated is different from the list
            if (!saveColors.contains(randomColor)){
                saveColors.add(randomColor);
            }
            if (saveColors.size() == range){
                break;
            }
        }

        //set the new value to the generated color template
        for (int index=0; index<range; index++){
            result[index] = saveColors.get(index);
        }

        return result;
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


/*
    private ArrayList<String> setXAxisCategories(){
        ArrayList<String> cat = new ArrayList<>();
        cat.add("Other");
        cat.add("Test 1");
        cat.add("Test 2");
        cat.add("Test 3");
        cat.add("Test 4");
        cat.add("Test 5");
        cat.add("Test 6");
        cat.add("Test 7");
        cat.add("Test 8");
        cat.add("Test 9");
        cat.add("Test 10");
        cat.add("Test 11");
        return cat;
    }

    private void categoryProduit(){
        barChart.getDescription().setText("XXXX à 506 Produits.");
        barChart.getDescription().setTextSize(14f);
        barChart.setNoDataText("Aucune donnée disponible");
        barChart.setDragDecelerationFrictionCoef(0.99f);
        barChart.animateY(1000, Easing.EaseInOutCubic);


        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 23));
        barEntries.add(new BarEntry(1, 50));
        barEntries.add(new BarEntry(2, 45));
        barEntries.add(new BarEntry(3, 70));
        barEntries.add(new BarEntry(4, 90));
        barEntries.add(new BarEntry(5, 45));
        barEntries.add(new BarEntry(6, 70));
        barEntries.add(new BarEntry(7, 90));
        barEntries.add(new BarEntry(8, 45));
        barEntries.add(new BarEntry(9, 70));
        barEntries.add(new BarEntry(10, 90));
        barEntries.add(new BarEntry(11, 45));

        //create data list
        BarDataSet barDataSet = new BarDataSet(barEntries, "Nombre de produit par catégory");
        barDataSet.setValueTextSize(14);
        barDataSet.setColors(generateColorTemplate(barEntries.size()));

        //add legend to chart
        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(18f);

        //create bar data object
        BarData bareData = new BarData(barDataSet);
        bareData.setBarWidth(0.5f);
        bareData.setValueTextSize(10f);
        bareData.setValueTextColor(Color.BLACK);

        barChart.setData(bareData);
        barChart.invalidate();

        Description descrip = new Description();
        descrip.setText("");
        nbCmdClient_bc.setDescription(descrip);
        nbCmdClient_bc.getAxisRight().setEnabled(false);

        XAxis xAxis = nbCmdClient_bc.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(setXAxisCategories()));
    }
    */
}
