package com.iSales.task;

import android.os.AsyncTask;
import android.content.Context;
import android.os.Environment;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.iSales.database.AppDatabase;
import com.iSales.database.entry.TokenEntry;
import com.iSales.database.entry.UserEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveUserTask {
    private String TAG = SaveUserTask.class.getSimpleName();
    private Context mContext;
    private final String DirectoryBackUp = Environment.getDataDirectory()+"/iSales_BackUp";
    private final String FileNameBackUp = "BackUp.txt";
    private final String BackUpHeader = "BACKUP;ISALES";
    private final String FileSplit = ";";

    public SaveUserTask(Context mContext){
        this.mContext = mContext;
    }

    private void SetRestoreBackUpData(String task){
        if (task.equals("SET") || task.equals("Set") || task.equals("set")){
            File backUpRoot = new File(DirectoryBackUp);

            //Check if directory and or file exist
            if (!backUpRoot.exists()){
                generateSaveData(mContext, FileNameBackUp, backUpData());
            }
        } else if (task.equals("RESTORE") || task.equals("Restore") || task.equals("restore")){



        }
    }

    private void generateSaveData(Context context, String FileName, String Body) {
        try {
            File root = new File(DirectoryBackUp);
            if (!root.exists()) {
                root.mkdirs();
            }
            if (!FileName.isEmpty() && Body != null) {
                File file = new File(root, FileName);
                FileWriter writer = new FileWriter(file);
                writer.append(Body);
                writer.flush();
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String backUpData(){
        AppDatabase db = AppDatabase.getInstance(mContext);
        UserEntry userEntry = db.userDao().getUser().get(0);
        TokenEntry tokenEntry = db.tokenDao().getAllToken().get(0);
        String result = null;

        if (userEntry != null && tokenEntry != null){

            result = BackUpHeader+FileSplit+"\n";
            result += userEntry.getId()+FileSplit+userEntry.getStatut()+FileSplit+userEntry.getEmployee()+FileSplit+userEntry.getGender()+FileSplit+
                    userEntry.getBirth()+FileSplit+userEntry.getFirstname()+FileSplit+userEntry.getLastname()+FileSplit+userEntry.getName()+FileSplit+
                    userEntry.getCountry()+FileSplit+userEntry.getDateemployment()+FileSplit+userEntry.getPhoto()+FileSplit+userEntry.getDatelastlogin()+FileSplit+
                    userEntry.getDatec()+FileSplit+userEntry.getDatem()+FileSplit+userEntry.getAdmin()+FileSplit+userEntry.getLogin()+FileSplit+
                    userEntry.getTown()+FileSplit+userEntry.getAddress()+FileSplit;
            result += tokenEntry.getId()+FileSplit+tokenEntry.getToken()+FileSplit+tokenEntry.getMessage()+FileSplit;

            return result;
        }

        return result;
    }

    private void restoreFileData(Context context){
        String line = "";
        try {
            File file = new File(DirectoryBackUp+FileSplit+FileNameBackUp);
            if (file.exists()) {

                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);

                while ((line = br.readLine()) != null) {
                    System.out.println(br.readLine());
                }
                fileReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!line.isEmpty()){
            String result = "";
            String[] values = line.split(FileSplit);
            AppDatabase db = AppDatabase.getInstance(mContext);
            UserEntry user = new UserEntry();
            TokenEntry token = new TokenEntry();

            

        }
    }
}
