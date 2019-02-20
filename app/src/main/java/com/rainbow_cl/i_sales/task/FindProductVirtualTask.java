package com.rainbow_cl.i_sales.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rainbow_cl.i_sales.database.AppDatabase;
import com.rainbow_cl.i_sales.database.entry.PaymentTypesEntry;
import com.rainbow_cl.i_sales.interfaces.FindPaymentTypesListener;
import com.rainbow_cl.i_sales.interfaces.FindProductVirtualListener;
import com.rainbow_cl.i_sales.remote.ApiUtils;
import com.rainbow_cl.i_sales.remote.model.PaymentTypes;
import com.rainbow_cl.i_sales.remote.model.ProductVirtual;
import com.rainbow_cl.i_sales.remote.rest.FindPaymentTypesREST;
import com.rainbow_cl.i_sales.remote.rest.FindProductVirtualREST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FindProductVirtualTask extends AsyncTask<Void, Void, FindProductVirtualREST> {
    private static final String TAG = FindProductVirtualTask.class.getSimpleName();

    private FindProductVirtualListener task;
    private long productId;

    private Context context;
    private AppDatabase mDb;

    public FindProductVirtualTask(Context context, long productId, FindProductVirtualListener taskComplete) {
        this.task = taskComplete;
        this.context = context;
        this.productId = productId;
        this.mDb = AppDatabase.getInstance(context);
    }

    @Override
    protected FindProductVirtualREST doInBackground(Void... voids) {
        Log.e(TAG, "doInBackground: ");

//        Requete de connexion de l'internaute sur le serveur
        Call<ArrayList<ProductVirtual>> call = ApiUtils.getISalesRYImg(context).ryFindProductVirtual(this.productId);
        try {
            Response<ArrayList<ProductVirtual>> response = call.execute();
            if (response.isSuccessful()) {
                ArrayList<ProductVirtual> productVirtualArrayList = response.body();
                Log.e(TAG, "doInBackground: FindProductVirtualREST=" + productVirtualArrayList.size());

                FindProductVirtualREST rest = new FindProductVirtualREST();
                rest.setProductVirtuals(productVirtualArrayList);
                rest.setProduct_parent_id(this.productId);

                return rest;
            } else {
                Log.e(TAG, "doInBackground: !isSuccessful");
                String error = null;
                FindProductVirtualREST findProductVirtualREST = new FindProductVirtualREST();
                findProductVirtualREST.setProductVirtuals(null);
                findProductVirtualREST.setErrorCode(response.code());
                try {
                    error = response.errorBody().string();
//                    JSONObject jsonObjectError = new JSONObject(error);
//                    String errorCode = jsonObjectError.getString("errorCode");
//                    String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "doInBackground: onResponse err: " + error + " code=" + response.code());
                    findProductVirtualREST.setErrorBody(error);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return findProductVirtualREST;
            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IOException");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(FindProductVirtualREST findProductVirtualREST) {
//        Log.e(TAG, "onPostExecute: ");

        if (task != null) {
            task.onFindProductVirtualCompleted(findProductVirtualREST);
        }
    }
}