package com.tranzvision.gd.TZPXBundle.model;

public class PxScoreLog extends PxScoreLogKey {
    private String changeType;

    private Integer changeScore;

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType == null ? null : changeType.trim();
    }

    public Integer getChangeScore() {
        return changeScore;
    }

    public void setChangeScore(Integer changeScore) {
        this.changeScore = changeScore;
    }
}