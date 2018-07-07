package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxCourse {
    private String tzCourseId;

    private String tzCourseTypeId;

    private String tzCourseNumber;

    private String tzCourseName;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private String tzCourseDesc;

    public String getTzCourseId() {
        return tzCourseId;
    }

    public void setTzCourseId(String tzCourseId) {
        this.tzCourseId = tzCourseId == null ? null : tzCourseId.trim();
    }

    public String getTzCourseTypeId() {
        return tzCourseTypeId;
    }

    public void setTzCourseTypeId(String tzCourseTypeId) {
        this.tzCourseTypeId = tzCourseTypeId == null ? null : tzCourseTypeId.trim();
    }

    public String getTzCourseNumber() {
        return tzCourseNumber;
    }

    public void setTzCourseNumber(String tzCourseNumber) {
        this.tzCourseNumber = tzCourseNumber == null ? null : tzCourseNumber.trim();
    }

    public String getTzCourseName() {
        return tzCourseName;
    }

    public void setTzCourseName(String tzCourseName) {
        this.tzCourseName = tzCourseName == null ? null : tzCourseName.trim();
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

    public String getTzCourseDesc() {
        return tzCourseDesc;
    }

    public void setTzCourseDesc(String tzCourseDesc) {
        this.tzCourseDesc = tzCourseDesc == null ? null : tzCourseDesc.trim();
    }
}