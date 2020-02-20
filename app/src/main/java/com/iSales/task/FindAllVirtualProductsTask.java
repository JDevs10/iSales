package com.iSales.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.iSales.database.AppDatabase;
import com.iSales.database.entry.DebugItemEntry;
import com.iSales.database.entry.ProduitEntry;
import com.iSales.interfaces.FindProductVirtualListener;
import com.iSales.remote.ApiUtils;
import com.iSales.remote.model.ProductVirtual;
import com.iSales.remote.rest.FindProductVirtualREST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FindAllVirtualProductsTask extends AsyncTask<Void, Void, Integer> {
    private static final String TAG = com.iSales.task.FindAllVirtualProductsTask.class.getSimpleName();
    private FindProductVirtualListener task;
    private Context context;
    private ProgressDialog mProgressDialog;
    private AppDatabase mDb;

    public FindAllVirtualProductsTask(Context context, FindProductVirtualListener taskComplete) {
        this.task = taskComplete;
        this.context = context;
        this.mProgressDialog = new ProgressDialog(this.context);
        this.mDb = AppDatabase.getInstance(this.context);

        mDb.debugMessageDao().insertDebugMessage(
                new DebugItemEntry(context, (System.currentTimeMillis()/1000), "Ticket", FindAllVirtualProductsTask.class.getSimpleName(), "FindAllVirtualProductsTask()", "Called.", ""));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mProgressDialog.setMessage("Progress start");
        this.mProgressDialog.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        Log.e(TAG, " doInBackground : There are "+mDb.virtualProductDao().getAllVirtualProduct().size()+" to be deleted!");

        List<ProduitEntry> produitEntries = mDb.produitDao().getAllProduits();
        mDb.virtualProductDao().deleteAllVirtualProduct();

        int allProducts = produitEntries.size();
        Call<ArrayList<ProductVirtual>> call;
        Response<ArrayList<ProductVirtual>> response;

        if (produitEntries.size() > 0) {

            int currentPosition = 0;
            for (int i = 0; i < produitEntries.size(); i++) {
                this.mDb = null;
                this.mDb = AppDatabase.getInstance(this.context);

                if (!produitEntries.get(i).getRef().contains("C") && !produitEntries.get(i).getRef().contains("P")) {
                    //executeFindVirtualProducts(Long.valueOf(produitEntry.getId()));
                    Log.e(TAG, "doInBackground: " + String.format("%s. %s / %s ", "Téléchargement des Produits Visuel en cours... ", (i+1), allProducts));
                    Log.e(TAG, "doInBackground: FindAllVirtualProductsTask ID = " + produitEntries.get(i).getId() + " | Ref: " + produitEntries.get(i).getRef());

                    call = ApiUtils.getISalesRYImg(context).ryFindProductVirtual(produitEntries.get(i).getId());
//                        mDb.debugMessageDao().insertDebugMessage(
//                                new DebugItemEntry(context, (System.currentTimeMillis() / 1000), "Ticket", FindProductVirtualTask.class.getSimpleName(), "FindProductVirtualTask() => doInBackground()", String.format("%s. %s / %s ", "Téléchargement des Produits Visuel en cours... ", x, allProducts), ""));

                    try {
                        response = call.execute();
                        Log.e(TAG, "JSon: " + toJSON(response.body()));
                        if (response.isSuccessful()) {
                            ArrayList<ProductVirtual> productVirtualArrayList = response.body();
                            Log.e(TAG, "doInBackground: FindProductVirtualREST size =" + productVirtualArrayList.size());

                            if (productVirtualArrayList.size() > 0) {
                                Log.e(TAG, "onFindThirdpartieCompleted: getPrice(): " + (productVirtualArrayList.get(0).getPrice().equals("") ? "0.0" : productVirtualArrayList.get(0).getPrice()) + " || getQty(): " + (productVirtualArrayList.get(0).getQty().equals("") ? "0.0" : productVirtualArrayList.get(0).getQty()));
                                Log.e(TAG, "onFindThirdpartieCompleted: getPrice_ttc(): " + (productVirtualArrayList.get(0).getPrice_ttc().equals("") ? "0.0" : productVirtualArrayList.get(0).getPrice_ttc()) + " || getQty(): " + (productVirtualArrayList.get(0).getQty().equals("") ? "0.0" : productVirtualArrayList.get(0).getQty()));

                                double price0 = Double.parseDouble((productVirtualArrayList.get(0).getPrice().equals("") ? "0.0" : productVirtualArrayList.get(0).getPrice())) * Math.round(Double.parseDouble((productVirtualArrayList.get(0).getQty().equals("") ? "0.0" : productVirtualArrayList.get(0).getQty())));
                                double priceTTC0 = Double.parseDouble((productVirtualArrayList.get(0).getPrice_ttc().equals("") ? "0.0" : productVirtualArrayList.get(0).getPrice_ttc())) * Math.round(Double.parseDouble((productVirtualArrayList.get(0).getQty().equals("") ? "0.0" : productVirtualArrayList.get(0).getQty())));
                                productVirtualArrayList.get(0).setPrice("" + price0);
                                productVirtualArrayList.get(0).setPrice_ttc("" + priceTTC0);

                                Log.e(TAG, "onFindThirdpartieCompleted: insert virtualProductEntry int: 0");
                                //insertion du client dans la BD
                                mDb.virtualProductDao().insertVirtualProduct(productVirtualArrayList.get(0));

                                for (int z = 1; z < productVirtualArrayList.size(); z++) {
                                    Log.e(TAG, "ID : " + productVirtualArrayList.get(z - 1).get_0() + " | RowId : " + productVirtualArrayList.get(z - 1).getRowid() + " | Ref : " + productVirtualArrayList.get(z - 1).getRef() + " | Price : " + productVirtualArrayList.get(z - 1).getPrice() + " | Qte : " + Math.round(Double.parseDouble(productVirtualArrayList.get(z).getQty())));

                                    Log.e(TAG, "onFindThirdpartieCompleted: getPrice(): " + (productVirtualArrayList.get(z - 1).getPrice().equals("") ? "0.0" : productVirtualArrayList.get(z - 1).getPrice()) + " || getQty(): " + productVirtualArrayList.get(z - 1).getQty());
                                    Log.e(TAG, "onFindThirdpartieCompleted: getPrice_ttc(): " + productVirtualArrayList.get(z - 1).getPrice_ttc() + " || getQty(): " + productVirtualArrayList.get(z).getQty());

                                    double price = Double.parseDouble((productVirtualArrayList.get(z - 1).getPrice().equals("") ? "0.0" : productVirtualArrayList.get(z - 1).getPrice())) * Math.round(Double.parseDouble((productVirtualArrayList.get(z - 1).getQty().equals("") ? "0.0" : productVirtualArrayList.get(z - 1).getQty())));
                                    double priceTTC = Double.parseDouble((productVirtualArrayList.get(z - 1).getPrice_ttc().equals("") ? "0.0" : productVirtualArrayList.get(z - 1).getPrice_ttc())) * Math.round(Double.parseDouble((productVirtualArrayList.get(z - 1).getQty().equals("") ? "0.0" : productVirtualArrayList.get(z - 1).getQty())));
                                    productVirtualArrayList.get(z).setPrice("" + price);
                                    productVirtualArrayList.get(z).setPrice_ttc("" + priceTTC);

                                    Log.e(TAG, "onFindThirdpartieCompleted: insert virtualProductEntry int: " + z);
                                    //insertion du client dans la BD
                                    mDb.virtualProductDao().insertVirtualProduct(productVirtualArrayList.get(z));
                                }
                                //allProductVirtual.addAll(productVirtualArrayList);

                            } else {
                                //allErrorCode += "\t- Code: " + response.code() + " Aucun produit virtuel n'est attaché au produit ref: " + produitEntries.get(i).getId() + "\n";
                                Log.e(TAG, "doInBackground: FindProductVirtualREST No Virtual Products");
                            }
                        } else {
                            Log.e(TAG, "doInBackground: !isSuccessful");
                            String error;
                            try {
                                error = response.errorBody().string();
                                //allErrorCode += "\t- Code: " + response.code() + " | doInBackground: onResponse err:: " + error + "\n\n";

//                                    mDb.debugMessageDao().insertDebugMessage(
//                                            new DebugItemEntry(context, (System.currentTimeMillis() / 1000), "Ticket", FindProductVirtualTask.class.getSimpleName(), "FindProductVirtualTask() => doInBackground()", "doInBackground: onResponse err: " + error + " code=" + response.code(), ""));
                            } catch (IOException e) {
                                Log.e(TAG, "doInBackground: ********** IOException !isSuccessful **********");
                                Log.e(TAG, "URL: " + call.request().url());
                                Log.e(TAG, "Message: " + e.getMessage());
                                Log.e(TAG, "StackTrace: " + e.getStackTrace());
//                                    mDb.debugMessageDao().insertDebugMessage(
//                                            new DebugItemEntry(context, (System.currentTimeMillis() / 1000), "Ticket", FindProductVirtualTask.class.getSimpleName(), "FindProductVirtualTask() => doInBackground()", "***** IOException *****\nMessage: " + e.getMessage(), "" + e.getStackTrace()));
                            }
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "doInBackground: ********** IOException **********");
                        Log.e(TAG, "URL: " + call.request().url());
                        Log.e(TAG, "Message: " + e.getMessage());
                        Log.e(TAG, "StackTrace: " + e.getStackTrace());
//                            mDb.debugMessageDao().insertDebugMessage(
//                                    new DebugItemEntry(context, (System.currentTimeMillis() / 1000), "Ticket", FindProductVirtualTask.class.getSimpleName(), "FindProductVirtualTask() => doInBackground()", "***** IOException *****\nMessage: " + e.getMessage(), "" + e.getStackTrace()));
                        //return 0;
                    }

                } else {
                    Log.e(TAG, "Product contains 'C' or 'P' in the reference!");
                    Log.e(TAG, "findVirtualProducts: FindProductVirtualTask ID = " + produitEntries.get(i).getId() + " | Ref: " + produitEntries.get(i).getRef());
                }
            }
            this.mDb = null;
            return 1;
        } else {
            Log.e(TAG, "Aucun produit synchronisé!");
            mProgressDialog.dismiss();
            return -1;
        }
    }

    String toJSON(ArrayList<ProductVirtual> list) {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        for(ProductVirtual d : list) {
            sb.append(gson.toJson(d)+"");
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        task.onFindProductVirtualCompleted_test(integer);
    }
}
