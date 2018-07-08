package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PkStuAppCourseT extends PkStuAppCourseTKey {
    private Date tzAppData;

    private String tzAppStatus;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public Date getTzAppData() {
        return tzAppData;
    }

    public void setTzAppData(Date tzAppData) {
        this.tzAppData = tzAppData;
    }

    public String getTzAppStatus() {
        return tzAppStatus;
    }

    public void setTzAppStatus(String tzAppStatus) {
        this.tzAppStatus = tzAppStatus == null ? null : tzAppStatus.trim();
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