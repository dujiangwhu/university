package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxCourseTypeT {
    private String tzCourseTypeId;

    private String tzCourseTypeName;

    private String tzCourseType;

    private Integer tzMaxAge;

    private Integer tzMinAge;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzCourseTypeId() {
        return tzCourseTypeId;
    }

    public void setTzCourseTypeId(String tzCourseTypeId) {
        this.tzCourseTypeId = tzCourseTypeId == null ? null : tzCourseTypeId.trim();
    }

    public String getTzCourseTypeName() {
        return tzCourseTypeName;
    }

    public void setTzCourseTypeName(String tzCourseTypeName) {
        this.tzCourseTypeName = tzCourseTypeName == null ? null : tzCourseTypeName.trim();
    }

    public String getTzCourseType() {
        return tzCourseType;
    }

    public void setTzCourseType(String tzCourseType) {
        this.tzCourseType = tzCourseType == null ? null : tzCourseType.trim();
    }

    public Integer getTzMaxAge() {
        return tzMaxAge;
    }

    public void setTzMaxAge(Integer tzMaxAge) {
        this.tzMaxAge = tzMaxAge;
    }

    public Integer getTzMinAge() {
        return tzMinAge;
    }

    public void setTzMinAge(Integer tzMinAge) {
        this.tzMinAge = tzMinAge;
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