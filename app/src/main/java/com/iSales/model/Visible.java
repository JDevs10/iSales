package com.iSales.model;

public class Visible {
    private String rowid;
    private String visible;

    public Visible() {

    }

    public Visible(String rowid, String visible) {
        this.rowid = rowid;
        this.visible = visible;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
}
