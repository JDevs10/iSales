package com.iSales.database.entry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "settings")
public class SettingsEntry {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private boolean showDescripCataloge;
    private String email;
    private String email_Pwd;

    public SettingsEntry() {

    }

    public SettingsEntry(int id, boolean showDescripCataloge) {
        this.id = id;
        this.showDescripCataloge = showDescripCataloge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isShowDescripCataloge() {
        return showDescripCataloge;
    }

    public void setShowDescripCataloge(boolean showDescripCataloge) {
        this.showDescripCataloge = showDescripCataloge;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_Pwd() {
        return email_Pwd;
    }

    public void setEmail_Pwd(String email_Pwd) {
        this.email_Pwd = email_Pwd;
    }
}
