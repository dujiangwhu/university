package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxStuReviewTeaT {
    private String tzReviewId;

    private String stuOprid;

    private String teaOprid;

    private String tzReviewType;

    private Date tzReviewTime;

    private String tzReviewStatus;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private String tzReviewDesc;

    public String getTzReviewId() {
        return tzReviewId;
    }

    public void setTzReviewId(String tzReviewId) {
        this.tzReviewId = tzReviewId == null ? null : tzReviewId.trim();
    }

    public String getStuOprid() {
        return stuOprid;
    }

    public void setStuOprid(String stuOprid) {
        this.stuOprid = stuOprid == null ? null : stuOprid.trim();
    }

    public String getTeaOprid() {
        return teaOprid;
    }

    public void setTeaOprid(String teaOprid) {
        this.teaOprid = teaOprid == null ? null : teaOprid.trim();
    }

    public String getTzReviewType() {
        return tzReviewType;
    }

    public void setTzReviewType(String tzReviewType) {
        this.tzReviewType = tzReviewType == null ? null : tzReviewType.trim();
    }

    public Date getTzReviewTime() {
        return tzReviewTime;
    }

    public void setTzReviewTime(Date tzReviewTime) {
        this.tzReviewTime = tzReviewTime;
    }

    public String getTzReviewStatus() {
        return tzReviewStatus;
    }

    public void setTzReviewStatus(String tzReviewStatus) {
        this.tzReviewStatus = tzReviewStatus == null ? null : tzReviewStatus.trim();
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

    public String getTzReviewDesc() {
        return tzReviewDesc;
    }

    public void setTzReviewDesc(String tzReviewDesc) {
        this.tzReviewDesc = tzReviewDesc == null ? null : tzReviewDesc.trim();
    }
}