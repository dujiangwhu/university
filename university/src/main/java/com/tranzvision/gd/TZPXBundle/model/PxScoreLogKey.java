package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxScoreLogKey {
    private String teaOprid;

    private Date changeTime;

    public String getTeaOprid() {
        return teaOprid;
    }

    public void setTeaOprid(String teaOprid) {
        this.teaOprid = teaOprid == null ? null : teaOprid.trim();
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }
}