package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PkStuCourseChangeT {
    private String tzChangeId;

    private String oprid;

    private String tzChangeType;

    private Integer tzBeforeChange;

    private Integer tzAfterChange;

    private Integer tzChange;

    private Date tzChangeTime;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private String tzScheduleId;

    public String getTzChangeId() {
        return tzChangeId;
    }

    public void setTzChangeId(String tzChangeId) {
        this.tzChangeId = tzChangeId == null ? null : tzChangeId.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzChangeType() {
        return tzChangeType;
    }

    public void setTzChangeType(String tzChangeType) {
        this.tzChangeType = tzChangeType == null ? null : tzChangeType.trim();
    }

    public Integer getTzBeforeChange() {
        return tzBeforeChange;
    }

    public void setTzBeforeChange(Integer tzBeforeChange) {
        this.tzBeforeChange = tzBeforeChange;
    }

    public Integer getTzAfterChange() {
        return tzAfterChange;
    }

    public void setTzAfterChange(Integer tzAfterChange) {
        this.tzAfterChange = tzAfterChange;
    }

    public Integer getTzChange() {
        return tzChange;
    }

    public void setTzChange(Integer tzChange) {
        this.tzChange = tzChange;
    }

    public Date getTzChangeTime() {
        return tzChangeTime;
    }

    public void setTzChangeTime(Date tzChangeTime) {
        this.tzChangeTime = tzChangeTime;
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }

    public String getTzScheduleId() {
        return tzScheduleId;
    }

    public void setTzScheduleId(String tzScheduleId) {
        this.tzScheduleId = tzScheduleId == null ? null : tzScheduleId.trim();
    }
}