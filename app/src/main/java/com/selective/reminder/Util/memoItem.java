package com.selective.reminder.Util;

import java.util.Date;

public class memoItem {
    private Long id;
    private int icon_type;
    private String memo;
    private Date date;
    private boolean is_do;

    public memoItem() {
        id=0L;
        icon_type=0;
        memo="";
        date=new Date();
        is_do=false;
    }

    public memoItem(Long id, int icon_type, String memo, Date date, boolean is_do) {
        this.id = id;
        this.icon_type=icon_type;
        this.memo=memo;
        this.date=date;
        this.is_do=is_do;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setIcon_type(int icon_type) {
        this.icon_type = icon_type;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setIs_do(boolean is_do) {
        this.is_do = is_do;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getIcon_type() {
        return icon_type;
    }

    public String getMemo() {
        return memo;
    }
}
