package com.iSales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.iSales.R;

import java.util.ArrayList;

public class StatistiqueCmdClientAdapter extends RecyclerView.Adapter<StatistiqueCmdClientAdapter.MyViewHolder> {
    private String TAG = "StatistiqueBasicAdapter ";
    private Context mContext;


    public StatistiqueCmdClientAdapter(Context mContext){
        this.mContext = mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name_tv;
        BarChart barGraph;

        public MyViewHolder(View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.custom_cmdc_statistique_name);
            barGraph = itemView.findViewById(R.id.custom_cmdc_statistique_barGraph);

            barGraph.setTouchEnabled(true);
            barGraph.setDragEnabled(true);
            barGraph.setScaleEnabled(true);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cmd_client_statistique, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name_tv.setText("Montant Commande par mois");

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
