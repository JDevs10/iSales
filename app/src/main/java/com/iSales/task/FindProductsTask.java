package com.iSales.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.iSales.interfaces.FindProductsListener;
import com.iSales.pages.home.fragment.CommandesFragment;
import com.iSales.pages.ticketing.model.DebugItem;
import com.iSales.pages.ticketing.task.SaveLogs;
import com.iSales.remote.ApiUtils;
import com.iSales.remote.rest.FindProductsREST;
import com.iSales.remote.model.Product;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by netserve on 29/08/2018.
 */

public class FindProductsTask extends AsyncTask<Void, Void, FindProductsREST> {
    private static final String TAG = com.iSales.task.FindProductsTask.class.getSimpleName();

    private FindProductsListener task;
    private String sortfield;
    private String sortorder;
    private long limit;
    private long page;
    private long category;
    private int mode = 1;
    private Context context;

    /**
     *
     * @param context
     * @param taskComplete
     * @param sortfield
     * @param sortorder
     * @param limit
     * @param page envoyer une valeur négative pour renvoyer la liste sans pagination
     * @param category
     */
    public FindProductsTask(Context context, FindProductsListener taskComplete, String sortfield, String sortorder, long limit, long page, long category) {
        this.task = taskComplete;
        this.sortfield = sortfield;
        this.sortorder = sortorder;
        this.limit = limit;
        this.page = page;
        this.category = category;
        this.context = context;
    }

    @Override
    protected FindProductsREST doInBackground(Void... voids) {
//        filtre les produits qui sont en vente
        String sqlfilters = "tosell=1";
//        Requete de connexion de l'internaute sur le serveur
        Call<ArrayList<Product>> call = null;
        if (page < 0) {
            call = ApiUtils.getISalesService(context).findProductsByCategorie(sqlfilters, sortfield, this.sortorder, this.limit, this.category, this.mode);
        } else {
            call = ApiUtils.getISalesService(context).findProducts(sqlfilters, sortfield, this.sortorder, this.limit, this.page, this.mode);
        }
        try {
            Response<ArrayList<Product>> response = call.execute();
            Log.e(TAG, "URL : "+call.request().url());
            new SaveLogs(context).writeLogFile(
                    new DebugItem(
                            (System.currentTimeMillis()/1000),
                            "DEB", FindProductsTask.class.getSimpleName(),
                            "doInBackground()",
                            "URL : " + call.request().url(),
                            ""
                    )
            );
            if (response.isSuccessful()) {
                ArrayList<Product> productArrayList = response.body();
                ArrayList<Product> products = new ArrayList<>();
//                Log.e(TAG, "doInBackground: products=" + productArrayList.size());

                productArrayList = new FindProductVisibilityTask(context).writeVisibleFile(productArrayList);

                return new FindProductsREST(productArrayList, category);
            } else {
                String error = null;
                FindProductsREST findProductsREST = new FindProductsREST();
                findProductsREST.setProducts(null);
                findProductsREST.setErrorCode(response.code());
                try {
                    error = response.errorBody().string();
//                    JSONObject jsonObjectError = new JSONObject(error);
//                    String errorCode = jsonObjectError.getString("errorCode");
//                    String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "doInBackground: onResponse err: " + error + " code=" + response.code());
                    findProductsREST.setErrorBody(error);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return findProductsREST;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Product> getProductsVisibility(ArrayList<Product> productsList){
        String msg = "";
        for(int x = 0; x < productsList.size(); x++){
            final Call<String> call = ApiUtils.getISalesRYImg(context).findVisibility(productsList.get(x).getId());

            Log.e(TAG, "getProductsVisibility :: URL "+call.request().url());
            msg += "getProductsVisibility :: URL "+call.request().url()+"\n";

            try {
                Response<String> response = call.execute();
                if(response.isSuccessful()){
                    String visibility_info = response.body();
                    Log.e(TAG, "getProductsVisibility :: x : "+x+" || response.isSuccessful "+response.message() + " || body : "+visibility_info);
                    msg += "- getProductsVisibility :: x : "+x+" || body : "+ visibility_info + "\n\n";
                }else{
                    try {
                        Log.e(TAG, "getProductsVisibility: getProductsVisibility :: x : "+x+" || err: " + response.errorBody().string() + " code=" + response.code());
                        msg += "- getProductsVisibility :: x : "+x+" || err: " + response.errorBody().string() + " code=" + response.code() + "\n\n";

                    } catch (IOException e) {
                        e.printStackTrace();
                        msg += "- getProductsVisibility ::IOException:: x : "+x+" || err : "+ e.getMessage() + "\n\n";
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                msg += "- getProductsVisibility :IOException: x : "+x+" ||  IOException : " + e.getMessage() + "\n\n";
            }
        }

        new SaveLogs(context).writeLogFile(
                new DebugItem(
                        (System.currentTimeMillis()/1000),
                        "DEB", FindProductsTask.class.getSimpleName(),
                        "getProductsVisibility()",
                        "Message : " + msg,
                        ""
                )
        );

        return productsList;
    }

    @Override
    protected void onPostExecute(FindProductsREST findProductsREST) {
//        super.onPostExecute(findProductsREST);
        try {
            task.onFindProductsCompleted(findProductsREST);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
