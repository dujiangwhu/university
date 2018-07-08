package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxTeaScheduleT {
    private String tzScheduleId;

    private String oprid;

    private String tzCourseTypeId;

    private String tzCourseId;

    private String tzAppStatus;

    private Date tzClassStartTime;

    private Date tzClassEndTime;

    private String tzScheduleType;

    private Date tzScheduleDate;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

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

    public String getTzCourseTypeId() {
        return tzCourseTypeId;
    }

    public void setTzCourseTypeId(String tzCourseTypeId) {
        this.tzCourseTypeId = tzCourseTypeId == null ? null : tzCourseTypeId.trim();
    }

    public String getTzCourseId() {
        return tzCourseId;
    }

    public void setTzCourseId(String tzCourseId) {
        this.tzCourseId = tzCourseId == null ? null : tzCourseId.trim();
    }

    public String getTzAppStatus() {
        return tzAppStatus;
    }

    public void setTzAppStatus(String tzAppStatus) {
        this.tzAppStatus = tzAppStatus == null ? null : tzAppStatus.trim();
    }

    public Date getTzClassStartTime() {
        return tzClassStartTime;
    }

    public void setTzClassStartTime(Date tzClassStartTime) {
        this.tzClassStartTime = tzClassStartTime;
    }

    public Date getTzClassEndTime() {
        return tzClassEndTime;
    }

    public void setTzClassEndTime(Date tzClassEndTime) {
        this.tzClassEndTime = tzClassEndTime;
    }

    public String getTzScheduleType() {
        return tzScheduleType;
    }

    public void setTzScheduleType(String tzScheduleType) {
        this.tzScheduleType = tzScheduleType == null ? null : tzScheduleType.trim();
    }

    public Date getTzScheduleDate() {
        return tzScheduleDate;
    }

    public void setTzScheduleDate(Date tzScheduleDate) {
        this.tzScheduleDate = tzScheduleDate;
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
}