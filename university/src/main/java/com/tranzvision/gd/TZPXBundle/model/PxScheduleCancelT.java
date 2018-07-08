package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxScheduleCancelT {
    private String tzScheduleId;

    private String oprid;

    private Date tzCancelTime;

    private String tzCancelDesc;

    private String newOprid;

    private String newTzScheduleId;

    private String rowLastmantOprid;

    private Date rowLastmantDttm;

    public String getTzScheduleId() {
        return tzScheduleId;
    }

    public void setTzScheduleId(String tzScheduleId) {
        this.tzScheduleId = tzScheduleId == null ? null : tzScheduleId.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public Date getTzCancelTime() {
        return tzCancelTime;
    }

    public void setTzCancelTime(Date tzCancelTime) {
        this.tzCancelTime = tzCancelTime;
    }

    public String getTzCancelDesc() {
        return tzCancelDesc;
    }

    public void setTzCancelDesc(String tzCancelDesc) {
        this.tzCancelDesc = tzCancelDesc == null ? null : tzCancelDesc.trim();
    }

    public String getNewOprid() {
        return newOprid;
    }

    public void setNewOprid(String newOprid) {
        this.newOprid = newOprid == null ? null : newOprid.trim();
    }

    public String getNewTzScheduleId() {
        return newTzScheduleId;
    }

    public void setNewTzScheduleId(String newTzScheduleId) {
        this.newTzScheduleId = newTzScheduleId == null ? null : newTzScheduleId.trim();
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }
}