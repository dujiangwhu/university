package com.tranzvision.gd.TZJudgesTypeBundle.model;

public class PsTzPwHyTblKey {
    private String tzJgId;

    private String oprid;

    private String tzPwhyId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzPwhyId() {
        return tzPwhyId;
    }

    public void setTzPwhyId(String tzPwhyId) {
        this.tzPwhyId = tzPwhyId == null ? null : tzPwhyId.trim();
    }
}