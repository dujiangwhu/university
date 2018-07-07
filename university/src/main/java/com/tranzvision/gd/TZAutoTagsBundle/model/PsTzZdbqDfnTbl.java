package com.tranzvision.gd.TZAutoTagsBundle.model;

public class PsTzZdbqDfnTbl {
    private String tzZdbqId;

    private String tzZdbqName;

    private String tzDescr;

    private String tzIsvalid;

    private String tzJavaClass;

    private String tzViewClpwd;

    public String getTzZdbqId() {
        return tzZdbqId;
    }

    public void setTzZdbqId(String tzZdbqId) {
        this.tzZdbqId = tzZdbqId == null ? null : tzZdbqId.trim();
    }

    public String getTzZdbqName() {
        return tzZdbqName;
    }

    public void setTzZdbqName(String tzZdbqName) {
        this.tzZdbqName = tzZdbqName == null ? null : tzZdbqName.trim();
    }

    public String getTzDescr() {
        return tzDescr;
    }

    public void setTzDescr(String tzDescr) {
        this.tzDescr = tzDescr == null ? null : tzDescr.trim();
    }

    public String getTzIsvalid() {
        return tzIsvalid;
    }

    public void setTzIsvalid(String tzIsvalid) {
        this.tzIsvalid = tzIsvalid == null ? null : tzIsvalid.trim();
    }

    public String getTzJavaClass() {
        return tzJavaClass;
    }

    public void setTzJavaClass(String tzJavaClass) {
        this.tzJavaClass = tzJavaClass == null ? null : tzJavaClass.trim();
    }

    public String getTzViewClpwd() {
        return tzViewClpwd;
    }

    public void setTzViewClpwd(String tzViewClpwd) {
        this.tzViewClpwd = tzViewClpwd == null ? null : tzViewClpwd.trim();
    }
}