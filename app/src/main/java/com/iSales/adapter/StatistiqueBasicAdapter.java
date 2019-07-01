package com.iSales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iSales.R;


public class StatistiqueBasicAdapter extends RecyclerView.Adapter<StatistiqueBasicAdapter.MyViewHolder> {
    private String TAG = "StatistiqueBasicAdapter ";
    private Context mContext;

    String[] names;
    int[] icons;
    int[] data;

    public StatistiqueBasicAdapter(Context mContext, String[] names, int[] icons, int[] data){
        this.mContext = mContext;
        this.names = names;
        this.icons = icons;
        this.data = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name_tv;
        ImageView icons_iv;
        TextView data_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.custom_basic_statistique_name);
            icons_iv = itemView.findViewById(R.id.custom_basic_statistique_icon);
            data_tv = itemView.findViewById(R.id.custom_basic_statistique_data);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_basic_statistique, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name_tv.setText(names[position]);
        holder.icons_iv.setImageResource(icons[position]);
        holder.data_tv.setText(data[position]+"");
    }

    @Override
    public int getItemCount() {
        return names.length;
    }
}
