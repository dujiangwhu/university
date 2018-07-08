package com.tranzvision.gd.TZPXBundle.model;

public class PxStuFocusTeaTKey {
    private String stuOprid;

    private String teaOprid;

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
}