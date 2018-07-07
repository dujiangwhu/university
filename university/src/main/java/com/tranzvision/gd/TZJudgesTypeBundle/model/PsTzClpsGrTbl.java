package com.tranzvision.gd.TZJudgesTypeBundle.model;

public class PsTzClpsGrTbl {
    private String tzClpsGrId;

    private String tzJgId;

    private String tzJugtypId;

    private String tzClpsGrName;

    public String getTzClpsGrId() {
        return tzClpsGrId;
    }

    public void setTzClpsGrId(String tzClpsGrId) {
        this.tzClpsGrId = tzClpsGrId == null ? null : tzClpsGrId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzJugtypId() {
        return tzJugtypId;
    }

    public void setTzJugtypId(String tzJugtypId) {
        this.tzJugtypId = tzJugtypId == null ? null : tzJugtypId.trim();
    }

    public String getTzClpsGrName() {
        return tzClpsGrName;
    }

    public void setTzClpsGrName(String tzClpsGrName) {
        this.tzClpsGrName = tzClpsGrName == null ? null : tzClpsGrName.trim();
    }
}