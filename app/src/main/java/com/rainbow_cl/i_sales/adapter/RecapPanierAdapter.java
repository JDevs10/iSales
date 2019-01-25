package com.rainbow_cl.i_sales.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.rainbow_cl.i_sales.R;
import com.rainbow_cl.i_sales.database.entry.PanierEntry;
import com.rainbow_cl.i_sales.interfaces.PanierProduitAdapterListener;
import com.rainbow_cl.i_sales.utility.ISalesUtility;

import java.util.ArrayList;

/**
 * Created by netserve on 01/10/2018.
 */

public class RecapPanierAdapter extends RecyclerView.Adapter<RecapPanierAdapter.RecapPanierViewHolder> {
    private static final String TAG = RecapPanierAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<PanierEntry> panierList;

    //    ViewHolder de l'adapter
    public class RecapPanierViewHolder extends RecyclerView.ViewHolder {
        public TextView label, price, quantite;

        public RecapPanierViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.tv_boncmde_produit_label);
            price = view.findViewById(R.id.tv_boncmde_produit_price);
            quantite = view.findViewById(R.id.tv_boncmde_produit_quantite);

        }
    }


    public RecapPanierAdapter(Context context, ArrayList<PanierEntry> panierEntries) {
        this.mContext = context;
        this.panierList = panierEntries;
    }

    @Override
    public RecapPanierAdapter.RecapPanierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_boncmde_produit, parent, false);

        return new RecapPanierAdapter.RecapPanierViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecapPanierAdapter.RecapPanierViewHolder holder, int position) {
        final PanierEntry panierEntry = panierList.get(position);
        holder.label.setText(panierEntry.getLabel());
        holder.price.setText(String.format("%s %s", ISalesUtility.amountFormat2(panierEntry.getPrice_ttc()), mContext.getString(R.string.symbole_euro)));
        holder.quantite.setText(String.format("%d ", panierEntry.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if (panierList != null) {
            return panierList.size();
        }
        return 0;
    }
}