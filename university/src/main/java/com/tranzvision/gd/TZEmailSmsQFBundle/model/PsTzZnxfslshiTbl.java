package com.tranzvision.gd.TZEmailSmsQFBundle.model;

import java.util.Date;

public class PsTzZnxfslshiTbl {
    private String tzRwslId;

    private String oprid;

    private String tzZnxZt;

    private Date tzFsDt;

    private String tzEmlSmsTaskId;

    private String tzFsZt;

    private String tzJcslId;

    private String tzAudcyId;

    public String getTzRwslId() {
        return tzRwslId;
    }

    public void setTzRwslId(String tzRwslId) {
        this.tzRwslId = tzRwslId == null ? null : tzRwslId.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzZnxZt() {
        return tzZnxZt;
    }

    public void setTzZnxZt(String tzZnxZt) {
        this.tzZnxZt = tzZnxZt == null ? null : tzZnxZt.trim();
    }

    public Date getTzFsDt() {
        return tzFsDt;
    }

    public void setTzFsDt(Date tzFsDt) {
        this.tzFsDt = tzFsDt;
    }

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzFsZt() {
        return tzFsZt;
    }

    public void setTzFsZt(String tzFsZt) {
        this.tzFsZt = tzFsZt == null ? null : tzFsZt.trim();
    }

    public String getTzJcslId() {
        return tzJcslId;
    }

    public void setTzJcslId(String tzJcslId) {
        this.tzJcslId = tzJcslId == null ? null : tzJcslId.trim();
    }

    public String getTzAudcyId() {
        return tzAudcyId;
    }

    public void setTzAudcyId(String tzAudcyId) {
        this.tzAudcyId = tzAudcyId == null ? null : tzAudcyId.trim();
    }
}