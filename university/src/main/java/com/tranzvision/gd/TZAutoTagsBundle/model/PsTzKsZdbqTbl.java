package com.tranzvision.gd.TZAutoTagsBundle.model;

public class PsTzKsZdbqTbl {
    private Long tzAppInsId;

    private String tzZdbqId;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzZdbqId() {
        return tzZdbqId;
    }

    public void setTzZdbqId(String tzZdbqId) {
        this.tzZdbqId = tzZdbqId == null ? null : tzZdbqId.trim();
    }
}