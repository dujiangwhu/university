package com.tranzvision.gd.TZPXBundle.model;

public class PkStuAppCourseTKey {
    private String oprid;

    private String tzScheduleId;

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzScheduleId() {
        return tzScheduleId;
    }

    public void setTzScheduleId(String tzScheduleId) {
        this.tzScheduleId = tzScheduleId == null ? null : tzScheduleId.trim();
    }
}