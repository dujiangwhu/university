package com.tranzvision.gd.TZPXBundle.model;

public class PxTeaToCrash extends PxTeaToCrashKey {
    private Integer score;

    private String operateOprid;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getOperateOprid() {
        return operateOprid;
    }

    public void setOperateOprid(String operateOprid) {
        this.operateOprid = operateOprid == null ? null : operateOprid.trim();
    }
}