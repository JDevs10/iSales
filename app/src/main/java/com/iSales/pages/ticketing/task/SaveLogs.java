package com.iSales.pages.ticketing.task;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iSales.pages.ticketing.model.DebugItem;
import com.iSales.pages.ticketing.model.DebugItemList;

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
    private final int DEBUG_ITEM_LIST_LIMIT = 200;

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
                ArrayList<DebugItemList> logs = readLogFile();

                if(logs == null){
                    //first log to save
                    File file = new File(root + File.separator + FileName);
                    Log.e(TAG, " writeLogFile() 1 : Root path: "+root.getAbsolutePath()+"\nFile path: "+file.getAbsolutePath());

                    //Folder || File doesn't exist
                    //FileReader fileReader = new FileReader(file);

                    FileWriter fileWriter = new FileWriter(file);

                    JSONParser parser = new JSONParser();
                    org.json.simple.JSONArray logJsononArray = (org.json.simple.JSONArray) parser.parse("[]");
                    Log.e(TAG, " writeLogFile() || logArray 1:  "+logJsononArray.toString());

                    ArrayList<DebugItemList> mDebugItemList = new Gson().fromJson(logJsononArray.toString(), new TypeToken<ArrayList<DebugItemList>>(){}.getType());
                    Log.e(TAG, " writeLogFile() || mDebugItemList 1:  "+mDebugItemList.size());

                    //add new obj
                    data.setRowId(1);
                    data.setMask(data.getMask() + "-1");
                    mDebugItemList.add(new DebugItemList(System.currentTimeMillis(), data));
                    Log.e(TAG, " writeLogFile() || mDebugItemList added 1:  "+mDebugItemList.size());

                    String fileBody = new Gson().toJson(mDebugItemList);
                    Log.e(TAG, " writeLogFile() || fileBody 1:  "+fileBody);

                    fileWriter.write(fileBody);
                    fileWriter.flush();
                    fileWriter.close();

                }else{
                    //logs were saved
                    File file = new File(root + File.separator + FileName);
                    /*
                    Log.e(TAG, " writeLogFile() V2: Root path: "+root.getAbsolutePath()+"\nFile path: "+file.getAbsolutePath() + " || size: " +file.length());

                    FileReader fileReader = new FileReader(file);

                    JSONParser parser = new JSONParser();
                    org.json.simple.JSONArray logJsononArray = (org.json.simple.JSONArray) parser.parse(fileReader);
                    Log.e(TAG, " writeLogFile() || logArray V2:  "+logJsononArray.toString());

                    ArrayList<DebugItemList> mDebugItemList = new Gson().fromJson(logJsononArray.toString(), new TypeToken<ArrayList<DebugItemList>>(){}.getType());
                    Log.e(TAG, " writeLogFile() || mDebugItemList V2:  "+mDebugItemList.size());

                    //add new obj
                    DebugItemList lastDebugItemList = mDebugItemList.get(mDebugItemList.size()-1);
                    data.setRowId((lastDebugItemList.getDebugItem().getRowId() + 1));
                    data.setMask(data.getMask() + "-" + (lastDebugItemList.getDebugItem().getRowId() + 1));

                    mDebugItemList.add(new DebugItemList(System.currentTimeMillis(), data));
                    Log.e(TAG, " writeLogFile() || mDebugItemList added V2:  "+mDebugItemList.size());

                    String fileBody = new Gson().toJson(mDebugItemList);
                    Log.e(TAG, " writeLogFile() || fileBody V2:  "+fileBody);

                    FileWriter fileWriter = new FileWriter(file);

                    fileWriter.write(fileBody);
                    fileWriter.flush();
                    fileWriter.close();
                    */

                    //add new obj
                    DebugItemList lastDebugItemList;
                    if (logs.size() == 0){
                        data.setRowId(1);
                        data.setMask(data.getMask() + "-1");
                    }else{
                        lastDebugItemList = logs.get(logs.size()-1);
                        data.setRowId((lastDebugItemList.getDebugItem().getRowId() + 1));
                        data.setMask(data.getMask() + "-" + (lastDebugItemList.getDebugItem().getRowId() + 1));
                    }

                    logs.add(new DebugItemList(System.currentTimeMillis(), data));
                    Log.e(TAG, " writeLogFile() || mDebugItemList added V3:  "+logs.size());

                    String fileBody = new Gson().toJson(logs);
                    Log.e(TAG, " writeLogFile() || fileBody V3:  "+fileBody);

                    FileWriter fileWriter = new FileWriter(file);

                    fileWriter.write(fileBody);
                    fileWriter.flush();
                    fileWriter.close();
                }
            }else{
                Log.e(TAG, " writeLogFile(): File doesn't exist, ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DebugItemList> readLogFile(){
        ArrayList<DebugItemList> mDebugItemList_ = new ArrayList<>();

        try{
            File root = new File(DirectoryLocal, DirectoryName);
            File file = new File(root + File.separator + FileName);
            Log.e(TAG, " readLogFile() : Root path: "+root.getAbsolutePath()+"\nFile path: "+file.getAbsolutePath());

            if (root.exists() && file.exists()){
                //Folder || File doesn't exist
                FileReader fileReader = new FileReader(file);

                JSONParser parser = new JSONParser();
                org.json.simple.JSONArray logJsononArray = (org.json.simple.JSONArray) parser.parse(fileReader);
                Log.e(TAG, " readLogFile() || logArray:  "+logJsononArray.toString());

                mDebugItemList_ = new Gson().fromJson(logJsononArray.toString(), new TypeToken<ArrayList<DebugItemList>>(){}.getType());
                Log.e(TAG, " readLogFile() || mDebugItemList:  "+mDebugItemList_.size());

                fileReader.close();

                //filter the list and limit 150 debug items
                mDebugItemList_ = logLimit(mDebugItemList_,DEBUG_ITEM_LIST_LIMIT);

                return mDebugItemList_;

            }else{
                //Folder && File exist
                //Log.e(TAG, " readLogFile() : Log File doesn't exist. ");
                if (!root.exists()){
                    Log.e(TAG, " readLogFile() : Directory root not created: " + root.exists());
                }
                if (!file.exists()){
                    Log.e(TAG, " readLogFile() : File not created: ");
                }
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return mDebugItemList_;
    }

    public File convertLogFileToReadableFile() {
        String body = "";

        try {
            File root = new File(DirectoryLocal, DirectoryName);
            File file = new File(root + File.separator + FileName);
            Log.e(TAG, " convertLogFileToReadableFile() : root path: "+root.getAbsolutePath());

            if (root.exists() && file.exists()) {
                File newFile = new File(root + File.separator + ReadableFileName);

                ArrayList<DebugItemList> logs = readLogFile();
                FileWriter writer = new FileWriter(newFile);

                body += "#################################################################################################################\n";
                body += "#################################################  iSales Logs  #################################################\n";
                body += "#################################################################################################  By JDevs  ####\n";
                body += "#################################################################################################################\n\n";
                writer.write(body);
                writer.flush();

                for (DebugItemList debugItem : logs){
                    body = DateFormat.format("dd-MM-yyyy hh:mm:ss", new Date(debugItem.getDebugItem().getDatetimeLong()*1000)).toString() + " | Mask : " + debugItem.getDebugItem().getMask() + "\n" +
                            "Class : " + debugItem.getDebugItem().getClassName() + " => Method : " + debugItem.getDebugItem().getMethodName() + "\n" +
                            "Message : " + debugItem.getDebugItem().getMessage() + "\n" +
                            "StraceStack : " + debugItem.getDebugItem().getStackTrace() + "\n";

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

    public void deleteLogFile(){
        File root = new File(DirectoryLocal, DirectoryName);
        File file = new File(root + File.separator + FileName);
        Log.e(TAG, " deleteLogFile() :: Root path: "+root.getAbsolutePath());
        Log.e(TAG, " deleteLogFile() :: File path: "+file.getAbsolutePath() + " || size: "+file.length());

        if (root.exists() && file.exists()) {
            file.delete();
            root.delete();
            Log.e(TAG, " deleteLogFile(): Log Root and File were deleted!");
        }else{
            Log.e(TAG, " deleteLogFile(): Log Root and File doesn't exist.");
        }
    }

    private ArrayList<DebugItemList> logLimit(ArrayList<DebugItemList> list, int limits){
        Log.e(TAG, " logLimit() :: list size: "+ list.size() + " || limits: "+limits);
        if (list.size() > limits){

            ArrayList<DebugItemList> newList = new ArrayList<>(list.subList((limits-1), list.size()));
            Log.e(TAG, " logLimit() :: newList size: "+newList.size());
            return newList;
        }
        return list;
    }

    public void delete24hLogs(){

        try {
            File root = new File(DirectoryLocal, DirectoryName);
            File file = new File(root + File.separator + FileName);
            Log.e(TAG, " delete24hLogs() :: Root path: "+root.getAbsolutePath());
            Log.e(TAG, " delete24hLogs() :: File path: "+file.getAbsolutePath() + " || size: "+file.length());

            if (root.exists() && file.exists()) {

                ArrayList<DebugItemList> mDebugItemList = readLogFile();

                int cpt = 0;
                long time = (System.currentTimeMillis()/1000) - 86400000;
                for (DebugItemList item : mDebugItemList) {
                    if (item.getDebugItem().getDatetimeLong() < time) {
                        mDebugItemList.remove(item);
                        cpt++;
                    }
                }
                Log.e(TAG, " delete24hLogs() :: "+cpt+" Debug Log Items cleaned!");

                String fileBody = new Gson().toJson(mDebugItemList);
                Log.e(TAG, " delete24hLogs() || fileBody :  "+fileBody);

                FileWriter fileWriter = new FileWriter(file);

                fileWriter.write(fileBody);
                fileWriter.flush();
                fileWriter.close();

            }else{
                Log.e(TAG, " readLogFile(): Log File doesn't exist, ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}