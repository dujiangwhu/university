package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxCourseAnnexT extends PxCourseAnnexTKey {
    private String tzAttachsysfilena;

    private String tzAttachfileName;

    private String tzAttPUrl;

    private String tzAttAUrl;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzAttachsysfilena() {
        return tzAttachsysfilena;
    }

    public void setTzAttachsysfilena(String tzAttachsysfilena) {
        this.tzAttachsysfilena = tzAttachsysfilena == null ? null : tzAttachsysfilena.trim();
    }

    public String getTzAttachfileName() {
        return tzAttachfileName;
    }

    public void setTzAttachfileName(String tzAttachfileName) {
        this.tzAttachfileName = tzAttachfileName == null ? null : tzAttachfileName.trim();
    }

    public String getTzAttPUrl() {
        return tzAttPUrl;
    }

    public void setTzAttPUrl(String tzAttPUrl) {
        this.tzAttPUrl = tzAttPUrl == null ? null : tzAttPUrl.trim();
    }

    public String getTzAttAUrl() {
        return tzAttAUrl;
    }

    public void setTzAttAUrl(String tzAttAUrl) {
        this.tzAttAUrl = tzAttAUrl == null ? null : tzAttAUrl.trim();
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