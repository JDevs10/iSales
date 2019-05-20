package com.iSales.database.entry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.util.Log;

import com.iSales.database.AppDatabase;
import com.iSales.remote.model.DebugItem;

@Entity(tableName = "debug_message")
public class DebugItemEntry {

    @PrimaryKey(autoGenerate = true)
    private int rowId;
    private long datetimeLong;
    private String mask;
    private String errorMessage;

    public DebugItemEntry() {
    }

    public DebugItemEntry(Context context, long timeStamp, String mask, String message){
        this.datetimeLong = timeStamp;
        this.mask = mask + AppDatabase.getInstance(context).debugMessageDao().getAllDebugMessages().size();
        this.errorMessage = message;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public long getDatetimeLong() {
        return datetimeLong;
    }

    public void setDatetimeLong(long datetimeLong) {
        this.datetimeLong = datetimeLong;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
