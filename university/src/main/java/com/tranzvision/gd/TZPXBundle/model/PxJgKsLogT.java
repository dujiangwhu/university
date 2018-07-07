package com.tranzvision.gd.TZPXBundle.model;

import java.util.Date;

public class PxJgKsLogT extends PxJgKsLogTKey {
    private String tzKsModifyType;

    private Integer tzTimecardBefore;

    private Integer tzTimecardAfter;

    private Integer tzTimecardModify;

    private String oprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzKsModifyType() {
        return tzKsModifyType;
    }

    public void setTzKsModifyType(String tzKsModifyType) {
        this.tzKsModifyType = tzKsModifyType == null ? null : tzKsModifyType.trim();
    }

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

    public Integer getTzTimecardModify() {
        return tzTimecardModify;
    }

    public void setTzTimecardModify(Integer tzTimecardModify) {
        this.tzTimecardModify = tzTimecardModify;
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
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