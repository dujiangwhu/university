package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzStuExtTKey {
    private String oprid;

    private String tzMshId;

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzMshId() {
        return tzMshId;
    }

    public void setTzMshId(String tzMshId) {
        this.tzMshId = tzMshId == null ? null : tzMshId.trim();
    }
}