package com.selective.reminder.Util;

import java.util.Date;

public class memoItem {
    private int id;
    private Long backid;
    private int icon_type;
    private String memo;
    private String h;
    private String m;
    private boolean is_do;

    public memoItem() {
        id=0;
        backid = 0L;
        icon_type=0;
        memo="";
        h="00";
        m="00";
        is_do=false;
    }

    public memoItem(int id,Long backid, int icon_type, String memo, String h,String m, boolean is_do) {
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
    public void setBackid(Long backid) {
        this.backid = backid;
    }

    public void setIcon_type(int icon_type) {
        this.icon_type = icon_type;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public void sethour(String h){
        this.h=h;
    }
    public void setminute(String m){
        this.m=m;
    }
    public void setIs_do(boolean is_do) {
        this.is_do = is_do;
    }

    public int getId() {
        return id;
    }

    public Long getBackid() {
        return backid;
    }
    public String gethour() {
        return h;
    }
    public String getminute() {
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
