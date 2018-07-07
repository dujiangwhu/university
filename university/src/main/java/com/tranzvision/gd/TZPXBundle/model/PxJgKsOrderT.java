package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxJgKsOrderT extends PxJgKsOrderTKey {
    private Integer tzTimecardBefore;

    private Integer tzTimecardAfter;

    private Integer tzTimecardAdd;

    private Integer tzTimecardMoney;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public Integer getTzTimecardBefore() {
        return tzTimecardBefore;
    }

    public void setTzTimecardBefore(Integer tzTimecardBefore) {
        this.tzTimecardBefore = tzTimecardBefore;
    }

    public Integer getTzTimecardAfter() {
        return tzTimecardAfter;
    }

    public void setTzTimecardAfter(Integer tzTimecardAfter) {
        this.tzTimecardAfter = tzTimecardAfter;
    }

    public Integer getTzTimecardAdd() {
        return tzTimecardAdd;
    }

    public void setTzTimecardAdd(Integer tzTimecardAdd) {
        this.tzTimecardAdd = tzTimecardAdd;
    }

    public Integer getTzTimecardMoney() {
        return tzTimecardMoney;
    }

    public void setTzTimecardMoney(Integer tzTimecardMoney) {
        this.tzTimecardMoney = tzTimecardMoney;
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