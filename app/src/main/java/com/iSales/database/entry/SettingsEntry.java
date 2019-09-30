package com.iSales.database.entry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "settings")
public class SettingsEntry {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private boolean showDescripCataloge;

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
}
