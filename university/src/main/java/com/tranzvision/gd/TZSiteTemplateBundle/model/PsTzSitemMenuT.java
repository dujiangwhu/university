package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemMenuT extends PsTzSitemMenuTKey {
    private String tzMenuName;

    private String tzMenuColumn;

    private String tzMenuState;

    private String tzMenuTypeId;

    private String tzIsDel;

    private String tzIsEditor;

    private Integer tzMenuXh;

    private String tzShowMobileFlg;

    private Integer tzShowmOrder;

    private String tzMobileNewflg;

    public String getTzMenuName() {
        return tzMenuName;
    }

    public void setTzMenuName(String tzMenuName) {
        this.tzMenuName = tzMenuName == null ? null : tzMenuName.trim();
    }

    public String getTzMenuColumn() {
        return tzMenuColumn;
    }

    public void setTzMenuColumn(String tzMenuColumn) {
        this.tzMenuColumn = tzMenuColumn == null ? null : tzMenuColumn.trim();
    }

    public String getTzMenuState() {
        return tzMenuState;
    }

    public void setTzMenuState(String tzMenuState) {
        this.tzMenuState = tzMenuState == null ? null : tzMenuState.trim();
    }

    public String getTzMenuTypeId() {
        return tzMenuTypeId;
    }

    public void setTzMenuTypeId(String tzMenuTypeId) {
        this.tzMenuTypeId = tzMenuTypeId == null ? null : tzMenuTypeId.trim();
    }

    public String getTzIsDel() {
        return tzIsDel;
    }

    public void setTzIsDel(String tzIsDel) {
        this.tzIsDel = tzIsDel == null ? null : tzIsDel.trim();
    }

    public String getTzIsEditor() {
        return tzIsEditor;
    }

    public void setTzIsEditor(String tzIsEditor) {
        this.tzIsEditor = tzIsEditor == null ? null : tzIsEditor.trim();
    }

    public Integer getTzMenuXh() {
        return tzMenuXh;
    }

    public void setTzMenuXh(Integer tzMenuXh) {
        this.tzMenuXh = tzMenuXh;
    }

    public String getTzShowMobileFlg() {
        return tzShowMobileFlg;
    }

    public void setTzShowMobileFlg(String tzShowMobileFlg) {
        this.tzShowMobileFlg = tzShowMobileFlg == null ? null : tzShowMobileFlg.trim();
    }

    public Integer getTzShowmOrder() {
        return tzShowmOrder;
    }

    public void setTzShowmOrder(Integer tzShowmOrder) {
        this.tzShowmOrder = tzShowmOrder;
    }

    public String getTzMobileNewflg() {
        return tzMobileNewflg;
    }

    public void setTzMobileNewflg(String tzMobileNewflg) {
        this.tzMobileNewflg = tzMobileNewflg == null ? null : tzMobileNewflg.trim();
    }
}