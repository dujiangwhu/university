package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxTeaToCrashKey {
    private String teaOprid;

    private Date operateTime;

    public String getTeaOprid() {
        return teaOprid;
    }

    public void setTeaOprid(String teaOprid) {
        this.teaOprid = teaOprid == null ? null : teaOprid.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}