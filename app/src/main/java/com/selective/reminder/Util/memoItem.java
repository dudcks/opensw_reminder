package com.selective.reminder.Util;

import java.util.Date;

public class memoItem {
    private int id;
    private int backid;
    private int icon_type;
    private String memo;
    private int h;
    private int m;
    private boolean is_do;

    public memoItem() {
        id=0;
        backid = 0;
        icon_type=0;
        memo="";
        h=14;
        m=50;
        is_do=false;
    }

    public memoItem(int id,int backid, int icon_type, String memo, int h,int m, boolean is_do) {
        this.id = id;
        this.backid = backid;
        this.icon_type=icon_type;
        this.memo=memo;
        this.h=h;
        this.m=m;
        this.is_do=is_do;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setBackid(int backid) {
        this.backid = backid;
    }

    public void setIcon_type(int icon_type) {
        this.icon_type = icon_type;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public void sethour(int h){
        this.h=h;
    }
    public void setminute(int m){
        this.m=m;
    }
    public void setIs_do(boolean is_do) {
        this.is_do = is_do;
    }

    public int getId() {
        return id;
    }

    public int getBackid() {
        return backid;
    }
    public int gethour() {
        return h;
    }
    public int getminute() {
        return m;
    }
    public boolean getIs_do() {
        return is_do;
    }

    public int getIcon_type() {
        return icon_type;
    }

    public String getMemo() {
        return memo;
    }
}
