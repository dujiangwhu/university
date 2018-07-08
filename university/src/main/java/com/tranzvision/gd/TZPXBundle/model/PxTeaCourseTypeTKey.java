package com.tranzvision.gd.TZPXBundle.model;

public class PxTeaCourseTypeTKey {
    private String oprid;

    private String tzCourseTypeId;

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
}