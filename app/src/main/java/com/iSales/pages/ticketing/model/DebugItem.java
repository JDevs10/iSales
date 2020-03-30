package com.iSales.pages.ticketing.model;

public class DebugItem {
    private int rowId;
    private long datetimeLong;
    private String mask;
    private String className;
    private String methodName;
    private String message;
    private String stackTrace;

    public DebugItem(long timeStamp, String mask, String className, String methodName, String message, String stackTrace){
        this.datetimeLong = timeStamp;
        this.mask = mask;
        this.className = className;
        this.methodName = methodName;
        this.message = message;
        this.stackTrace = stackTrace;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
