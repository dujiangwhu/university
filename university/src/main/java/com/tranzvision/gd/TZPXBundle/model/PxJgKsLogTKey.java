package com.tranzvision.gd.TZPXBundle.model;

public class PxJgKsLogTKey {
    private String tzJgId;

    private String tzLogInsId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzLogInsId() {
        return tzLogInsId;
    }

    public void setTzLogInsId(String tzLogInsId) {
        this.tzLogInsId = tzLogInsId == null ? null : tzLogInsId.trim();
    }
}