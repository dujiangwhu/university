package com.tranzvision.gd.TZPXBundle.model;

public class PxStudentTKey {
    private String oprid;

    private String tzJgId;

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}