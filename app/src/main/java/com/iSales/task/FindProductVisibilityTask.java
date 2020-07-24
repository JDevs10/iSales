package com.iSales.task;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iSales.model.Visible;
import com.iSales.remote.ApiUtils;
import com.iSales.remote.model.Product;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindProductVisibilityTask {
    private final String TAG = FindProductVisibilityTask.class.getSimpleName();
    private final File DirectoryLocal = Environment.getExternalStorageDirectory();
    private final String DirectoryName = "iSales_visible";
    private final String FILE_NAME = "product_visibility.json";
    private Context mContext;

    public FindProductVisibilityTask(Context mContext){
        this.mContext = mContext;
    }

    public ArrayList<Product> writeVisibleFile(final ArrayList<Product> data){
        FileWriter fileWriter = null;
        /*
        try {
            File root = new File(DirectoryLocal, DirectoryName);
            Log.e(TAG, "writeLogFile() :: root path: "+root.getAbsolutePath());

            if (!root.exists()) {
                if (root.mkdirs()) {
                    Log.e(TAG, " writeLogFile() : Directory root: " + root.exists());
                }else{
                    Log.e(TAG, " writeLogFile() : Directory root not created: " + root.exists());
                }
            }
            if (root.exists() && data != null) {
                //logs were saved
                File file = new File(root + File.separator + FILE_NAME);


                //add new obj
                final ArrayList<Visible> newData = new ArrayList<>();
                String msg = "";
                for(int x = 0; x < data.size(); x++){
                    Call<String> call = ApiUtils.getISalesRYImg(mContext).findVisibility(data.get(x).getId());

                    Log.e(TAG, "getProductsVisibility :: URL "+call.request().url());
                    msg += "getProductsVisibility :: URL "+call.request().url()+"\n";

                    try {
                        Response<String> response = call.execute();
                        if(response.isSuccessful()){
                            String visibility_info = response.body();
                            newData.add(new Visible(data.get(x).getId(), visibility_info));

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
                        call.cancel();
                        call = null;
                    }
                }
                Log.e(TAG, "Message : "+msg);
                */

                ////////////////////////

                /*
                for (int x = 0; x < data.size(); x++){
                    try{
                        Call<String> call = ApiUtils.getISalesRYImg(mContext).findVisibility(data.get(x).getId());
                        Log.e(TAG, "getProductsVisibility :: URL "+call.request().url());
                        final int finalX = x;
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.isSuccessful()){

                                    String visibility_info = response.body();
                                    newData.add(new Visible(data.get(finalX).getId(), visibility_info));

                                    Log.e(TAG, "getProductsVisibility :onResponse: response.isSuccessful "+response.message() + " || body : "+ visibility_info);
                                    //msg[0] += "- getProductsVisibility :onResponse: body : "+ visibility_info[0] + "\n\n";
                                }else{
                                    try {
                                        Log.e(TAG, "getProductsVisibility: getProductsVisibility :: err: " + response.errorBody().string() + " code=" + response.code());
                                        //msg[0] += "- getProductsVisibility :onResponse: err: " + response.errorBody().string() + " code=" + response.code() + "\n\n";

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        //msg[0] += "- getProductsVisibility :onResponse:IOException:: err : "+ e.getMessage() + "\n\n";
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                t.getStackTrace();
                                //msg[0] += "- getProductsVisibility :onFailure:IOException:: err : "+ t.getMessage() + "\n\n";
                            }
                        });

                    }catch (Exception e){
                        e.getStackTrace();
                    }
                }
                */

                //////////////////////////
        /*
                Log.e(TAG, " writeLogFile() || newData size :  "+newData.size());

                String fileBody = new Gson().toJson(newData);
                Log.e(TAG, " writeLogFile() || fileBody :  "+fileBody);

                fileWriter = new FileWriter(file);

                fileWriter.write(fileBody);
                fileWriter.flush();
                fileWriter.close();
            }else {
                Log.e(TAG, " writeLogFile(): File doesn't exist, ");

            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if(fileWriter != null){
                    fileWriter.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        */
        return data;
    }

    public ArrayList<Visible> readVisibleFile(){
        ArrayList<Visible> mVisibleItemList_ = new ArrayList<>();

        /*

        FileReader fileReader = null;
        try{
            File root = new File(DirectoryLocal, DirectoryName);
            File file = new File(root + File.separator + FILE_NAME);
            Log.e(TAG, " readVisibleFile() : Root path: "+root.getAbsolutePath()+"\nFile path: "+file.getAbsolutePath());

            if (root.exists() && file.exists()){
                //Folder || File doesn't exist
                fileReader = new FileReader(file);

                JSONParser parser = new JSONParser();
                org.json.simple.JSONArray logJsononArray = (org.json.simple.JSONArray) parser.parse(fileReader);
                Log.e(TAG, " readVisibleFile() || Array:  "+logJsononArray.toString());

                mVisibleItemList_ = new Gson().fromJson(logJsononArray.toString(), new TypeToken<ArrayList<Visible>>(){}.getType());
                Log.e(TAG, " readVisibleFile() || mVisibleItemList_ size:  "+mVisibleItemList_.size());

                fileReader.close();
                return mVisibleItemList_;

            }else{
                //Folder && File exist
                //Log.e(TAG, " readLogFile() : Log File doesn't exist. ");
                if (!root.exists()){
                    Log.e(TAG, " readVisibleFile() : Directory root not created: " + root.exists());
                }
                if (!file.exists()){
                    Log.e(TAG, " readVisibleFile() : File not created: ");
                }
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            try {
                if(fileReader != null){
                    fileReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        */
        return mVisibleItemList_;
    }

    public boolean deleteFile(){
        File root = new File(DirectoryLocal, DirectoryName);
        File file = new File(root + File.separator + FILE_NAME);
        Log.e(TAG, " deleteLogFile() :: Root path: "+root.getAbsolutePath());
        Log.e(TAG, " deleteLogFile() :: File path: "+file.getAbsolutePath() + " || size: "+file.length());

        if (root.exists() && file.exists()) {
            file.delete();
            root.delete();
            Log.e(TAG, " deleteLogFile(): Log Root and File were deleted!");
            return true;
        }else{
            Log.e(TAG, " deleteLogFile(): Log Root and File doesn't exist.");
            return false;
        }
    }
}
