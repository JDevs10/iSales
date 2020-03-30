package com.iSales.pages.ticketing.task;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.iSales.pages.ticketing.model.DebugItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class SaveLogs {
    private String TAG = SaveLogs.class.getSimpleName();
    private Context mContext;
    private final File DirectoryLocal = Environment.getExternalStorageDirectory();
    private final String DirectoryName = "iSales_Logs";
    private final String FileName = "logs.json";
    private final String ReadableFileName = "log.txt";

    public SaveLogs(Context mContext){
        this.mContext = mContext;
    }

    public void writeLogFile(DebugItem data){
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
                ArrayList<DebugItem> logs = readLogFile();

                if(logs == null){
                    //first log to save
                    logs = new ArrayList<>();
                    data.setMask(data.getMask() + "-1");
                    logs.add(data);

                    JSONArray logsList = new JSONArray();

                    File file = new File(root + File.separator + FileName);
                    FileWriter writer = new FileWriter(file);

                    for (DebugItem logs_ : logs){
                        JSONObject log = new JSONObject();
                        final JSONObject details = new JSONObject();

                        details.put("rowId", 1);
                        details.put("datetimeLong",logs_.getDatetimeLong());
                        details.put("mask", logs_.getMask());
                        details.put("className", logs_.getClassName());
                        details.put("methodName", logs_.getMethodName());
                        details.put("message", logs_.getMessage());
                        details.put("stackTrace", logs_.getStackTrace());

                        log.put("log_id", System.currentTimeMillis());
                        log.put("DebugItem", details);
                        logsList.put(log);
                    }
                    //Log.e(TAG, " writeLogFile() | logs == null :: logsList : " + logsList.toString());


                    writer.write(logsList.toString());
                    writer.flush();
                    writer.close();

                }else{
                    //logs were saved
                    logs.add(data);

                    JSONArray logsList = new JSONArray();

                    File file = new File(root + File.separator + FileName);
                    FileWriter writer = new FileWriter(file);

                    int cpt = 1;
                    for (DebugItem logs_ : logs){
                        JSONObject log = new JSONObject();
                        final JSONObject details = new JSONObject();

                        details.put("rowId", cpt);
                        details.put("datetimeLong", logs_.getDatetimeLong());

                        if (logs_.getMask().contains("-")){
                            details.put("mask", logs_.getMask());
                            Log.e(TAG, " writeLogFile() : mask 1 : " + logs_.getMask());
                        }else{
                            details.put("mask", logs_.getMask()+"-"+cpt);
                            Log.e(TAG, " writeLogFile() : mask 2 : " + logs_.getMask());
                        }
                        details.put("className", logs_.getClassName());
                        details.put("methodName", logs_.getMethodName());
                        details.put("message", logs_.getMessage());
                        details.put("stackTrace", logs_.getStackTrace());

                        log.put("log_id", System.currentTimeMillis());
                        log.put("DebugItem", details);
                        logsList.put(log);
                        cpt++;
                    }

                    writer.write(logsList.toString());
                    writer.flush();
                    writer.close();
                }
            }else{
                Log.e(TAG, " generateSaveData(): File doesn't exist, ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DebugItem> readLogFile(){
        ArrayList<DebugItem> logArray = new ArrayList<>();

        try {
            File root = new File(DirectoryLocal, DirectoryName);
            File file = new File(root + File.separator + FileName);
            Log.e(TAG, " readLogFile() : Root path: "+root.getAbsolutePath()+"\nFile path: "+file.getAbsolutePath());

            if (root.exists() && file.exists()) {
                FileReader fileReader = new FileReader(file);

                JSONParser parser = new JSONParser();
                org.json.simple.JSONArray array = (org.json.simple.JSONArray)parser.parse(fileReader);

                int cpt = 0;
                for(Object obj : array){
                    /* Extract each JSONObject */

                    cpt++;
                    if (obj != null){
                        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
                        long id_log = (long) jsonObject.get("log_id");

                        Log.e(TAG, "Find Log count : "+cpt+" || rowid : "+id_log+ " || found !!!!!");

                        // getting log infos
                        Gson gson = new Gson();
                        DebugItem mDebugItem = gson.fromJson(jsonObject.get("DebugItem").toString(), DebugItem.class);
                        logArray.add(mDebugItem);
                    }else{
                        Log.e(TAG, "Log count : "+cpt+" || NULL found !!!!!");
                    }
                }
            }else{
                Log.e(TAG, " readLogFile(): Log File doesn't exist, ");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return logArray;
    }

    public File convertLogFileToReadableFile() {
        String body = "";

        try {
            File root = new File(DirectoryLocal, DirectoryName);
            File file = new File(root + File.separator + FileName);
            Log.e(TAG, " convertLogFileToReadableFile() : root path: "+root.getAbsolutePath());

            if (root.exists() && file.exists()) {
                File newFile = new File(root + File.separator + ReadableFileName);

                ArrayList<DebugItem> logs = readLogFile();
                FileWriter writer = new FileWriter(newFile);

                body += "#################################################################################################################\n";
                body += "#################################################  iSales Logs  #################################################\n";
                body += "#################################################################################################  By JDevs  ####\n";
                body += "#################################################################################################################\n\n";
                writer.write(body);
                writer.flush();

                for (DebugItem debugItem : logs){
                    body = DateFormat.format("dd-MM-yyyy hh:mm:ss", new Date(debugItem.getDatetimeLong()*1000)).toString() + " | Mask : " + debugItem.getMask() + "\n" +
                            "Class : " + debugItem.getClassName() + " => Method : " + debugItem.getMethodName() + "\n" +
                            "Message : " + debugItem.getMessage() + "\n" +
                            "StraceStack : " + debugItem.getStackTrace() + "\n";

                    writer.append(body);
                    writer.flush();
                }
                writer.close();

                return newFile;
            }else{
                Log.e(TAG, " generateSaveData(): File doesn't exist, ");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete24hLogs(){
        ArrayList<DebugItem> logArray = new ArrayList<>();

        try {
            File root = new File(DirectoryLocal, DirectoryName);
            Log.e(TAG, " delete24hLogs() :: root path: "+root.getAbsolutePath());

            if (root.exists()) {
                File file = new File(root + File.separator + FileName);
                FileReader fileReader = new FileReader(file);

                JSONParser parser = new JSONParser();
                org.json.simple.JSONArray array = (org.json.simple.JSONArray)parser.parse(fileReader);

                for(Object obj : array){
                    /* Extract each JSONObject */

                    if(obj != null){
                        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
                        Gson gson = new Gson();
                        DebugItem mDebugItem = gson.fromJson(jsonObject.get("DebugItem").toString(), DebugItem.class);
                        logArray.add(mDebugItem);
                    }
                }

                //remove all logs after 24h
                long time = (System.currentTimeMillis()/1000) - 86400000;
                for(int x = 0; x < logArray.size(); x++){

                    if(logArray.get(x).getDatetimeLong() < time){
                        logArray.remove(x);
                    }
                }

                JSONArray logsList = new JSONArray(logArray);
                FileWriter writer = new FileWriter(file);

                writer.write(logsList.toString());
                writer.flush();
                writer.close();

            }else{
                Log.e(TAG, " readLogFile(): Log File doesn't exist, ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
