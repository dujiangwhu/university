package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxStuFocus extends PxStuFocusKey {
    private Date tzFocusTime;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public Date getTzFocusTime() {
        return tzFocusTime;
    }

    public void setTzFocusTime(Date tzFocusTime) {
        this.tzFocusTime = tzFocusTime;
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