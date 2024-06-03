package com.selective.reminder.Util;

public class gridmemo {
    private int backid;
    private String memo;
    private String day;

    public gridmemo(int backid, String memo, String day) {
        this.backid = backid;
        this.memo = memo;
        this.day = day;
    }

    public gridmemo() {
        backid=0;
        memo="";
        day="";
    }

    public int getBackid() {
        return backid;
    }

    public String getMemo() {
        return memo;
    }

    public String getDay() {
        return day;
    }
    public void setBackid(int backid) {
        this.backid = backid;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
