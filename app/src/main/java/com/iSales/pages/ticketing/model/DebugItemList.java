package com.iSales.pages.ticketing.model;

public class DebugItemList {
    private long log_id;
    private DebugItem DebugItem;

    public DebugItemList() {
    }

    public DebugItemList(long log_id, DebugItem debugItem) {
        this.log_id = log_id;
        DebugItem = debugItem;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public DebugItem getDebugItem() {
        return DebugItem;
    }

    public void setDebugItem(DebugItem debugItem) {
        DebugItem = debugItem;
    }
}
